package mobilecomputing.com.audioplayground;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent transition = new Intent(LauncherActivity.this, MainActivity.class);
                LauncherActivity.this.startActivity(transition);
                LauncherActivity.this.finish();
            }
        }, 5000);
    }


}
