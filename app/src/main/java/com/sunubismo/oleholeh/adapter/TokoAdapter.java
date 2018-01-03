package com.sunubismo.oleholeh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunubismo.oleholeh.R;
import com.sunubismo.oleholeh.model.Toko;

import java.util.List;

public class TokoAdapter extends RecyclerView.Adapter<TokoAdapter.ViewHolder>{

    private final OnItemClickListener listener;
    private List<Toko> toko;
    private Context context;

    public TokoAdapter(Context context, List<Toko> toko, OnItemClickListener listener) {
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
        Picasso.with(viewHolder.img.getContext()).load(toko.get(i).getImage()).into(viewHolder.img);
        viewHolder.id.setText(toko.get(i).getId());
        viewHolder.nama.setText(toko.get(i).getNama());
        viewHolder.rating.setText("Rating: "+ String.valueOf(toko.get(i).getRating())+"");
        viewHolder.alamat.setText("Alamat: "+toko.get(i).getAlamat()+"");
        viewHolder.hari.setText("Buka Hari "+toko.get(i).getHaribuka()+"");
        viewHolder.jam.setText("Jam "+toko.get(i).getJambuka()+"");
        viewHolder.telepon.setText("Telepon: "+toko.get(i).getTelepon()+"");
    }

    @Override
    public int getItemCount() {
        return toko.size();
    }

    public interface OnItemClickListener {
        void onClick(Toko Item);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView id, nama, rating, alamat, hari, jam, telepon;
        private ImageView img;
        ViewHolder(View view) {
            super(view);

            img = (ImageView)view.findViewById(R.id.img_toko);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            id = (TextView)view.findViewById(R.id.tv_toko_id);
            rating = (TextView)view.findViewById(R.id.tv_toko_rating);
            nama = (TextView)view.findViewById(R.id.tv_toko_nama);
            alamat = (TextView)view.findViewById(R.id.tv_toko_alamat);
            hari = (TextView)view.findViewById(R.id.tv_toko_hari);
            jam = (TextView)view.findViewById(R.id.tv_toko_jam);
            telepon = (TextView)view.findViewById(R.id.tv_toko_telepon);
        }
        void click(final Toko toko, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(toko);
                }
            });
        }
    }
}
