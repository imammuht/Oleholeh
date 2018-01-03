package com.sunubismo.oleholeh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunubismo.oleholeh.helper.SessionManager;

import java.util.HashMap;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ImageView img = (ImageView) findViewById(R.id.user_image);
        TextView tvName = (TextView) findViewById(R.id.user_name);
        TextView tvEmail = (TextView) findViewById(R.id.user_email);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        Picasso.with(getApplicationContext()).load(user.get(SessionManager.KEY_IMAGE)).into(img);
        tvName.setText("Nama: "+user.get(SessionManager.KEY_NAME)+"");
        tvEmail.setText("Email: "+user.get(SessionManager.KEY_EMAIL)+"");
    }
}
