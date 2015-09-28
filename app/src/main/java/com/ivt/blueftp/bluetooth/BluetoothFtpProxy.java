package com.ivt.blueftp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothFtp;
import android.bluetooth.BluetoothFtpObject;
import android.bluetooth.BluetoothProfile;

import com.ivt.blueftp.BlueFtpApp;

import java.util.ArrayList;
import java.util.List;

public class BluetoothFtpProxy {
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothFtpProxy";

    final BluetoothProfile.ServiceListener mListener;
    BluetoothFtp mFtpService;
    private static BluetoothFtpProxy sInst;
    private static final Object LOCK = new Object();
    private BluetoothFtp.EventListener mEventListener;
    private List<BluetoothFtpObject> mFiles;
    private BrowseCallback mBrowseCallback;
    private String mCurrentDir = "/";
    private DeleteCallback mDeleteCallback;
    private DownloadCallback mDownloadCallback;
    private UploadCallback mUploadCallback;

    public static BluetoothFtpProxy getFtpProxy() {
        synchronized (LOCK) {
            if (sInst == null) {
                sInst = new BluetoothFtpProxy();
            }
        }

        return sInst;
    }

    public void create() {
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(BlueFtpApp.getDefaultnContext(), mListener, BluetoothProfile.FTP);
    }

    public void destroy() {
        BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.FTP, mFtpService);
    }

    private BluetoothFtpProxy() {
        mListener = new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                mFtpService = (BluetoothFtp) proxy;
                mFtpService.registerEventListener(mEventListener);
            }

            @Override
            public void onServiceDisconnected(int profile) {
                mFtpService.unregisterEventListener(mEventListener);
                mFtpService = null;
            }
        };

        mEventListener = new BluetoothFtp.EventListener() {
            @Override
            public void onOperationResult(BluetoothFtpObject bluetoothFtpObject, Operation operation, Result result) {
                log("operation:" + operation + ",result:" + result);
                if (operation.equals(Operation.OPERATION_BROWSE)) {
                    if (mBrowseCallback != null) {
                        mBrowseCallback.onBrowseFinished(mFiles);
                    }
                } else if (operation.equals(Operation.OPERATION_DELETE)) {
                    if (mDeleteCallback != null) {
                        mDeleteCallback.onDeleteFinished(result.ordinal());
                    }
                } else if (operation.equals(Operation.OPERATION_PULL)) {
                    if (mDownloadCallback != null) {
                        mDownloadCallback.onDownloadFinished(bluetoothFtpObject.name, result.ordinal());
                    }
                } else if (operation.equals(Operation.OPERATION_PUSH)) {
                    if (mUploadCallback != null) {
                        mUploadCallback.onUploadFinished(bluetoothFtpObject.name, result.ordinal());
                    }
                }
            }

            @Override
            public void onPushInProgress(BluetoothFtpObject object, int size) {
                log("onPushInProgress(" + object.name + ",size:" + size);
                if (mUploadCallback != null) {
                    mUploadCallback.onUploadInProgress(object.name, object.size, size);
                }
            }

            @Override
            public void onPullInProgress(BluetoothFtpObject object, int size) {
                log("onPushInProgress(" + object.name + ",size:" + size);
                if (mDownloadCallback != null) {
                    mDownloadCallback.onDownloadInProgress(object.name, object.size, size);
                }
            }

            @Override
            public void onObjectBrowsed(BluetoothFtpObject object) {
                log("received object:" + object);
                mFiles.add(object);
            }
        };

        mFiles = new ArrayList<BluetoothFtpObject>();
    }

    private void log(String s) {
        if (DBG) {
            android.util.Log.d(TAG, s);
        }
    }

    public boolean connect(BluetoothDevice device) {
        if (mFtpService != null) {
            return mFtpService.connect(device);
        }

        return false;
    }

    public void browse(BluetoothDevice device, String dir, BrowseCallback callback) {
        if (mFtpService != null) {
            log("browse(" + dir + ")");
            // TODO
            mFiles.clear();
            mBrowseCallback = callback;
            if (dir != null) {
                String[] ds = dir.split("/");
                String[] cs = mCurrentDir.split("/");
                mCurrentDir = dir;
                if (ds.length < cs.length) {
                    dir = ".."; // FIXME
                } else if (ds.length > cs.length) {
                    dir = ds[ds.length - 1];
                }
                mFtpService.changeRemoteFolder(dir);
                // FIXME
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mFtpService.browseRemoteFolder();
        }
    }

    public boolean delete(BluetoothDevice device, String file, DeleteCallback callback) {
        mDeleteCallback = callback;

        if (mFtpService != null) {
            BluetoothFtpObject object = new BluetoothFtpObject();
            object.name = file;
            return mFtpService.deleteObject(object);
        }
        return false;
    }

    public boolean download(BluetoothDevice device, String file, DownloadCallback callback) {
        mDownloadCallback = callback;

        if (mFtpService != null) {
            BluetoothFtpObject object = new BluetoothFtpObject();
            object.name = file;

            return mFtpService.pullObject(object);
        }
        return false;
    }

    public boolean upload(BluetoothDevice device, String file, UploadCallback callback) {
        mUploadCallback = callback;

        if (mFtpService != null) {
            BluetoothFtpObject object = new BluetoothFtpObject();
            object.name = file;

            return mFtpService.pushObject(object);
        }
        return false;
    }

    public void disconnect(BluetoothDevice device) {
        // FIXME
        mCurrentDir = "/";
        if (mFtpService != null) {
            mFtpService.disconnect(device);
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        if (mFtpService != null) {
            return mFtpService.getConnectionState(device);
        }
        return -1;
    }

    interface BrowseCallback {
        void onBrowseFinished(List<BluetoothFtpObject> objects);
    }

    interface DeleteCallback {
        void onDeleteFinished(int result);
    }

    interface DownloadCallback {
        void onDownloadFinished(String file, int result);
        void onDownloadInProgress(String file, long finished, long total);
    }

    interface UploadCallback {
        void onUploadFinished(String file, int result);
        void onUploadInProgress(String file, long finished, long total);
    }

}