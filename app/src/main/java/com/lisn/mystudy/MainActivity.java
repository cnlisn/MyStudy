package com.lisn.mystudy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<String> bledata_compare = null; //用于比较
    private ArrayList<String> bledata = null;    //用于存储

    /*
    名词解释：
    rssi ： Received Signal Strength Indicator , 接收的信号强度指示 , 单位是dbm ,
    无线发送层的可选部分 , 用来判定链接质量，以及是否增大广播发送强度。
    它的实现是在反向通道基带接收滤波器之后进行的
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        //创建容器，用于数据传递
        this.bledata_compare = new ArrayList<>();
        this.bledata = new ArrayList<>();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * BluetoothAdapter
     * 　　本地的蓝牙适配器。该类主要用来操作蓝牙的基本服务。
     * 比如：初始化设备的可见，查询可匹配的设备集，使用一个已知的MAC地址来初始化一个BluetoothDevice类（远程蓝牙装置），
     * 创建一个BluetoothServerSocket类以监听其它设备对本机的连接请求等。
     */
    private BluetoothAdapter bleadapter = BluetoothAdapter.getDefaultAdapter();

    BluetoothLeScanner bluetoothLeScanner = bleadapter.getBluetoothLeScanner();

    public void enableBle(View v) {
        if (!bleadapter.isEnabled()) {
            bleadapter.enable();
        }
    }

    public void disableBle(View v) {
        if (bleadapter.isEnabled()) {
            bleadapter.disable();
        }
    }

    //  BluetoothDevice
    //　该类是一个远程蓝牙设备。我们可以创建一个带有各自设备的BluetoothDevice或者查询其皆如名称、地址、类和连接状态等信息。
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            //此处写处理信息
            final ble_device ble_device = new ble_device();  //此处为新建的一个类,为ble_device
            ble_device.ble_address = bluetoothDevice.getAddress();

            if (bledata_compare.contains(ble_device.ble_address)) {
            }   //若列表中已经有了相应设备信息，则不添加进去
            else {
                bledata_compare.add(ble_device.ble_address);
                String data = "address:" + bluetoothDevice.getAddress() + "\nname:" + bluetoothDevice.getName() + "\nrssi:" + i;
                bledata.add(data);
                Log.e(TAG, "onLeScan: data = " + data);
                //adapter.notifyDataSetChanged();
            }
        }
    };

    public void startScanBle1(View view) {
        bledata_compare.clear();
        bledata.clear();
        if (bleadapter.isEnabled()) {
            bleadapter.startLeScan(leScanCallback);
        }
    }

    public void stopScanBle1(View view) {
        if (bleadapter.isEnabled()) {
            bleadapter.stopLeScan(leScanCallback);
        }
        int size = bledata.size();
        Log.e(TAG, "stopScanBle1: size = " + size);
        if (size > 0) {
            for (String bledatum : bledata) {
                Log.e(TAG, "stopScanBle1: bledatum =" + bledatum);
            }
        }
    }

    private ScanCallback ScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.e(TAG, "onScanResult: callbackType = " + callbackType + "  result = " + result);
            BluetoothDevice device = result.getDevice();
            int rssi = result.getRssi();
            final ble_device ble_device = new ble_device();  //此处为新建的一个类,为ble_device
            ble_device.ble_address = device.getAddress();

            if (bledata_compare.contains(ble_device.ble_address)) {
            }   //若列表中已经有了相应设备信息，则不添加进去
            else {
                bledata_compare.add(ble_device.ble_address);
                String data = "address:" + device.getAddress() + "\nname:" + device.getName() + "\nrssi:" + rssi;
                bledata.add(data);
                Log.e(TAG, "onScanResult: data = " + data);
                //adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            if (results != null) {
                for (ScanResult result : results) {
                    Log.e(TAG, "onBatchScanResults: " + result);
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "onScanFailed: errorCode = " + errorCode);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startScanBle2(View view) {
        bledata_compare.clear();
        bledata.clear();
        if (bleadapter.isEnabled()) {
            bluetoothLeScanner.startScan(ScanCallback);
        }
    }


    public void stopScanBle2(View view) {
        if (bleadapter.isEnabled()) {
            bluetoothLeScanner.stopScan(ScanCallback);
        }
    }
}