package com.ivt.blueftp.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.bluetooth.BluetoothFtpObject;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ivt.blueftp.BlueFtpApp;
import com.ivt.blueftp.R;
import com.ivt.blueftp.bluetooth.BluetoothActionFactory;
import com.ivt.blueftp.bluetooth.BluetoothFtpProxy;
import com.ivt.blueftp.module.BaseFile;
import com.ivt.blueftp.module.BtFile;
import com.ivt.blueftp.module.BtFileStorage;
import com.ivt.blueftp.module.FileListEntry;
import com.ivt.blueftp.worker.Finder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by android on 15-9-28.
 */
public class FileListActivity extends ListActivity {
    private static final String TAG = FileListActivity.class.getName();

    private static final String CURRENT_DIR_DIR = "current-dir";
    private static final boolean DBG = true;

    private ListView explorerListView;
    private BtFile currentDir;
    private List<FileListEntry> files;
    private FileListAdapter adapter;
    protected Object mCurrentActionMode;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private CharSequence[] gotoLocations;
    private BlueFtpApp app;
    private BaseFile previousOpenDirChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (BlueFtpApp) getApplication();

        BluetoothFtpProxy.getFtpProxy().create();

        gotoLocations = getResources().getStringArray(R.array.goto_locations);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        prepareActionBar();
        currentDir = (BtFile) BtFileStorage.getInstance().get(BtFile.PATH_PREFIX);

        files = new ArrayList<>();
        initFileListView();

        BluetoothActionFactory factory = BluetoothActionFactory.getInstance();
        factory.createBluetoothAction(this, BluetoothActionFactory.DISCOVER);
        factory.createBluetoothAction(this, BluetoothActionFactory.CONNECT);
        factory.createBluetoothAction(this, BluetoothActionFactory.DELETE);
        factory.createBluetoothAction(this, BluetoothActionFactory.DOWNLOAD);
        factory.createBluetoothAction(this, BluetoothActionFactory.UPLOAD);
    }

    private void initFileListView() {
        explorerListView = getListView();
        adapter = new FileListAdapter(this, files);
        explorerListView.setAdapter(adapter);
        explorerListView.setTextFilterEnabled(true);
        explorerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (explorerListView.isClickable()) {
                    FileListEntry file = (FileListEntry) explorerListView
                            .getAdapter().getItem(position);
//                    log("click(" + position + "):" + file.getPath() + "," + file.getName());
//                    select(file.getPath());
                }
            }

        });

        explorerListView.setOnItemLongClickListener(getLongPressListener());
        registerForContextMenu(explorerListView);
    }

    private void prepareActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        mSpinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, gotoLocations);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, getActionbarListener(actionBar));

    }

    private ActionBar.OnNavigationListener getActionbarListener(final ActionBar actionBar) {
        return new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {

                int selectedIndex = actionBar.getSelectedNavigationIndex();

                if (selectedIndex == 0) {
                    return false;
                }
                switch (selectedIndex) {

                    case 1:
//                        listContents(getPreferenceHelper().getStartDir());
                        break;

                    default:
                        break;
                }


                return true;
            }

        };
    }

    private AdapterView.OnItemLongClickListener getLongPressListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0,
                                           final View view, int arg2, long arg3) {

                if (!explorerListView.isLongClickable())
                    return true;
                view.setSelected(true);

                final FileListEntry fileListEntry = (FileListEntry) adapter
                        .getItem(arg2);


                if (mCurrentActionMode != null) {
                    return false;
                }
                /*
                if (Util.isProtected(fileListEntry
                        .getPath())) {
                    return false;
                }

                if (fileListEntry.getPath().isOpened()) {
                    fileListEntry.getPath().close(FileListActivity.this);
                    refresh();
                    return true;
                }

                explorerListView.setEnabled(false);

                mCurrentActionMode = FileListActivity.this
                        .startActionMode(new FileActionsCallback(
                                FileListActivity.this, fileListEntry) {

                            @Override
                            public void onDestroyActionMode(
                                    ActionMode mode) {
                                view.setSelected(false);
                                mCurrentActionMode = null;
                                explorerListView.setEnabled(true);
                            }

                        });
                view.setSelected(true);*/
                return true;
            }

        };
    }

    public void listContents(BaseFile dir, BaseFile previousOpenDirChild) {
        log("listContents(" + dir + ", " + previousOpenDirChild);

        if (dir == null || !dir.isDirectory() || dir.isProtected()) {
            return;
        }
        if (previousOpenDirChild != null) {
            this.previousOpenDirChild = BtFileStorage.getInstance().get(previousOpenDirChild.getAbsolutePath());
        } else {
            this.previousOpenDirChild = null;
        }
        new Finder(this).execute(dir);
    }

    private static void log(String log) {
        if (DBG) {
            android.util.Log.d(TAG, log);
        }
    }

    public void setCurrentDirAndChildren(BaseFile currentDir, List<BluetoothFtpObject> childFilesList) {
        // TODO
    }
}
