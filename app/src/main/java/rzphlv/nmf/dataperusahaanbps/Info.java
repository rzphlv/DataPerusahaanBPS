package rzphlv.nmf.dataperusahaanbps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate ( Bundle savedInstanceState ){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_info);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
