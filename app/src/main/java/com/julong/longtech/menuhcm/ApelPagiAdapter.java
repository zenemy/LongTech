package com.julong.longtech.menuhcm;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.julong.longtech.DatabaseHelper;
import com.julong.longtech.DialogHelper;
import com.julong.longtech.R;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.julong.longtech.menuhcm.ApelPagi.REQUEST_IMAGE_CAPTURE;
import static com.julong.longtech.menuhcm.ApelPagi.btnActionApel;
import static com.julong.longtech.menuhcm.ApelPagi.byteGambarApel;
import static com.julong.longtech.menuhcm.ApelPagi.dataProcess;
import static com.julong.longtech.menuhcm.ApelPagi.nodocApel;
import static com.julong.longtech.menuhcm.ApelPagi.scanBarcode;

public class ApelPagiAdapter extends ArrayAdapter<ApelPagiList> {

    //storing all the names in the list
    private List<ApelPagiList> listApelPagi;
    DatabaseHelper dbhelper;
    DialogHelper dialogHelper;
    String tipeTidakHadir;

    private Context context;
    //constructor
    public ApelPagiAdapter(Context context, List<ApelPagiList> listApelPagi) {
        super(context, R.layout.apelpagi_adapter, listApelPagi);
        this.context = context;
        this.listApelPagi = listApelPagi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dbhelper = new DatabaseHelper(context);
        dialogHelper = new DialogHelper(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        final View listViewItem = inflater.inflate(R.layout.apelpagi_adapter, null, true);
        final TextView tvEmpName = (TextView) listViewItem.findViewById(R.id.tvEmpApelPagiLV);
        final TextView tvPositionName = (TextView) listViewItem.findViewById(R.id.tvJabatanApelPagiLv);
        final TextView tvJudulKehadiran = (TextView) listViewItem.findViewById(R.id.tvJudulKehadiranApelLv);
        final TextView tvJenisKehadiran = (TextView) listViewItem.findViewById(R.id.tvJenisKehadiranApelPagiLv);
        final TextView tvWaktuKehadiran = (TextView) listViewItem.findViewById(R.id.tvWaktuKehadiranApelPagiLv);
        final ImageView imgViewAnggota = (ImageView) listViewItem.findViewById(R.id.imgViewAdapterApel);
        final ApelPagiList listApel = listApelPagi.get(position);

        tvEmpName.setText(listApel.getEmployeeName() + listApel.getShiftcode());
        tvPositionName.setText(listApel.getPositionName() + " " + listApel.getUnitCode());

        try {
            if (listApel.getImgApel() != null) {
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(listApel.getImgApel(), 0, listApel.getImgApel().length);
                imgViewAnggota.setBackground(null);
                imgViewAnggota.setImageBitmap(compressedBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listApel.getWaktuAbsem() != null) {
            tvJenisKehadiran.setText(listApel.getMetodeAbsen());
            tvWaktuKehadiran.setText(listApel.getWaktuAbsem());
        } else {
            tvJenisKehadiran.setTextColor(Color.RED);
            tvJudulKehadiran.setTextColor(Color.RED);
        }

        //Check if briefing hav been completed
        if (dbhelper.check_existingapel(4).equals("0") || dbhelper.check_existingapel(4).equals("1")) {

        }
        else {
            listViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (byteGambarApel == null) {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("Foto apel dahulu").setConfirmText("OK").show();
                    }
                    else {
                        dialogHelper.dlgApelAnggota(listApel.getEmployeeName(), listApel.getEmployeeCode(), listApel.getItemData());
                    }
                }
            });
        }

        return listViewItem;
    }



}
