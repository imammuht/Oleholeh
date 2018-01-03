package com.sunubismo.oleholeh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sunubismo.oleholeh.adapter.RatingAdapter;
import com.sunubismo.oleholeh.api.RestAPI;
import com.sunubismo.oleholeh.api.RetrofitService;
import com.sunubismo.oleholeh.model.DataResponse;
import com.sunubismo.oleholeh.model.Rating;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {

    public static final String KEY_ID_TOKO = "id_toko";

    RecyclerView list;
    String idtoko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Intent intent = getIntent();
        idtoko = intent.getStringExtra(KEY_ID_TOKO);

        init();
        loadRating();
    }

    private void loadRating(){
        final RestAPI service = RetrofitService.createRetrofitClient();
        Call<DataResponse> getRating = service.loadAllRating(idtoko);
        getRating.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                RatingAdapter adapter = new RatingAdapter(getApplicationContext(), response.body().getRating(), new RatingAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Rating Item) {
                        Toast.makeText(RatingActivity.this, String.valueOf(Item.getNilai()), Toast.LENGTH_SHORT).show();
                    }
                });
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
            }
        });
    }

    private void init(){
        list = (RecyclerView) findViewById(R.id.rvAllRating);
        list.hasFixedSize();
        LinearLayoutManager llm = new LinearLayoutManager(RatingActivity.this);
        list.setLayoutManager(llm);
    }

}
