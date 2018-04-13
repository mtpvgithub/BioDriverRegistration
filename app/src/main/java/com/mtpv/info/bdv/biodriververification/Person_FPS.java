package com.mtpv.info.bdv.biodriververification;

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
import com.mtpv.info.bdv.biodriververification.bluetooth.BluetoothComm;
import com.mtpv.info.bdv.biodriververification.storage.Constants;
import com.mtpv.info.bdv.biodriververification.storage.Storage;
import com.mtpv.info.bdv.biodriververification.update.moduleicon_Activity;
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

public class Person_FPS extends AppCompatActivity {
    TextView fps_name ,fps_dlnum ,fps_appId ;
    RadioGroup rd_group;
    RadioButton rd_left, rd_right;

    RadioButton ch_LF, ch_RF, ch_MF, ch_IF, ch_Thumb;
    Button img_fps, verify_btn, final_ftp_btn;
    ImageView scan_btn  ,back;
    LinearLayout llout_finger;

    public static StringBuffer Outcheck;
    public String str1;

    public static final int PROGRESS_DIALOG = 1;

    ImageLoader imageLoader;
    public  static String imagepath;
    OutputStream outputStream;
    InputStream inputstream;
    public static String verftn_hand_value = "null", verify_finger_vale = "null", verifyPerStatus = "null";

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
    byte[] bMinutiaeData;
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
        setContentView(R.layout.activity_person__fps);

        rd_group = (RadioGroup) findViewById(R.id.rd_group);

        web_pdf=(ImageView)findViewById(R.id.web_pdf);

        fps_dlnum=(TextView)findViewById(R.id.fps_dlnum);
        fps_appId=(TextView)findViewById(R.id.fps_appID);

        rd_left = (RadioButton) findViewById(R.id.rd_left);
        rd_right = (RadioButton) findViewById(R.id.rd_right);
        ch_LF = (RadioButton) findViewById(R.id.ch_LF);
        ch_RF = (RadioButton) findViewById(R.id.ch_RF);
        ch_MF = (RadioButton) findViewById(R.id.ch_MF);
        ch_IF = (RadioButton) findViewById(R.id.ch_IF);
        ch_Thumb = (RadioButton) findViewById(R.id.ch_Thumb);

        storage = new Storage(Person_FPS.this);

        img_fps = (Button) findViewById(R.id.img_fps);
        verify_btn = (Button) findViewById(R.id.verify_btn);
        scan_btn = (ImageView) findViewById(R.id.scan_btn);
        back=(ImageView)findViewById(R.id.btn_back);
        final_ftp_btn = (Button) findViewById(R.id.final_ftp_btn);
        llout_finger = (LinearLayout) findViewById(R.id.llout_finger);


        if (null == mBT) {
            Toast.makeText(this, "Bluetooth module not found", LENGTH_LONG).show();
            this.finish();
        }

