package com.mtpv.info.bdv.biodriververification;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.mtpv.info.bdv.biodriververification.Registration.PROGRESS_DIALOG;

public class Document_verification extends AppCompatActivity {


 /*Peraonal Information*/

    public  static TextView per_appl_id ,per_name , per_Fname , per_dob , per_address , per_contact_one ,getPer_contact_two ,
            getGetPer_contact_email , per_bookcentre ,getPer_bookdate ;
    public static ImageView img_other_doc ;

    /* LICENCE DETAILS */
    public static ImageView  imgv_personal, licence_image ;
    public static TextView tv_licnce_ownername_spot ,tvt_getdlnum;
    public static TextView tv_lcnce_father_name_spot;
    public static TextView tv_lcnce_dl_points;
    public static TextView tv_lcnce_address_spot;
    public static TextView tv_dl_no;

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

    CheckBox checkbox ;

    LinearLayout ll_aadhar_layout  ,  llayout_aadhara  ,llayout_dlnum  , lllicences_dldetails ;

    Button back ,next  ;

    public static String imgString ="NA";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_document_verification);

        Log.i("respones_docver-->", " ****WELCOME*****");


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



        imgString = img_other_doc.toString() ;

        img_other_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (img_other_doc.equals("empty_profile_img")){
                    showToast("No Data Not Found");
                }else {

                    SharedPreferences sharedPreference = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = sharedPreference.edit();

                    try {
                        edit.putString("picture", imgString);
                    } catch (Exception e) {
                        System.out.println("imgString ::" + e);
                    }
                    edit.commit();
                }*/
                Intent zoom = new Intent(getApplicationContext(), Zoom_Activity.class);
                startActivity(zoom);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar c1 = Calendar.getInstance();
        int mSec = c1.get(Calendar.MILLISECOND);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strdate1 = sdf1.format(c1.getTime());
        date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        //id_date.setText(strdate1);
        Current_Date = strdate1;
        Log.i("Current_Date--------->",""+Current_Date);
        Log.i("Current_Date--------->",""+date);


        per_appl_id.setText(""+MenuBorad.respones_docver[0]);
        per_name.setText(""+MenuBorad.respones_docver[3]);
        per_dob.setText(""+MenuBorad.respones_docver[6]);
        per_Fname.setText(""+MenuBorad.respones_docver[4]);
        per_address.setText(""+MenuBorad.respones_docver[10]+","+""+MenuBorad.respones_docver[11]+","+""+MenuBorad.respones_docver[12]+","+""+MenuBorad.respones_docver[13]+","+""+MenuBorad.respones_docver[14]);
        per_contact_one.setText(""+MenuBorad.respones_docver[7]);
        // getPer_contact_two.setText(""+MenuBorad.respones_docver[8]);
        getGetPer_contact_email.setText(""+MenuBorad.respones_docver[9]);
        getPer_bookdate.setText(""+MenuBorad.respones_docver[17]);
        per_bookcentre.setText(""+MenuBorad.respones_docver[18]);

        //******* start driving license image display *****
        if (null != MenuBorad.respones_docver[19] && MenuBorad.respones_docver[19].toString().trim().equals("0")) {
            img_other_doc.setImageResource(R.drawable.empty_profile_img);
        } else {
            try {
                byte[] decodestring = Base64.decode("" + MenuBorad.respones_docver[19].toString().trim(),
                        Base64.DEFAULT);
                Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                img_other_doc.setImageBitmap(decocebyte);

            } catch (Exception e) {
                e.printStackTrace();
                img_other_doc.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
            }
        }
        //******* End driving license image display *****

        //******* start driving othetr doc image display *****
        if (null != MenuBorad.respones_docver[20] && MenuBorad.respones_docver[20].toString().trim().equals("0")) {
            imgv_personal.setImageResource(R.drawable.empty_profile_img);
        } else {
            try {
                byte[] decodestring = Base64.decode("" + MenuBorad.respones_docver[20].toString().trim(),
                        Base64.DEFAULT);
                Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                imgv_personal.setImageBitmap(decocebyte);
            } catch (Exception e) {
                e.printStackTrace();
                imgv_personal.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
            }
        }
        //******* end driving other doc image display *****


        ///////////////////licence details master//////////////////
            if (ServiceHelper.licence_data.equalsIgnoreCase("")||ServiceHelper.licence_data.equalsIgnoreCase("NA")||ServiceHelper.licence_data.equalsIgnoreCase("anyType{}")||ServiceHelper.licence_data.equalsIgnoreCase("0")) {

            llayout_dlnum.setVisibility(View.VISIBLE);
                lllicences_dldetails.setVisibility(View.GONE);
                tvt_getdlnum.setText("" + MenuBorad.ed_aadharnum.getText().toString().trim());
        }else {
                llayout_dlnum.setVisibility(View.GONE);
                lllicences_dldetails.setVisibility(View.VISIBLE);
                tv_dl_no.setText("" + MenuBorad.ed_aadharnum.getText().toString().trim());
                tv_licnce_ownername_spot.setText("" + MenuBorad.licence_details_master[0]);
                tv_lcnce_father_name_spot.setText("" + MenuBorad.licence_details_master[1]);

                tv_lcnce_address_spot.setText("" + MenuBorad.licence_details_master[4] + "," + MenuBorad.licence_details_master[3]);
                Log.i("address data", "-->" + MenuBorad.licence_details_master[2]);


                //******* start driving license image display ******//*
                if (MenuBorad.licence_details_master[5].toString().trim().equals(null)) {
                    licence_image.setImageResource(R.drawable.empty_profile_img);
                } else {
                    byte[] decodestring = Base64.decode(""
                            + MenuBorad.licence_details_master[5].toString()
                            .trim(), Base64.DEFAULT);
                    Bitmap decocebyte = BitmapFactory.decodeByteArray(
                            decodestring, 0, decodestring.length);
                    licence_image.setImageBitmap(decocebyte);
                }
            }
        //******* end driving license image display ******//*

        if (ServiceHelper.aadhar_data.equalsIgnoreCase("")||ServiceHelper.aadhar_data.equalsIgnoreCase("0")||ServiceHelper.aadhar_data.equalsIgnoreCase("NA")||ServiceHelper.aadhar_data.equalsIgnoreCase("anyType{}")) {
            ll_aadhar_layout.setVisibility(View.GONE);
            llayout_aadhara.setVisibility(View.VISIBLE);
            getaadhara.setText(""+MenuBorad.respones_docver[2].trim());

        }else {
            ll_aadhar_layout.setVisibility(View.VISIBLE);
            llayout_aadhara.setVisibility(View.GONE);
            try {
                tv_aadhar_uid.setText("" + MenuBorad.respones_docver[2]);
                tv_aadhar_user_name.setText("" + MenuBorad.aadhar_details[0] != null ? MenuBorad.aadhar_details[0].trim().toUpperCase() : "NA");
                tv_aadhar_care_off.setText("" + (!MenuBorad.aadhar_details[1].equalsIgnoreCase("0") ? MenuBorad.aadhar_details[1].trim().toUpperCase() : "NA"));
                tv_aadhar_address.setText("" + (!MenuBorad.aadhar_details[2].equalsIgnoreCase(null) ? MenuBorad.aadhar_details[2].trim().toUpperCase() : "NA") + ", " + (!MenuBorad.aadhar_details[3].equalsIgnoreCase(null) ? MenuBorad.aadhar_details[3]
                        .trim().toUpperCase() + ", "
                        : "NA")
                        + (!MenuBorad.aadhar_details[4]
                        .equalsIgnoreCase(null) ? MenuBorad.aadhar_details[4]
                        .trim().toUpperCase() + ", "
                        : "NA")
                        + (!MenuBorad.aadhar_details[5]
                        .equalsIgnoreCase(null) ? MenuBorad.aadhar_details[5]
                        .trim().toUpperCase() + ", "
                        : "NA")
                        + (!MenuBorad.aadhar_details[6]
                        .equalsIgnoreCase(null) ? MenuBorad.aadhar_details[6]
                        .trim().toUpperCase() + ", "
                        : "NA")
                        + (!MenuBorad.aadhar_details[7]
                        .equalsIgnoreCase(null) ? MenuBorad.aadhar_details[7]
                        .trim().toUpperCase() + ", "
                        : "NA"));
                tv_aadhar_mobile_number
                        .setText(""
                                + (!MenuBorad.aadhar_details[8]
                                .equalsIgnoreCase(null) ? MenuBorad.aadhar_details[8]
                                .trim().toUpperCase() : "NA"));
                tv_aadhar_gender
                        .setText(""
                                + (!MenuBorad.aadhar_details[9]
                                .equalsIgnoreCase(null) ? MenuBorad.aadhar_details[9]
                                .trim().toUpperCase() : "NA"));
                tv_aadhar_dob
                        .setText(""
                                + (!MenuBorad.aadhar_details[10]
                                .equalsIgnoreCase(null) ? MenuBorad.aadhar_details[10]
                                .trim().toUpperCase() : "NA"));
                tv_aadhar_uid
                        .setText(""
                                + (!MenuBorad.aadhar_details[11]
                                .equalsIgnoreCase(null) ? MenuBorad.aadhar_details[11]
                                .trim().toUpperCase() : "NA"));

                if (MenuBorad.aadhar_details[13].toString().trim().equals(null)) {
                    img_aadhar_image.setImageResource(R.drawable.photo);

                } else {
                    try {
                        byte[] decodestring = Base64.decode("" + MenuBorad.aadhar_details[13]
                                .toString().trim(), Base64.DEFAULT);
                        Bitmap decocebyte = BitmapFactory.decodeByteArray(
                                decodestring, 0, decodestring.length);
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkbox.isChecked()) {
                    showToast("Please Click Checkbox ");
                    //Toast.makeText(Document_verification.this,"Please Click Checkbox ", Toast.LENGTH_SHORT).show();
                }else{
                    if (isOnline()) {
                        new Asyntask_sendOTP_verification().execute();
                    }else {
                        showToast("Please Check Your Internet Connection...!");
                    }

                }
            }
        });

    }


    public class Asyntask_sendOTP_verification extends AsyncTask<Void, Void, String> {
        //   private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Document_verification.this);

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

            ServiceHelper.sendOTP(""+MenuBorad.ed_aadharnum.getText().toString().trim(), ""+per_contact_one.getText().toString().trim(),""+date.toUpperCase());
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);

            if (!ServiceHelper.otp_data.equals("NA")){
                Intent next = new Intent(getApplicationContext(),Next_ScreenActivity.class);
                startActivity(next);
            }

           /* if(ServiceHelper.otp_data.equals("NA")||ServiceHelper.otp_data.equals("null")||ServiceHelper.otp_data.equals("anytype{}")){
                showToast("No Data Found");
            }else{
                    Intent next = new Intent(getApplicationContext(),Next_ScreenActivity.class);
                    startActivity(next);
            }*/
        }
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressLint("NewApi")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);

                return pd;
            default:

                break;

        }
        return null;
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
        // TODO Auto-generated method stub
        // super.onBackPressed();

        showToast("Please Click on Back Button");
    }

}
