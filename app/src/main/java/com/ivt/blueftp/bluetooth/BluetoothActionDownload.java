package com.ivt.blueftp.bluetooth;

import android.bluetooth.BluetoothFtp;
import android.content.Context;

/**
 * Created by android on 15-9-28.
 */
public class BluetoothActionDownload extends BluetoothAction implements BluetoothFtpProxy.DownloadCallback {
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothActionDownload";
    private final BluetoothFtpProxy proxy;
    private Context mContext;
    private String mDownloadFile;
    private DownloadCallback mCallback;
    private String mResult;

    public BluetoothActionDownload(Context context) {
        mContext = context;
        proxy = BluetoothFtpProxy.getFtpProxy();
    }

    @Override
    public void setParameter(Object... parameter) {
        mDownloadFile = (String) parameter[0];
        mCallback = (DownloadCallback) parameter[1];
    }

    @Override
    protected void doExecute() {
        proxy.download(null, mDownloadFile, this);
    }

    @Override
    public Object getResult() {
        return mResult;
    }

    @Override
    public void onDownloadFinished(String file, int result) {
        log("onDownloadFinished(" + file + ":" + result + ")");

        if (result == BluetoothFtp.EventListener.Result.FINISHED.ordinal()) {
            mResult = "OK";
        } else if (result == BluetoothFtp.EventListener.Result.ABORTED.ordinal()) {
            mResult = "Failure";
        }
        finish();
    }

    @Override
    public void onDownloadInProgress(String file, long finished, long total) {
        log("onDownloadInProgress(" + file + "," + finished + "," + total + ")");
        if (mCallback != null) {
            mCallback.onUpdate(finished, total);
        }
    }

    private void log(String log) {
        if (DBG) {
            android.util.Log.d(TAG, log);
        }
    }

    public interface DownloadCallback {
        void onUpdate(long size, long total);
    }
}
