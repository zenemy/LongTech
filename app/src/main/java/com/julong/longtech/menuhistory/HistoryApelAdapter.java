package com.julong.longtech.menuhistory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.R;

import java.util.List;

public class HistoryApelAdapter extends ArrayAdapter<ListHistoryApel> {

    //storing all the names in the list
    private List<ListHistoryApel> listHistoryApel;
    DatabaseHelper dbhelper;

    private Context context;
    //constructor
    public HistoryApelAdapter(Context context, int resource, List<ListHistoryApel> listHistoryApel) {
        super(context, resource, listHistoryApel);
        this.context = context;
        this.listHistoryApel = listHistoryApel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dbhelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        final View listViewItem = inflater.inflate(R.layout.listview_historyapelpagi, null, true);
        final TextView tvNoDoc = (TextView) listViewItem.findViewById(R.id.tvNodocHistoryApel);
        final TextView tvTglReport = (TextView) listViewItem.findViewById(R.id.tvHistoryDateApel);
        final TextView tvTimeReport = (TextView) listViewItem.findViewById(R.id.tvHistoryTimeApel);
        final TextView tvEmpNameApel = (TextView) listViewItem.findViewById(R.id.tvEmpApelHistory);
        final TextView tvPositionApel = (TextView) listViewItem.findViewById(R.id.tvPositionApelHistory);
        final TextView tvKehadiranApel = (TextView) listViewItem.findViewById(R.id.tvKehadiranApelHistory);
        final TextView tvMetodeAbsen = (TextView) listViewItem.findViewById(R.id.tvMetodeAbsenApelHistory);
        final ImageView imgViewApel = (ImageView) listViewItem.findViewById(R.id.imgHistoryApelPagi);
        final ImageView imgUploaded = (ImageView) listViewItem.findViewById(R.id.imgUploadHistoryApel);
        final ListHistoryApel reportsList = listHistoryApel.get(position);

        tvNoDoc.setText(reportsList.getDocumentNumber());
        tvTglReport.setText(reportsList.getTglApel());
        tvTimeReport.setText(reportsList.getWaktuApel());
        tvEmpNameApel.setText(reportsList.getEmployeeName());
        tvPositionApel.setText(reportsList.getEmpPosition());
        tvKehadiranApel.setText(reportsList.getKehadiranEmp());

        try {
            if (reportsList.getFotoApel() != null) {
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(reportsList.getFotoApel(), 0, reportsList.getFotoApel().length);
                imgViewApel.setBackground(null);
                imgViewApel.setImageBitmap(compressedBitmap);
            }
            if (reportsList.getMetodeAbsen() != null) {
                tvMetodeAbsen.setText("(" + reportsList.getMetodeAbsen() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (reportsList.getIsUploaded() == 0 ) {
            imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
        }
        else {
            imgUploaded.setImageResource(R.drawable.bluetick);
        }

        return listViewItem;
    }

}
