package com.example.sos.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sos.ListElement;
import com.example.sos.Adapter.MyAdapter;
import com.example.sos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncidentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<ListElement> list;

    public IncidentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_incidents, container, false);
        olaylar();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
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

                        Log.e("CEVAP3", durum + " " + username + " " + olayturu + " " + olaybilgi + " " + konum + " " + tarih + " " + " " + image);
                        if (durum.equals("1")) {
                            ListElement olay = new ListElement(1, durum, username, olayturu, olaybilgi, konum, tarih, image);
                            list.add(olay);
                        }
                    }
                    adapter = new MyAdapter(getContext(), list);
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
