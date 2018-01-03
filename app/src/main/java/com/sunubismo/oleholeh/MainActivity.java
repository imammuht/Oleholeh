package com.sunubismo.oleholeh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sunubismo.oleholeh.adapter.TokoAdapter;
import com.sunubismo.oleholeh.api.RestAPI;
import com.sunubismo.oleholeh.api.RetrofitService;
import com.sunubismo.oleholeh.helper.SessionManager;
import com.sunubismo.oleholeh.model.DataResponse;
import com.sunubismo.oleholeh.model.Toko;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ID_TOKO = "id_toko";

    RecyclerView list;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Toast.makeText(this, nama, Toast.LENGTH_SHORT).show();

        final RestAPI service = RetrofitService.createRetrofitClient();
        Call<DataResponse> getData = service.loadToko();
        getData.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                final List<Toko> toko = response.body().getToko();
                TokoAdapter adapter = new TokoAdapter(getApplicationContext(), toko, new TokoAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Toko Item) {
                        Intent i = new Intent(MainActivity.this, TokoActivity.class);
                        i.putExtra(KEY_ID_TOKO, Item.getId());
                        startActivity(i);
                    }
                });
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent iProfile = new Intent(MainActivity.this, UserActivity.class);
                startActivity(iProfile);
                return true;
            case R.id.logout:
                sessionManager.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
