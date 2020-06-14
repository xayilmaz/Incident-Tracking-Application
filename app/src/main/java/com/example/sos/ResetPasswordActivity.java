package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.example.sos.Mail.GMailSender;

public class ResetPasswordActivity extends AppCompatActivity {
    private Button buttonGonder;
    private Button buttonDogrula;
    private EditText txtMail;
    private EditText txtCode;
    final private String username = "Your Mail";
    final private String password = "Mail Password";
    private ImageView imageView3;
    public int code;
    private TextView txtyazi1, txtyazi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        buttonGonder = findViewById(R.id.btnGonder);
        buttonDogrula = findViewById(R.id.btnDogrula);
        imageView3 = findViewById(R.id.imageView3);
        buttonDogrula.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.INVISIBLE);

        txtMail = findViewById(R.id.txtMail);
        txtCode = findViewById(R.id.txtCode);
        txtCode.setVisibility(View.INVISIBLE);

        txtyazi1 = findViewById(R.id.textView13);
        txtyazi2 = findViewById(R.id.textView);
        txtyazi2.setVisibility(View.INVISIBLE);


        Random random = new Random();
        code = random.nextInt(900000) + 100000;

        buttonGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Giris();
            }
        });

        buttonDogrula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code == Integer.parseInt(txtCode.getText().toString())) {
                    Intent intent = new Intent(ResetPasswordActivity.this, ChangePasswordActivity.class);
                    intent.putExtra("mail", txtMail.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Confirmation code is invalid.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void Giris() {
        String url = "mailkontrol.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    int durum = jsonResponse.getInt("succes");

                    if (durum == 1) {
                        try {
                            GMailSender sender = new GMailSender(username, password);
                            sender.sendMail(
                                    "Your Password Reset Code on S.O.S App",
                                    "Hello, your reset code is " + Integer.toString(code),
                                    username, txtMail.getText().toString());
                            Toast.makeText(getApplicationContext(), "Code Sent.Please Check Your E-com.example.sos.Mail.", Toast.LENGTH_LONG).show();
                            txtMail.setEnabled(false);
                            txtCode.setVisibility(View.VISIBLE);
                            buttonDogrula.setVisibility(View.VISIBLE);
                            imageView3.setVisibility(View.VISIBLE);
                            buttonGonder.setVisibility(View.INVISIBLE);
                            txtyazi1.setVisibility(View.INVISIBLE);
                            txtyazi2.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Toast.makeText(ResetPasswordActivity.this, "Invalid E-com.example.sos.Mail Addres.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "There Is Not A com.example.sos.Mail Address.", Toast.LENGTH_LONG).show();
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
                params.put("mail", txtMail.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(istek);
    }
}
