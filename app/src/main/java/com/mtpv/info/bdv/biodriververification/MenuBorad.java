package com.mtpv.info.bdv.biodriververification;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.info.bdv.biodriververification.update.Update_FPS;
import com.mtpv.info.bdv.biodriververification.update.moduleicon_Activity;

import static com.mtpv.info.bdv.biodriververification.Registration.PROGRESS_DIALOG;

public class MenuBorad extends AppCompatActivity {

    public static String[] exituser  ,respones_docver ,licence_details_master , aadhar_details;

    public static EditText ed_appID , ed_aadharnum ;

    public static Button btn_docver , btn_perver ;

    public static TextView welcome ;

    ImageView back  ;

    public static String person_application_is ="" ;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_borad);

        ed_appID = (EditText)findViewById(R.id.input_applicationID);
        ed_aadharnum = (EditText)findViewById(R.id.input_aadharnum);

        btn_docver=(Button)findViewById(R.id.docver_btn);
        btn_perver=(Button)findViewById(R.id.perver_btn);
      //  update_btn=(Button)findViewById(R.id.update_btn);
        welcome = (TextView)findViewById(R.id.txt_welcome);
        back = (ImageView)findViewById(R.id.btn_back);

        // 0000012662
        // DLFAP12858482009
       /* ed_appID.setText("0000012906");
        ed_aadharnum.setText("AP03020130007577");*/

        /*update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updata = new Intent(getApplicationContext(), Update_FPS.class);
                startActivity(updata);
            }
        });*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked","Cliked");
                Intent model = new Intent(getApplicationContext(),moduleicon_Activity.class);
                startActivity(model);
            }
        });

        btn_docver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ed_appID.getText().toString().trim().equals("")){
                    ed_appID.setError("Please Enter Application Number");
                    ed_appID.requestFocus();
                }else if (ed_aadharnum.getText().toString().trim().equals("")){
                    ed_aadharnum.setError("Please Enter Driving Licence Number");
                    ed_aadharnum.requestFocus();
                }else {
                    if (isOnline()) {
                        new Asyn_Document_verification_driver().execute();
                    }else{
                        showToast("Please Check Your Internet Connection");
                    }

                }
            }
        });

        btn_perver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ed_appID.getText().toString().trim().equals("")){
                    ed_appID.setError("Please Enter Application Number");
                    ed_appID.requestFocus();
                }else if (ed_aadharnum.getText().toString().trim().equals("")){
                    ed_aadharnum.setError("Please Enter Driving Licence Number");
                    ed_aadharnum.requestFocus();
                }else {
                    if (isOnline()) {
                        new Asyn_FPS_verification_driver().execute();
                       //new  Asyn_Document_verification_driver().execute();
                       /* Intent doc = new Intent(getApplicationContext(), Person_FPS.class);
                        startActivity(doc);*/
                    } else {
                        showToast("Please Check Your Internet Connection...!");

                        Intent doc = new Intent(getApplicationContext(), Person_FPS.class);
                        startActivity(doc);
                    }
                }
            }
        });

       welcome.setText(""+ServiceHelper.login_master[1]);

    }


    public class Asyn_Document_verification_driver extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(MenuBorad.this);

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
            ServiceHelper.Document_verif(ed_aadharnum.getText().toString().trim(),ed_appID.getText().toString().trim());
            return null;
    }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.new_docment_verif_data.equals("NA")||ServiceHelper.new_docment_verif_data.equals("")||ServiceHelper.new_docment_verif_data.equals("anyType{}")){

                showToast("No Details Found...!");
                ed_appID.setText("");
                ed_aadharnum.setText("");
            }


            if (ServiceHelper.new_docment_verif_data!=null && ServiceHelper.new_docment_verif_data.length()>0) {
                MenuBorad.respones_docver = new String[0];
                MenuBorad.respones_docver = ServiceHelper.new_docment_verif_data.split("!");
              //  Log.i("respones_docver-->", "" + MenuBorad.respones_docver[0]);
                //Log.i("respones_docver1-->", "" + MenuBorad.respones_docver[1]);
               // Log.i("aaadharnum-->",""+MenuBorad.respones_docver[2]);
                if (MenuBorad.respones_docver[0].equals("NA")||MenuBorad.respones_docver[0].equals("anyType{}")||MenuBorad.respones_docver[0].equalsIgnoreCase("0")||MenuBorad.respones_docver[0].equalsIgnoreCase("")) {
                    showToast("DETAILS NOT FOUND");
                } else {
                 //   Log.i("respones_docver2-->", "" +MenuBorad.respones_docver[0]);
                    if (!MenuBorad.respones_docver[0].equals("NA")){

                        if (!MenuBorad.respones_docver[1].equals("NA")){
                            if (isOnline()) {
                                new Asyn_driver_licenceverification().execute();
                            }else {
                                showToast("Please Check Your Internet Connection...!");
                            }
                        }
                        if (!MenuBorad.respones_docver[2].equals("NA")){
                   //         Log.i("aaadharnum-->",""+MenuBorad.respones_docver[2]);
                            if (isOnline()) {
                                new Asyn_driver_aadhraverification().execute();
                            }else {
                                showToast("Please Check Your Internet Connection...!");
                            }
                        }

                     //   Log.i("respones_docver3-->", "" +MenuBorad.respones_docver[0]);
                        /* // person_application_is =""+MenuBorad.respones_docver[0] ;

                          Document_verification.per_appl_id.setText(""+MenuBorad.respones_docver[0]);
                          Document_verification.per_name.setText(""+MenuBorad.respones_docver[3]);

                          Document_verification.per_dob.setText(""+MenuBorad.respones_docver[6]);
                          Document_verification.per_Fname.setText(""+MenuBorad.respones_docver[4]);

                          Document_verification.per_address.setText(""+MenuBorad.respones_docver[10]+","+""+MenuBorad.respones_docver[11]+","+""+MenuBorad.respones_docver[12]+","+""+MenuBorad.respones_docver[13]+","+""+MenuBorad.respones_docver[14]);
                          Document_verification.per_contact_one.setText(""+MenuBorad.respones_docver[7]);

                         // Document_verification.getPer_contact_two.setText(""+MenuBorad.respones_docver[8]);
                          Document_verification.getGetPer_contact_email.setText(""+MenuBorad.respones_docver[9]);

                          Document_verification.getPer_bookdate.setText(""+MenuBorad.respones_docver[17]);
                          Document_verification.per_bookcentre.setText(""+MenuBorad.respones_docver[18]);


                          /*//******* start driving license image display *****
                         if (null != respones_docver[19] && respones_docver[19].toString().trim().equals("0")) {
                         Document_verification.img_other_doc.setImageResource(R.drawable.empty_profile_img);
                         } else {
                         try {
                         byte[] decodestring = Base64.decode("" + respones_docver[19].toString().trim(),
                         Base64.DEFAULT);
                         Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                         Document_verification.img_other_doc.setImageBitmap(decocebyte);

                         } catch (Exception e) {
                         e.printStackTrace();
                         Document_verification.img_other_doc.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
                         }
                         }
                         /*//******* End driving license image display ******/

                        //******* start driving license image display *****
     /*   if (null != respones_docver[20] && respones_docver[20].toString().trim().equals("0")) {
            Document_verification.imgv_personal.setImageResource(R.drawable.empty_profile_img);
        } else {
            try {
                byte[] decodestring = Base64.decode("" + respones_docver[20].toString().trim(),
                        Base64.DEFAULT);
                Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                Document_verification.imgv_personal.setImageBitmap(decocebyte);
            } catch (Exception e) {
                e.printStackTrace();
                Document_verification.imgv_personal.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
            }
        }
                          /*//******* end driving license image display *****
                         */
                    }
                }
            }
        }
    }

    public class Asyn_FPS_verification_driver extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(MenuBorad.this);

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
            Log.i("Dl_Task", "Entered_One");
            ServiceHelper.FPS_verification(ed_aadharnum.getText().toString().trim(),ed_appID.getText().toString().trim());
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.new_FPS_verif_data.equals("0")||ServiceHelper.new_FPS_verif_data.equals("NA")){
                //showToast("Please Verify Documents...!");

                ed_appID.setText("");
                ed_aadharnum.setText("");

                //showToast("Document Verification is Incompleted ");
                TextView title = new TextView(MenuBorad.this);
                title.setText("FPS Verification Status");
                title.setBackgroundColor(Color.BLUE);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exit_logo, 0, R.drawable.exit_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);


                String otp_message = "\n Please Complete Document Verification First...! \n";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuBorad.this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.exit_logo);
                alertDialogBuilder.setMessage(otp_message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                // finish();
                                Intent doc = new Intent(getApplicationContext(), MenuBorad.class);
                                startActivity(doc);

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.getWindow().getAttributes();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(28);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);


                Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn.setTextSize(22);
                btn.setTextColor(Color.WHITE);
                btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
                btn.setBackgroundColor(Color.BLUE);


            }else {
                //equals("1")
                    Log.i("**WELCOME**","ENTERED***INTO*FPS CLASS");
                Intent doc = new Intent(getApplicationContext(), Person_FPS.class);
                startActivity(doc);
            }
        }
    }


    public class Asyn_driver_licenceverification extends AsyncTask<Void, Void, String> {
                private android.content.Context yourContext;

                ProgressDialog pdia = new ProgressDialog(MenuBorad.this);

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
                    ServiceHelper.Licence_Document_verif(ed_aadharnum.getText().toString().trim());
                    return null;
                }


                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //pdia.dismiss();
                    removeDialog(PROGRESS_DIALOG);

                    Log.i(" licence_data before-->",""+ServiceHelper.licence_data);
            if (ServiceHelper.licence_data!=null) {
                if (ServiceHelper.licence_data.equalsIgnoreCase("") || ServiceHelper.licence_data.equals("NA") || ServiceHelper.licence_data.equals("0") || "null".equalsIgnoreCase(ServiceHelper.licence_data)
                        ||ServiceHelper.licence_data.equals("anyType{}")) {
                    Log.i(" 2 licence_data-->", "" + ServiceHelper.licence_data);
                    //driver_address_layout.setVisibility(View.VISIBLE);
                    // Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(" 3 licence_data-->", "" + ServiceHelper.licence_data);
                    licence_details_master = new String[0];
                    licence_details_master = ServiceHelper.licence_data.split("!");
                    Log.i("Response String", "" + licence_details_master);
                    Log.i("Response_Dl", "" + licence_details_master[0]);
                    Log.i("licence_----data-->", "" + ServiceHelper.licence_data);
                   /* Document_verification.tv_dl_no.setText(""+ed_aadharnum.getText().toString().trim());
                    Document_verification.tv_licnce_ownername_spot.setText("" + licence_details_master[0]);
                    Document_verification.tv_lcnce_father_name_spot.setText("" + licence_details_master[1]);
                    // driver_address.setText(""+licence_details_master[2]);



                    Document_verification.tv_lcnce_address_spot.setText(""+licence_details_master[4]+","+licence_details_master[3]);
                        Log.i("address data","-->"+licence_details_master[2]);
                    //Document_verification.tv_lcnce_dl_points.setText("Total Violation Points:"+licence_details_master[7]);

                   //******* start driving license image display ******//*
                    if (licence_details_master[5].toString().trim().equals(null)) {
                        Document_verification.licence_image.setImageResource(R.drawable.empty_profile_img);
                    } else {
                        byte[] decodestring = Base64.decode(""
                                + licence_details_master[5].toString()
                                .trim(), Base64.DEFAULT);
                        Bitmap decocebyte = BitmapFactory.decodeByteArray(
                                decodestring, 0, decodestring.length);
                        Document_verification.licence_image.setImageBitmap(decocebyte);
                    }*/
                    //******* end driving license image display ******//*

                    //driver_image.setImageDrawable();*/
                }
            }else {
                //driver_address_layout.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
            }


        }
    }


    public class Asyn_driver_aadhraverification extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(MenuBorad.this);

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
            ServiceHelper.aadhra_Document_verif(MenuBorad.respones_docver[2].trim(),"");
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);
            Log.i(" aadhar_details_data-->", "" + ServiceHelper.aadhar_data);

            if (ServiceHelper.aadhar_data.equalsIgnoreCase("") || ServiceHelper.aadhar_data.equals("NA") || ServiceHelper.aadhar_data.equals("0") || "null".equalsIgnoreCase(ServiceHelper.aadhar_data)
                    || ServiceHelper.aadhar_data.equals("anyType{}")) {
                Log.i(" 2 aadhar_data-->", "" + ServiceHelper.aadhar_data);

                Intent doc = new Intent(getApplicationContext(), Document_verification.class);
                startActivity(doc);

            } else {
                aadhar_details = new String[0];
                aadhar_details = ServiceHelper.aadhar_data.split("@");


                /*try {
                    Document_verification.tv_aadhar_uid.setText(""+MenuBorad.respones_docver[2]);
                    Document_verification.tv_aadhar_user_name.setText("" + aadhar_details[0] != null ? aadhar_details[0].trim().toUpperCase() : "NA");
                    Document_verification.tv_aadhar_care_off.setText("" + (!aadhar_details[1].equalsIgnoreCase("0") ? aadhar_details[1].trim().toUpperCase() : "NA"));
                    Document_verification.tv_aadhar_address.setText("" + (!aadhar_details[2].equalsIgnoreCase(null) ? aadhar_details[2].trim().toUpperCase() : "NA") + ", " + (!aadhar_details[3].equalsIgnoreCase(null) ? aadhar_details[3]
                            .trim().toUpperCase() + ", "
                            : "NA")
                            + (!aadhar_details[4]
                            .equalsIgnoreCase(null) ? aadhar_details[4]
                            .trim().toUpperCase() + ", "
                            : "NA")
                            + (!aadhar_details[5]
                            .equalsIgnoreCase(null) ? aadhar_details[5]
                            .trim().toUpperCase() + ", "
                            : "NA")
                            + (!aadhar_details[6]
                            .equalsIgnoreCase(null) ? aadhar_details[6]
                            .trim().toUpperCase() + ", "
                            : "NA")
                            + (!aadhar_details[7]
                            .equalsIgnoreCase(null) ? aadhar_details[7]
                            .trim().toUpperCase() + ", "
                            : "NA"));
                    Document_verification.tv_aadhar_mobile_number
                            .setText(""
                                    + (!aadhar_details[8]
                                    .equalsIgnoreCase(null) ? aadhar_details[8]
                                    .trim().toUpperCase() : "NA"));
                    Document_verification.tv_aadhar_gender
                            .setText(""
                                    + (!aadhar_details[9]
                                    .equalsIgnoreCase(null) ? aadhar_details[9]
                                    .trim().toUpperCase() : "NA"));
                    Document_verification.tv_aadhar_dob
                            .setText(""
                                    + (!aadhar_details[10]
                                    .equalsIgnoreCase(null) ? aadhar_details[10]
                                    .trim().toUpperCase() : "NA"));
                    Document_verification.tv_aadhar_uid
                            .setText(""
                                    + (!aadhar_details[11]
                                    .equalsIgnoreCase(null) ? aadhar_details[11]
                                    .trim().toUpperCase() : "NA"));

                    if (aadhar_details[13].toString().trim().equals(null)) {
                        Document_verification.img_aadhar_image.setImageResource(R.drawable.photo);

                    } else {
                        try {
                            byte[] decodestring = Base64.decode("" + aadhar_details[13]
                                    .toString().trim(), Base64.DEFAULT);
                            Bitmap decocebyte = BitmapFactory.decodeByteArray(
                                    decodestring, 0, decodestring.length);
                            Document_verification.img_aadhar_image.setImageBitmap(decocebyte);

                        } catch (IllegalArgumentException e) {
                            // TODO: handle exception
                            Document_verification.img_aadhar_image.setImageResource(R.drawable.photo);
                        }
                    }


                } catch (Exception e){
                    e.printStackTrace();
                }*/

                Intent doc = new Intent(getApplicationContext(), Document_verification.class);
                startActivity(doc);
            }
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
