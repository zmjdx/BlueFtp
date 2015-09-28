package com.ivt.blueftp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothFtp;
import android.content.Context;

/**
 * Created by android on 15-9-25.
 */
public class BluetoothActionDelete extends BluetoothAction implements BluetoothFtpProxy.DeleteCallback {
    private Context mContext;
    private BluetoothDevice mTargetDevice;
    private String mFileName;
    private String mDeleteResult;

    public BluetoothActionDelete(Context context) {
        mContext = context;
    }

    @Override
    public void setParameter(Object... parameter) {
        mTargetDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice((String) parameter[0]);
        mFileName = (String) parameter[1];
    }

    @Override
    protected void doExecute() {
        BluetoothFtpProxy proxy = BluetoothFtpProxy.getFtpProxy();
        proxy.delete(mTargetDevice, mFileName, this);
    }

    @Override
    public Object getResult() {
        return new String[] {mDeleteResult};
    }

    @Override
    public void onDeleteFinished(int result) {
        mDeleteResult = "Failure";

        if (result == BluetoothFtp.EventListener.Result.FINISHED.ordinal()) {
            mDeleteResult = "OK";
        }

        finish();
    }
}
