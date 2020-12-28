package com.lisn.mystudy.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisn.mystudy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date : 2020/11/4 3:30 PM
 * @desc :
 */
public class BtDevAdapter extends RecyclerView.Adapter<BtDevAdapter.VH> {
    private static final String TAG = BtDevAdapter.class.getSimpleName();
    private final List<BluetoothDevice> mDevices = new ArrayList<>();
    private final Listener mListener;

    public BtDevAdapter(Listener listener) {
        mListener = listener;
    }

    /**
     * 添加扫描到的设备
     *
     * @param dev
     */
    public void add(BluetoothDevice dev) {
        if (mDevices.contains(dev)) {
            int index = mDevices.indexOf(dev);
            if (dev.getBondState() != mDevices.get(index).getBondState()) {
                mDevices.set(index, dev);
                notifyDataSetChanged();
            }
            return;
        }
        mDevices.add(dev);
        notifyDataSetChanged();
    }

    /**
     * 重新扫描
     */
    public void reScan() {
        mDevices.clear();
        addBound();
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        // isDiscovering 正在发现
        if (!bt.isDiscovering())
            bt.startDiscovery();
        notifyDataSetChanged();
    }

    /**
     * 添加已绑定设备
     */
    private void addBound() {
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (bondedDevices != null)
            mDevices.addAll(bondedDevices);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dev, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        BluetoothDevice dev = mDevices.get(position);
        String name = dev.getName();
        String address = dev.getAddress();
        int bondState = dev.getBondState();
        holder.name.setText(name == null ? "" : name);
        holder.address.setText(String.format("%s (%s)", address, bondState == 10 ? "未配对" : "配对"));

    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView name;
        final TextView address;

        public VH(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Log.d(TAG, "onClick, getAdapterPosition=" + pos);
            if (pos >= 0 && pos < mDevices.size())
                mListener.onItemClick(mDevices.get(pos));
        }
    }

    public interface Listener {
        void onItemClick(BluetoothDevice dev);
    }

}
