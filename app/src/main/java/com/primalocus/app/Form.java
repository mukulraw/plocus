package com.primalocus.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.primalocus.app.loginPOJO.loginBean;
import com.primalocus.app.statePOJO.stateBean;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;
import com.sucho.placepicker.Constants;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Form extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Form";
    Toolbar toolbar;
    Spinner datasource, availability;

    List<String> dat, sta, cit, ava;


    double lat, lng;

    EditText state, city;

    GoogleMap mMap;

    EditText location, address , propid , landmark;

    TextView change;

    private static final int DEFAULT_ZOOM = 15;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 12;

    String pid, pid2 , type, date;

    String ds, st, ci, av;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        pid = getIntent().getStringExtra("pid");
        pid2 = getIntent().getStringExtra("pid");
        type = getIntent().getStringExtra("type");
        date = getIntent().getStringExtra("date");


        dat = new ArrayList<>();
        sta = new ArrayList<>();
        cit = new ArrayList<>();
        ava = new ArrayList<>();




        location = findViewById(R.id.location);
        address = findViewById(R.id.address);
        propid = findViewById(R.id.pid);
        toolbar = findViewById(R.id.toolbar2);
        change = findViewById(R.id.change);
        progress = findViewById(R.id.progressBar);
        landmark = findViewById(R.id.landmark);
        datasource = findViewById(R.id.datasource);
        availability = findViewById(R.id.availability);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);


        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String cii = addresses.get(0).getLocality();
            String stat = addresses.get(0).getAdminArea();

            Log.i(TAG, "Place: " + addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getAdminArea() + " , " + addresses.get(0).getLocality() + " , " + addresses.get(0).getPremises() + " , " + addresses.get(0).getSubLocality() + " , " + addresses.get(0).getAddressLine(0));

            city.setText(cii);
            state.setText(stat);

            st = stat;
            ci = cii;


        } catch (IOException e) {
            e.printStackTrace();
        }


        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.arrowleft);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle(pid);
        propid.setText(pid);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        dat.add("Survey");
        dat.add("Reference");
        dat.add("LL Updated");
        dat.add("Inventory Emailer");


        ava.add("Ready to move in (RTM)");
        ava.add("Under Construction");
        ava.add("Built to Suit (BTS)");


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dat);
        datasource.setAdapter(adapter2);

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ava);
        availability.setAdapter(adapter1);




        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountries(Collections.singletonList("IN"))
                        .build(Form.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });



        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Form.this, LocationPickerActivity.class);
                intent.putExtra(MapUtility.LATITUDE, lat);
                intent.putExtra(MapUtility.LONGITUDE, lng);
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);

                /*List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME , Place.Field.LAT_LNG);

// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(Form.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);*/


            }
        });





        datasource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ds = dat.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        availability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                av = ava.get(position);

                if (position == 0) {

                    pid = pid2 + "-" + "RTM";
                    toolbar.setTitle(pid);
                    propid.setText(pid);

                    rrtm frag = new rrtm();

                    frag.setData(Form.this);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.replace, frag);
                    fragmentTransaction.commit();

                } else if(position == 1)
                {

                    pid = pid2 + "-" + "UC";
                    toolbar.setTitle(pid);
                    propid.setText(pid);

                    rrtm frag = new rrtm();

                    frag.setData(Form.this);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.replace, frag);
                    fragmentTransaction.commit();

                }
                else
                {
                    pid = pid2 + "-" + "BTS";
                    toolbar.setTitle(pid);
                    propid.setText(pid);

                    rbts frag = new rbts();

                    frag.setData(Form.this);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.replace, frag);
                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }





    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat,
                        lng), DEFAULT_ZOOM));

    }

    public String getJson() {
        String json = null;
        try {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + " , " + place.getLatLng().latitude);

                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    String cii = addresses.get(0).getLocality();
                    String stat = addresses.get(0).getAdminArea();

                    city.setText(cii);
                    state.setText(stat);

                    st = stat;
                    ci = cii;


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                lat = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                lng = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                    String cii = addresses.get(0).getLocality();
                    String stat = addresses.get(0).getAdminArea();

                    Log.i(TAG, "Place: " + addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getAdminArea() + " , " + addresses.get(0).getLocality() + " , " + addresses.get(0).getPremises() + " , " + addresses.get(0).getSubLocality() + " , " + addresses.get(0).getAddressLine(0));

                    city.setText(cii);
                    state.setText(stat);

                    st = stat;
                    ci = cii;


                } catch (IOException e) {
                    e.printStackTrace();
                }


                String addr = data.getStringExtra(MapUtility.ADDRESS);
                address.setText(addr);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lat,
                                lng), DEFAULT_ZOOM));
            }
        }




    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}
