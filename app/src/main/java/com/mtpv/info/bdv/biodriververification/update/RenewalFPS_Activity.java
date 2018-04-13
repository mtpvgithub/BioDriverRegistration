package com.mtpv.info.bdv.biodriververification.update;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.mtpv.info.bdv.biodriververification.Person_FPS;
import com.mtpv.info.bdv.biodriververification.Printpreview;
import com.mtpv.info.bdv.biodriververification.R;
import com.mtpv.info.bdv.biodriververification.ServiceHelper;
import com.mtpv.info.bdv.biodriververification.Welcome;
import com.mtpv.info.bdv.biodriververification.bluetooth.BluetoothComm;
import com.mtpv.info.bdv.biodriververification.storage.Constants;
import com.mtpv.info.bdv.biodriververification.storage.Storage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import static android.widget.Toast.LENGTH_LONG;
import static com.mtpv.info.bdv.biodriververification.Registration.PROGRESS_DIALOG;

public class RenewalFPS_Activity extends AppCompatActivity {

    Button img_fps_btn , verify_btn , submit ;
    public static  TextView finger , hand , moblie;
    public static  EditText otp ;
    public static String verif_otpstatus = "Y" ;
    ImageView scan_btn;
    LinearLayout llout_finger;
    public static final int PROGRESS_DIALOG = 1;

    boolean doubleBackToExitPressedOnce = false;
    ImageLoader imageLoader;
    public  static String imagepath ;
    OutputStream outputStream;
    InputStream inputstream;
    private byte[] bMinutiaeData ;
    private byte[] brecentminituaedata = {};
    FpsConfig fpsconfig = new FpsConfig();
    private static final String TAG = "Prow LeopardImp App";
    private boolean mbBonded = false;
    public static BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
    public static BluetoothDevice mBDevice = null;
    static Setup setupInstance = null;
    final Context context = this;
    private GlobalPool mGP = null;
    private boolean mbBleStatusBefore = false;
    public static final byte REQUEST_DISCOVERY = 0x01;
    private Hashtable<String, String> mhtDeviceInfo = new Hashtable<String, String>();
    Dialog dlgRadioBtn;
    public static boolean blnResetBtnEnable = false;
    public static ProgressDialog prgDialog;
    static BaudChange bdchange = null;
    private int iRetVal;
    private final int MESSAGE_BOX = 1;
    public static final int DEVICE_NOTCONNECTED = -100;
    static ProgressDialog dlgCustom, dlgpd;
    FPS fps;
    private boolean blVerifyfinger = false;
    Storage storage;
    String str1;
    // Uri for image path
    private static Uri imageUri = null;

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
    public static String date, Current_Date;
    public static String verifyPerFPSStatus = "0" ;
	/* DATE & TIME END */

    ImageView web_pdf ;

    private BroadcastReceiver _mPairingRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = null;
            if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                    mbBonded = true;
                else
                    mbBonded = false;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // this.mGP.closeConn();
        // (null != mBT && !this.mbBleStatusBefore)
        // mBT.disable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renewal_fps_);

        hand=(TextView)findViewById(R.id.txt_hand);
        finger=(TextView)findViewById(R.id.txt_finger);
        moblie=(TextView)findViewById(R.id.txt_moblie);

        img_fps_btn=(Button)findViewById(R.id.img_fps_btn);

        verify_btn=(Button)findViewById(R.id.verify_btn);
        submit=(Button)findViewById(R.id.fin_submit);

        web_pdf=(ImageView)findViewById(R.id.web_Pdf);
        scan_btn=(ImageView)findViewById(R.id.scan_bt);
        otp=(EditText)findViewById(R.id.ed_otp_xml);

        if (ServiceHelper.Fps_data_master[23].equalsIgnoreCase("LH")){
            hand.setText("Left Hand");
        }else if (ServiceHelper.Fps_data_master[23].equalsIgnoreCase("RH")){
            hand.setText("Right Hand");
        }

        Log.i("Hand","-->"+ServiceHelper.Fps_data_master[23]);

        // hand.setText(""+ServiceHelper.Fps_data_master[23]);
        finger.setText(""+ServiceHelper.Fps_data_master[22]);


        storage = new Storage(RenewalFPS_Activity.this);

        if (null == mBT) {
            Toast.makeText(this, "Bluetooth module not found", LENGTH_LONG).show();
            this.finish();
        }

