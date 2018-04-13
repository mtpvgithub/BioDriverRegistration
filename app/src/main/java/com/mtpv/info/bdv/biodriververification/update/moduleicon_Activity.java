package com.mtpv.info.bdv.biodriververification.update;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.info.bdv.biodriververification.LoginActivity;
import com.mtpv.info.bdv.biodriververification.MenuBorad;
import com.mtpv.info.bdv.biodriververification.R;
import com.mtpv.info.bdv.biodriververification.Welcome;

import java.util.Timer;
import java.util.TimerTask;

public class moduleicon_Activity extends AppCompatActivity {

    Button    btn_issues ;
    ImageView slidingimage ,log_out ,btn_meun , btn_renwal;
    boolean doubleBackToExitPressedOnce = false;
    public int currentimageindex=0;

    private int[] IMAGE_IDS = {
            R.drawable.exit_logo, R.drawable.splash, R.drawable.splash_one};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_moduleicon_);

        Log.i("WelcometoMODULE","Entered");
        final Handler mHandler = new Handler();
        btn_meun = (ImageView) findViewById(R.id.btn_meun_xml);
        btn_renwal = (ImageView) findViewById(R.id.btn_renwal_xml);
      //  btn_issues = (Button) findViewById(R.id.btn_issues_xml);
        log_out= (ImageView)findViewById(R.id.logout_btn);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertLogout();
              /*  finish();
                android.os.Process.killProcess(android.os.Process.myPid());*/
            }
        });


        btn_meun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mode = new Intent(getApplicationContext(), MenuBorad.class);
                startActivity(mode);
            }
        });

        btn_renwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mode = new Intent(getApplicationContext(), Update_FPS.class);
                startActivity(mode);
            }
        });

      /*  btn_issues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mode = new Intent(getApplicationContext(), MenuBorad.class);
                startActivity(mode);
            }
        });*/


        // Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {

            AnimateandSlideShow();

        }
    };

    int delay = 1000; // delay for 1 sec.

    int period = 3000; // repeat every 4 sec.

    Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

        public void run() {

            mHandler.post(mUpdateResults);

        }

    }, delay, period);

}


    /**
     * Helper method to start the animation on the splash screen
     */
    private void AnimateandSlideShow() {


        slidingimage = (ImageView)findViewById(R.id.slider_img);
        slidingimage.setImageResource(IMAGE_IDS[currentimageindex%IMAGE_IDS.length]);

        currentimageindex++;

        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.fade_in);


        slidingimage.startAnimation(rotateimage);



    }

    public void AlertLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Logout?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(moduleicon_Activity.this, Welcome.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                }).create().show();
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
        //super.onBackPressed();

        showToast("Please Click on Logout Button");
    }
}
