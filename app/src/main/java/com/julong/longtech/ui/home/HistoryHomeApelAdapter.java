package com.julong.longtech.ui.home;

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

public class HistoryHomeApelAdapter extends ArrayAdapter<ListHistoryHomeApel> {

    //storing all the names in the list
    private List<ListHistoryHomeApel> listHistoryApel;
    DatabaseHelper dbhelper;

    private Context context;
    //constructor
    public HistoryHomeApelAdapter(Context context, int resource, List<ListHistoryHomeApel> listHistoryApel) {
        super(context, resource, listHistoryApel);
        this.context = context;
        this.listHistoryApel = listHistoryApel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dbhelper = new DatabaseHelper(getContext());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        final View listViewItem = inflater.inflate(R.layout.fragment_apelhistory, null, true);
        final TextView tvNoDoc = (TextView) listViewItem.findViewById(R.id.tvNodocHistoryHomeApel);
        final TextView tvTglReport = (TextView) listViewItem.findViewById(R.id.tvDateHistoryHomeApel);
        final TextView tvTimeReport = (TextView) listViewItem.findViewById(R.id.tvHistoryHomeTimeApel);
        final TextView tvEmpNameApel = (TextView) listViewItem.findViewById(R.id.tvEmpHistoryHomeApel);
        final TextView tvPositionApel = (TextView) listViewItem.findViewById(R.id.tvPositionHistoryHomeApel);
        final TextView tvKehadiranApel = (TextView) listViewItem.findViewById(R.id.tvKehadiranEmpApelHistoryHome);
        final TextView tvMetodeAbsen = (TextView) listViewItem.findViewById(R.id.tvMetodeAbsenHistoryHome);
        final ImageView imgViewApel = (ImageView) listViewItem.findViewById(R.id.imgHistoryHomeApel);
        final ImageView imgUploaded = (ImageView) listViewItem.findViewById(R.id.imgUploadHistoryHomeApel);
        final ListHistoryHomeApel reportsList = listHistoryApel.get(position);

        tvNoDoc.setText(reportsList.getDocumentNumber());
        tvTglReport.setText(reportsList.getTglApel());
        tvTimeReport.setText(reportsList.getWaktuApel());
        tvEmpNameApel.setText(reportsList.getEmployeeName());
        tvPositionApel.setText(reportsList.getEmpPosition());
        tvKehadiranApel.setText(reportsList.getKehadiranEmp());

        if (reportsList.getIsUploaded() == 0 ) {
            imgUploaded.setImageResource(R.drawable.ic_baseline_accesstime_24);
        }
        else {
            imgUploaded.setImageResource(R.drawable.bluetick);
        }

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

        return listViewItem;
    }

}
