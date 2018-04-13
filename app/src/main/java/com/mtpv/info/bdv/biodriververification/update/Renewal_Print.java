package com.mtpv.info.bdv.biodriververification.update;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.info.bdv.biodriververification.MenuBorad;
import com.mtpv.info.bdv.biodriververification.Printpreview;
import com.mtpv.info.bdv.biodriververification.R;
import com.mtpv.info.bdv.biodriververification.ServiceHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Renewal_Print extends AppCompatActivity {

    WebView print;

    Button back_btn ;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renewal__print);

        // print=(WebView)findViewById(R.id.print);
        back_btn=(Button)findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(),Update_FPS.class);
                startActivity(back);
                //ed_appID , ed_aadharnum

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


        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        //File folder = new File(extStorageDirectory, "Driver"+File.separator+date);


        String pdfFile=extStorageDirectory+ File.separator+"Driver"+File.separator+date+File.separator+ ServiceHelper.renewal_data_master[2]+".pdf";
        File file=new File(pdfFile);

        //  Log.i("PDF_Path-->",""+path);

        try {

            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(path, "application/pdf");
                objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(objIntent);
            } else {
                Toast.makeText(Renewal_Print.this, "File NotFound",Toast.LENGTH_SHORT).show();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Renewal_Print.this,"No Viewer Application Found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


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