        fps_dlnum.setText(""+MenuBorad.ed_aadharnum.getText().toString().trim());
        fps_appId.setText(""+MenuBorad.ed_appID.getText().toString().trim());
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
            new connSocketTask().execute(storage.getValue(Constants.BT_ADDRESS));
        } else {
            Toast.makeText(context, "Device name not retrieved", LENGTH_LONG).show();
        }


      /*  rd_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rd_left.isChecked()){
                    llout_finger.setVisibility(View.VISIBLE);
                    verftn_hand_value = "RH";
                    Log.i("Select hand--->",""+verftn_hand_value);
                }else if(rd_right.isChecked()){
                    llout_finger.setVisibility(View.VISIBLE);
                    verftn_hand_value = "RH";
                    Log.i("Select hand--->",""+verftn_hand_value);
                }
            }
        });*/


        rd_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rd_left.isChecked()) {
                    llout_finger.setVisibility(View.VISIBLE);
                    verftn_hand_value = "LH";
                    Log.i("Select hand--->", "" + verftn_hand_value);

                }
            }
        });


        rd_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rd_right.isChecked()) {
                    llout_finger.setVisibility(View.VISIBLE);
                    verftn_hand_value = "RH";
                    Log.i("Select hand--->", "" + verftn_hand_value);
                    // ch_LF, ch_RF, ch_MF, ch_IF, ch_Thumb;

                }
            }
        });


        // ch_LF, ch_RF, ch_MF, ch_IF, ch_Thumb;

        ch_LF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_LF.isChecked()) {
                    //ch_LF.setEnabled(true);
                    ch_LF.setChecked(true);
                    ch_RF.setChecked(false);
                    ch_MF.setChecked(false);
                    ch_IF.setChecked(false);
                    ch_Thumb.setChecked(false);
                }
                verify_finger_vale = "Baby";
            }
        });

        ch_RF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ch_RF.isChecked()) {
                    ch_LF.setChecked(false);
                    ch_RF.setChecked(true);
                    ch_MF.setChecked(false);
                    ch_IF.setChecked(false);
                    ch_Thumb.setChecked(false);
                }
                verify_finger_vale = "Ring finger";
            }
        });

        ch_MF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_MF.isChecked()) {

                    ch_LF.setChecked(false);
                    ch_RF.setChecked(false);
                    ch_MF.setChecked(true);
                    ch_IF.setChecked(false);
                    ch_Thumb.setChecked(false);

                }
                verify_finger_vale = "Middle finger";
            }
        });

        ch_IF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_IF.isChecked()) {

                    ch_LF.setChecked(false);
                    ch_RF.setChecked(false);
                    ch_MF.setChecked(false);
                    ch_IF.setChecked(true);
                    ch_Thumb.setChecked(false);

                }

                verify_finger_vale = "Index finger";
            }
        });


        ch_Thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_Thumb.isChecked()) {
                    ch_LF.setChecked(false);
                    ch_RF.setChecked(false);
                    ch_MF.setChecked(false);
                    ch_IF.setChecked(false);
                    ch_Thumb.setChecked(true);
                }

                verify_finger_vale = "Thumb finger";
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent model = new Intent(getApplicationContext(),MenuBorad.class);
                startActivity(model);
            }
        });


        img_fps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("**HAND_VALUES--->", "" + verftn_hand_value);
                Log.i("***FINGER_VALUES-->", "" + verify_finger_vale);
                if ("".equals(verftn_hand_value) || "null".equals(verftn_hand_value)) {
                    showToast("Please Choose Hand...!");
                } else if ("".equals(verify_finger_vale) || "null".equals(verify_finger_vale)) {
                    showToast("Please Choose any One Figaur...!");
                } else {
                    // showToast("Please Choose Hand...!");
                   /* if (!ch_Thumb.isChecked() || !ch_IF.isChecked() || !ch_MF.isChecked() || !ch_RF.isChecked() || !ch_LF.isChecked()) {
                        showToast("Please Choose any One Figaur...!");
                    } else {*/

                       /*  Outcheck = new StringBuffer();
                        Outcheck.append("Baby Finger").append(ch_LF.isChecked());
                        Outcheck.append("Ring Finger").append(ch_RF.isChecked());
                        Outcheck.append("Middle Finger").append(ch_MF.isChecked());
                        Outcheck.append("Index Finger").append(ch_IF.isChecked());
                        Outcheck.append("Thumb Finger").append(ch_Thumb.isChecked());
                        Log.i("Select Finger--->", "" + Outcheck.toString());
                        // Toast.makeText(Person_FPS.this, Outcheck.toString(), Toast.LENGTH_LONG).show();*/

                    try {
                        outputStream = BluetoothComm.mosOut;
                        inputstream = BluetoothComm.misIn;
                        Log.i("outputStream","-->"+outputStream);
                        Log.i("inputstream","-->"+inputstream);

                        fps = new FPS(setupInstance, outputStream, inputstream);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    CaptureFingerAsyn captureFinger = new CaptureFingerAsyn();
                    captureFinger.execute(0);

                }
            }
        });

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyTempleAsync verifyTemp = new VerifyTempleAsync();
                verifyTemp.execute(0);
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
        final_ftp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("***FINGER_VALUES-->", "" + verify_finger_vale);
                if (isOnline()) {
                    new AsyncTask_ftp_final_submit().execute();
                } else {
                    showToast("Please Check Your Internet Connection...!");

                    Log.i("***ISONLINE-->", "Please Check Your Internet Connection...!");
                }
            }
        });
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
   /* public void download(View v)
    {

    }*/

    /*private class DownloadFile extends AsyncTask<String, Void, Void>{

        protected void onPreExecute() {
            progressDialog(context, "Place your Wait ...");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            File fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Driver");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.i(" url ",""+fileUrl );


            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
        @Override
        protected void onPostExecute(Integer result) {
            dlgCustom.dismiss();
        }
    }*/

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
            Toast.makeText(Person_FPS.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
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
        protected Integer doInBackground(Integer... params) {
            if (blVerifyfinger == false) {
                dlgCustom.dismiss();
                handler.obtainMessage(MESSAGE_BOX, "Please capture finger and then verify").sendToTarget();
            } else if (blVerifyfinger == true) {
                try {
                    // convert string(Response fps value stored in db) to byte[] and send in below line
                    iRetVal = fps.iFpsVerifyMinutiae(brecentminituaedata, new FpsConfig(1, (byte) 0x0f));
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
                //blVerifyfinger=false;
                // call Renewal AsyncTask
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
            }
            super.onPostExecute(result);
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
                 bMinutiaeData = fps.bGetMinutiaeData();
                Log.i("bMinutiaeData**","-->"+bMinutiaeData);
                str1 = HexString.bufferToHex(bMinutiaeData);
                Log.i("Capture", "Finger Data:\n" + str1);
                if (str1.equals("") || str1.equals("0") || str1.equals("NA")) {
                    verifyPerStatus = "0";
                } else {
                    verifyPerStatus = "1";
                }
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
            mpd = new ProgressDialog(Person_FPS.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Person_FPS.this);
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
            this.mpd = new ProgressDialog(Person_FPS.this);
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
                Toast.makeText(Person_FPS.this, getString(R.string.actMain_msg_device_connect_succes), Toast.LENGTH_SHORT).show();
               // showBaudRateSelection();
            } else {
                showToast("Please Click Scan Connection");
                Toast.makeText(Person_FPS.this, "Device Conncetion Failed", Toast.LENGTH_SHORT).show();
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

    public class AsyncTask_ftp_final_submit extends AsyncTask<Void, Void, String> {

        private android.content.Context yourContext;

        ProgressDialog pdia = new ProgressDialog(Person_FPS.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
            /*pdia.setMessage("Please Wait...");
            pdia.setCancelable(false);
            pdia.show();*/
        }

        /*  verifyPerDl(String dlno, String applicationId,
                      String verifyPerByPid, String verifyPerStatus, String verifyPerDt,
                      String fingerprintdata, String biometricChoices, String fingerType)*/
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("Dl_Task", "Entered_One");

           /* Log.i("Dl_Task", "Entered_One" + MenuBorad.ed_aadharnum.getText().toString().trim());
            Log.i("Dl_Task", "Entered_One" + MenuBorad.ed_appID.getText().toString().trim());
            Log.i("Dl_Task", "Entered_One" + LoginActivity.input_username.getText().toString().trim());
            Log.i("Dl_Task", "Entered_One" + 1);
            Log.i("Dl_Task", "Entered_One" + date.toString());
            Log.i("Dl_Task", "Entered_One" + str1);
            Log.i("Dl_Task", "Entered_One" + verftn_hand_value);
            Log.i("Dl_Task", "Entered_One" + verify_finger_vale);*/


            ServiceHelper.final_verifyPerDl(MenuBorad.ed_aadharnum.getText().toString().trim(), MenuBorad.ed_appID.getText().toString().trim(),
                    "" + Welcome.input_username.getText().toString().trim(), "" + "1", "" + date.toString(),
                    "" + str1, "" + verftn_hand_value, "" + verify_finger_vale);
            Log.i("Dl_Task-->", "Final_serives Calling");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            removeDialog(PROGRESS_DIALOG);
           // pdia.dismiss();
            Log.i("TWO_Task", "Entered_TWO");

            if (ServiceHelper.final_fpsdata.equals("")||ServiceHelper.final_fpsdata.equals("NA")||ServiceHelper.final_fpsdata.equals("0")) {
                showToast("No Data Found");
            }else {

                if (ServiceHelper.final_data_master[0].equals("0")) {
                    Log.i("FINAL DATA_postion0 ", "" + ServiceHelper.final_data_master[0]);
                    Toast.makeText(Person_FPS.this, "No data Found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Person_FPS.this, "Person verification success", Toast.LENGTH_SHORT).show();
                    Log.i("FINAL DATA_postion1 ", "" + ServiceHelper.final_fpsdata);

                    if (!ServiceHelper.final_data_master[1].equals("")) {
                        Log.i("FINAL DATA_postion2", "" + ServiceHelper.final_data_master[1]);

                      //  showToast("Document Verification is Incompleted ");
                        TextView title = new TextView(Person_FPS.this);
                        title.setText("FPS Verification Status");
                        title.setBackgroundColor(Color.BLUE);
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(Color.WHITE);
                        title.setTextSize(26);
                        title.setTypeface(title.getTypeface(), Typeface.BOLD);
                        //title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exit_logo, 0, R.drawable.exit_logo, 0);
                        title.setPadding(20, 0, 20, 0);
                        title.setHeight(70);


                        String otp_message = "\n Person verification success...! \n";

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Person_FPS.this, AlertDialog.THEME_HOLO_LIGHT);
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
                                        if (isOnline()) {

                                            new Aysn_download_pdffile().execute();
                                            //    new Aysn_download_txetfile().execute();
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


                    }
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

            ProgressDialog pdia = new ProgressDialog(Person_FPS.this);

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
                String fileUrl=ServiceHelper.final_data_master[1];
                String fileName=ServiceHelper.final_data_master[2]+".pdf";
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

                Intent i = new Intent(getApplicationContext(),Printpreview.class);
                startActivity(i);
                //FileDownloader.downloadFile(fileUrl, pdfFile);
                return null;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                removeDialog(PROGRESS_DIALOG);
               //  pdia.dismiss();

            }
        }


        public class Aysn_download_txetfile extends AsyncTask<Void, Void, String> {
            private android.content.Context yourContext;

            ProgressDialog pdia = new ProgressDialog(Person_FPS.this);

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
               Log.i("Wel come to pdf","********************");
             String str=ServiceHelper.final_data_master[1];

               /* if (!ServiceHelper.final_data_master[1].toString().trim().equals("0")) {
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
                }*/
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



             /* try {
                    byte[] decodestring = Base64.decode("" + MenuBorad.aadhar_details[13]
                            .toString().trim(), Base64.DEFAULT);
                    Bitmap decocebyte = BitmapFactory.decodeByteArray(
                            decodestring, 0, decodestring.length);
                    web_pdf.setImageBitmap(decocebyte);

                } catch (IllegalArgumentException e) {
                    // TODO: handle exception
                   // web_pdf.setImageResource(R.drawable.photo);
                }*/
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                 removeDialog(PROGRESS_DIALOG);
                //pdia.dismiss();

            }
        }



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
                    ProgressDialog pd = ProgressDialog.show(Person_FPS.this, "", "",	true);
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
        // TODO Auto-generated method stub
        // super.onBackPressed();

        showToast("Please Click on Back Button");
    }
}