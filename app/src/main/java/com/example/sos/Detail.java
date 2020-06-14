package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.mail.Quota;

import static com.example.sos.R.color.red;

public class Detail extends AppCompatActivity {

    private TextView txtOlayTuru;
    private TextView txtOlayBilgisi;
    private TextView txtUser;
    private TextView txtKonum;
    private TextView txtTarih;
    private ImageView img;
    private ImageView imageViewapp, imageViewdislike, imageViewlike;
    private TextView textViewapproval, textViewlike, textViewdislike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtOlayTuru = findViewById(R.id.txtheader);
        txtOlayBilgisi = findViewById(R.id.txtexp);
        txtUser = findViewById(R.id.txtuser);
        txtKonum = findViewById(R.id.txtloca);
        txtTarih = findViewById(R.id.txtdate);
        img = findViewById(R.id.imageinci);

        String olayTuru = getIntent().getStringExtra("olayturu");
        String olayBilgisi = getIntent().getStringExtra("olaybilgi");
        String user = getIntent().getStringExtra("username");
        String tarih = getIntent().getStringExtra("tarih");
        String konum = getIntent().getStringExtra("lokasyon");
        String image = getIntent().getStringExtra("image");


        txtOlayTuru.setText(olayTuru);
        txtOlayBilgisi.setText(olayBilgisi);
        txtUser.setText("Added By : " + user);
        txtTarih.setText("Date : " + tarih);
        txtKonum.setText(konum);

        Picasso.get().load(image)
                .into(img);

        imageViewapp = findViewById(R.id.imageViewapp);
        imageViewdislike = findViewById(R.id.imageViewdislike);
        imageViewlike = findViewById(R.id.imageViewlike);
        textViewapproval = findViewById(R.id.textViewapproval);
        textViewdislike = findViewById(R.id.textViewdislike);
        textViewlike = findViewById(R.id.textViewlike);
    }

    boolean begen = true;
    boolean begenme = true;
    boolean app = true;

    public void like(View view) {
        if (begen) {
            begen = false;
            textViewlike.setText("Liked");
            textViewdislike.setText(" ");
            textViewapproval.setText(" ");
        } else {
            begen = true;
            textViewlike.setText(" ");
        }
    }

    public void disslike(View view) {
        if (begenme) {
            begenme = false;
            textViewdislike.setText("Disliked");
            textViewlike.setText(" ");
            textViewapproval.setText(" ");
        } else {
            begenme = true;
            textViewdislike.setText(" ");
        }
    }

    public void app(View view) {
        if (app) {
            app = false;
            textViewapproval.setText("Checked");
            textViewlike.setText(" ");
            textViewdislike.setText(" ");
        } else {
            app = true;
            textViewapproval.setText(" ");
        }
    }
}
