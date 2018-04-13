package com.mtpv.info.bdv.biodriververification;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.mtpv.info.bdv.biodriververification.Registration.PROGRESS_DIALOG;

public class Next_ScreenActivity extends Activity {

    TextView dlno ;
    RadioGroup rd_yesno ;
    RadioButton rd_yes ,rd_no ;
    public static EditText remark ,input_otp ;
    Button okk ;
    public static String verif_otpstatus = "Y" ;

    public static String final_valuesbyrdG = null ;

    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_next__screen);

        dlno=(TextView)findViewById(R.id.txtx_dlno );

        rd_yesno =(RadioGroup)findViewById(R.id.rd_yesno);
        rd_yes=(RadioButton)findViewById(R.id.rd_yes);
        rd_no=(RadioButton)findViewById(R.id.rd_no);

        remark = (EditText)findViewById(R.id.input_remark);
        input_otp=(EditText)findViewById(R.id.input_otp);
        okk=(Button) findViewById(R.id.ok_btn);

        dlno .setText(""+MenuBorad.ed_aadharnum.getText().toString().trim());

        //manageBlinkEffect();

     /*   rd_yesno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        rd_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final_valuesbyrdG = "1" ;

            }
        });
        rd_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final_valuesbyrdG = "4" ;
            }
        });


        okk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rd_yes.isChecked()){
                    if (input_otp.getText().toString().trim().equals("")) {
                        input_otp.setError("Please Enter OTP Number");
                        input_otp.requestFocus();
                        showToast("Please Enter OTP");
                    }else {

                        if (isOnline()) {
                            new Asyn_verifyOTP().execute();
                        }else {
                            showToast("Please Check Your Internet Connection...!");
                        }
                    }
                }else if(rd_no.isChecked()){
                    if (input_otp.getText().toString().trim().equals("")) {
                        input_otp.setError("Please Enter OTP Number");
                        input_otp.requestFocus();
                        showToast("Please Enter OTP");
                    }else {

                        if (isOnline()) {
                            new Asyn_verifyOTP().execute();
                        }else {
                            showToast("Please Check Your Internet Connection...!");
                        }
                    }
                }else{
                    showToast("Please Check Radio button");
                }
            }
        });
    }


    public class Asyn_verifyOTP extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Next_ScreenActivity.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
           /* pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();*/
        }

        //   public String verifyOTP(String dlno,String mobileNo,String date, String otp,String verify_status);
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("Dl_Task", "Entered_One");
            ServiceHelper.verifyOTP(""+MenuBorad.ed_aadharnum.getText().toString().trim(), ""+Document_verification.per_contact_one.getText().toString().trim(),
                    ""+Document_verification.date,""+input_otp.getText().toString().trim(),
                    ""+verif_otpstatus);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);

            if(ServiceHelper.verifyOTPdata.equals("NA")||ServiceHelper.verifyOTPdata.equals("null")||ServiceHelper.verifyOTPdata.equals("anytype{}")){
                showToast("No Data Found");
                Log.i("OTP_VER Replay--->",""+ServiceHelper.verifyOTPdata);
            }else{
                if (ServiceHelper.verifyOTPdata.equals("1")) {
                    if (isOnline()) {
                        new Asyn_finalverifyDl().execute();
                        Log.i("Welcome-->","Final Asyn Task calling****");
                    }else {
                        showToast("Please Check Your Internet Connection...!");
                    }

                }else {
                    if (ServiceHelper.verifyOTPdata.equals("0")) {
                        showToast("Please Enter Vaild OTP");
                        input_otp.setText("");
                    }
                }
            }
        }
    }


    public class Asyn_finalverifyDl extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Next_ScreenActivity.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
           /* pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();*/
        }

        //public String  verifyDl(String dlno,String applicationId,String verifyDocByPid,String verifyDocStatus,String verifyDocDt,String verifyPerByPid,String verifyPerStatus,String verifyPerDt );
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("Dl_Task", "Entered_One");
            ServiceHelper.verifyDl("" + MenuBorad.ed_aadharnum.getText().toString().trim(), "" + MenuBorad.ed_appID.getText().toString().trim(), "" + Welcome.input_username.getText().toString().trim(),
                    "" + final_valuesbyrdG ,"" + Document_verification.date, "" + null,"" + null,
                    "" + verif_otpstatus);
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.finalverifydata.equals("")||ServiceHelper.finalverifydata.equals("NA")||ServiceHelper.finalverifydata.equals("0")){
                showToast("No Details Found");
            }

            if (ServiceHelper.finalverifydata.equals("1")){
                Log.i("FinalResponse**",""+ServiceHelper.finalverifydata);

               // showToast("Document Verification is Completed ");

                TextView title = new TextView(Next_ScreenActivity.this);
                title.setText("Verification Status");
                title.setBackgroundColor(Color.BLUE);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                // title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exit_logo, 0, R.drawable.exit_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);
                String otp_message=null;
                if(final_valuesbyrdG=="1"){
                     otp_message = "\n Document Verification is Completed...! \n" ;
                }else {
                     otp_message = "\n Document Verification is Rejected, Please verify your Documents properly...! \n" ;
                }


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Next_ScreenActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.exit_logo);
                alertDialogBuilder.setMessage(otp_message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //  finish();
                                Intent men = new Intent(getApplicationContext(),MenuBorad.class);
                                startActivity(men);
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
                showToast("Document Verification is Incompleted ");
                TextView title = new TextView(Next_ScreenActivity.this);
                title.setText("Verification Status");
                title.setBackgroundColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                //title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exit_logo, 0, R.drawable.exit_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);


                String otp_message = "\n Document Verification is Incompleted...! \n" ;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Next_ScreenActivity.this, AlertDialog.THEME_HOLO_LIGHT);
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
                                Intent men = new Intent(getApplicationContext(),MenuBorad.class);
                                startActivity(men);
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

            }

        }
    }

/*    @SuppressLint("WrongConstant")
    private void manageBlinkEffect() {
        ObjectAnimator anim = ObjectAnimator.ofInt(okk, "backgroundColor", Color.WHITE, Color.GREEN,
                Color.WHITE);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(REVERSE);
        anim.setRepeatCount(INFINITE);
        anim.start();
    }*/

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
       // super.onBackPressed();
        showToast("Please Click on Back Button");
    }
}
