package com.sunubismo.oleholeh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunubismo.oleholeh.R;
import com.sunubismo.oleholeh.model.Toko;
import com.sunubismo.oleholeh.model.toko.Datum;

import java.math.BigDecimal;
import java.util.List;

public class TokoAdapter extends RecyclerView.Adapter<TokoAdapter.ViewHolder>{

    private final OnItemClickListener listener;
    private List<Datum> toko;
    private Context context;

    public TokoAdapter(Context context, List<Datum> toko, OnItemClickListener listener) {
        this.context = context;
        this.toko = toko;
        this.listener = listener;
    }

    @Override
    public TokoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_toko, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TokoAdapter.ViewHolder viewHolder, int i) {
        viewHolder.click(toko.get(i),listener);
        viewHolder.locationClick(toko.get(i),listener);
        Picasso.with(viewHolder.img.getContext()).load(toko.get(i).getGambar()).into(viewHolder.img);
//        viewHolder.id.setText(toko.get(i).getId());
        viewHolder.nama.setText(toko.get(i).getNama()+" ("+String.valueOf(toko.get(i).getDistance()).substring(0,4)+" km)");
        viewHolder.rb.setEnabled(false);
        viewHolder.rb.setMax(5);
        viewHolder.rb.setStepSize(0.01f);
        viewHolder.rb.setRating(toko.get(i).getNRating());
        viewHolder.rb.invalidate();
        viewHolder.rating.setText(String.valueOf(round(toko.get(i).getNRating(), 1)) + " dari 5 bintang (" + String.valueOf(toko.get(i).getmTRating()) + ")" );
        viewHolder.alamat.setText("Alamat: "+toko.get(i).getAlamat());
        viewHolder.hari.setText("Buka Hari "+toko.get(i).getHariBuka());
        viewHolder.jam.setText("Jam "+toko.get(i).getJamKerja());
        viewHolder.telepon.setText("Telepon: "+toko.get(i).getTelepon());
    }

    public static float round(float number, int decimalPlace) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    public int getItemCount() {
        return toko.size();
    }

    public interface OnItemClickListener {
        void onClick(Datum Item);
        void onLocation(Datum Item);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView id, nama, rating, alamat, hari, jam, telepon;
        private ImageView img;
        private RatingBar rb;
        private Button btToko, btLokasi;
        ViewHolder(View view) {
            super(view);

            img = (ImageView)view.findViewById(R.id.img_toko);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            id = (TextView)view.findViewById(R.id.tv_toko_id);
            rb = (RatingBar)view.findViewById(R.id.rb_rating);
            rating = (TextView)view.findViewById(R.id.tv_toko_rating);
            nama = (TextView)view.findViewById(R.id.tv_toko_nama);
            alamat = (TextView)view.findViewById(R.id.tv_toko_alamat);
            hari = (TextView)view.findViewById(R.id.tv_toko_hari);
            jam = (TextView)view.findViewById(R.id.tv_toko_jam);
            telepon = (TextView)view.findViewById(R.id.tv_toko_telepon);
            btToko = (Button)view.findViewById(R.id.bt_toko);
            btLokasi = (Button)view.findViewById(R.id.bt_lokasi);
        }
        void click(final Datum toko, final OnItemClickListener listener) {
            btToko.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(toko);
                }
            });
        }
        void locationClick(final Datum toko, final OnItemClickListener listener) {
            btLokasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onLocation(toko);
                }
            });
        }
    }
}
