package com.ivt.blueftp.module;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by android on 15-9-28.
 */
public class BtFileStorage {
    private static final int MAX_LEVEL = 10;
    private static final String TAG = "BtFileStorage";
    private int mLevel;
    private HashMap<String, CacheFile> mCache;
    private static BtFileStorage sInst;
    private static final Object LOCK = new Object();

    public static BtFileStorage getInstance() {
        synchronized (LOCK) {
            if (sInst == null) {
                sInst = new BtFileStorage();
            }
        }
        return sInst;
    }

    private BtFileStorage() {
        mCache = new HashMap<String, CacheFile>();
        mLevel = 0;
    }

    private static class CacheFile {
        int level;
        BaseFile file;
    }

    public void put(BaseFile file) {
        CacheFile cFile = new CacheFile();
        cFile.level = mLevel;
        cFile.file = file;

        mCache.put(file.getAbsolutePath(), cFile);
    }

    public BaseFile get(String path) {
        CacheFile cacheFile = mCache.get(path);

        if (cacheFile != null) {
            return cacheFile.file;
        }

        Log.d(TAG, "create new file(" + path + ")");
        BtFile f = new BtFile(path);
        put(f);
        return f;
    }

    public void commit() {
        mLevel++;

        if (mLevel < MAX_LEVEL)
            return;

        int fence = mLevel / 2;
        Iterator it = mCache.entrySet().iterator();
        CacheFile file;
        while (it.hasNext()) {
            HashMap.Entry<String, CacheFile> entry = (HashMap.Entry<String, CacheFile>) it.next();
            file = entry.getValue();

            if (file != null) {
                if (file.level < fence) {
                    it.remove();
                } else {
                    file.level -= fence;
                }
            }
        }
        mLevel -= fence;
    }
}
