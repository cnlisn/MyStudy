package com.lisn.mystudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lisn.mystudy.bt.BtBase;
import com.lisn.mystudy.bt.BtClient;
import com.lisn.mystudy.bt.BtDevAdapter;
import com.lisn.mystudy.util.BtReceiver;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Set;

public class BtClientActivity extends AppCompatActivity implements BtReceiver.Listener, BtDevAdapter.Listener, BtBase.Listener {
    private static final String TAG = BtClientActivity.class.getSimpleName();
    private BtReceiver mBtReceiver;
    private final BtDevAdapter mBtDevAdapter = new BtDevAdapter(this);
    private BluetoothAdapter mBluetoothAdapter;
    private final BtClient mClient = new BtClient(this);
    private TextView mTips;
    private TextView mLogs;
    private EditText mInputMsg;
    private EditText mInputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_client);

        mTips = findViewById(R.id.tv_tips);
        mInputMsg = findViewById(R.id.input_msg);
        mInputFile = findViewById(R.id.input_file);
        mLogs = findViewById(R.id.tv_log);

        RecyclerView rv = findViewById(R.id.rv_bt);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mBtDevAdapter);

        // 注册蓝牙监听广播
        mBtReceiver = new BtReceiver(this, this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //获取本机蓝牙名称
        String name = mBluetoothAdapter.getName();
        //获取本机蓝牙地址
        String address = mBluetoothAdapter.getAddress();
        Log.d(TAG, "bluetooth name =" + name + " address =" + address);
        //获取已配对蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        Log.d(TAG, "bonded device size =" + devices.size());
        for (BluetoothDevice bonddevice : devices) {
            Log.d(TAG, "bonded device name =" + bonddevice.getName() + " address=" + bonddevice.getAddress());
        }

        scanBlue();
    }

    /**
     * 设备是否支持蓝牙  true为支持
     *
     * @return
     */
    public boolean isSupportBlue() {
        return mBluetoothAdapter != null;
    }

    /**
     * 蓝牙是否打开   true为打开
     *
     * @return
     */
    public boolean isBlueEnable() {
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }

    /**
     * 自动打开蓝牙（异步：蓝牙不会立刻就处于开启状态）
     * 这个方法打开蓝牙不会弹出提示
     */
    public void openBlueAsyn() {
        if (isSupportBlue()) {
            mBluetoothAdapter.enable();
        }
    }

    /**
     * 自动打开蓝牙（同步）
     * 这个方法打开蓝牙会弹出提示
     * 需要在onActivityResult 方法中判断resultCode == RESULT_OK  true为成功
     */
    public void openBlueSync(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 扫描的方法 返回true 扫描成功
     * 通过接收广播获取扫描到的设备
     *
     * @return
     */
    public boolean scanBlue() {
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return false;
        }

        //当前是否在扫描，如果是就取消当前的扫描，重新扫描
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        //此方法是个异步操作，一般搜索12秒
        return mBluetoothAdapter.startDiscovery();
    }

    /**
     * 取消扫描蓝牙
     *
     * @return true 为取消成功
     */
    public boolean cancelScanBule() {
        if (isSupportBlue()) {
            return mBluetoothAdapter.cancelDiscovery();
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBtReceiver);
        mClient.unListener();
        mClient.close();
    }

    @Override
    public void foundDev(BluetoothDevice dev, short rssi) {
        Log.e(TAG, "foundDev: " + dev.getName() + " " + dev.getAddress());
        mBtDevAdapter.add(dev);
    }

    @Override
    public void onItemClick(BluetoothDevice dev) {
        Log.e(TAG, "onItemClick: " + dev.getName() + " " + dev.getAddress());
//        pin(dev);
        if (mClient.isConnected(dev)) {
            APP.toast("已经连接了", 0);
            return;
        }
        mClient.connect(dev);
        APP.toast("正在连接...", 0);
        mTips.setText("正在连接...");
    }


    /**
     * 配对（配对成功与失败通过广播返回）
     *
     * @param device
     */
    public void pin(BluetoothDevice device) {
        if (device == null) {
            Log.e(TAG, "bond device null");
            return;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //配对之前把扫描关闭
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //判断设备是否配对，没有配对再配，已经配对就不需要配了
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to bond:" + device.getName());
            try {
                Method createBondMethod = device.getClass().getMethod("createBond");
                Boolean returnValue = (Boolean) createBondMethod.invoke(device);
                boolean b = returnValue.booleanValue();
                Log.e(TAG, "pin: returnValue " + b);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "attemp to bond fail!");
            }
        }
    }

    /**
     * 取消配对（取消配对成功与失败通过广播返回 也就是配对失败）
     *
     * @param device
     */
    public void cancelPinBule(BluetoothDevice device) {
        if (device == null) {
            Log.d(TAG, "cancel bond device null");
            return;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //判断设备是否配对，没有配对就不用取消了
        if (device.getBondState() != BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to cancel bond:" + device.getName());
            try {
                Method removeBondMethod = device.getClass().getMethod("removeBond");
                Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
                returnValue.booleanValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "attemp to cancel bond fail!");
            }
        }
    }


    public void reScan(View view) {
        mBtDevAdapter.reScan();
    }

    @Override
    public void socketNotify(int state, Object obj) {
        Log.e(TAG, "socketNotify: state = " + state);
        if (isDestroyed())
            return;
        String msg = null;
        switch (state) {
            case BtBase.Listener.CONNECTED:
                BluetoothDevice dev = (BluetoothDevice) obj;
                msg = String.format("与%s(%s)连接成功", dev.getName(), dev.getAddress());
                mTips.setText(msg);
                break;
            case BtBase.Listener.DISCONNECTED:
                msg = "连接断开";
                mTips.setText(msg);
                break;
            case BtBase.Listener.MSG:
                msg = String.format("\n%s", obj);
                mLogs.append(msg);
                break;
        }
        APP.toast(msg, 0);
    }

    public void sendMsg(View view) {
        if (mClient.isConnected(null)) {
            String msg = mInputMsg.getText().toString();
            if (TextUtils.isEmpty(msg))
                APP.toast("消息不能空", 0);
            else
                mClient.sendMsg(msg);
        } else
            APP.toast("没有连接", 0);
    }

    public void sendFile(View view) {
        if (mClient.isConnected(null)) {
            String filePath = mInputFile.getText().toString();
            if (TextUtils.isEmpty(filePath) || !new File(filePath).isFile())
                APP.toast("文件无效", 0);
            else
                mClient.sendFile(filePath);
        } else
            APP.toast("没有连接", 0);
    }
}