package com.example.sos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sos.Detail;
import com.example.sos.ListElement;
import com.example.sos.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapterProfil extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ListElement> list;

    public MyAdapterProfil(Context mContext, List<ListElement> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public Switch switch3;
        public ConstraintLayout layout;
        public TextView txtOlayAdi, textinci, textLoca, textDate;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOlayAdi = (TextView) itemView.findViewById(R.id.txtOlayAdi);
            textinci = (TextView) itemView.findViewById(R.id.textinci);
            textLoca = (TextView) itemView.findViewById(R.id.textLoca);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            switch3 = (Switch) itemView.findViewById(R.id.switch3);
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
                View listItem = layoutInflater.inflate(R.layout.incident_list_profil, parent, false);
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

                olay.txtOlayAdi.setText("Incident: " + ll.getOlayturu());
                olay.textinci.setText(ll.getOlaybilgi());
                olay.textLoca.setText("Location : " + ll.getKonum());
                olay.textDate.setText(ll.getTarih());

                if (ll.getDurum().equals("0")) {
                    olay.switch3.setChecked(false);
                    olay.layout.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                }

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
                olay.switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            olay.layout.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                            Giris("1", ll.getOlaybilgi());
                        } else {
                            olay.layout.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                            Giris("0", ll.getOlaybilgi());
                        }

                    }

                });

        }
    }

    public void Giris(final String durum, final String olay) {
        String url = "durum_degis.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    int durum = jsonResponse.getInt("succes");

                    if (durum == 1) {
                        //Toast.makeText(mContext,"Başarılı",Toast.LENGTH_LONG).show();
                        Log.e("Cevap5", "Başarılı");
                    } else {
                        //Toast.makeText(mContext,"Başarısız",Toast.LENGTH_LONG).show();
                        Log.e("Cevap5", "Başarısız");
                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("durum", durum);
                params.put("olaybilgi", olay);


                return params;
            }
        };

        Volley.newRequestQueue(mContext).add(istek);
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