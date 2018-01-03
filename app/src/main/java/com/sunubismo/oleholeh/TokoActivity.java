package com.sunubismo.oleholeh;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.sunubismo.oleholeh.adapter.RatingAdapter;
import com.sunubismo.oleholeh.api.RestAPI;
import com.sunubismo.oleholeh.api.RetrofitService;
import com.sunubismo.oleholeh.helper.SessionManager;
import com.sunubismo.oleholeh.model.DataResponse;
import com.sunubismo.oleholeh.model.Rating;
import com.sunubismo.oleholeh.model.Toko;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String KEY_ID_TOKO = "id_toko";
    public static final String KEY_NAME = "nama";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LON = "longitude";

    final RestAPI service = RetrofitService.createRetrofitClient();

    RecyclerView list;
    ImageView image;
    TextView tvNama, tvKontak, tvAlamat, tvDesc;
    Button btLokasi, btPanggil, btPesan, btEmail, btRating;
    RatingBar ratingBar;
    RatingAdapter adapter;

    Toko toko;
    String idtoko;

    SessionManager sessionManager;

    final Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko);

        Intent intent = getIntent();
        idtoko = intent.getStringExtra(KEY_ID_TOKO);

        init();
        loadProfilToko();
        loadRating();

        sessionManager = new SessionManager(getApplicationContext());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                showCommentDialog((int) rating);
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.bt_lokasi:
//                String strUri = "http://maps.google.com/maps?q=loc:" + toko.getLat() + "," + toko.getLon() + " (" + toko.getNama() + ")";
//                Intent iMaps = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
//                iMaps.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                Intent iMaps = new Intent(TokoActivity.this, MapsActivity.class);
                iMaps.putExtra(KEY_LAT, toko.getLat());
                iMaps.putExtra(KEY_LON, toko.getLon());
                startActivity(iMaps);
                break;
            case R.id.bt_panggil:
                Intent intCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+toko.getTelepon()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intCall);
                break;
            case R.id.bt_pesan:
                Intent intSms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+toko.getTelepon()));
                intSms.putExtra("sms_body", "via Oleh-oleh App");
                startActivity(intSms);
                break;
            case R.id.bt_email:
                Intent intMail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+toko.getEmail()));
                startActivity(intMail);
                break;
            case R.id.bt_all_rating:
                Intent intRating = new Intent(TokoActivity.this, RatingActivity.class);
                intRating.putExtra(KEY_ID_TOKO, String.valueOf(toko.getId()));
                startActivity(intRating);
                break;
            default:
                //do nothing
                break;
        }
    }

    public void loadProfilToko(){
        Call<Toko> getProfil = service.loadProfileToko(idtoko);
        getProfil.enqueue(new Callback<Toko>() {
            @Override
            public void onResponse(Call<Toko> call, Response<Toko> response) {
                toko = response.body();
                Picasso.with(getApplicationContext()).load(toko.getImage()).into(image);
                tvNama.setText(toko.getNama());
                tvKontak.setText("Telepon: "+toko.getTelepon()+"");
                tvAlamat.setText("Alamat: "+toko.getAlamat()+"");
                tvDesc.setText(toko.getDeskripsi());
            }

            @Override
            public void onFailure(Call<Toko> call, Throwable t) {

            }
        });
    }

    private void loadRating(){
        Call<DataResponse> getRating = service.loadRating(idtoko);
        getRating.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                adapter = new RatingAdapter(getApplicationContext(), response.body().getRating(), new RatingAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Rating Item) {
                        Toast.makeText(TokoActivity.this, String.valueOf(Item.getNilai()), Toast.LENGTH_SHORT).show();
                    }
                });
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Snackbar.make(ratingBar, "Gagal Memuat Komentar", Snackbar.LENGTH_LONG)
                        .setAction("Muat Ulang", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadRating();
                            }
                        })
                        .show();
            }
        });
    }

    private void postRating(int rating, String komentar){
        HashMap<String, String> user = sessionManager.getUserDetails();
        String iduser = user.get(SessionManager.KEY_ID);
        RequestBody id_user = RequestBody.create(MediaType.parse("text/plain"), iduser);
        RequestBody id_toko = RequestBody.create(MediaType.parse("text/plain"), idtoko);
        RequestBody nilai = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(rating));
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), komentar);
        Call<DataResponse> postRating = service.postRating(id_user, id_toko, nilai, comment);
        postRating.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                Toast.makeText(TokoActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                loadRating();
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }

    public void showCommentDialog(final int rating){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_comment, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Toast.makeText(TokoActivity.this, String.valueOf(rating) + " Bintang. " + userInputDialogEditText.getText(), Toast.LENGTH_SHORT).show();
                        postRating(rating, userInputDialogEditText.getText().toString());
                    }
                })

                .setNegativeButton("Batalkan",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public void init(){
        list = (RecyclerView) findViewById(R.id.rvRatingList);
        list.hasFixedSize();
        LinearLayoutManager llm = new LinearLayoutManager(TokoActivity.this);
        list.setLayoutManager(llm);
        list.setNestedScrollingEnabled(false);

        image = (ImageView) findViewById(R.id.img_detail);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tvNama = (TextView) findViewById(R.id.tv_nama);
        tvKontak = (TextView) findViewById(R.id.tv_kontak);
        tvAlamat = (TextView) findViewById(R.id.tv_alamat);
        tvDesc = (TextView) findViewById(R.id.tv_deskripsi);
        btLokasi = (Button) findViewById(R.id.bt_lokasi);
        btLokasi.setOnClickListener(this);
        btPanggil = (Button) findViewById(R.id.bt_panggil);
        btPanggil.setOnClickListener(this);
        btPesan = (Button) findViewById(R.id.bt_pesan);
        btPesan.setOnClickListener(this);
        btEmail = (Button) findViewById(R.id.bt_email);
        btEmail.setOnClickListener(this);
        btRating = (Button) findViewById(R.id.bt_all_rating);
        btRating.setOnClickListener(this);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }
}
