package com.ivt.blueftp.bluetooth;

import android.content.Context;

/**
 * Created by zhuminjue on 2015/9/23.
 */
public class BluetoothActionFactory {
    public static final String DISCOVER = "discover";
    public static final String CONNECT = "connect";
    public static final String BROWSE = "browse";
    public static final String DELETE = "delete";
    public static final String DOWNLOAD = "download";
    public static final String UPLOAD = "upload";

    private static BluetoothActionFactory sInst;
    private static byte[] LOCK = new byte[0];
    private BluetoothAction mDiscoverAction;
    private BluetoothAction mConnectAction;
    private BluetoothActionBrowse mBrowseAction;
    private BluetoothActionDelete mDeleteAction;
    private BluetoothActionDownload mDownloadAction;
    private BluetoothActionUpload mUploadAction;

    public static BluetoothActionFactory getInstance() {
        synchronized (LOCK) {
            if (sInst == null) {
                sInst = new BluetoothActionFactory();
            }
        }
        return sInst;
    }

    public BluetoothAction createBluetoothAction(Context context, String action) {
        if (action.equals(DISCOVER)) {
            if (mDiscoverAction == null) {
                mDiscoverAction = new BluetoothActionDiscover(context);
            }
            return mDiscoverAction;
        } else if (action.equals(CONNECT)) {
            if (mConnectAction == null) {
                mConnectAction = new BluetoothActionConnect(context);
            }
            return mConnectAction;
        } else if (action.equals(BROWSE)) {
            if (mBrowseAction == null) {
                mBrowseAction = new BluetoothActionBrowse(context);
            }
            return mBrowseAction;
        } else if (action.equals(DELETE)) {
            if (mDeleteAction == null) {
                mDeleteAction = new BluetoothActionDelete(context);
            }
            return mDeleteAction;
        } else if (action.equals(DOWNLOAD)) {
            if (mDownloadAction == null) {
                mDownloadAction = new BluetoothActionDownload(context);
            }
            return mDownloadAction;
        } else if (action.equals(UPLOAD)) {
            if (mUploadAction == null) {
                mUploadAction = new BluetoothActionUpload(context);
            }
            return mUploadAction;
        }
        return null;
    }

    public void destroy() {
        if (mDiscoverAction != null) {
            mDiscoverAction.destroy();
        }
        if (mConnectAction != null) {
            mConnectAction.destroy();
        }
        if (mBrowseAction != null) {
            mBrowseAction.destroy();
        }
        if (mDeleteAction != null) {
            mDeleteAction.destroy();
        }
        if (mDownloadAction != null) {
            mDownloadAction.destroy();
        }
        if (mUploadAction != null) {
            mUploadAction.destroy();
        }
    }
}
