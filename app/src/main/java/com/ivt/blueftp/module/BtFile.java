package com.ivt.blueftp.module;

import android.content.Context;

/**
 * Created by android on 15-9-28.
 */
public class BtFile implements BaseFile {
    private static final boolean DBG = true;
    private static final String TAG = "BtFile";
    public static final String PATH_PREFIX = "@";
    public static final String PATH_SEPARATOR = "/";
    private BtFile mParent;
    private String mAddress;
    private String mPath;
    private String mName;
    private String mRemotePath;
    private int mAttr;
    private long mSize;
    private long mLastModifiedTime;

    private static final int FILE_ATTR_CAN_READ = 0x01;
    private static final int FILE_ATTR_CAN_WRITE = 0x02;
    private static final int FILE_ATTR_CAN_EXEC = 0x04;
    private static final int FILE_ATTR_IS_FOLER = 0x08;
    private boolean mConnected = false;

    public BtFile(String path) {

    }

    @Override
    public String getAbsolutePath() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean canRead() {
        return false;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public BaseFile getParentFile() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public String[] list() {
        return new String[0];
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public String getCanonicalPath() {
        return null;
    }

    @Override
    public long length() {
        return 0;
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean mkdirs() {
        return false;
    }

    @Override
    public BaseFile getParent() {
        return null;
    }

    @Override
    public BaseFile[] listFiles() {
        return new BaseFile[0];
    }

    @Override
    public boolean renameTo(BaseFile baseFile) {
        return false;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public String open(Context context) {
        return null;
    }

    @Override
    public String close(Context context) {
        return null;
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    @Override
    public String getChildPath(String child) {
        return null;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean isProtected() {
        return (mAttr & FILE_ATTR_CAN_EXEC) == 0;
    }
}
