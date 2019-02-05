package com.sunubismo.oleholeh;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sunubismo.oleholeh.adapter.TokoAdapter;
import com.sunubismo.oleholeh.api.RestAPI;
import com.sunubismo.oleholeh.api.RetrofitService;
import com.sunubismo.oleholeh.helper.SessionManager;
import com.sunubismo.oleholeh.model.DataResponse;
import com.sunubismo.oleholeh.model.Toko;
import com.sunubismo.oleholeh.model.toko.Datum;
import com.sunubismo.oleholeh.model.toko.TokoResponse;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ID_TOKO = "id_toko";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LON = "longitude";

    RecyclerView list;
    SessionManager sessionManager;
    Location mLastLocation;
    Toolbar toolbar;
    SearchView searchView;
    String keyword = "", sorting = "nama";

    private FusedLocationProviderClient mFusedLocationClient;

    final RestAPI service = RetrofitService.createRetrofitClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastLocation = location;
                            }
                        }
                    });
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (SearchView) findViewById(R.id.search_view);
        list = (RecyclerView) findViewById(R.id.rv_toko);
        list.hasFixedSize();
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        list.setLayoutManager(llm);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String id = user.get(SessionManager.KEY_ID);
        String nama = user.get(SessionManager.KEY_NAME);
        String email = user.get(SessionManager.KEY_EMAIL);
        String image = user.get(SessionManager.KEY_IMAGE);

        getListToko();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                getListToko();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getListToko() {
        service.loadToko(keyword, sorting)
        .enqueue(new Callback<TokoResponse>() {
            @Override
            public void onResponse(Call<TokoResponse> call, Response<TokoResponse> response) {
                final List<Datum> toko = response.body().getData();

                for (int i = 0; i < toko.size(); i++) {
                    LatLng start;
                    if (mLastLocation != null) {
                        start = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    } else {
                        start = new LatLng(-7.792981, 110.408345);
                    }
                    LatLng end = new LatLng(toko.get(i).getLat(), toko.get(i).getLng());
                    toko.get(i).setDistance(getDistance(start, end));
                }

                if (sorting.equalsIgnoreCase("nama")) {
                    Collections.sort(toko, new Comparator<Datum>(){
                        public int compare(Datum obj1, Datum obj2) {
                            // ## Ascending order
                            return Double.valueOf(obj1.getDistance()).compareTo(obj2.getDistance()); // To compare integer values
                        }
                    });
                }

                TokoAdapter adapter = new TokoAdapter(getApplicationContext(), toko, new TokoAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Datum Item) {
                        Intent i = new Intent(MainActivity.this, TokoActivity.class);
                        i.putExtra("Toko", Item);
                        startActivity(i);
                    }

                    @Override
                    public void onLocation(Datum Item) {
                        Intent iMaps = new Intent(MainActivity.this, MapsActivity.class);
                        iMaps.putExtra(KEY_LAT, Item.getLat());
                        iMaps.putExtra(KEY_LON, Item.getLng());
                        startActivity(iMaps);
                    }
                });
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TokoResponse> call, Throwable t) {
            }
        });
    }

    public double getDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
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
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            mLastLocation = location;
                                            Toast.makeText(MainActivity.this, "Get Location Success", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.name:
                sorting = "nama";
                getListToko();
                return true;
            case R.id.rating:
                sorting = "rating";
                getListToko();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
