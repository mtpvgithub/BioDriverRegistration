package com.mtpv.info.bdv.biodriververification;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mtpv.info.bdv.biodriververification.storage.Constants;
import com.mtpv.info.bdv.biodriververification.storage.Storage;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    LinearLayout splash_layout;
    //ScaleAnimation scale;
    Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Log.i(TAG, "--------Entered---------");

        storage = new Storage(SplashActivity.this);

        splash_layout = (LinearLayout)findViewById(R.id.splash_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (storage.getValue(Constants.BT_ADDRESS).length() > 0) {
                    Log.i(TAG, "BT_NAME--->" + storage.getValue(Constants.BT_NAME));
                    Log.i(TAG, "BT_ADDRESS--->" + storage.getValue(Constants.BT_ADDRESS));
                    Intent i = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 2000);

    }
}
