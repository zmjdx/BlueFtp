package com.ivt.blueftp;

import android.app.Application;
import android.content.Context;

/**
 * Created by android on 15-9-28.
 */
public class BlueFtpApp extends Application {
    private static BlueFtpApp sApp = null;
    private static final byte[] LOCK = new byte[0];

    public BlueFtpApp() {
        synchronized (LOCK) {
            if (sApp == null) {
                sApp = this;
            }
        }
    }

    public static Context getDefaultnContext() {
        if (sApp != null) {
            return sApp.getApplicationContext();
        }

        return null;
    }

}
