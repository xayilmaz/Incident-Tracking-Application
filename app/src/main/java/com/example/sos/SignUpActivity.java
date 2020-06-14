package com.example.sos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    private Button buttonCreate;
    private EditText userName, nameandSurname, password, phoneNumber, mail;
    private ImageView imageProfil;
    Bitmap selectedImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Uri imagedata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        imageProfil = findViewById(R.id.imageProfil);
        buttonCreate = findViewById(R.id.buttonCreate);
        userName = findViewById(R.id.editTextuserName);
        nameandSurname = findViewById(R.id.nameandSurname);
        password = findViewById(R.id.sifre);
        phoneNumber = findViewById(R.id.phoneNumber);
        mail = findViewById(R.id.mail);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public void upload(View view){

        if(imagedata != null)
        {
            UUID uuid = UUID.randomUUID();
            final String imagename ="images/"+uuid+".jpg";
            storageReference.child(imagename).putFile(imagedata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Yüklendi", Toast.LENGTH_LONG).show();

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imagename);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String dowlandUrl = uri.toString();
                            System.out.println(dowlandUrl);
                            KisiEkle(dowlandUrl);
                            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void SelectImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode==RESULT_OK && data!=null){
            imagedata = data.getData();
            try {
                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imagedata);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageProfil.setImageBitmap(selectedImage);
                }
                else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagedata);
                    imageProfil.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed(){
        super.onBackPressed();

    }
    public void KisiEkle(final String urll) {
        String url = "insert.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley",error.toString());
                        //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("username", userName.getText().toString());
                params.put("namesurname", nameandSurname.getText().toString());
                params.put("password",password.getText().toString());
                params.put("phoneno",phoneNumber.getText().toString());
                params.put("mail",mail.getText().toString());
                params.put("image",urll);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
}
