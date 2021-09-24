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
import static com.julong.longtech.menuhcm.ApelPagi.selectedEmp;
import static com.julong.longtech.menuhcm.ApelPagi.selectedItemData;
import static com.julong.longtech.menuhcm.ApelPagi.selectedJabatan;
import static com.julong.longtech.menuhcm.ApelPagi.selectedShift;
import static com.julong.longtech.menuhcm.ApelPagi.selectedUnit;

public class ApelPagiAdapter extends ArrayAdapter<ApelPagiList> {

    public static Dialog dlgMetodeAbsen, dlgTidakHadir;

    //storing all the names in the list
    private List<ApelPagiList> listApelPagi;
    DatabaseHelper dbhelper;
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

        dbhelper = new DatabaseHelper(getContext());
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

        if (dbhelper.check_existingapel(4, selectedShift).equals("0")
                || dbhelper.check_existingapel(4, selectedShift).equals("1")) {

        }
        else {
            listViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dbhelper.check_existingapel(0, selectedShift).equals("1") && (dbhelper.check_existingapel(5, selectedShift).equals("0")
                            || dbhelper.check_existingapel(5, selectedShift).equals("1"))) {

                    }
                    else if (byteGambarApel == null) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Foto apel dahulu").setConfirmText("OK").show();
                    }
                    else {
                        //Show method dialog
                        dlgMetodeAbsen = new Dialog(getContext());
                        dlgMetodeAbsen.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dlgMetodeAbsen.setContentView(R.layout.dialog_metodeabsen);
                        dlgMetodeAbsen.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        Window windowMetodeAbsen = dlgMetodeAbsen.getWindow();
                        windowMetodeAbsen.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        TextView tvHeaderDlgMetode = dlgMetodeAbsen.findViewById(R.id.tvHeaderDlgMetodeApel);
                        Button btnApelQR = dlgMetodeAbsen.findViewById(R.id.btnApelQR);
                        Button btnApelFoto = dlgMetodeAbsen.findViewById(R.id.btnApelFoto);
                        Button btnApelTidakHadir = dlgMetodeAbsen.findViewById(R.id.btnApelTidakHadir);
                        Button btnBackDlgApel = dlgMetodeAbsen.findViewById(R.id.btnBackDlgApel);
                        tvHeaderDlgMetode.setText(listApel.getEmployeeName());
                        dlgMetodeAbsen.show();

                        btnBackDlgApel.setOnClickListener(view1 -> dlgMetodeAbsen.dismiss());

                        // Metode Foto
                        btnApelFoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dataProcess = 2;
                                selectedEmp = listApel.getEmployeeCode();
                                selectedJabatan = listApel.getPositionCode();
                                selectedUnit = listApel.getUnitCode();
                                selectedItemData = listApel.getItemData();
                                dlgMetodeAbsen.dismiss();
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                try {
                                    ((ApelPagi) context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                } catch (ActivityNotFoundException e) {
                                    // display error state to the user
                                }
                            }
                        });

                        // Metode Scan
                        btnApelQR.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dataProcess = 3;
                                selectedEmp = listApel.getEmployeeCode();
                                selectedItemData = listApel.getItemData();
                                dlgMetodeAbsen.dismiss();
                                scanBarcode((ApelPagi) context);
                            }
                        });

                        // Tidak Hadir
                        btnApelTidakHadir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dlgMetodeAbsen.dismiss();

                                // Show dialog tidak hadir
                                dlgTidakHadir = new Dialog(getContext());
                                dlgTidakHadir.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dlgTidakHadir.setContentView(R.layout.dialog_apeltdkhadir);
                                dlgTidakHadir.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                Window windowTdkHadir = dlgTidakHadir.getWindow();
                                windowTdkHadir.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                TextView tvHeaderDlgMetode = dlgTidakHadir.findViewById(R.id.tvHeaderDlgTdkHadirApel);

                                // Radio selection tdk hadir
                                RadioButton radioApelCuti = dlgTidakHadir.findViewById(R.id.radioApelCuti);
                                RadioButton radioApelP1 = dlgTidakHadir.findViewById(R.id.radioApelP1);
                                RadioButton radioApelTK = dlgTidakHadir.findViewById(R.id.radioApelTK);
                                RadioButton radioApelSakit = dlgTidakHadir.findViewById(R.id.radioApelSakit);
                                RadioButton radioApelP2 = dlgTidakHadir.findViewById(R.id.radioApelP2);
                                RadioButton radioApelMangkir = dlgTidakHadir.findViewById(R.id.radioApelMangkir);

                                EditText etNoteDlgTdkHadir = dlgTidakHadir.findViewById(R.id.etNoteDlgTdkHadir);
                                Button btnKonfirmTdkHadir = dlgTidakHadir.findViewById(R.id.btnApelConfirmTdkHadir);
                                Button btnBackTdkHadir = dlgTidakHadir.findViewById(R.id.btnBackDlgApelTdkHadir);
                                tvHeaderDlgMetode.setText(listApel.getEmployeeName());
                                dlgTidakHadir.show();

                                selectedEmp = listApel.getEmployeeCode();
                                selectedItemData = listApel.getItemData();

                                radioApelCuti.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        tipeTidakHadir = "CT";
                                        radioApelCuti.setChecked(true);
                                        radioApelP1.setChecked(false);
                                        radioApelMangkir.setChecked(false);
                                        radioApelTK.setChecked(false);
                                        radioApelP2.setChecked(false);
                                        radioApelSakit.setChecked(false);
                                    }
                                });

                                radioApelP1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        tipeTidakHadir = "P1";
                                        radioApelP1.setChecked(true);
                                        radioApelCuti.setChecked(false);
                                        radioApelMangkir.setChecked(false);
                                        radioApelTK.setChecked(false);
                                        radioApelP2.setChecked(false);
                                        radioApelSakit.setChecked(false);
                                    }
                                });

                                radioApelP2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        tipeTidakHadir = "P2";
                                        radioApelP2.setChecked(true);
                                        radioApelP1.setChecked(false);
                                        radioApelMangkir.setChecked(false);
                                        radioApelTK.setChecked(false);
                                        radioApelCuti.setChecked(false);
                                        radioApelSakit.setChecked(false);
                                    }
                                });

                                radioApelSakit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        tipeTidakHadir = "S1";
                                        radioApelSakit.setChecked(true);
                                        radioApelP1.setChecked(false);
                                        radioApelMangkir.setChecked(false);
                                        radioApelTK.setChecked(false);
                                        radioApelP2.setChecked(false);
                                        radioApelCuti.setChecked(false);
                                    }
                                });

                                radioApelMangkir.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        tipeTidakHadir = "MK";
                                        radioApelMangkir.setChecked(true);
                                        radioApelP1.setChecked(false);
                                        radioApelCuti.setChecked(false);
                                        radioApelTK.setChecked(false);
                                        radioApelP2.setChecked(false);
                                        radioApelSakit.setChecked(false);
                                    }
                                });

                                radioApelTK.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        tipeTidakHadir = "TK";
                                        radioApelTK.setChecked(true);
                                        radioApelP1.setChecked(false);
                                        radioApelMangkir.setChecked(false);
                                        radioApelCuti.setChecked(false);
                                        radioApelP2.setChecked(false);
                                        radioApelSakit.setChecked(false);
                                    }
                                });

                                btnKonfirmTdkHadir.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (tipeTidakHadir != null) {
                                            dbhelper.updateapel_tidakhadir(nodocApel, selectedItemData, selectedEmp,
                                                    tipeTidakHadir, etNoteDlgTdkHadir.getText().toString());
                                            btnActionApel.performClick();
                                            dlgTidakHadir.dismiss();

                                            final SweetAlertDialog dlgStartOK = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                                            dlgStartOK.setTitleText("Tidak Hadir").setContentText(dbhelper.get_empname(selectedEmp)).setConfirmText("OK").show();
                                        }
                                        else {
                                            Toast.makeText(context, "Pilih alasan tidak hadir", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });

                                btnBackTdkHadir.setOnClickListener(view12 -> {
                                    dlgTidakHadir.dismiss();
                                    dlgMetodeAbsen.show();
                                });
                            }
                        });

                    }
                }
            });
        }


        return listViewItem;
    }



}
