package com.ivt.blueftp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothFtp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.*;

/**
 * Created by zhuminjue on 2015/9/23.
 */
public class BluetoothActionConnect extends BluetoothAction {
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothActionConnect";
    private final BroadcastReceiver mReceiver;
    private final Handler mHandler;
    private final BluetoothFtpProxy proxy;
    private Context mContext;
    private BluetoothDevice mTargetDevice;
    private String mConnectResult;
    private boolean mDoConnect = true;
    private boolean mDoDisconnect = false;

    public BluetoothActionConnect(Context context) {
        mContext = context;
        proxy = BluetoothFtpProxy.getFtpProxy();

        IntentFilter it = new IntentFilter();
        it.addAction(BluetoothFtp.ACTION_CONNECTION_STATE_CHANGED);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                log("received action:" + action);
                if (BluetoothFtp.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
//                    BluetoothDevice device = intent.getParcelableExtra(BluetoothFtp.EXTRA_STATE);
                    int state = intent.getIntExtra(BluetoothFtp.EXTRA_STATE, 0);

                    if (state == BluetoothFtp.STATE_CONNECTED) {
                        mConnectResult = "connected";
                        finish();
                    } else if (state == BluetoothFtp.STATE_DISCONNECTED) {
                        mConnectResult = "disconnected";
                        finish();
                    }
                    log("state = " + state + ", result:" + mConnectResult + " BluetoothFtp.STATE_DISCONNECTED=" + BluetoothFtp.STATE_DISCONNECTED);
                }
            }
        };

        HandlerThread broadcastThread = new HandlerThread("BroadcastReceiverThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        broadcastThread.start();
        mHandler = new Handler(broadcastThread.getLooper());
        mContext.registerReceiver(mReceiver, it, null, mHandler);
    }

    @Override
    public void setParameter(Object... parameter) {
        String address = (String) parameter[0];
        Boolean toConnect = (Boolean) parameter[1];
        Boolean doDisconnect = (Boolean) parameter[2];

        mDoConnect = toConnect.booleanValue();
        mDoDisconnect = !doDisconnect.booleanValue();
        mTargetDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
    }

    @Override
    protected boolean block() {
        return mDoConnect;
    }

    @Override
    public void doExecute() {
        if (proxy != null) {
            if (mDoConnect) {
                if (!mDoDisconnect) {
                    if (!proxy.connect(mTargetDevice))
                    {
                        finish();
                    }
                } else {
                    proxy.disconnect(mTargetDevice);
                }
            } else {
                mConnectResult = proxy.getConnectionState(mTargetDevice) == BluetoothFtp.STATE_CONNECTED ?
                        "connected" : "disconnected";
            }
        }
        log("execute, to connect " + mTargetDevice + "," + mDoConnect + ", result:" + mConnectResult);

    }

    @Override
    public void destroy() {
        mContext.unregisterReceiver(mReceiver);
    }

    private void log(String s) {
        if (DBG) {
            android.util.Log.d(TAG, s);
        }
    }

    @Override
    public String[] getResult() {
        return new String[]{mConnectResult};
    }
}
