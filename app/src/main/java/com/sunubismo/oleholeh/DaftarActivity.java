package com.sunubismo.oleholeh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sunubismo.oleholeh.api.RestAPI;
import com.sunubismo.oleholeh.api.RetrofitService;
import com.sunubismo.oleholeh.model.DataResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    EditText etNama, etEmail, etPassword, etConfirm;
    Button btDaftar, btBackLogin, btGambar;
    TextView tvGambar;
    ProgressBar pbDaftar;

    RequestBody image, nama, email, password;

    public static final int PICK_IMAGE = 100;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        etNama = (EditText) findViewById(R.id.et_daftar_nama);
        etEmail = (EditText) findViewById(R.id.et_daftar_email);
        etPassword = (EditText) findViewById(R.id.et_daftar_password);
        etConfirm = (EditText) findViewById(R.id.et_daftar_confirm);
        tvGambar = (TextView) findViewById(R.id.tv_gambar);
        pbDaftar = (ProgressBar) findViewById(R.id.pb_daftar);

        btDaftar = (Button) findViewById(R.id.bt_daftar);
        btBackLogin = (Button) findViewById(R.id.bt_back_login);
        btGambar = (Button) findViewById(R.id.bt_daftar_gambar);

        btGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });

        btDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = RequestBody.create(MediaType.parse("text/plain"), etNama.getText().toString());
                email = RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString());
                password = RequestBody.create(MediaType.parse("text/plain"), etPassword.getText().toString());
                showWait();
                SignUp();
            }
        });

        btBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String img = getStringImage(bitmap);
            image = RequestBody.create(MediaType.parse("text/plain"), img);
            tvGambar.setText(selectedImage.toString());

        }
    }

    public void SignUp(){
        final RestAPI service = RetrofitService.createRetrofitClient();
        Call<DataResponse> req = service.signup(image, nama, email, password);
        req.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                String status = response.body().getStatus();
                if (status.equalsIgnoreCase("sukses")){
                    Toast.makeText(DaftarActivity.this, "Daftar Sukses", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else if (status.equalsIgnoreCase("gagal")){
                    Toast.makeText(DaftarActivity.this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DaftarActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
                removeWait();
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DaftarActivity.this, "Terjadi Kesalahan "+t.toString(), Toast.LENGTH_SHORT).show();
                removeWait();
            }
        });
    }

    public String getStringImage(Bitmap bmp){
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void showWait(){
        pbDaftar.setVisibility(View.VISIBLE);
        etNama.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
        etConfirm.setEnabled(false);
        btGambar.setEnabled(false);
        btDaftar.setEnabled(false);
        btBackLogin.setEnabled(false);
    }

    public void removeWait(){
        pbDaftar.setVisibility(View.INVISIBLE);
        etNama.setEnabled(true);
        etEmail.setEnabled(true);
        etPassword.setEnabled(true);
        etConfirm.setEnabled(true);
        btGambar.setEnabled(true);
        btDaftar.setEnabled(true);
        btBackLogin.setEnabled(true);
    }
}
