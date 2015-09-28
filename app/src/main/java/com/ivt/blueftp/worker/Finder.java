package com.ivt.blueftp.worker;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothFtpObject;
import android.os.AsyncTask;
import android.util.Log;

import com.ivt.blueftp.R;
import com.ivt.blueftp.activity.FileListActivity;
import com.ivt.blueftp.module.BaseFile;
import com.ivt.blueftp.module.BtFileStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 15-9-28.
 */
public class Finder extends AsyncTask<BaseFile, Integer, List<BluetoothFtpObject>>
{

    private static final String TAG = Finder.class.getName();

    private FileListActivity caller;
    private ProgressDialog waitDialog;

    private BaseFile currentDir;

    public Finder(FileListActivity caller) {

        this.caller = caller;
    }

    @Override
    protected void onPostExecute(List<BluetoothFtpObject> result) {

        List<BluetoothFtpObject> childFilesList = result;
        Log.v(TAG, "Children for " + currentDir.getAbsolutePath() + " received");

        if(waitDialog!=null && waitDialog.isShowing())
        {
            waitDialog.dismiss();
        }
        Log.v(TAG, "Children for "+currentDir.getAbsolutePath()+" passed to caller");
        caller.setCurrentDirAndChildren(currentDir, childFilesList);

    }
    @Override
    protected List<BluetoothFtpObject> doInBackground(BaseFile... params) {

        Thread waitForASec = new Thread() {

            @Override
            public void run() {

                waitDialog = new ProgressDialog(caller);
                waitDialog.setTitle("");
                waitDialog.setMessage(caller.getString(R.string.querying_filesys));
                waitDialog.setIndeterminate(true);

                try {
                    Thread.sleep(100);
                    if(this.isInterrupted())
                    {
                        return;
                    }
                    else
                    {
                        caller.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if(waitDialog!=null)
                                    waitDialog.show();
                            }
                        });

                    }
                } catch (InterruptedException e) {

                    Log.e(TAG, "Progressbar waiting thread encountered exception ",e);
                    e.printStackTrace();
                }


            }
        };
        caller.runOnUiThread(waitForASec);

        currentDir = params[0];
        Log.d(TAG, "Received directory to list paths - " + currentDir.getAbsolutePath());

        String[] children = currentDir.list();
        List<BluetoothFtpObject> childFiles = new ArrayList<BluetoothFtpObject>();

        String path;
        BtFileStorage storage = BtFileStorage.getInstance();

        for(String fileName : children)
        {
            path = currentDir.getChildPath(fileName);
            BaseFile f = storage.get(path);
            Log.d(TAG, "create item(" + path + "):" + f);
            if(!f.exists())
            {
                continue;
            }
            if(f.isProtected())
            {
                continue;
            }
            if(f.isHidden())
            {
                continue;
            }

            String fname = f.getName();

            BluetoothFtpObject child = new BluetoothFtpObject();
            child.name = fname;
            child.time = f.lastModified();
            child.size = f.length();
            childFiles.add(child);
        }
        
        if(waitForASec.isAlive())
        {
            try
            {
                waitForASec.interrupt();
            }
            catch (Exception e) {

                Log.e(TAG, "Error while interrupting thread",e);
            }
        }
        return childFiles;
    }
}