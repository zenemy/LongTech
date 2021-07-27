package com.julong.longtech.menuhcm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class ApelPagiAdapter extends ArrayAdapter<ApelPagiList> {

    //storing all the names in the list
    private List<ApelPagiList> listApelPagi;
    DatabaseHelper dbhelper;

    //context object
    private Context context;
    //constructor
    public ApelPagiAdapter(Context context, int resource, List<ApelPagiList> listApelPagi) {
        super(context, resource, listApelPagi);
        this.context = context;
        this.listApelPagi = listApelPagi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dbhelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        final View listViewItem = inflater.inflate(R.layout.apelpagi_adapter, null, true);
        final TextView tvEmpName = (TextView) listViewItem.findViewById(R.id.tvEmpApelPagiLV);
        final TextView tvPositionName = (TextView) listViewItem.findViewById(R.id.tvJabatanApelPagiLv);
        final TextView tvJenisKehadiran = (TextView) listViewItem.findViewById(R.id.tvJenisKehadiranApelPagiLv);
        final ApelPagiList listApel = listApelPagi.get(position);

        return listViewItem;
    }

}
