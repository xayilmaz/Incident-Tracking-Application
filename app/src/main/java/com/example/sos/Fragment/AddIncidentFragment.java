package com.example.sos.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.icu.text.Transliterator;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddIncidentFragment extends Fragment implements AdapterView.OnItemSelectedListener, LocationListener {
    SharedPreferences session;
    private ArrayList<String> indicent = new ArrayList<>();
    private static final int CALL_PERMISSION_REQUEST_CODE = 1234;
    private TextView textViewdate;
    int day, month, year;
    public String tarih;
    private Spinner spinnerIncident;
    private ImageView imageViewPhoto1, imageViewPhoto2, imageViewPhoto3;
    private EditText editTextDiscription;
    private int permissionControl;
    private LocationManager locationManager;
    private String locationProvider = "gps";
    private Button buttonReport;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Bitmap selectedImage;
    Uri imagedata1;


    public AddIncidentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_incident, container, false);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        ///////////////////
        //LOCATION PART////
        ///////////////////
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);//location operators
        ImageView imageViewLocation = (ImageView) view.findViewById(R.id.imageViewLocation);
        imageViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionControl = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionControl != PackageManager.PERMISSION_GRANTED) {
                    //it hasn't been given permission yet
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else {
                    //it has been allowed before
                    Location konum = locationManager.getLastKnownLocation(locationProvider);
                    if (konum != null) {
                        onLocationChanged(konum);
                    } else {
                        Toast.makeText(getContext(), "Location Service Is Not Active !", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        ////////////////
        //CALL PHONE////
        ////////////////
        ImageView imageViewPhone = (ImageView) view.findViewById(R.id.imageViewPhone);
        imageViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        /////////////
        //SPINNER////
        /////////////
        spinnerIncident = view.findViewById(R.id.spinnerIncident);
        spinnerIncident.setBackgroundColor(getResources().getColor(R.color.change));
        //Add elements in Array:
        indicent.add("Broken Road");
        indicent.add("Accident");
        indicent.add("Robbery");
        indicent.add("Murder");
        indicent.add("Violance");
        indicent.add("Sabotage");
        indicent.add("Lost");
        indicent.add("Other");
        //Veri Adaptörü
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, indicent);
        //(HangiSayfadaOldugumuz,GosterilecekListItem,YazdırılacakText,HangiArray)
        spinnerIncident.setAdapter(dataAdapter);
        spinnerIncident.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)//Position=index no, id=long
            {
                if (position > 0) {
                    //Toast.makeText(getContext(), "Choosen Indicent: " + indicent.get(position), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ////////////////////
        //ACCESS GALLERY////
        ////////////////////
        imageViewPhoto1 = view.findViewById(R.id.imageViewPhoto1);
        imageViewPhoto2 = view.findViewById(R.id.imageViewPhoto2);
        imageViewPhoto3 = view.findViewById(R.id.imageViewPhoto3);
        imageViewPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
            }
        });
        imageViewPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 3);
                }

            }
        });
        imageViewPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 4);
                }
            }
        });
        ////////////////
        ////CALENDAR////
        ////////////////
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        textViewdate = view.findViewById(R.id.textViewdate);
        Date dt = new Date();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        String curTime = hours + ":" + minutes + ":";
        tarih = "Today : " + Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
        textViewdate.setText("Today : " + day + "/" + month + "/" + year + " Time : " + curTime);
        editTextDiscription = view.findViewById(R.id.editTextDiscription);

        session = getActivity().getSharedPreferences("SESSION", Context.MODE_PRIVATE);

        ///////////////////////
        //REPORT BUTTON CLICK//
        //////////////////////
        buttonReport = view.findViewById(R.id.buttonReport);
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        return view;
    }

    public void upload() {
        String edit = editTextDiscription.getText().toString();
        if (imagedata1 != null && edit.trim().length() != 0) {
            UUID uuid1 = UUID.randomUUID();
            final String imagename1 = "olay/" + uuid1 + ".jpg";
            storageReference.child(imagename1).putFile(imagedata1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imagename1);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String dowlandurl = uri.toString();

                            String url = "insert_olay.php";//WebService

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Log.e("CEVAP",response);
                                    JSONObject jsonResponse = null;
                                    try {
                                        jsonResponse = new JSONObject(response);
                                        int durum = jsonResponse.getInt("succes");
                                        //Log.e("CEVAP6",String.valueOf(durum));
                                        /*if(durum == 1){
                                            Toast.makeText(getContext(),"Incident was added successfully!",Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(getContext(),"Incident can not be added!",Toast.LENGTH_LONG).show();
                                        }*/
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();

                                    params.put("durum", "1");
                                    params.put("username", session.getString("name", ""));
                                    params.put("olayturu", indicent.get(spinnerIncident.getSelectedItemPosition()));
                                    params.put("olaybilgi", editTextDiscription.getText().toString());
                                    params.put("konum", konum);
                                    params.put("tarih", tarih);
                                    params.put("image", dowlandurl);
                                    return params;
                                }
                            };
                            Volley.newRequestQueue(getContext()).add(stringRequest);
                            //Log.e("Cevap",dowlandurl);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            Toast.makeText(getContext(), "Incident was added successfully!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Photo and Discription can't be empty!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 || requestCode == 3 || requestCode == 4 && resultCode == Activity.RESULT_OK && data != null) {
            imagedata1 = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imagedata1);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    if (requestCode == 2) {
                        imageViewPhoto1.setImageBitmap(selectedImage);
                    }
                    if (requestCode == 3) {
                        imageViewPhoto2.setImageBitmap(selectedImage);
                    }
                    if (requestCode == 4) {
                        imageViewPhoto3.setImageBitmap(selectedImage);
                    }
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imagedata1);
                    if (requestCode == 2) {
                        imageViewPhoto1.setImageBitmap(selectedImage);
                    }
                    if (requestCode == 3) {
                        imageViewPhoto2.setImageBitmap(selectedImage);
                    }
                    if (requestCode == 4) {
                        imageViewPhoto3.setImageBitmap(selectedImage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    ///////////////////
    //To Call Number///
    ///////////////////
    void call() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "123"));
            getActivity().startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_REQUEST_CODE);
        }
    }

    //////////////////////////////
    //It checks the permission////
    //////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }
    }

    public String konum;

    @Override
    public void onLocationChanged(Location location) {
        // Latitude and Longtitude Informations are supplied in there.
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        Toast.makeText(getContext(), "Location Information :\n" + "Latitude :" + latitude + "\n" + "Longtitude :" + longitude, Toast.LENGTH_LONG).show();
        konum = latitude + " " + longitude;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
