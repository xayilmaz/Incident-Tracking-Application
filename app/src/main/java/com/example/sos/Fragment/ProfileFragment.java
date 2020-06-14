package com.example.sos.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sos.ListElement;
import com.example.sos.MainActivity;
import com.example.sos.Adapter.MyAdapterProfil;
import com.example.sos.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView name, phone, mail;
    private ImageView imgProfil;
    private RecyclerView recyclerView;
    private MyAdapterProfil adapter;
    private ArrayList<ListElement> list;

    public ProfileFragment() {
    }

    SharedPreferences session;
    private String name1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        imgProfil = view.findViewById(R.id.profile_image);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        olaylar();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SESSION", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        ImageView imageViewout = (ImageView) view.findViewById(R.id.imageViewout);
        imageViewout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                getActivity().startActivity(intent);

                editor.clear();
                editor.commit();
            }
        });

        session = getActivity().getSharedPreferences("SESSION", Context.MODE_PRIVATE);

        name = (TextView) view.findViewById(R.id.profile_username);
        phone = (TextView) view.findViewById(R.id.profile_phoneNo);
        mail = (TextView) view.findViewById(R.id.profile_email);

        name1 = session.getString("name", "");
        name.setText(name1);
        String phone1 = session.getString("phone", "");
        phone.setText(phone1);
        String mail1 = session.getString("mail", "");
        mail.setText(mail1);
        String imageurl = session.getString("image", "");
        Picasso.get().load(imageurl)
                .into(imgProfil);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void olaylar() {
        String url = "tum_olaylar.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray olaylar = jsonObject.getJSONArray("olaylar");

                    for (int i = 0; i < olaylar.length(); i++) {
                        JSONObject n = olaylar.getJSONObject(i);
                        String durum = n.getString("durum");
                        String username = n.getString("username");
                        String olayturu = n.getString("olayturu");
                        String olaybilgi = n.getString("olaybilgi");
                        String konum = n.getString("konum");
                        String tarih = n.getString("tarih");
                        String image = n.getString("image");

                        Log.e("CEVAP3", durum + " " + username + " " + olayturu + " " + olaybilgi + " " + konum + " " + tarih + " ");
                        if (username.equals(name1)) {
                            ListElement olay = new ListElement(1, durum, username, olayturu, olaybilgi, konum, tarih, image);
                            list.add(olay);
                        }

                    }
                    adapter = new MyAdapterProfil(getContext(), list);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
