package com.mtpv.info.bdv.biodriververification.update;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leopard.api.BaudChange;
import com.leopard.api.FPS;
import com.leopard.api.FpsConfig;
import com.leopard.api.HexString;
import com.leopard.api.Setup;
import com.mtpv.info.bdv.biodriververification.Act_BTDiscovery;
import com.mtpv.info.bdv.biodriververification.Document_verification;
import com.mtpv.info.bdv.biodriververification.GlobalPool;
import com.mtpv.info.bdv.biodriververification.LoginActivity;
import com.mtpv.info.bdv.biodriververification.MenuBorad;
import com.mtpv.info.bdv.biodriververification.Next_ScreenActivity;
import com.mtpv.info.bdv.biodriververification.R;
import com.mtpv.info.bdv.biodriververification.ServiceHelper;
import com.mtpv.info.bdv.biodriververification.Welcome;
import com.mtpv.info.bdv.biodriververification.bluetooth.BluetoothComm;
import com.mtpv.info.bdv.biodriververification.storage.Constants;
import com.mtpv.info.bdv.biodriververification.storage.Storage;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import java.io.File;

import android.content.ActivityNotFoundException;


import android.os.Environment;



import static android.widget.Toast.LENGTH_LONG;
import static com.mtpv.info.bdv.biodriververification.Registration.PROGRESS_DIALOG;

public class Update_FPS extends AppCompatActivity {


    RadioGroup rg_edityesno ;
    RadioButton rd_lic , rd_appid ;
    ImageView  logout ;

    public static EditText applID   ;
    Button get_btn ;
    public static String radio_btn_edit = null ;


     /*Peraonal Information*/

    public  static TextView  welcome, per_appl_id ,per_name , per_Fname , per_dob , per_address , per_contact_one ,getPer_contact_two ,
            getGetPer_contact_email , per_bookcentre ,getPer_bookdate ;
    public static ImageView img_other_doc ;

    /* LICENCE DETAILS */
    public static ImageView  imgv_personal, licence_image ;
    public static TextView tv_licnce_ownername_spot ,tvt_getdlnum;
    public static TextView tv_lcnce_father_name_spot;
    public static TextView tv_lcnce_dl_points;
    public static TextView tv_lcnce_address_spot;
    public static TextView tv_dl_no;

    /* AADHAR public static TextView */
    public static TextView tv_aadhar_header , getaadhara;
    public static TextView tv_aadhar_user_name;
    public static TextView tv_aadhar_care_off;
    public static TextView tv_aadhar_address;
    public static TextView tv_aadhar_mobile_number;
    public static TextView tv_aadhar_gender;
    public static TextView tv_aadhar_dob;
    public static TextView tv_aadhar_uid;
    public static TextView tv_aadhar_eid;
    public static TextView tv_violation_amnt;
    public static ImageView img_aadhar_image, imgLicenseData;

    LinearLayout rewalLayout ,  ll_aadhar_layout  ,  llayout_aadhara  ,llayout_dlnum  , lllicences_dldetails , ll_mainlayout;

    Button back ,next  ;

    CheckBox checkbox ;

    /* DATE & TIME START */
    SimpleDateFormat date_format;
    Calendar calendar;
    int present_date;
    int present_month;
    int present_year;

    int present_hour;
    int present_minutes;
    String macAddress = "";
    String present_date_toSend = "";
    StringBuffer present_time_toSend;
    public static String date ,Current_Date;
	/* DATE & TIME END */

