package com.ivt.blueftp.bluetooth;

import android.bluetooth.BluetoothFtp;
import android.content.Context;

/**
 * Created by android on 15-9-28.
 */
public class BluetoothActionUpload extends BluetoothAction implements BluetoothFtpProxy.UploadCallback {
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothActionUpload";
    private final BluetoothFtpProxy proxy;
    private Context mContext;
    private String mUploadFile;
    private UploadCallback mCallback;
    private String mResult;


    public BluetoothActionUpload(Context context) {
        mContext = context;
        proxy = BluetoothFtpProxy.getFtpProxy();
    }

    @Override
    public void setParameter(Object... parameter) {
        mUploadFile = (String) parameter[0];
        mCallback = (UploadCallback) parameter[1];
    }

    @Override
    protected void doExecute() {
        proxy.upload(null, mUploadFile, this);
    }

    @Override
    public Object getResult() {
        return mResult;
    }

    @Override
    public void onUploadFinished(String file, int result) {
        if (result == BluetoothFtp.EventListener.Result.FINISHED.ordinal()) {
            mResult = "OK";
        } else if (result == BluetoothFtp.EventListener.Result.ABORTED.ordinal()) {
            mResult = "Failure";
        }

        finish();
    }

    @Override
    public void onUploadInProgress(String file, long finished, long total) {
        if (mCallback != null) {
            mCallback.onUpdate(finished, total);
        }
    }

    public interface UploadCallback {
        void onUpdate(long size, long total);
    }
}
