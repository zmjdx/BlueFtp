package com.ivt.blueftp.module;

import android.content.Context;

/**
 * Created by android on 15-9-28.
 */
public interface BaseFile {
    public static String separator = "/";

    String getAbsolutePath();

    boolean isDirectory();

    boolean canRead();

    boolean canWrite();

    BaseFile getParentFile();

    String getName();

    boolean exists();

    String[] list();

    boolean isHidden();

    String getCanonicalPath();

    long length();

    long lastModified();

    boolean isFile();

    boolean mkdirs();

    BaseFile getParent();

    BaseFile[] listFiles();

    boolean renameTo(BaseFile baseFile);

    boolean isRoot();

    String open(Context context);

    String close(Context context);

    boolean isOpened();

    String getChildPath(String child);

    boolean delete();

    boolean isProtected();
}
