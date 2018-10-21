package com.sunubismo.oleholeh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sunubismo.oleholeh.helper.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sessionManager = new SessionManager(getApplicationContext());
                Intent intent;
                if(sessionManager.isLoggedIn()){
                    intent = new Intent(SplashScreenActivity.this, MenuActivity.class);
                }else {
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