        try {
            setupInstance = new Setup();
            boolean activate = setupInstance.blActivateLibrary(context, R.raw.licence_full);
            if (activate == true) {
                Log.d(TAG, "Driver Verification Library Activated......");
            } else if (activate == false) {
                Log.d(TAG, "Driver Verification Library Not Activated...");
            }
        } catch (Exception e) {
        }

        mGP = ((GlobalPool) this.getApplicationContext());

        Log.i("mBDevice", "-->" + storage.getValue(Constants.BT_ADDRESS));
        if (storage.getValue(Constants.BT_ADDRESS).length() > 4) {
            new RenewalFPS_Activity.connSocketTask().execute(storage.getValue(Constants.BT_ADDRESS));
        } else {
            Toast.makeText(context, "Device name not retrieved", LENGTH_LONG).show();
        }


   /*     img_fps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    outputStream = BluetoothComm.mosOut;
                    inputstream = BluetoothComm.misIn;
                    Log.i("inputstream","-->"+inputstream);
                    Log.i("outputStream ","-->"+outputStream);
                    Log.i("setupInstance ","-->"+setupInstance);
                    fps = new FPS(setupInstance, outputStream, inputstream);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                CaptureFingerAsyn verifyTemp = new CaptureFingerAsyn();
                verifyTemp.execute(0);
            }
        });
*/
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    outputStream = BluetoothComm.mosOut;
                    inputstream = BluetoothComm.misIn;
                  /*  Log.i("inputstream","-->"+inputstream);
                    Log.i("outputStream ","-->"+outputStream);
                    Log.i("setupInstance ","-->"+setupInstance);*/

