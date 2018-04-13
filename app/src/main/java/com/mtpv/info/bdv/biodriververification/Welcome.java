package com.mtpv.info.bdv.biodriververification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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

import static com.mtpv.info.bdv.biodriververification.Registration.PROGRESS_DIALOG;


public class Welcome extends Activity {

    Button newuser, submit;

    TextView txt_regn ;
    public static EditText input_username, inputPassword ;

    public static String IMEI;

    public static String URL = "http://www.echallan.info/DriverVerification/services/DriverVerificationServiceImpl?wsdl";

    public static final int PROGRESS_DIALOG = 0;
    boolean doubleBackToExitPressedOnce = false;
    private final int REQUEST_CONTACT = 111;

    private static final int REQUEST_APP_SETTINGS = 168;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        Log.i("Welcome","**WELCOME*TO*Welcome*SCreen**");


        /**************Adding Shortcut of Application**************/
        SharedPreferences prefs = getSharedPreferences("ShortCutPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("isFirstTime", false)) {
            Log.i("**addShortcut------>", "**********addShortcut*******");
            addShortcut();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstTime", true);
            editor.commit();
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        IMEI = telephonyManager.getDeviceId();


        Log.d(" IMEI_numb-->", IMEI);


        input_username = (EditText)findViewById(R.id.input_username);
        inputPassword = (EditText)findViewById(R.id.inputPassword);
       // newuser = (Button)findViewById(R.id.newuser_btn);
        submit = (Button)findViewById(R.id.submit);
        txt_regn=(TextView)findViewById(R.id.txt_regn);

        txt_regn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent user = new Intent(getApplicationContext(),Registration.class);
                startActivity(user);

            }
        });

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


                /*Intent login = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(login);*/

            }
        });

    }


    public class Aysn_Login extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Welcome.this);

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
              //  MenuBorad.exituser = new String[0];
              //  MenuBorad.exituser= ServiceHelper.new_ExitUser_data.split("@");
               // Log.i("exituser0-->",""+MenuBorad.exituser[0]);
             //   Log.i("exituser1-->",""+MenuBorad.exituser[1]);
                // MenuBorad.welcome.setText(""+MenuBorad.exituser[1]);
                // Log.i("exituser1-->",""+Public_Remark_Activity.exituser[2]);

                //Log.i("exituser3-->",""+exituser[0]);
                if (ServiceHelper.login_master[0].equals("0")||ServiceHelper.login_master[0].equals("NA")||ServiceHelper.login_master[0].equalsIgnoreCase("null")||ServiceHelper.login_master[1].equalsIgnoreCase("NA")||ServiceHelper.login_master[1].equals("0")||ServiceHelper.login_master[0].equals("null")){
                    showToast("Please Check Login Credentials...!");
                }/*else if(Public_Remark_Activity.exituser[2].equals("NA")) {
                    showToast("Please Contact to e-Challan \n 040-27853416");
                }*/  else {

                    if(ServiceHelper.login_master[0].equals("1")) {
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

    /**************Adding Shortcut of Application**************/
    private void addShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
        shortcutIntent.addFlags(flags);

        Intent addIntent = new Intent();
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource
                .fromContext(getApplicationContext(), R.drawable.exit_logo));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
    /**************Adding Shortcut of Application**************/

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public boolean hasPermissions(String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkCallingOrSelfPermission(permission))
                return false;
        return true;
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
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
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
