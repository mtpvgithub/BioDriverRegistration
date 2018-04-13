package com.mtpv.info.bdv.biodriververification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class Zoom_Activity extends AppCompatActivity {

    ImageView web_display ;
    ZoomControls zoom;
    RelativeLayout imag ;
    public static String image ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zoom_);

        web_display =(ImageView)findViewById(R.id.web_display);

        imag=(RelativeLayout)findViewById(R.id.imag);
        zoom = (ZoomControls) findViewById(R.id.zoomControls1);
        //zoom.setVisibility(View.GONE);




        zoom.setOnZoomInClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                float x = imag.getScaleX();
                float y = imag.getScaleY();

                imag.setScaleX((float) (x+0.2));
                imag.setScaleY((float) (y+0.2));
            }
        });

        zoom.setOnZoomOutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                float x = web_display.getScaleX();
                float y = web_display.getScaleY();

                web_display.setScaleX((float) (x-0.2));
                web_display.setScaleY((float) (y-0.2));
            }
        });


        Log.i("otherDocZoom0-->",""+MenuBorad.respones_docver[19]);
        //******* start driving license image display *****
        if (null !=MenuBorad.respones_docver[19] && MenuBorad.respones_docver[19].toString().trim().equals("0")) {
            web_display.setImageResource(R.drawable.empty_profile_img);
            Log.i("otherDocZoom1-->",""+MenuBorad.respones_docver[19]);
        } else {
            try {
                byte[] decodestring = Base64.decode("" + MenuBorad.respones_docver[19].toString().trim(),
                        Base64.DEFAULT);
                Bitmap decocebyte = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
                web_display.setImageBitmap(decocebyte);

                Log.i("otherDocZoom2-->",""+MenuBorad.respones_docver[19]);

            } catch (Exception e) {
                e.printStackTrace();
                web_display.setImageDrawable(getResources().getDrawable(R.drawable.empty_profile_img));
            }
        }
        //******* End driving license image display *****

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
    
}
