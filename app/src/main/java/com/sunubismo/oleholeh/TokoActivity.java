package com.sunubismo.oleholeh;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.sunubismo.oleholeh.model.rating.RatingResponse;
import com.sunubismo.oleholeh.model.toko.Datum;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoActivity extends BaseActivity implements View.OnClickListener{

    public static final String KEY_ID_TOKO = "id_toko";
    public static final String KEY_NAME = "nama";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LON = "longitude";

    final RestAPI service = RetrofitService.createRetrofitClient();

    RecyclerView list;
    ImageView image;
    TextView tvNama, tvKontak, tvAlamat, tvDesc;
    Button btRating;
    ImageButton btLokasi, btPanggil, btPesan, btEmail;
    RatingBar ratingBar;
    RatingAdapter adapter;

    Datum toko;

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko);

        toko = (Datum) getIntent().getSerializableExtra("Toko");

        init();
        showToko();
        loadRating();

        sessionManager = new SessionManager(getApplicationContext());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                if (toko.getDistance() > 0.1) {
                    Toast.makeText(TokoActivity.this, "Anda harus berjarak minimal 100 meter dari toko untuk memberi rating", Toast.LENGTH_SHORT).show();
                } else {
                    showCommentDialog((int) rating);
                }
            }
        });
    }

    private void showToko() {
        Picasso.with(getApplicationContext()).load(toko.getGambar()).into(image);
        tvNama.setText(toko.getNama());
        tvKontak.setText("Telepon: "+toko.getTelepon()+"");
        tvAlamat.setText("Alamat: "+toko.getAlamat()+"");
        tvDesc.setText(toko.getDeskripsi());
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
                iMaps.putExtra(KEY_LON, toko.getLng());
                startActivity(iMaps);
                break;
            case R.id.bt_panggil:
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkCallPermission();
                } else {
                    Intent intCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+toko.getTelepon()));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intCall);
                }
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

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 88;
    public void checkCallPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            Intent intCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+toko.getTelepon()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intCall);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent intCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+toko.getTelepon()));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intCall);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private void loadRating(){
        Call<RatingResponse> getRating = service.loadRating(toko.getId());
        getRating.enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(Call<RatingResponse> call, Response<RatingResponse> response) {
                adapter = new RatingAdapter(getApplicationContext(), response.body().getData(), new RatingAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(com.sunubismo.oleholeh.model.rating.Datum Item) {
                        Toast.makeText(TokoActivity.this, String.valueOf(Item.getNilai()), Toast.LENGTH_SHORT).show();
                    }
                });
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<RatingResponse> call, Throwable t) {
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
        String token = user.get(SessionManager.KEY_TOKEN);
        String iduser = user.get(SessionManager.KEY_ID);
        Call<DataResponse> postRating = service.postRating(token, iduser, toko.getId(), String.valueOf(rating), komentar);
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
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(TokoActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_comment, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(TokoActivity.this);
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
        btLokasi = (ImageButton) findViewById(R.id.bt_lokasi);
        btLokasi.setOnClickListener(this);
        btPanggil = (ImageButton) findViewById(R.id.bt_panggil);
        btPanggil.setOnClickListener(this);
        btPesan = (ImageButton) findViewById(R.id.bt_pesan);
        btPesan.setOnClickListener(this);
        btEmail = (ImageButton) findViewById(R.id.bt_email);
        btEmail.setOnClickListener(this);
        btRating = (Button) findViewById(R.id.bt_all_rating);
        btRating.setOnClickListener(this);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }
}
