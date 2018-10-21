package com.sunubismo.oleholeh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sunubismo.oleholeh.api.RestAPI;
import com.sunubismo.oleholeh.api.RetrofitService;
import com.sunubismo.oleholeh.helper.SessionManager;
import com.sunubismo.oleholeh.model.user.Datum;
import com.sunubismo.oleholeh.model.user.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btLogin, btToDaftar;

    String email, password;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.et_login_email);
        etPassword = (EditText) findViewById(R.id.et_login_password);
        btLogin = (Button) findViewById(R.id.bt_login);
        btToDaftar = (Button) findViewById(R.id.bt_to_daftar);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        btToDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private void attemptLogin(){
        boolean cancel = false;
        View focusView = null;
        if (!isEmailValid(etEmail.getText().toString())) {
            etEmail.setError("Email tidak valid");
            focusView = etEmail;
            cancel = true;
        }
        if (cancel) {
            //jika email tidak valid
            focusView.requestFocus();
        } else {
            //jika email valid, lakukan login
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            Login();
        }
    }

    private void Login(){
        final RestAPI service = RetrofitService.createRetrofitClient();
        Call<LoginResponse> req = service.login(email, password);
        req.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse.getSuccess()){
                    Datum user = loginResponse.getData().get(0);
                    sessionManager = new SessionManager(getApplicationContext());
                    sessionManager.createLoginSession(user.getToken(), user.getId(), user.getNama(), user.getEmail(), user.getGambar());
                    Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(i);
                } else if (!loginResponse.getSuccess()){
                    Toast.makeText(LoginActivity.this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
