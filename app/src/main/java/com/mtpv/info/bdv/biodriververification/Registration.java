package com.mtpv.info.bdv.biodriververification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends Activity {

    public static EditText psname, name, gennum, moblienumber, password, con_password;

    public static Button untiname , cancel_reg , submit_reg ;

    boolean doubleBackToExitPressedOnce = false;
    public static final int PROGRESS_DIALOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        psname = (EditText) findViewById(R.id.ed_psname);
        name = (EditText) findViewById(R.id.ed_name);
        gennum = (EditText) findViewById(R.id.ed_gennum);
        moblienumber = (EditText) findViewById(R.id.ed_moblienumber);
        password = (EditText) findViewById(R.id.ed_password);
        con_password = (EditText) findViewById(R.id.ed_conpassword);

        //  untiname.setText("Select Unti Code");
        psname.getText().clear();
        name.getText().clear();
        gennum.getText().clear();
        moblienumber.getText().clear();
        password.getText().clear();
        con_password.getText().clear();

        untiname = (Button) findViewById(R.id.ed_untiname);
        submit_reg = (Button) findViewById(R.id.submit_btn_reg);
        cancel_reg = (Button) findViewById(R.id.cancel_btn_reg);

        cancel_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        submit_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (untiname.getText().toString().trim().equals("Select Unti Code")) {
                    untiname.setError("Please Enter Unit Name Like CYB OR RAC OR HYB");
                    untiname.requestFocus();
                } else */if (psname.getText().toString().trim().equals("")) {
                    psname.setError("Please Enter PS Name");
                    psname.requestFocus();
                } else if (name.getText().toString().trim().equals("")) {
                    name.setError("Please Enter Name");
                    name.requestFocus();
                } else if (gennum.getText().toString().trim().equals("")) {
                    gennum.setError("Please Enter General Number");
                    gennum.requestFocus();
                } else if (moblienumber.getText().toString().trim().equals("")) {
                    moblienumber.setError("Please Enter Contact Number");
                    moblienumber.requestFocus();
                }else if (moblienumber.getText().toString().trim().length()>1 && moblienumber.getText().toString().trim().length() != 10) {
                    showToast("Please Enter 10 Digit Contact Number");
                    moblienumber.requestFocus();
                }else if (!validateMobileNo(moblienumber.getText().toString().trim())) {
                    showToast("Please Enter 10 Digit Valid Contact Number");
                    moblienumber.requestFocus();
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Please Enter Password");
                    password.requestFocus();
                } else if (con_password.getText().toString().trim().equals("")) {
                    con_password.setError("Please Re-Enter Password");
                    con_password.requestFocus();
                } else if (password.getText().toString().equals(con_password.getText().toString())) {

                    if (isOnline()) {
                        new Asyn_Register_finalsubmit().execute();
                    } else {
                        showToast("Please Check Your Internet Connection");
                    }

                } else {

                    con_password.getText().clear();
                    password.getText().clear();
                    showToast("Please Enter Correct Password!");
                }
            }
        });

    }


    public class Asyn_Register_finalsubmit extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Registration.this);

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
            ServiceHelper.Regn_ForLogin("" + 22 ,psname.getText().toString().trim(),name.getText().toString().trim(),gennum.getText().toString().trim()
                    , moblienumber.getText().toString().trim(),Welcome.IMEI,password.getText().toString().trim());
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);
            if (ServiceHelper.new_regn_data!=null && ServiceHelper.new_regn_data.length()>0) {
                MenuBorad.exituser = new String[0];
                MenuBorad.exituser = ServiceHelper.new_regn_data.split("@");
                Log.i("exituser0-->", "" + MenuBorad.exituser[0]);
                Log.i("exituser1-->", "" + MenuBorad.exituser[1]);
                if (MenuBorad.exituser[1].equals("NA")) {
                    showToast("Device DETAILS NOT FOUND \n Please Contact e-Challan ");
                } else {
                    showToast("You Have Successfully Registered");
                    showToast("Thank You");
                    Intent reg = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(reg);
                }
            }

        }

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



    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /***********************Mobile Number Validation Method***************************/
    protected boolean validateMobileNo(String mobileNo) {
        boolean flg=false;
        try {
            if(mobileNo!=null &&  mobileNo.trim().length()==10
                    &&("6".equals(mobileNo.trim().substring(0,1))
                    || "7".equals(mobileNo.trim().substring(0,1))
                    || "8".equals(mobileNo.trim().substring(0,1))
                    || "9".equals(mobileNo.trim().substring(0,1)))){
                flg=true;
            }else if(mobileNo!=null &&  mobileNo.trim().length()==11 && "0".equals(mobileNo.trim().substring(0,1)) ){
                flg=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flg=false;

        }
        Log.i("mobile number",mobileNo+" Length "+mobileNo.trim().length()+"1 letter"+mobileNo.trim().substring(0,1));
        Log.i("mobile verify flag",flg+"");

        return flg;
    }
    /***********************Mobile Number Validation Method Ends***************************/


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

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            //startActivity(new Intent(Registration.this, LoginActivity.class));
            return;
        }
        // alert_string = "loginback";
        this.doubleBackToExitPressedOnce = true;
        showToast("Please click BACK again to exit");
        // Toast.makeText(getApplicationContext(),"Please click BACK again to exit",Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
