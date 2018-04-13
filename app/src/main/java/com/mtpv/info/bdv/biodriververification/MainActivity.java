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
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button bt_scan, fps_capture, fps_verify, bt_connect ,next ;
    private TextView mtvDeviceInfo = null;

    OutputStream outputStream;
    InputStream inputstream;

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
    static BaudChange bdchange=null;
    private int iRetVal;
    private final int MESSAGE_BOX = 1;
    public static final int DEVICE_NOTCONNECTED = -100;
    static ProgressDialog dlgCustom,dlgpd;
    FPS fps;
    private boolean blVerifyfinger = false;
    Storage storage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Welcome","**WELCOME*TO*MainScreen**");

        storage = new Storage(MainActivity.this);
        bt_scan = (Button)findViewById(R.id.bt_scan);
        fps_capture = (Button)findViewById(R.id.fps_capture);
        fps_verify = (Button)findViewById(R.id.fps_verify);
        bt_connect = (Button)findViewById(R.id.bt_connect);
        next = (Button)findViewById(R.id.fps_next);
        this.mtvDeviceInfo = (TextView)this.findViewById(R.id.mtvDeviceInfo);
        if (null == mBT){
            Toast.makeText(this, "Bluetooth module not found", Toast.LENGTH_LONG).show();
            this.finish();
        }

        try {
            setupInstance = new Setup();
            boolean activate = setupInstance.blActivateLibrary(context,R.raw.licence_full);
            if (activate == true) {
                Log.d(TAG,"Leopard Library Activated......");
            } else if (activate == false) {
                Log.d(TAG,"Leopard Library Not Activated...");
            }
        } catch (Exception e) { }

        mGP = ((GlobalPool)this.getApplicationContext());
        new startBluetoothDeviceTask().execute("");

        //bt_scan.setOnClickListener(this);

        fps_capture.setOnClickListener(this);
        fps_verify.setOnClickListener(this);
        next.setOnClickListener(this);
        bt_connect.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.bt_connect:
                Log.i("mBDevice","-->"+storage.getValue(Constants.BT_ADDRESS));
                if (storage.getValue(Constants.BT_ADDRESS)!=null) {
                    new connSocketTask().execute(storage.getValue(Constants.BT_ADDRESS));
                }
                else {
                    Toast.makeText(context, "Device name not retrieved", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.fps_capture :
                try {
                    outputStream = BluetoothComm.mosOut;
                    inputstream = BluetoothComm.misIn;
                    Log.i("Welcome-->",""+outputStream);
                    Log.i("Welcome-->",""+inputstream);
                    fps = new FPS(setupInstance, outputStream, inputstream);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                CaptureFingerAsyn captureFinger = new CaptureFingerAsyn();
                captureFinger.execute(0);
                break;

            case R.id.fps_verify :
                VerifyTempleAsync verifyTemp = new VerifyTempleAsync();
                verifyTemp.execute(0);
                break;
            case R.id.fps_next :
                Intent next = new Intent(getApplicationContext(),Welcome.class);
                startActivity(next);
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
                handler.obtainMessage(MESSAGE_BOX,"Please capture finger and then verify").sendToTarget();
            } else if (blVerifyfinger == true) {
                try {
                    // convert string(Response fps value stored in db) to byte[] and send in below line
                    iRetVal = fps.iFpsVerifyMinutiae(brecentminituaedata,new FpsConfig(1, (byte) 0x0f));
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
                handler.obtainMessage(DEVICE_NOTCONNECTED,"Device not connected").sendToTarget();
                Toast.makeText(context, "Device not connected", Toast.LENGTH_LONG).show();
            } else if (iRetVal == FPS.SUCCESS) {
                handler.obtainMessage(MESSAGE_BOX,"Captured template verification is success").sendToTarget();
                //blVerifyfinger=false;
                // call Renewal AsyncTask
            } else if (iRetVal == FPS.FPS_INACTIVE_PERIPHERAL) {
                handler.obtainMessage(MESSAGE_BOX,"Peripheral is inactive").sendToTarget();
            } else if (iRetVal == FPS.TIME_OUT) {
                handler.obtainMessage(MESSAGE_BOX, "Capture finger time out").sendToTarget();
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                handler.obtainMessage(MESSAGE_BOX, "Illegal library").sendToTarget();
            } else if (iRetVal == FPS.FAILURE) {
                handler.obtainMessage(MESSAGE_BOX,"Captured template verification is failed,\nWrong finger placed").sendToTarget();
            } else if (iRetVal == FPS.PARAMETER_ERROR) {
                handler.obtainMessage(MESSAGE_BOX, "Parameter error").sendToTarget();
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                handler.obtainMessage(MESSAGE_BOX,"Library is in demo version").sendToTarget();
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                handler.obtainMessage(MESSAGE_BOX,"Connected  device is not license authenticated.").sendToTarget();
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                handler.obtainMessage(MESSAGE_BOX,"Library not valid").sendToTarget();
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
                byte[] bMinutiaeData =fps.bGetMinutiaeData();
                String str1 = HexString.bufferToHex(bMinutiaeData);
                Log.i("Capture","Finger Data:\n"+str1);
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
                handler.obtainMessage(DEVICE_NOTCONNECTED,"Device not connected").sendToTarget();
            } else if (iRetVal == FPS.SUCCESS) {
                blVerifyfinger = true;
                handler.obtainMessage(MESSAGE_BOX, "Capture finger success").sendToTarget();
            } else if (iRetVal == FPS.FPS_INACTIVE_PERIPHERAL) {
                handler.obtainMessage(MESSAGE_BOX,"Peripheral is inactive").sendToTarget();
            } else if (iRetVal == FPS.TIME_OUT) {
                handler.obtainMessage(MESSAGE_BOX, "Capture finger time out").sendToTarget();
            } else if (iRetVal == FPS.FAILURE) {
                handler.obtainMessage(MESSAGE_BOX, "Capture finger failed").sendToTarget();
            } else if (iRetVal == FPS.PARAMETER_ERROR) {
                handler.obtainMessage(MESSAGE_BOX, "Parameter error").sendToTarget();
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                handler.obtainMessage(MESSAGE_BOX,"Connected  device is not license authenticated.").sendToTarget();
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                handler.obtainMessage(MESSAGE_BOX,"Library not valid").sendToTarget();
            }
            super.onPostExecute(result);
        }
    }


    /* Handler to display UI response messages */
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
        };
    };


    /* To show response messages */
    public void ShowDialog(String str) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Leopard Demo Application");
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
        public void onPreExecute(){
            mpd = new ProgressDialog(MainActivity.this);
            mpd.setMessage("Please wait...!");
            mpd.setCancelable(false);
            mpd.setCanceledOnTouchOutside(false);
            mpd.show();
            mbBleStatusBefore = mBT.isEnabled();
        }
        @Override
        protected Integer doInBackground(String... arg0){
            int iWait = miWATI_TIME * 1000;
			/* BT isEnable */
            if (!mBT.isEnabled()){
                mBT.enable();
                //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
                while(iWait > 0){
                    if (!mBT.isEnabled()){
                        iWait -= miSLEEP_TIME;
                        Log.i("Entered1", "Finish Bluetooth");
                    }
                    else
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
        public void onPostExecute(Integer result){
            if (mpd.isShowing())
                mpd.dismiss();
            if (RET_BLUETOOTH_START_FAIL == result){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("BT Alert");
                builder.setMessage("Bluetooth device start fail");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        mBT.disable();
                        Log.i("Entered2", "Finish Bluetooth");
                        finish();
                    }
                });
                builder.create().show();
            }
            else if (RET_BULETOOTH_IS_START == result){
                openDiscovery();
            }
        }
    }

    private class connSocketTask extends AsyncTask<String, String, Integer>{
        /**Process waits prompt box*/
        private ProgressDialog mpd = null;
        /**Constants: connection fails*/
        private static final int CONN_FAIL = 0x01;
        /**Constant: the connection is established*/
        private static final int CONN_SUCCESS = 0x02;
        /**
         *Thread start initialization
         */
        @Override
        public void onPreExecute(){
            this.mpd = new ProgressDialog(MainActivity.this);
            this.mpd.setMessage(getString(R.string.actMain_msg_device_connecting));
            this.mpd.setCancelable(false);
            this.mpd.setCanceledOnTouchOutside(false);
            this.mpd.show();
        }
        @Override
        protected Integer doInBackground(String... arg0){
            if (mGP.createConn(arg0[0])){
                return CONN_SUCCESS;
            }
            else{
                return CONN_FAIL;
            }
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result){
            this.mpd.dismiss();
            if (CONN_SUCCESS == result){
                Toast.makeText(MainActivity.this,getString(R.string.actMain_msg_device_connect_succes),Toast.LENGTH_SHORT).show();
                showBaudRateSelection();
            }else{
                Toast.makeText(MainActivity.this, "Device Conncetion Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showBaudRateSelection() { //TODO
        dlgRadioBtn = new Dialog(context);
        dlgRadioBtn.setCancelable(false);
        dlgRadioBtn.setTitle("Leopard Demo Application");
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
    private void openDiscovery(){
        Intent intent = new Intent(this, Act_BTDiscovery.class);
        this.startActivityForResult(intent, REQUEST_DISCOVERY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //mainlay.setVisibility(View.VISIBLE);
        if (requestCode == REQUEST_DISCOVERY){
            if (Activity.RESULT_OK == resultCode){
                //this.mllDeviceCtrl.setVisibility(View.VISIBLE);
                this.mhtDeviceInfo.put("NAME", data.getStringExtra("NAME"));
                this.mhtDeviceInfo.put("MAC", data.getStringExtra("MAC"));
                this.mhtDeviceInfo.put("COD", data.getStringExtra("COD"));
                this.mhtDeviceInfo.put("RSSI", data.getStringExtra("RSSI"));
                this.mhtDeviceInfo.put("DEVICE_TYPE", data.getStringExtra("DEVICE_TYPE"));
                this.mhtDeviceInfo.put("BOND", data.getStringExtra("BOND"));
                this.showDeviceInfo();

            }else if (Activity.RESULT_CANCELED == resultCode){
                this.finish();
            }
        }
        else if (requestCode==3) {
            finish();
        }
    }
    @SuppressLint("StringFormatMatches")
    private void showDeviceInfo(){
        this.mtvDeviceInfo.setText(
                String.format(getString(R.string.actMain_device_info),
                        this.mhtDeviceInfo.get("NAME"),
                        this.mhtDeviceInfo.get("MAC"),
                        this.mhtDeviceInfo.get("COD"),
                        this.mhtDeviceInfo.get("RSSI"),
                        this.mhtDeviceInfo.get("DEVICE_TYPE"),
                        this.mhtDeviceInfo.get("BOND"))
        );
    }

    // increases the device baud rate from 9600bps to 115200bps
    public class BaudRateTask extends AsyncTask<Integer, Integer, Integer> {
        private static final int CONN_FAIL = 0x01;
        /**Constant: the connection is established*/
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
                        BluetoothComm.mosOut,BluetoothComm.misIn);
                iRetVal = bdchange.iSwitchPeripheral1152();
                Log.e(TAG, "iRetval....."+iRetVal);
                Log.e(TAG, "bdchange is instantiated");
                if(iRetVal==BaudChange.BC_SUCCESS){
                    Log.e(TAG, "BaudChange.BC_SUCCESS");
                    //SystemClock.sleep(2000);
                    Log.e(TAG, "1");
                    //BluetoothComm.mosOut=null;
                    //BluetoothComm.misIn=null;
                    mGP.mosOut=null;
                    mGP.misIn=null;
                    //btcomm.closeConn();
                    mGP.mBTcomm .closeConn();
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
                    Log.e(TAG, "+++++++++++bConnected......"+b);
                    //boolean b = mGP.createConn();
                    Log.e(TAG, "4");
                    if(b==true)
                        mGP.mBTcomm.isConnect();
                    Log.e(TAG, "5");
                    SystemClock.sleep(3000);
                    bdchange.iSwitchBT1152(BluetoothComm.mosOut,BluetoothComm.misIn);
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
            if (CONN_SUCCESS == result){
                hander.obtainMessage(MESSAGE_BOX,"Baud Change Successful").sendToTarget();
                /*Intent all_intent = new Intent(getApplicationContext(),Act_SelectPeripherals.class);
                all_intent.putExtra("connected", false);
                startActivityForResult(all_intent, 3);*/
            } else if (CONN_NO_DEVICE==result){
                Log.e(TAG,"Bletooth No device is set");
                hander.obtainMessage(MESSAGE_BOX,"Please connect to Bluetooth and chagne baudrate").sendToTarget();
            } else {
                hander.obtainMessage(MESSAGE_BOX,"Baud Change FAIL").sendToTarget();
            }
        }
    }

    /* Handler to display UI response messages   */
    @SuppressLint("HandlerLeak")
    Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_BOX:
                    String str = (String) msg.obj;
                    showdialog(str);

            }
        };
    };


    public void showdialog(String str) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Leopard Demo Application");
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
}
