package com.julong.longtech.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterHomeInfo extends ArrayAdapter<ParamListHomeInfo> {

    //storing all the names in the list
    private List<ParamListHomeInfo> listHomeInfos;
    DatabaseHelper dbhelper;

    //context object
    private Context context;
    //constructor
    public AdapterHomeInfo(Context context, int resource, List<ParamListHomeInfo> listHomeInfos) {
        super(context, resource, listHomeInfos);
        this.context = context;
        this.listHomeInfos = listHomeInfos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dbhelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        final View listViewItem = inflater.inflate(R.layout.item_lvworkinfohome, null, true);
        final TextView tvWorkType = (TextView) listViewItem.findViewById(R.id.tvWorkTypeInfoHome);
        final TextView tvWorkStatus = (TextView) listViewItem.findViewById(R.id.tvWorkStatusInfoHome);
        final ImageView imgUploaded = (ImageView) listViewItem.findViewById(R.id.imgUploadInfoHome);
        final ParamListHomeInfo listInfo = listHomeInfos.get(position);

        tvWorkType.setText(listInfo.getDatatype());

        if (listInfo.getWorkstatus().equals("0")) {
            tvWorkStatus.setText("Pekerjaan belum diupload");
            imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
        }
        else if (listInfo.getTransactiondate().equals("") && listInfo.getWorkstatus().equals("")) {
            tvWorkStatus.setText("Pekerjaan belum selesai");
            tvWorkStatus.setTextColor(Color.RED);
            imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
        }
        else {
            tvWorkStatus.setText("Pekerjaan sudah diupload");
            imgUploaded.setImageResource(R.drawable.bluetick);
        }

        return listViewItem;
    }

}
