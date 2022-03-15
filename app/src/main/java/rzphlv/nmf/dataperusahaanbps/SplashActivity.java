package rzphlv.nmf.dataperusahaanbps;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate ( Bundle savedInstanceState ){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,HomeAdmin.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
    @Override
    public void onBackPressed() {
    }
}
