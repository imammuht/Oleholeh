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
import com.sunubismo.oleholeh.model.Rating;
import com.sunubismo.oleholeh.model.rating.Datum;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder>{

    private final OnItemClickListener listener;
    private List<Datum> rating;
    private Context context;


    public RatingAdapter(Context context, List<Datum> rating, OnItemClickListener listener) {
        this.context = context;
        this.rating = rating;
        this.listener = listener;
    }

    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rating, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RatingAdapter.ViewHolder viewHolder, int i) {
        viewHolder.click(rating.get(i),listener);
        Picasso.with(viewHolder.image.getContext()).load(rating.get(i).getGambar()).into(viewHolder.image);
        viewHolder.nama.setText(rating.get(i).getNama());
        viewHolder.nilai.setText(""+rating.get(i).getNilai()+" Bintang");
        viewHolder.komentar.setText(rating.get(i).getKomentar());
    }

    @Override
    public int getItemCount() {
        return rating.size();
    }

    public interface OnItemClickListener {
        void onClick(Datum Item);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nama, nilai, komentar;
        private ImageView image;
        ViewHolder(View view) {
            super(view);
            image = (ImageView)view.findViewById(R.id.img_toko);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            nama = (TextView)view.findViewById(R.id.tv_user_nama);
            nilai = (TextView)view.findViewById(R.id.tv_user_rating);
            komentar = (TextView)view.findViewById(R.id.tv_user_komentar);
        }
        void click(final Datum toko, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(toko);
                }
            });
        }
    }
}