                    fps = new FPS(setupInstance, outputStream, inputstream);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                VerifyTempleAsync verifyTemp = new VerifyTempleAsync();
                verifyTemp.execute(0);
                Log.i("verifyPerFPSStatus","-->"+verifyPerFPSStatus);

            }
        });

        Calendar c1 = Calendar.getInstance();
        int mSec = c1.get(Calendar.MILLISECOND);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strdate1 = sdf1.format(c1.getTime());
        date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        //id_date.setText(strdate1);
        Current_Date = strdate1;
        Log.i("Current_Date--------->", "" + Current_Date);
        Log.i("Current_Date--------->", "" + date);


        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Driver Verification Application");
                alertDialogBuilder.setMessage("One Device is already Paired, Would you like to change it").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                mBT.disable();
                                new startBluetoothDeviceTask().execute("");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Log.i("***FINGER_VALUES-->", "" + verify_finger_vale);
                if (isOnline()) {
                    if (otp.getText().toString().trim().equals("")){
                        otp.setError("Please Enter OTP Number");
                        otp.requestFocus();
                        showToast("Please Enter OTP");

                    }else if (verifyPerFPSStatus=="0") {
                        showToast("Please Capture Finger Print and Verify ...!");
                        Log.i("SHOWING DIALOGBOX","ENTERED");
                        TextView title = new TextView(RenewalFPS_Activity.this);
                        title.setText("Verification Status");
                        title.setBackgroundColor(Color.RED);
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(Color.WHITE);
                        title.setTextSize(26);
                        title.setTypeface(title.getTypeface(), Typeface.BOLD);
                        // title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exit_logo, 0, R.drawable.exit_logo, 0);
                        title.setPadding(20, 0, 20, 0);
                        title.setHeight(70);


                        String otp_message = "\nPlease Capture Finger Print and Verify ...! \n" ;

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RenewalFPS_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
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
                        btn.setBackgroundColor(Color.RED);

                    }else{
                        new Asyn_verifyOTP().execute();
                    }
                } else {
                    showToast("Please Check Your Internet Connection...!");
                }
            }
        });

    }


    public class Asyn_verifyOTP extends AsyncTask<Void, Void, String> {
        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(RenewalFPS_Activity.this);

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
            ServiceHelper.verifyOTP(""+Update_FPS.applID.getText().toString().trim(), ""+ Update_FPS.per_contact_one.getText().toString().trim(),
                    ""+Update_FPS.date,""+otp.getText().toString().trim(),
                    ""+verif_otpstatus);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //pdia.dismiss();
            removeDialog(PROGRESS_DIALOG);

            if(ServiceHelper.verifyOTPdata.equals("NA")||ServiceHelper.verifyOTPdata.equals("null")||ServiceHelper.verifyOTPdata.equals("anytype{}")||ServiceHelper.verifyOTPdata.equals("0")||ServiceHelper.verifyOTPdata.equalsIgnoreCase("")){
                showToast("No Data Found");
                otp.setText("");
                Log.i("OTP_VER Replay-00-->",""+ServiceHelper.verifyOTPdata);
            }else{
                Log.i("OTP_VER Replay-11-->",""+ServiceHelper.verifyOTPdata);
                if (ServiceHelper.verifyOTPdata.equals("1")) {

                    if (isOnline()) {
                            new AsyncTask_RenewalFPS_final_submit().execute();
                            Log.i("Calling-->","AsyncTask_RenewalFPS_final_submit");
                    }else {
                        showToast("Please Check Your Internet Connection...!");
                    }
                }else {
                    if (ServiceHelper.verifyOTPdata.equals("0")) {
                        showToast("Please Enter Vaild OTP");
                        otp.setText("");
                    }
                }
            }
        }
    }

    private boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public void view(View v)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "lax.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(RenewalFPS_Activity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }


    public class CaptureFingerAsyn extends AsyncTask<Integer, Integer, Integer> {
        /* displays the progress dialog until background task is completed */
        @Override
        protected void onPreExecute() {
            progressDialog(context, "Place your finger on FPS ...");
            super.onPreExecute();
        }

        /* Task of CaptureFinger performing in the background */
        @Override
        protected Integer doInBackground(Integer... params) {

                try {
                    brecentminituaedata = new byte[3500];
                    fpsconfig = new FpsConfig(0, (byte) 0x0F);
                    brecentminituaedata = fps.bFpsCaptureMinutiae(fpsconfig);
                    iRetVal = fps.iGetReturnCode();
                    byte[] bMinutiaeData = fps.bGetMinutiaeData();
                    Log.i("bMinutiaeData**", "-->" + bMinutiaeData);
                    str1 = HexString.bufferToHex(bMinutiaeData);
                    Log.i("service finger data ", "" + ServiceHelper.Fps_data_master[21]);
                    brecentminituaedata = HexString.hexToBuffer(ServiceHelper.Fps_data_master[21]);

                } catch (NullPointerException e) {
                    iRetVal = DEVICE_NOTCONNECTED;
                    return iRetVal;
                }
            return iRetVal;
        }

        /*
         * This function sends message to handler to display the status messages
         * of Diagnose in the dialog box
         */
        @Override
        protected void onPostExecute(Integer result) {
            dlgCustom.dismiss();
            if (iRetVal == DEVICE_NOTCONNECTED) {
                handler.obtainMessage(DEVICE_NOTCONNECTED, "Device not connected").sendToTarget();
            } else if (iRetVal == FPS.SUCCESS) {
                blVerifyfinger = true;
                handler.obtainMessage(MESSAGE_BOX, "Capture finger success").sendToTarget();
            } else if (iRetVal == FPS.FPS_INACTIVE_PERIPHERAL) {
                handler.obtainMessage(MESSAGE_BOX, "Peripheral is inactive").sendToTarget();
            } else if (iRetVal == FPS.TIME_OUT) {
                handler.obtainMessage(MESSAGE_BOX, "Capture finger time out").sendToTarget();
            } else if (iRetVal == FPS.FAILURE) {
                handler.obtainMessage(MESSAGE_BOX, "Capture finger failed").sendToTarget();
            } else if (iRetVal == FPS.PARAMETER_ERROR) {
                handler.obtainMessage(MESSAGE_BOX, "Parameter error").sendToTarget();
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                handler.obtainMessage(MESSAGE_BOX, "Connected  device is not license authenticated.").sendToTarget();
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                handler.obtainMessage(MESSAGE_BOX, "Library not valid").sendToTarget();
            }
            super.onPostExecute(result);
        }
    }

    /* This method shows the VerifyTempleAsync AsynTask operation */
    public class VerifyTempleAsync extends AsyncTask<Integer, Integer, Integer> {


        /* displays the progress dialog until background task is completed */
        @Override
        protected void onPreExecute() {
            progressDialog(context, "Place your finger on FPS ...");
            super.onPreExecute();
        }

        /* Task of VerifyTempleAsync performing in the background */
        @Override
        protected Integer doInBackground(Integer... params)  {
            brecentminituaedata = HexString.hexToBuffer(ServiceHelper.Fps_data_master[21]);
            if(brecentminituaedata.equals("")||brecentminituaedata.equals("null")) {
                handler.obtainMessage(MESSAGE_BOX,"Please capture finger and then verify").sendToTarget();
                blVerifyfinger = false;
            } else  {
                try {
                    fpsconfig = new FpsConfig(0, (byte) 0x0F);
                    iRetVal = fps.iFpsVerifyMinutiae(brecentminituaedata,fpsconfig);
                    Log.i("Verifi_FPS",""+iRetVal);
                    if (iRetVal==(-5)){
                        verifyPerFPSStatus="1";
                    }else {
                        verifyPerFPSStatus="0";
                    }
                } catch (NullPointerException e) {
                    iRetVal = DEVICE_NOTCONNECTED;
                    return iRetVal;
                }
            }
            return iRetVal;
        }

        /*
         * This function sends message to handler to display the status messages
         * of Diagnose in the dialog box
         */
        @Override
        protected void onPostExecute(Integer result) {
            dlgCustom.dismiss();
            if (iRetVal == DEVICE_NOTCONNECTED) {
                handler.obtainMessage(DEVICE_NOTCONNECTED, "Device not connected").sendToTarget();
                Toast.makeText(context, "Device not connected", LENGTH_LONG).show();
            } else if (iRetVal == FPS.SUCCESS) {
                handler.obtainMessage(MESSAGE_BOX, "Captured template verification is success").sendToTarget();
                // blVerifyfinger=false;
            } else if (iRetVal == FPS.FPS_INACTIVE_PERIPHERAL) {
                handler.obtainMessage(MESSAGE_BOX, "Peripheral is inactive").sendToTarget();
            } else if (iRetVal == FPS.TIME_OUT) {
                handler.obtainMessage(MESSAGE_BOX, "Capture finger time out").sendToTarget();
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                handler.obtainMessage(MESSAGE_BOX, "Illegal library").sendToTarget();
            } else if (iRetVal == FPS.FAILURE) {
                handler.obtainMessage(MESSAGE_BOX, "Captured template verification is failed,\nWrong finger placed").sendToTarget();
            } else if (iRetVal == FPS.PARAMETER_ERROR) {
                handler.obtainMessage(MESSAGE_BOX, "Parameter error").sendToTarget();
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                handler.obtainMessage(MESSAGE_BOX, "Library is in demo version").sendToTarget();
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                handler.obtainMessage(MESSAGE_BOX, "Connected  device is not license authenticated.").sendToTarget();
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                handler.obtainMessage(MESSAGE_BOX, "Library not valid").sendToTarget();
            } else if (iRetVal == FPS.SUCCESS) {
                blVerifyfinger = true;
                handler.obtainMessage(MESSAGE_BOX, "Capture finger success").sendToTarget();
            }

            super.onPostExecute(result);
        }
    }


    /* Handler to display UI response messages-1 */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_BOX:
                    String str = (String) msg.obj;
                    ShowDialog(str);
                    break;
                default:
                    break;
            }
        }

        ;
    };


    /* To show response messages */
    public void ShowDialog(String str) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Driver Verification");
        alertDialogBuilder.setMessage(str).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    /* This performs Progress dialog box to show the progress of operation */
    public static void progressDialog(Context context, String msg) {
        dlgCustom = new ProgressDialog(context);
        dlgCustom.setMessage(msg);
        dlgCustom.setIndeterminate(true);
        dlgCustom.setCancelable(false);
        dlgCustom.show();
    }

    private class startBluetoothDeviceTask extends AsyncTask<String, String, Integer> {
        private static final int RET_BULETOOTH_IS_START = 0x0001;
        private static final int RET_BLUETOOTH_START_FAIL = 0x04;
        private static final int miWATI_TIME = 15;
        private static final int miSLEEP_TIME = 150;
        private ProgressDialog mpd;

        @Override
        public void onPreExecute() {
            mpd = new ProgressDialog(RenewalFPS_Activity.this);
            mpd.setMessage("Please wait...!");
            mpd.setCancelable(false);
            mpd.setCanceledOnTouchOutside(false);
            mpd.show();
            mbBleStatusBefore = mBT.isEnabled();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            int iWait = miWATI_TIME * 1000;
			/* BT isEnable */
            if (!mBT.isEnabled()) {
                mBT.enable();
                //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
                while (iWait > 0) {
                    if (!mBT.isEnabled()) {
                        iWait -= miSLEEP_TIME;
                        Log.i("Entered1", "Finish Bluetooth");
                    } else
                        break;
                    SystemClock.sleep(miSLEEP_TIME);
                }
                if (iWait < 0)
                    return RET_BLUETOOTH_START_FAIL;
            }
            return RET_BULETOOTH_IS_START;
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result) {
            if (mpd.isShowing())
                mpd.dismiss();
            if (RET_BLUETOOTH_START_FAIL == result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RenewalFPS_Activity.this);
                builder.setTitle("BT Alert");
                builder.setMessage("Bluetooth device start fail");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBT.disable();
                        Log.i("Entered2", "Finish Bluetooth");
                        finish();
                    }
                });
                builder.create().show();
            } else if (RET_BULETOOTH_IS_START == result) {
                openDiscovery();
            }
        }
    }

    private class connSocketTask extends AsyncTask<String, String, Integer> {
        /**
         * Process waits prompt box
         */
        private ProgressDialog mpd = null;
        /**
         * Constants: connection fails
         */
        private static final int CONN_FAIL = 0x01;
        /**
         * Constant: the connection is established
         */
        private static final int CONN_SUCCESS = 0x02;

        /**
         * Thread start initialization
         */
        @Override
        public void onPreExecute() {
            this.mpd = new ProgressDialog(RenewalFPS_Activity.this);
            this.mpd.setMessage(getString(R.string.actMain_msg_device_connecting));
            this.mpd.setCancelable(false);
            this.mpd.setCanceledOnTouchOutside(false);
            this.mpd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            if (mGP.createConn(arg0[0])) {
                return CONN_SUCCESS;
            } else {
                return CONN_FAIL;
            }
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result) {
            this.mpd.dismiss();
            if (CONN_SUCCESS == result) {
                Toast.makeText(RenewalFPS_Activity.this, getString(R.string.actMain_msg_device_connect_succes), Toast.LENGTH_SHORT).show();
               // showBaudRateSelection();
            } else {
                Toast.makeText(getApplicationContext(), "Device Conncetion Failed", Toast.LENGTH_SHORT).show();
                showToast("Please Check Bluetooth Connection");
                showToast("Please Click Scan Connection");
            }
        }
    }

    public void showBaudRateSelection() { //TODO
        dlgRadioBtn = new Dialog(context);
        dlgRadioBtn.setCancelable(false);
        dlgRadioBtn.setTitle("Driver Verification");
        dlgRadioBtn.setContentView(R.layout.dlg_bardchange);
 		/* when the application is started it is presumed that device is started
 		 * along with it (i.e. Switched ON) hence by default the device will be in
 		 * 9600bps so entering directly to next activity
 		 */
        RadioButton radioBtn9600 = (RadioButton) dlgRadioBtn.findViewById(R.id.first_radio);
        radioBtn9600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 				/* ResetBtnEnable will disable the reset button in Exit dialog box as
 				 * the connection is not made in bps */
                blnResetBtnEnable = false;
                dlgRadioBtn.dismiss();
               /* Intent all_intent = new Intent(getApplicationContext(),Act_SelectPeripherals.class);
                all_intent.putExtra("connected", false);
                startActivityForResult(all_intent, 3);*/
            }
        });
        RadioButton radioBtn1152 = (RadioButton) dlgRadioBtn.findViewById(R.id.second_radio);
        radioBtn1152.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 				/* ResetBtnEnable will enable the reset button in Exit dialog box as
 				 * the connection will be made in  	115200bps */
                //ResetBtnEnable = true;
                blnResetBtnEnable = true;
                dlgRadioBtn.dismiss();
                BaudRateTask increaseBaudRate = new BaudRateTask();
                increaseBaudRate.execute(0);
            }
        });
        RadioButton ibc = (RadioButton) dlgRadioBtn.findViewById(R.id.ibc_radio);
        ibc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 				/* ResetBtnEnable will disable the reset button in Exit dialog box as
 				 * the connection is not made in 115200bps */
                //ResetBtnEnable = false;
                dlgRadioBtn.dismiss();
                /*Intent all_intent = new Intent(getApplicationContext(),Act_SelectPeripherals.class);
                all_intent.putExtra("connected", false);
                startActivityForResult(all_intent, 3);*/
            }
        });
        dlgRadioBtn.show();
    }

    private void openDiscovery() {
        Intent intent = new Intent(this, Act_BTDiscovery.class);
        this.startActivityForResult(intent, REQUEST_DISCOVERY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //mainlay.setVisibility(View.VISIBLE);
        Log.i("resultcode", "--->" + resultCode);
        Log.i("requestCode", "--->" + requestCode);
        if (requestCode == REQUEST_DISCOVERY) {
            if (Activity.RESULT_OK == resultCode) {
                //this.mllDeviceCtrl.setVisibility(View.VISIBLE);
                this.mhtDeviceInfo.put("NAME", data.getStringExtra("NAME"));
                this.mhtDeviceInfo.put("MAC", data.getStringExtra("MAC"));
                this.mhtDeviceInfo.put("COD", data.getStringExtra("COD"));
                this.mhtDeviceInfo.put("RSSI", data.getStringExtra("RSSI"));
                this.mhtDeviceInfo.put("DEVICE_TYPE", data.getStringExtra("DEVICE_TYPE"));
                this.mhtDeviceInfo.put("BOND", data.getStringExtra("BOND"));
                this.showDeviceInfo();

                if (storage.getValue(Constants.BT_ADDRESS) != null) {
                    new connSocketTask().execute(storage.getValue(Constants.BT_ADDRESS));
                } else {
                    Toast.makeText(context, "Device name not retrieved", LENGTH_LONG).show();
                }
            } else if (Activity.RESULT_CANCELED == resultCode) {
                this.finish();
            }
        } else if (requestCode == 3) {
            finish();
        }
    }

    @SuppressLint("StringFormatMatches")
    private void showDeviceInfo() {
        /*this.mtvDeviceInfo.setText(
                String.format(getString(R.string.actMain_device_info),
                        this.mhtDeviceInfo.get("NAME"),
                        this.mhtDeviceInfo.get("MAC"),
                        this.mhtDeviceInfo.get("COD"),
                        this.mhtDeviceInfo.get("RSSI"),
                        this.mhtDeviceInfo.get("DEVICE_TYPE"),
                        this.mhtDeviceInfo.get("BOND"))
        );*/
    }

    // increases the device baud rate from 9600bps to 115200bps
    public class BaudRateTask extends AsyncTask<Integer, Integer, Integer> {
        private static final int CONN_FAIL = 0x01;
        /**
         * Constant: the connection is established
         */
        private static final int CONN_SUCCESS = 0x02;
        private static final int CONN_NO_DEVICE = 0x03;

        @Override
        protected void onPreExecute() { //TODO
            // shows a progress dialog until the baud rate process is complete
            ProgressDialog(context, "Please Wait ...");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                //Log.d(TAG, "Change the peripheral Speed");
                bdchange = new BaudChange(setupInstance,
                        BluetoothComm.mosOut, BluetoothComm.misIn);
                iRetVal = bdchange.iSwitchPeripheral1152();
                Log.e(TAG, "iRetval....." + iRetVal);
                Log.e(TAG, "bdchange is instantiated");
                if (iRetVal == BaudChange.BC_SUCCESS) {
                    Log.e(TAG, "BaudChange.BC_SUCCESS");
                    //SystemClock.sleep(2000);
                    Log.e(TAG, "1");
                    //BluetoothComm.mosOut=null;
                    //BluetoothComm.misIn=null;
                    mGP.mosOut = null;
                    mGP.misIn = null;
                    //btcomm.closeConn();
                    mGP.mBTcomm.closeConn();
                    Log.e(TAG, "2");
                    //mGP.mBTcomm.closeConn();
                    //SystemClock.sleep(3000);
                    if (mBT != null) {
                        mBT.cancelDiscovery();
                    }
                    Log.e(TAG, "3");
                    SystemClock.sleep(3000);
                    //boolean b = mGP.createConn("");
                    //Log.e(TAG, "baudchangereset task.... arg[0]"+arg0);
                    boolean b = mGP.mBTcomm.createConn();
                    Log.e(TAG, "+++++++++++bConnected......" + b);
                    //boolean b = mGP.createConn();
                    Log.e(TAG, "4");
                    if (b == true)
                        mGP.mBTcomm.isConnect();
                    Log.e(TAG, "5");
                    SystemClock.sleep(3000);
                    bdchange.iSwitchBT1152(BluetoothComm.mosOut, BluetoothComm.misIn);
                    //	select1152_RadioBtn = false;
                    return CONN_SUCCESS;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return CONN_FAIL;
            }
            return CONN_FAIL;
        }
				/*iRetVal = bdchange.iSwitchPeripheral1152();
				Log.e(TAG, "iswitch peripherals..."+iRetVal);
				if(iRetVal==BaudChange.BC_SUCCESS){
					Log.e(TAG, "baudchange suceess");
				 Thread.sleep(3000);
				 BluetoothComm.mosOut=null;
				 BluetoothComm.misIn=null;
				 mGP.closeConn();
				Thread.sleep(3000);
				if (mBT != null) {
					mBT.cancelDiscovery();
				}
				Thread.sleep(3000);
				boolean b =mGP.createConn(mBDevice.getAddress());
				if(b==true)
				mGP.mBTcomm.isConnect();
				Thread.sleep(3000);
				bdchange.iSwitchBT1152(BluetoothComm.mosOut,BluetoothComm.misIn);
				}
				Log.e(TAG, "baud change fail");
			} catch (Exception e) {
				e.printStackTrace();

			}
			return iRetVal;
		}*/

        /* goes to next activity after setting the new baud rate*/
        @Override
        protected void onPostExecute(Integer result) {
            prgDialog.dismiss();
            if (CONN_SUCCESS == result) {
                hander.obtainMessage(MESSAGE_BOX, "Baud Change Successful").sendToTarget();
                /*Intent all_intent = new Intent(getApplicationContext(),Act_SelectPeripherals.class);
                all_intent.putExtra("connected", false);
                startActivityForResult(all_intent, 3);*/
            } else if (CONN_NO_DEVICE == result) {
                Log.e(TAG, "Bletooth No device is set");
                hander.obtainMessage(MESSAGE_BOX, "Please connect to Bluetooth and chagne baudrate").sendToTarget();
            } else {
                hander.obtainMessage(MESSAGE_BOX, "Baud Change FAIL").sendToTarget();
            }
        }
    }

    /* Handler to display UI response messages-2   */
    @SuppressLint("HandlerLeak")
    Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_BOX:
                    String str = (String) msg.obj;
                    showdialog(str);

            }
        }

        ;
    };


    public void showdialog(String str) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Driver Verification");
        alertDialogBuilder.setMessage(str).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
				/* create alert dialog*/
        AlertDialog alertDialog = alertDialogBuilder.create();
				/* show alert dialog*/
        alertDialog.show();
    }

    public static void ProgressDialog(Context context, String msg) {
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage(msg);
        prgDialog.setIndeterminate(true);
        prgDialog.setCancelable(false);
        prgDialog.show();
    }

    public class AsyncTask_RenewalFPS_final_submit extends AsyncTask<Void, Void, String> {

        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(RenewalFPS_Activity.this);

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

            ServiceHelper.final_renewalverifyPerDl(Update_FPS.applID.getText().toString().trim(), Update_FPS.per_appl_id.getText().toString().trim(),
                    "" + Welcome.input_username.getText().toString().trim(), ""+Update_FPS.welcome.getText().toString().trim(), "" + "1", "" + date.toString());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            removeDialog(PROGRESS_DIALOG);
            //pdia.dismiss();
            Log.i("TWO_Task", "Entered_Final_Response"+ServiceHelper.renwaldataforServices);

            if (ServiceHelper.renwaldataforServices.equals("")||ServiceHelper.renwaldataforServices.equals("NA")||ServiceHelper.renwaldataforServices.equals("0")
                    ||ServiceHelper.renwaldataforServices.equals("anyType{}")||ServiceHelper.renwaldataforServices.equals("null")) {
                showToast("No DataFound");
            }else {
                // 1@null@Ap03020130007577

                if (ServiceHelper.renewal_data_master[0].equalsIgnoreCase("1")){

                   // showToast("Document Verification is Completed ");

                    TextView title = new TextView(RenewalFPS_Activity.this);
                    title.setText("Verification Status");
                    title.setBackgroundColor(Color.BLUE);
                    title.setGravity(Gravity.CENTER);
                    title.setTextColor(Color.WHITE);
                    title.setTextSize(26);
                    title.setTypeface(title.getTypeface(), Typeface.BOLD);
                    // title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exit_logo, 0, R.drawable.exit_logo, 0);
                    title.setPadding(20, 0, 20, 0);
                    title.setHeight(70);


                    String otp_message = "\nYour Renewal Document Verification is Updated...! \n" ;

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RenewalFPS_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
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
                                    if (isOnline()) {
                                        if (!ServiceHelper.renewal_data_master[1].equals("")) {
                                            Log.i("Final_dataMaster",""+ServiceHelper.renewal_data_master[1]);
                                            Aysn_download_pdffile pdf = new Aysn_download_pdffile();
                                            pdf.execute();
                                        }else {
                                            showToast(" PDF Not Found ");
                                        }
                                    } else {
                                        showToast("Please Check Your Internet Connection...!");
                                    }
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
                TextView title = new TextView(RenewalFPS_Activity.this);
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RenewalFPS_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
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
                                Intent men = new Intent(getApplicationContext(),moduleicon_Activity.class);
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

      /*  private boolean isOnline() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }*/


        public class Aysn_download_pdffile extends AsyncTask<Void, Void, String> {
            private android.content.Context yourContext;

            ProgressDialog pdia = new ProgressDialog(RenewalFPS_Activity.this);

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                showDialog(PROGRESS_DIALOG);
                /*pdia.setMessage("Please Wait...");
                pdia.setCancelable(false);
                pdia.show();*/
            }

            //    public String getExitUser(String glno,String imei,String password);
            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                Log.i("Dl_Task", "Entered_One");
                /*File file = new File(Environment.getExternalStorageDirectory(),
                        "Report.pdf");
                Uri path = Uri.fromFile(file);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(path, "application/pdf");
                try {
                    startActivity(pdfOpenintent);
                }
                catch (ActivityNotFoundException e) {

                }*/
              /*File f =new File(ServiceHelper.final_data_master[1]);*/
                // -> maven.pdf
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "Driver"+File.separator+date);
                folder.mkdir();
                //File fileUrl=new File(ServiceHelper.final_data_master[1]);
                String fileUrl=ServiceHelper.renewal_data_master[1];
                String fileName=ServiceHelper.renewal_data_master[2]+".pdf";
                Log.i("FileNAme-->",""+fileName);
                File pdfFile = new File(folder, fileName);

                try{
                    pdfFile.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.i(" url---> ",""+fileUrl );

                byte[] pdfAsBytes = Base64.decode(fileUrl, 0);
                FileOutputStream os;

                try {
                    os = new FileOutputStream(pdfFile, false);
                    os.write(pdfAsBytes);
                    os.flush();
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getApplicationContext(),Renewal_Print.class);
                startActivity(i);
                //FileDownloader.downloadFile(fileUrl, pdfFile);
                return null;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                 removeDialog(PROGRESS_DIALOG);
                //pdia.dismiss();
            }

        }


     /*   public class Aysn_download_txetfile extends AsyncTask<Void, Void, String> {
            private android.content.Context yourContext;

            ProgressDialog pdia = new ProgressDialog(RenewalFPS_Activity.this);

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                //showDialog(PROGRESS_DIALOG);
                pdia.setMessage("Please Wait...");
                pdia.setCancelable(false);
                pdia.show();
            }

            //    public String getExitUser(String glno,String imei,String password);
            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                Log.i("Wel come to pdf","********************");
                String str=ServiceHelper.final_data_master[1];

               *//* if (!ServiceHelper.final_data_master[1].toString().trim().equals("0")) {
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(Person_FPS.this).threadPriority(Thread.NORM_PRIORITY - 2)
                            .denyCacheImageMultipleSizesInMemory()
                            .discCacheFileNameGenerator(new Md5FileNameGenerator())
                            .tasksProcessingOrder(QueueProcessingType.LIFO).build();
                    ImageLoader.getInstance().init(config);

                    imageLoader = ImageLoader.getInstance();
                    imagepath=ServiceHelper.final_data_master[1].toString().trim();
                    imageLoader.displayImage(ServiceHelper.final_data_master[1].toString().trim(), web_pdf);
                    Log.i("displayImage-->",""+ServiceHelper.final_data_master[1].toString().trim());
                    //	img_display.displayImage(imageLoader);
                }*//*
                final File dwldsPath = new File("Driver" + "lax.pdf");
                byte[] pdfAsBytes = Base64.decode(str, 0);
                FileOutputStream os;

                try {
                    os = new FileOutputStream(dwldsPath, false);
                    os.write(pdfAsBytes);
                    os.flush();
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



             *//* try {
                    byte[] decodestring = Base64.decode("" + MenuBorad.aadhar_details[13]
                            .toString().trim(), Base64.DEFAULT);
                    Bitmap decocebyte = BitmapFactory.decodeByteArray(
                            decodestring, 0, decodestring.length);
                    web_pdf.setImageBitmap(decocebyte);

                } catch (IllegalArgumentException e) {
                    // TODO: handle exception
                   // web_pdf.setImageResource(R.drawable.photo);
                }*//*
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // removeDialog(PROGRESS_DIALOG);
                pdia.dismiss();

            }
        }*/



    /*// Share image
    private void shareImage(Uri imagePath) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image*//*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }*/

        @SuppressLint("NewApi")
        protected Dialog onCreateDialog(int id) {
            // TODO Auto-generated method stub
            switch (id) {
                case PROGRESS_DIALOG:
                    ProgressDialog pd = ProgressDialog.show(RenewalFPS_Activity.this, "", "",	true);
                    pd.setContentView(R.layout.custom_progress_dialog);
                    pd.setCancelable(false);

                    return pd;
                default:

                    break;

            }
            return null;
        }

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showToast("Please Click on Logout Button");
    }
}
