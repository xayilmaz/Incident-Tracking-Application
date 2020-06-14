package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    private Button buttonDegis;
    private EditText txtSifre, txtSifre2;
    public String mailadress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        buttonDegis = findViewById(R.id.btnDegis);
        txtSifre = findViewById(R.id.txtSifre);
        txtSifre2 = findViewById(R.id.txtSifre2);
        Intent intent = getIntent();
        mailadress = intent.getStringExtra("mail");
        Toast.makeText(getApplicationContext(), mailadress, Toast.LENGTH_LONG).show();

        buttonDegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sifre = txtSifre.getText().toString();
                String sifre2 = txtSifre2.getText().toString();

                if (sifre.equals(sifre2)) {
                    Giris();
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords are not same.Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void Giris() {
        String url = "passwordchange.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    int durum = jsonResponse.getInt("succes");

                    if (durum == 1) {
                        Toast.makeText(getApplicationContext(), "Şifre Değiştirildi!", Toast.LENGTH_LONG).show();
                        Intent newIntent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(newIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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

                params.put("mail", mailadress);
                params.put("password", txtSifre.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(istek);
    }
}
