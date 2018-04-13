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

import com.mtpv.info.bdv.biodriververification.update.moduleicon_Activity;

public class LoginActivity extends Activity {



    public static EditText input_username, inputPassword ;

    public static Button submit ;

    boolean doubleBackToExitPressedOnce = false;
    public static final int PROGRESS_DIALOG = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        Log.i("Welcome","**WELCOME*TO*Login*SCreen**");


        input_username = (EditText)findViewById(R.id.input_username);
        inputPassword = (EditText)findViewById(R.id.input_password);

        submit = (Button)findViewById(R.id.submit_btn);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (input_username.getText().toString().trim().equals("")){
                    input_username.setError("Please Enter General Number");
                    input_username.requestFocus();
                    // showToast("Please Enter General Number");
                }else if (inputPassword.getText().toString().trim().equals("")) {
                    inputPassword.setError("Please Enter Password");
                    inputPassword.requestFocus();
                    // showToast("Please Enter Password");
                }else {


                   /* Intent login = new Intent(getApplicationContext(), moduleicon_Activity.class);
                    startActivity(login);*/

                    if (isOnline()) {
                        new Aysn_Login().execute();
                    } else {
                        showToast("Please Check Your Internet Connection");
                    }
                }
            }
        });

    }

    //public String getExitUser(String glno,String imei,String password);

    public class Aysn_Login extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
           /* pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();*/
        }

        //    public String getExitUser(String glno,String imei,String password);
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("Dl_Task", "Entered_One");
            ServiceHelper.Login(input_username.getText().toString().trim(),"" +Welcome.IMEI,inputPassword.getText().toString().trim());
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            removeDialog(PROGRESS_DIALOG);
            // pdia.dismiss();

            try{
                MenuBorad.exituser = new String[0];
                MenuBorad.exituser= ServiceHelper.new_ExitUser_data.split("@");
                Log.i("exituser0-->",""+MenuBorad.exituser[0]);
                Log.i("exituser1-->",""+MenuBorad.exituser[1]);
                // MenuBorad.welcome.setText(""+MenuBorad.exituser[1]);
                // Log.i("exituser1-->",""+Public_Remark_Activity.exituser[2]);

                //Log.i("exituser3-->",""+exituser[0]);
                if (MenuBorad.exituser[0].equals("0")||MenuBorad.exituser[0].equals("NA")||MenuBorad.exituser[0].equalsIgnoreCase("null")||MenuBorad.exituser[1].equalsIgnoreCase("NA")||MenuBorad.exituser[1].equals("0")||MenuBorad.exituser.equals("null")){
                    showToast("Please Check Login Credentials...!");
                }/*else if(Public_Remark_Activity.exituser[2].equals("NA")) {
                    showToast("Please Contact to e-Challan \n 040-27853416");
                }*/  else {

                    if(MenuBorad.exituser[0].equals("1")) {
                        //  Log.i("exituser4-->",""+Public_Remark_Activity.exituser[1]);

                        Intent login = new Intent(getApplicationContext(), moduleicon_Activity.class);
                        startActivity(login);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();

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

        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            return;
        }
        // alert_string = "loginback";
        this.doubleBackToExitPressedOnce = true;
        showToast( "Please click BACK again to exit");
        //Toast.makeText(getApplicationContext(),"Please click BACK again to exit",Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
