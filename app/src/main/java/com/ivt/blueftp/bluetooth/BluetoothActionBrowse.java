package com.ivt.blueftp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothFtpObject;
import android.content.Context;

import java.util.List;

/**
 * Project name: File-explorer
 * Created by zhuminjue on 2015/9/24.
 */
public class BluetoothActionBrowse extends BluetoothAction implements BluetoothFtpProxy.BrowseCallback {
    private final Context mContext;
    private final BluetoothFtpProxy proxy;
    private BluetoothDevice mTargetDevice;
    private String mRemoteDir;
    private List<BluetoothFtpObject> files;

    public BluetoothActionBrowse(Context context) {
        mContext = context;
        proxy = BluetoothFtpProxy.getFtpProxy();
    }

    @Override
    public void setParameter(Object... parameter) {
        String address = (String) parameter[1];
        mTargetDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        mRemoteDir = (String) parameter[2];
        files = null;
    }

    @Override
    protected void doExecute() {
        if (mTargetDevice != null) {
            proxy.browse(mTargetDevice, mRemoteDir, this);
        }
    }

    @Override
    public Object getResult() {
       return files;
    }

    @Override
    public void onBrowseFinished(List<BluetoothFtpObject> objects) {
        files = objects;
        finish();
    }
}
