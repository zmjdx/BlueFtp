package com.ivt.blueftp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zhuminjue on 2015/9/22.
 */
public class BluetoothActionDiscover extends BluetoothAction {
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothActionDiscover";

    private List<BluetoothDevice> mDeviceList;
    private Context mContext;
    private boolean mRefresh;
    private BroadcastReceiver mReceiver;

    public BluetoothActionDiscover(Context context) {
        mContext = context;
        mDeviceList = new ArrayList<>();
        mRefresh = false;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                    finish();
                    mContext.unregisterReceiver(mReceiver);
                } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {

                } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    mDeviceList.add((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                }
            }
        };

    }

    @Override
    public void setParameter(Object... parameter) {
        mRefresh = ((Boolean) parameter[1]).booleanValue();
        mDeviceList.clear();

        if (mRefresh) {
            IntentFilter it = new IntentFilter();
            it.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            it.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            it.addAction(BluetoothDevice.ACTION_FOUND);
            mContext.registerReceiver(mReceiver, it);
        }
    }

    @Override
    protected boolean block() {
        return mRefresh;
    }

    @Override
    public void doExecute() {
        log("doExecute(" + mRefresh + ")");

        if (mRefresh) {
            BluetoothAdapter.getDefaultAdapter().startDiscovery();
            log("start discover...");
        }

        if (!mRefresh) {
            Set<BluetoothDevice> devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            mDeviceList.addAll(devices);
            log("device size:" + devices.size());
            finish();
        }
    }

    @Override
    public String[] getResult() {
        int i = 0;
        int size = mDeviceList.size();
        String[] devs = new String[size];

        for (BluetoothDevice dev : mDeviceList) {
            if (dev != null) {
                devs[i] = dev.getAddress() + dev.getName();
            }
            i++;
        }
        return devs;
    }


    private void log(String s) {
        if (DBG) {
            android.util.Log.d(TAG, s);
        }
    }
}