	public static String [] licence_details_master  ,aadhra_details_master  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__fps);

        Log.i("Welcome***","--->UpdataClass");

        welcome=(TextView)findViewById(R.id.updata_welcome);
        rg_edityesno=(RadioGroup)findViewById(R.id.rg_edityesno);

        rd_lic=(RadioButton)findViewById(R.id.rd_lic);
        rd_appid=(RadioButton)findViewById(R.id.rd_appID);

        applID=(EditText)findViewById(R.id.edit_dl_number);

        rewalLayout=(LinearLayout)findViewById(R.id.rewalLayout);

        get_btn=(Button)findViewById(R.id.get_details_btn);
        logout = (ImageView)findViewById(R.id.logout_bt);

        imgv_personal = (ImageView)findViewById(R.id.imgv_personal);
        img_other_doc = (ImageView)findViewById(R.id.imgv_oherdoc_spotchallan_xml);
        per_appl_id = (TextView) findViewById(R.id.personal_application_num);
        per_name = (TextView) findViewById(R.id.tvt_personname);
        per_Fname = (TextView) findViewById(R.id.tvt_personFname);
        per_dob = (TextView) findViewById(R.id.tvt_persondob);
        per_address = (TextView) findViewById(R.id.tvt_personaddress);
        per_contact_one = (TextView) findViewById(R.id.tvt_person_phone);
        getPer_contact_two = (TextView) findViewById(R.id.tvt_person_phone_one);
        getGetPer_contact_email = (TextView) findViewById(R.id.tvt_person_emaile);
        per_bookcentre = (TextView) findViewById(R.id.tvt_person_slot_book_centre);
        getPer_bookdate = (TextView) findViewById(R.id.tvt_person_slot_book_date);


        /* LICENCE COMPLETE DETAILS */
        tvt_getdlnum = (TextView)findViewById(R.id.tvt_getdlnum);
        licence_image = (ImageView) findViewById(R.id.imgv_licence_spotchallan_xml);
        tv_licnce_ownername_spot = (TextView) findViewById(R.id.tvlcnceownername_spotchallan_xml);
        tv_lcnce_father_name_spot = (TextView) findViewById(R.id.tvlcnce_fname_spotchallan_xml);
        tv_lcnce_address_spot = (TextView) findViewById(R.id.tv_lcnce_Address_spotchallan_xml);
        tv_lcnce_dl_points = (TextView) findViewById(R.id.tv_lcnce_dl_points_spotchallan_xml);
        tv_dl_no = (TextView) findViewById(R.id.dl_no_spotchallan_xml);

        /* AADHRA DETAILS */
        ll_aadhar_layout = (LinearLayout) findViewById(R.id.ll_aadhardetails_rtadetails_xml);
        llayout_aadhara = (LinearLayout) findViewById(R.id.llayout_aadhara);
        llayout_dlnum = (LinearLayout) findViewById(R.id.llayout_dlnum);
        lllicences_dldetails = (LinearLayout)findViewById(R.id.rl_licences_rtadetails_xml);
        ll_mainlayout = (LinearLayout)findViewById(R.id.ll_mainlayout);

        getaadhara = (TextView)findViewById(R.id.tvt_getaadhara);
        tv_aadhar_user_name = (TextView) findViewById(R.id.tvaadharname_spotchallan_xml);
        tv_aadhar_care_off = (TextView) findViewById(R.id.tvcareof_spotchallan_xml);
        tv_aadhar_address = (TextView) findViewById(R.id.tvaddress_spotchallan_xml);
        tv_aadhar_mobile_number = (TextView) findViewById(R.id.tvmobilenumber_spotchallan_xml);
        tv_aadhar_gender = (TextView) findViewById(R.id.tvgender_spotchallan_xml);
        tv_aadhar_dob = (TextView) findViewById(R.id.tvdob_spotchallan_xml);
        tv_aadhar_uid = (TextView) findViewById(R.id.tvuid_spotchallan_xml);
        img_aadhar_image = (ImageView) findViewById(R.id.imgv_aadhar_photo_spotchallan_xml);
        img_other_doc = (ImageView) findViewById(R.id.imgv_oherdoc_spotchallan_xml);

        checkbox = (CheckBox)findViewById(R.id.checkbox_text);

        next = (Button)findViewById(R.id.next_btn);
        back = (Button)findViewById(R.id.back_btn);

        welcome.setText(""+ ServiceHelper.login_master[1]);


        Calendar c1 = Calendar.getInstance();
        int mSec = c1.get(Calendar.MILLISECOND);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strdate1 = sdf1.format(c1.getTime());
        date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        //id_date.setText(strdate1);
        Current_Date = strdate1;
        Log.i("Current_Date--------->",""+Current_Date);
        Log.i("Current_Date--------->",""+date);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertLogout();
            }
        });

        rd_lic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_btn_edit = "1" ;
                applID.setHint("Enter License Number");
                Log.i("rd_dl",""+radio_btn_edit);
                applID.setText("");

            }
        });

        rd_appid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("rd_appdl",""+radio_btn_edit);
                applID.setHint("Enter Application ID");
                radio_btn_edit = "0" ;
                applID.setText("");

            }
        });

        get_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    // Log.i("radio ",""+radio_btn_edit);
                    if ( (!rd_lic.isChecked()) && (!rd_appid.isChecked()) ){
                        showToast("Please Select Radio buttons");
                        // Toast.makeText(getApplicationContext(),"Please Select Radio buttons",Toast.LENGTH_LONG).show();
                    }else if (applID.getText().toString().equals("")){
                        applID.setError("Please Enter Dl No");
                        applID.requestFocus();
                    }else {

                        new getupdataDriverFPS().execute();
                        if (isOnline()) {
                            new Asyn_driver_licenceverification().execute();
                        }else {
                            showToast("Please Check Your Internet Connection...!");
                        }
                        rewalLayout.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),moduleicon_Activity.class);
                startActivity(i);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkbox.isChecked()) {
                    showToast("Please Check the Checkbox");
                } else if (!ServiceHelper.Fps_data_master[5].equalsIgnoreCase("")) {

                    if (isOnline()) {
                        new Asyntask_sendOTP_verification().execute();
                        Intent drfps = new Intent(getApplicationContext(), RenewalFPS_Activity.class);
                        startActivity(drfps);
                    } else {
                        showToast("Please Check Your Internet Connection...!");
                    }
            }else {
                    showToast("No details Found ");
                }

            }
        });
    }
