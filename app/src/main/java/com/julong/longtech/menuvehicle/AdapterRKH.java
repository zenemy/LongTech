package com.julong.longtech.menuvehicle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.julong.longtech.R;
import com.julong.longtech.DatabaseHelper;

import java.util.Date;
import java.util.List;

import static com.julong.longtech.menuvehicle.RencanaKerjaHarian.loadListViewRKH;

public class AdapterRKH extends ArrayAdapter<ListParamRKH> {

    private List<ListParamRKH> rkhParams;
    private final Context context;
    public static Dialog dlgFuelRKH;
    DatabaseHelper dbhelper;

    public AdapterRKH(Context context, int list, List<ListParamRKH> rkhParams) {
        super(context, R.layout.item_lvrkh, rkhParams);
        this.context = context;
        this.rkhParams = rkhParams;
    }

    static class ViewHolder {
        protected LinearLayout layoutItemLvRKH, layoutHelper1LvRKH;
        protected TextView tvVehicleCodeLvRKH, tvShiftLvRKH, tvDriverNameLvRKH, tvHelper1NameLvRKH, tvBensinLvRKH;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        dbhelper = new DatabaseHelper(getContext());
        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(context);
            convertView = inflator.inflate(R.layout.item_lvrkh, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.layoutItemLvRKH = (LinearLayout) convertView.findViewById(R.id.layoutItemLvRKH);
            viewHolder.layoutHelper1LvRKH = (LinearLayout) convertView.findViewById(R.id.layoutHelper1LvRKH);
            viewHolder.tvVehicleCodeLvRKH = (TextView) convertView.findViewById(R.id.tvVehicleCodeLvRKH);
            viewHolder.tvDriverNameLvRKH = (TextView) convertView.findViewById(R.id.tvDriverNameLvRKH);
            viewHolder.tvHelper1NameLvRKH = (TextView) convertView.findViewById(R.id.tvHelper1NameLvRKH);
            viewHolder.tvBensinLvRKH = (TextView) convertView.findViewById(R.id.tvKebutuhanBBM);
            viewHolder.tvShiftLvRKH = (TextView) convertView.findViewById(R.id.tvShiftLvRKH);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tvVehicleCodeLvRKH, viewHolder.tvVehicleCodeLvRKH);
            convertView.setTag(R.id.tvDriverNameLvRKH, viewHolder.tvDriverNameLvRKH);
            convertView.setTag(R.id.tvHelper1NameLvRKH, viewHolder.tvHelper1NameLvRKH);
            convertView.setTag(R.id.tvKebutuhanBBM, viewHolder.tvBensinLvRKH);
            convertView.setTag(R.id.tvShiftLvRKH, viewHolder.tvShiftLvRKH);

        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvVehicleCodeLvRKH.setText(rkhParams.get(position).getVehicleCode());
        viewHolder.tvShiftLvRKH.setText(rkhParams.get(position).getShiftkerja());
        viewHolder.tvDriverNameLvRKH.setText(dbhelper.get_empname(rkhParams.get(position).getDrivername()));
        viewHolder.tvBensinLvRKH.setText(String.valueOf(rkhParams.get(position).getKebutuhanBBM()));

        if (rkhParams.get(position).getHelper1Name() == null || rkhParams.get(position).getHelper1Name().equals("")) {
            viewHolder.layoutHelper1LvRKH.setVisibility(View.GONE);

        }
        else {
            viewHolder.tvHelper1NameLvRKH.setText(dbhelper.get_empname(rkhParams.get(position).getHelper1Name()));
        }

        if (rkhParams.get(position).getKebutuhanBBM() == 0) {
            viewHolder.layoutItemLvRKH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlgFuelRKH = new Dialog(getContext());
                    dlgFuelRKH.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dlgFuelRKH.setContentView(R.layout.dialog_insertbbm_rkh);
                    dlgFuelRKH.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    Window windowFuelRKH = dlgFuelRKH.getWindow();
                    dlgFuelRKH.setCanceledOnTouchOutside(false);
                    windowFuelRKH.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView tvHeaderFuelDlgRKH = dlgFuelRKH.findViewById(R.id.tvHeaderFuelDlgRKH);
                    Button btnBackDlgFuelRKH = dlgFuelRKH.findViewById(R.id.btnBackDlgFuelRKH);
                    Button btnOkDlgFuelRKH = dlgFuelRKH.findViewById(R.id.btnOkDlgFuelRKH);
                    EditText etFuelDlgRKH = dlgFuelRKH.findViewById(R.id.etFuelDlgRKH);

                    tvHeaderFuelDlgRKH.setText("INPUT BBM " + rkhParams.get(position).getVehicleCode());
                    btnBackDlgFuelRKH.setOnClickListener(view1 -> dlgFuelRKH.dismiss());
                    btnOkDlgFuelRKH.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dbhelper.update_fueldlg_rkh(RencanaKerjaHarian.nodocRKH, rkhParams.get(position).getVehicleCode(),
                                    RencanaKerjaHarian.selectedDateRKH, rkhParams.get(position).getShiftkerja(), etFuelDlgRKH.getText().toString());
                            dlgFuelRKH.dismiss();
                            loadListViewRKH();
                        }
                    });
                    dlgFuelRKH.show();
                }
            });

        } else {
            viewHolder.layoutItemLvRKH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), InputRincianRKH.class);
                    intent.putExtra("nodoc", RencanaKerjaHarian.nodocRKH);
                    intent.putExtra("vehiclecode", rkhParams.get(position).getVehicleCode());
                    intent.putExtra("vehiclename", dbhelper.get_vehiclename(0, rkhParams.get(position).getVehicleCode()));
                    intent.putExtra("shiftkerja", viewHolder.tvShiftLvRKH.getText().toString());
                    intent.putExtra("drivercode", rkhParams.get(position).getDrivername());
                    intent.putExtra("drivername", viewHolder.tvDriverNameLvRKH.getText().toString());
                    context.startActivity(intent);
                }
            });
        }



        return convertView;
    }

}