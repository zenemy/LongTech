package com.julong.longtech.menusetup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.julong.longtech.R;
import com.julong.longtech.DatabaseHelper;

import java.util.Date;
import java.util.List;


public class UploadAdapter extends ArrayAdapter<UploadParam> {

    private List<UploadParam> uploadParams;
    private final Activity context;
    public static TextView data;
    DatabaseHelper dbhelper;
    public static CheckBox checkBox;

    public UploadAdapter(Activity context, int download_list, List<UploadParam> uploadParams) {
        super(context, R.layout.upload_list, uploadParams);
        this.context = context;
        this.uploadParams = uploadParams;
        this.uploadParams = uploadParams;
    }

    static class ViewHolder {
        protected TextView tvJudulUpload, tvSubJudulUpload;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.upload_list, null);

            ViewHolder viewHolder = new UploadAdapter.ViewHolder();
            viewHolder.tvJudulUpload = (TextView) convertView.findViewById(R.id.tvJudulUploadData);
            viewHolder.tvSubJudulUpload = (TextView) convertView.findViewById(R.id.tvSubJudulUploadData);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tvJudulUploadData, viewHolder.tvJudulUpload);
            convertView.setTag(R.id.tvSubJudulUploadData, viewHolder.tvSubJudulUpload);

        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvJudulUpload.setText(uploadParams.get(position).getNama());
        viewHolder.tvSubJudulUpload.setText("Jumlah Data : " +uploadParams.get(position).getJumlah_data() + " Data");

        return convertView;
    }

}