//need to chage


    public class Asyntask_sendOTP_verification extends AsyncTask<Void, Void, String> {
        //   private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Update_FPS.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
           /* pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();*/
        }

        //public String sendOTP(String dlno,String mobileNo,String date);
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("Dl_Task", "Entered_One");
            Log.i("send date ", date.toUpperCase());

            ServiceHelper.sendOTP(""+Update_FPS.applID.getText().toString().trim(), ""+per_contact_one.getText().toString().trim(),""+date.toUpperCase());
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);

            if (!ServiceHelper.otp_data.equalsIgnoreCase("") || ServiceHelper.otp_data.equals("NA") || ServiceHelper.otp_data.equalsIgnoreCase("0") || "null".equalsIgnoreCase(ServiceHelper.otp_data)
                    ||ServiceHelper.otp_data.equals("anyType{}")){
                Log.i("",""+ServiceHelper.otp_data);
               showToast("No Data Found");
            }
        }
    }

    //Lic card
    public class Asyn_driver_licenceverification extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Update_FPS.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
           /* pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();*/
        }

        // public String getNewUser(String unitcode,String psname,String name,String glno,  String contactNo,String imei,String password);
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("Dl_Task", "Entered_One");
            ServiceHelper.Licence_Document_verif(applID.getText().toString().trim());
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);
            Log.i(" licence_data before-->", "" + ServiceHelper.licence_data);
            if (ServiceHelper.licence_data!=null) {
                if (ServiceHelper.licence_data.equalsIgnoreCase("") || ServiceHelper.licence_data.equals("NA") || ServiceHelper.licence_data.equalsIgnoreCase("0") || "null".equalsIgnoreCase(ServiceHelper.licence_data)
                        ||ServiceHelper.licence_data.equals("anyType{}")) {
                    showToast("No Data Found");

                    llayout_dlnum.setVisibility(View.VISIBLE);
                    lllicences_dldetails.setVisibility(View.GONE);
                    tvt_getdlnum.setText("" + applID.getText().toString().trim().toUpperCase());
                }else {
                    ll_mainlayout.setVisibility(View.VISIBLE);
                    licence_details_master = new String[0];
                    licence_details_master = ServiceHelper.licence_data.split("!");

                    llayout_dlnum.setVisibility(View.GONE);
                    lllicences_dldetails.setVisibility(View.VISIBLE);
                    tv_dl_no.setText("" + applID.getText().toString().trim().toUpperCase());
                    tv_licnce_ownername_spot.setText("" +licence_details_master[0]);
                    tv_lcnce_father_name_spot.setText("" +licence_details_master[1]);

                    tv_lcnce_address_spot.setText("" +licence_details_master[4] + "," +licence_details_master[3]);
                    Log.i("address data", "-->" +licence_details_master[2]);

                    //******* start driving license image display ******//*
                    if (licence_details_master[5].toString().trim().equals("")) {
                        licence_image.setImageResource(R.drawable.empty_profile_img);
                        Log.i("Lic_image**-->",""+licence_details_master[5]);
                    } else {
                        byte[] decodestring = Base64.decode(""+licence_details_master[5].toString().trim(), Base64.DEFAULT);
                        Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                        licence_image.setImageBitmap(decocebyte);
                    }
                }
                //******* end driving license image display ******//*

            }

        }
    }

        private boolean isOnline() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
