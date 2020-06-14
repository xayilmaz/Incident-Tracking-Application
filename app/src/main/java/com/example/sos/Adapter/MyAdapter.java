package com.example.sos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sos.Detail;
import com.example.sos.ListElement;
import com.example.sos.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ListElement> list;

    public MyAdapter(Context mContext, List<ListElement> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOlayBilgi;
        public TextView txtOlayTuru;
        public TextView txtUser;
        public TextView txtLokasyon;
        public TextView txtTarih;
        public ImageView image;
        public ConstraintLayout layout;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOlayTuru = (TextView) itemView.findViewById(R.id.txtOlayAdi);
            txtOlayBilgi = (TextView) itemView.findViewById(R.id.textinci);
            txtUser = (TextView) itemView.findViewById(R.id.textusername);
            txtLokasyon = (TextView) itemView.findViewById(R.id.textLoca);
            txtTarih = (TextView) itemView.findViewById(R.id.textDate);
            image = (ImageView) itemView.findViewById(R.id.image);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1: {
                View listItem = layoutInflater.inflate(R.layout.incident_list, parent, false);
                RecyclerView.ViewHolder viewHolder = new CarViewHolder(listItem);
                return viewHolder;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ListElement ll = list.get(position);

        switch (holder.getItemViewType()) {
            case 1:
                final CarViewHolder olay = (CarViewHolder) holder;

                olay.txtOlayTuru.setText("Incident: " + ll.getOlayturu());
                olay.txtOlayBilgi.setText(ll.getOlaybilgi());
                olay.txtUser.setText("Added By : " + ll.getUsername());
                olay.txtLokasyon.setText("Location : " + ll.getKonum());
                olay.txtTarih.setText(ll.getTarih());

                Picasso.get().load(ll.getImage())
                        .into(olay.image);


                olay.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "You clicked the item " + position, Toast.LENGTH_SHORT).show();
                        Intent newIntent = new Intent(mContext, Detail.class);

                        newIntent.putExtra("olayturu", ll.getOlayturu());
                        newIntent.putExtra("olaybilgi", ll.getOlaybilgi());
                        newIntent.putExtra("username", ll.getUsername());
                        newIntent.putExtra("lokasyon", ll.getKonum());
                        newIntent.putExtra("tarih", ll.getTarih());
                        newIntent.putExtra("image", ll.getImage());

                        mContext.startActivity(newIntent);
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        final ListElement ll = list.get(position);
        return ll.getElement_type();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}