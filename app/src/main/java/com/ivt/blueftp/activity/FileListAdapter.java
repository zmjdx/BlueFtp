package com.ivt.blueftp.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivt.blueftp.module.FileListEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by android on 15-9-28.
 */
public class FileListAdapter extends BaseAdapter {
    private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static class ViewHolder
    {
        public TextView resName;
        public ImageView resIcon;
        public TextView resMeta;
        public TextView resTime;
    }

    private static final String TAG = FileListAdapter.class.getName();

    private FileListActivity mContext;
    private List<FileListEntry> files;
    private LayoutInflater mInflater;

    public FileListAdapter(FileListActivity context, List<FileListEntry> files) {
        super();
        mContext = context;
        this.files = files;
        mInflater = mContext.getLayoutInflater();

    }


    @Override
    public int getCount() {
        if(files == null)
        {
            return 0;
        }
        else
        {
            return files.size();
        }
    }

    @Override
    public Object getItem(int arg0) {

        if(files == null)
            return null;
        else
            return files.get(arg0);
    }

    public List<FileListEntry> getItems()
    {
        return files;
    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
/*
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.explorer_item, parent, false);
            holder = new ViewHolder();
            holder.resName = (TextView)convertView.findViewById(R.id.explorer_resName);
            holder.resMeta = (TextView)convertView.findViewById(R.id.explorer_resMeta);
            holder.resIcon = (ImageView)convertView.findViewById(R.id.explorer_resIcon);
            holder.resTime = (TextView) convertView.findViewById(R.id.id_tme);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        final FileListEntry currentFile = files.get(position);
        holder.resName.setText(currentFile.getName());
        holder.resIcon.setImageDrawable(Util.getIcon(mContext, currentFile.getPath()));
        String meta = Util.prepareMeta(currentFile, mContext);
        holder.resMeta.setText(meta);

        if (currentFile.getLastModified() != null) {
            holder.resTime.setText(df.format(currentFile.getLastModified()));
        }
*/
        return convertView;
    }
}
