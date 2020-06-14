package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class SignInActivity extends AppCompatActivity {
    private Button buttonLogin;
    private EditText editTextusername;
    private EditText editTextpassword;
    private TextView forgot;
    SharedPreferences session;
    SharedPreferences.Editor sessionKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextpassword = findViewById(R.id.editTextpassword);
        editTextusername = findViewById(R.id.editTextuserName);

        forgot = findViewById(R.id.forgotpassword);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });


        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Giris();

            }
        });
    }

    public void Giris() {
        String url = "singin.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    int durum = jsonResponse.getInt("succes");
                    String name = jsonResponse.getString("name");
                    String phone = jsonResponse.getString("phone");
                    String mail = jsonResponse.getString("mail");
                    String imageurl = jsonResponse.getString("image");

                    session = getSharedPreferences("SESSION", MODE_PRIVATE);
                    sessionKullanici = session.edit();

                    sessionKullanici.putString("name", name);
                    sessionKullanici.putString("phone", phone);
                    sessionKullanici.putString("mail", mail);
                    sessionKullanici.putString("image", imageurl);
                    sessionKullanici.apply();

                    if (durum == 1) {
                        Toast.makeText(getApplicationContext(), "Welcome !", Toast.LENGTH_LONG).show();
                        Intent newIntent = new Intent(SignInActivity.this, tabActivity2.class);
                        startActivity(newIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Username or Password Incorect", Toast.LENGTH_LONG).show();
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

                params.put("username", editTextusername.getText().toString());
                params.put("password", editTextpassword.getText().toString());

                return params;
            }
        };

        Volley.newRequestQueue(this).add(istek);
    }
}