//Personal regsitional Card

    public class getupdataDriverFPS extends AsyncTask<Void, Void, String> {

        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Update_FPS.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
           /* pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();*/
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //Log.i("Dl_Task", "Entered_One");
            ServiceHelper.updataFPS(""+radio_btn_edit,""+applID.getText().toString().trim().toUpperCase());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            removeDialog(PROGRESS_DIALOG);
            //pdia.dismiss();

            if (ServiceHelper.fpsdataforServices.equalsIgnoreCase("0")||ServiceHelper.fpsdataforServices.equalsIgnoreCase("")
                    ||ServiceHelper.fpsdataforServices.equalsIgnoreCase("NA")||ServiceHelper.fpsdataforServices.equalsIgnoreCase("anyType{}")){
                  showToast("NO Data Found");
            }else{
                ll_mainlayout.setVisibility(View.VISIBLE);
                per_appl_id.setText(""+ServiceHelper.Fps_data_master[0]);
                Log.i("aadharaaaacall","-->"+ServiceHelper.Fps_data_master[0]);

                if (!ServiceHelper.Fps_data_master[3].equalsIgnoreCase("")||!ServiceHelper.Fps_data_master[3].equalsIgnoreCase("NA")||!ServiceHelper.Fps_data_master[3].equalsIgnoreCase("anyType{}")){
                   Log.i("aadharaaaacall","-->"+ServiceHelper.Fps_data_master[3]);
                    if (isOnline()){
                        new Asyn_driver_aadhraverification().execute();
                    }else {
                        showToast("Please Check Your Internet Connection...!");
                    }

                }

                per_name.setText(""+ServiceHelper.Fps_data_master[1]);
                per_Fname.setText(""+ServiceHelper.Fps_data_master[2]);
                per_dob.setText(""+ServiceHelper.Fps_data_master[4]);
              //  per_address.setText(""+ServiceHelper.Fps_data_master[6]+","+""+ServiceHelper.Fps_data_master[7]+","+""+ServiceHelper.Fps_data_master[8]+","+""+ServiceHelper.Fps_data_master[9]+","+""+ServiceHelper.Fps_data_master[10]+","+""+ServiceHelper.Fps_data_master[11]+","+""+ServiceHelper.Fps_data_master[12]);
                per_address.setText(""+ServiceHelper.Fps_data_master[20]);
                per_contact_one.setText(""+ServiceHelper.Fps_data_master[5]);
               // getPer_contact_two.setText(""+ServiceHelper.Fps_data_master[6]);
                getGetPer_contact_email.setText(""+ServiceHelper.Fps_data_master[9]);
                per_bookcentre.setText(""+ServiceHelper.Fps_data_master[18]);
                getPer_bookdate.setText(""+ServiceHelper.Fps_data_master[17]);


                //******* start driving license image display *****
                if (null != ServiceHelper.Fps_data_master[24] && ServiceHelper.Fps_data_master[24].toString().trim().equals("0")) {
                    img_other_doc.setImageResource(R.drawable.empty_profile_img);
                    Log.i("other_Doc_img-->",""+ServiceHelper.Fps_data_master[19]);
                } else {
                    try {
                        byte[] decodestring = Base64.decode("" + ServiceHelper.Fps_data_master[24].toString().trim(),Base64.DEFAULT);
                        Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                        img_other_doc.setImageBitmap(decocebyte);
                    } catch (Exception e) {
                        e.printStackTrace();
                        img_other_doc.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
                    }
                }
                //******* End driving license image display *****

                //******* start driving othetr doc image display *****
                if (null != ServiceHelper.Fps_data_master[25] && ServiceHelper.Fps_data_master[25].toString().trim().equals("0")) {
                    imgv_personal.setImageResource(R.drawable.empty_profile_img);
                    Log.i("other_two*Doc_img-->",""+ServiceHelper.Fps_data_master[25]);
                } else {
                    try {
                        byte[] decodestring = Base64.decode("" + ServiceHelper.Fps_data_master[25].toString().trim(),
                                Base64.DEFAULT);
                        Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                        imgv_personal.setImageBitmap(decocebyte);
                    } catch (Exception e) {
                        e.printStackTrace();
                        imgv_personal.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
                    }
                }
                //******* end driving other doc image display *****

            }

        }
      }


    public class Asyn_driver_aadhraverification extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Update_FPS.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);

        }

        // public String getNewUser(String unitcode,String psname,String name,String glno,  String contactNo,String imei,String password);
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("Dl_Task", "Entered_One");
            ServiceHelper.aadhra_Document_verif(ServiceHelper.Fps_data_master[3], "");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);
            Log.i(" aadhar_details_data-->", "" + ServiceHelper.aadhar_data);
            //aadhar_data

            if (ServiceHelper.aadhar_data.equalsIgnoreCase("") || ServiceHelper.aadhar_data.equals("NA") || ServiceHelper.aadhar_data.equals("0") || "null".equalsIgnoreCase(ServiceHelper.aadhar_data)
                    || ServiceHelper.aadhar_data.equals("anyType{}")) {
                ll_aadhar_layout.setVisibility(View.GONE);
                llayout_aadhara.setVisibility(View.VISIBLE);
                getaadhara.setText(""+ServiceHelper.Fps_data_master[3]);
                Log.i("aadhar_data-->", "" + ServiceHelper.aadhar_data);
            }else {
                aadhra_details_master = new String[0];
                aadhra_details_master = ServiceHelper.licence_data.split("!");
                Log.i("AAdhramaster_","-->"+aadhra_details_master[0]);
                ll_aadhar_layout.setVisibility(View.VISIBLE);
                llayout_aadhara.setVisibility(View.GONE);
                try {
                    tv_aadhar_uid.setText("" + ServiceHelper.Fps_data_master[3]);
                    tv_aadhar_user_name.setText("" + aadhra_details_master[0] != null ? aadhra_details_master[0].trim().toUpperCase() : "NA");
                    tv_aadhar_care_off.setText("" + (!aadhra_details_master[1].equalsIgnoreCase("0") ? aadhra_details_master[1].trim().toUpperCase() : "NA"));
                    tv_aadhar_address.setText("" + (!aadhra_details_master[2].equalsIgnoreCase(null) ? aadhra_details_master[2].trim().toUpperCase() : "NA") + ", " + (!licence_details_master[3].equalsIgnoreCase(null) ? licence_details_master[3]
                            .trim().toUpperCase() + ", "
                            : "NA")
                            + (!aadhra_details_master[4]
                            .equalsIgnoreCase(null) ? aadhra_details_master[4]
                            .trim().toUpperCase() + ", "
                            : "NA")
                            + (!aadhra_details_master[5]
                            .equalsIgnoreCase(null) ? aadhra_details_master[5]
                            .trim().toUpperCase() + ", "
                            : "NA")
                            + (!aadhra_details_master[6]
                            .equalsIgnoreCase(null) ? aadhra_details_master[6]
                            .trim().toUpperCase() + ", "
                            : "NA")
                            + (!aadhra_details_master[7]
                            .equalsIgnoreCase(null) ? aadhra_details_master[7]
                            .trim().toUpperCase() + ", "
                            : "NA"));
                    tv_aadhar_mobile_number
                            .setText(""
                                    + (!aadhra_details_master[8]
                                    .equalsIgnoreCase(null) ? aadhra_details_master[8]
                                    .trim().toUpperCase() : "NA"));
                    tv_aadhar_gender
                            .setText(""
                                    + (!aadhra_details_master[9]
                                    .equalsIgnoreCase(null) ? aadhra_details_master[9]
                                    .trim().toUpperCase() : "NA"));
                    tv_aadhar_dob
                            .setText(""
                                    + (!aadhra_details_master[10]
                                    .equalsIgnoreCase(null) ? aadhra_details_master[10]
                                    .trim().toUpperCase() : "NA"));
                    tv_aadhar_uid
                            .setText(""
                                    + (!aadhra_details_master[11]
                                    .equalsIgnoreCase(null) ? aadhra_details_master[11]
                                    .trim().toUpperCase() : "NA"));

                    if (aadhra_details_master[13].toString().trim().equals(null)) {
                        img_aadhar_image.setImageResource(R.drawable.photo);
                    } else {
                        try {
                            Log.i("AADHRA_Img","-->"+aadhra_details_master[13]);
                            byte[] decodestring = Base64.decode("" + aadhra_details_master[13].toString().trim(), Base64.DEFAULT);
                            Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                            img_aadhar_image.setImageBitmap(decocebyte);

                        } catch (IllegalArgumentException e) {
                            // TODO: handle exception
                            img_aadhar_image.setImageResource(R.drawable.photo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


                @SuppressLint("NewApi")
        protected Dialog onCreateDialog(int id) {
            // TODO Auto-generated method stub
            switch (id) {
                case PROGRESS_DIALOG:
                    ProgressDialog pd = ProgressDialog.show(Update_FPS.this, "", "",	true);
                    pd.setContentView(R.layout.custom_progress_dialog);
                    pd.setCancelable(false);

                    return pd;
                default:

                    break;

            }
            return null;
        }


    public void AlertLogout() {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Logout?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(Update_FPS.this, Welcome.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }


                }).create().show();
    }



    @SuppressWarnings("unused")
    private void showToast(String msg) {
        // TODO Auto-generated method stub
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(14);
        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showToast("Please Click on Logout Button");
    }
}