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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
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
import java.net.CookieHandler;
import java.net.CookieManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Warehouse extends AppCompatActivity implements OnMapReadyCallback{

    private static final String TAG = "Form";
    Toolbar toolbar;
    Spinner datasource, availability;


    List<String> dat, sta, cit, ava;



    EditText state, city , propid;

    double lat, lng;

    GoogleMap mMap;


    TextView change;

    private static final int DEFAULT_ZOOM = 15;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 12;

    String pid , pid2, type, date;

    String ds, st, ci, avai;

    EditText location, address;
    public ProgressBar progress;

    String code = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        MapUtility.apiKey = getResources().getString(R.string.google_maps_key);



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

        toolbar = findViewById(R.id.toolbar2);
        change = findViewById(R.id.change);
        progress = findViewById(R.id.progressBar);
        propid = findViewById(R.id.pid);

        datasource = findViewById(R.id.datasource);
        availability = findViewById(R.id.availability);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);

        location = findViewById(R.id.location);
        address = findViewById(R.id.address);




        Geocoder geocoder = new Geocoder(this);
        try
        {
            List<Address> addresses = geocoder.getFromLocation(lat,lng, 1);
            String cii = addresses.get(0).getLocality();
            String stat = addresses.get(0).getAdminArea();

            Log.i(TAG, "Place: " + addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getAdminArea() + " , " + addresses.get(0).getLocality() + " , " + addresses.get(0).getPremises()  + " , " + addresses.get(0).getSubLocality() + " , " + addresses.get(0).getAddressLine(0));

            city.setText(cii);
            state.setText(stat);

            st = stat;
            ci = cii;


        } catch (IOException e){
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
        dat.add("Property Owner Updated");
        dat.add("Inventory Emailer");




        ava.add("Built to Suit (BTS)");
        ava.add("Ready to move in (RTM)");
        ava.add("Under Construction (UC)");





        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dat);
        datasource.setAdapter(adapter2);

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ava);
        availability.setAdapter(adapter1);






        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<loginBean> call = cr.calculateDistance(String.valueOf(lat) , String.valueOf(lng));

        call.enqueue(new Callback<loginBean>() {
            @Override
            public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                code = "-" + response.body().getMessage();

                toolbar.setTitle(pid + code);
                propid.setText(pid + code);

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<loginBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME , Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountries(Collections.singletonList("IN"))
                        .setTypeFilter(TypeFilter.CITIES)
                        .build(Warehouse.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });

        /*city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ci = cit.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Warehouse.this, LocationPickerActivity.class);
                intent.putExtra(MapUtility.LATITUDE, lat);
                intent.putExtra(MapUtility.LONGITUDE, lng);
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);

               /* Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(lat , lng)
                        .showLatLong(true)  // Show Coordinates in the Activity
                        .setMapZoom(12.0f)
                        .build(Warehouse.this);
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);*/


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

                avai = ava.get(position);



                if (position == 2)
                {

                    pid = pid2 + "-" + "UC";
                    toolbar.setTitle(pid + code);
                    propid.setText(pid + code);

                    wuc frag = new wuc();

                    frag.setData(Warehouse.this);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.replace, frag);
                    fragmentTransaction.commit();



                }
                else if(position == 1)
                {

                    pid = pid2 + "-" + "RTM";
                    toolbar.setTitle(pid + code);
                    propid.setText(pid + code);

                    wrtm frag = new wrtm();

                    frag.setData(Warehouse.this);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.replace, frag);
                    fragmentTransaction.commit();



                }
                else
                {

                    pid = pid2 + "-" + "BTS";
                    toolbar.setTitle(pid + code);
                    propid.setText(pid + code);

                    wbts frag = new wbts();

                    frag.setData(Warehouse.this);
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

        LatLng coordinate = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions().position(coordinate).icon(BitmapDescriptorFactory.fromResource(com.shivtechs.maplocationpicker.R.drawable.ic_place_red_800_24dp));
        Marker marker = mMap.addMarker(markerOptions);

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


                Geocoder geocoder = new Geocoder(this);
                try
                {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
                    String cii = addresses.get(0).getLocality();
                    String stat = addresses.get(0).getAdminArea();

                    Log.i(TAG, "Place: " + addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getAdminArea() + " , " + addresses.get(0).getLocality() + " , " + addresses.get(0).getPremises()  + " , " + addresses.get(0).getSubLocality() + " , " + addresses.get(0).getAddressLine(0));

                    city.setText(cii);
                    state.setText(stat);

                    st = stat;
                    ci = cii;


                } catch (IOException e){
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
            if (resultCode == RESULT_OK && data != null) {



                lat = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                lng = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                Call<loginBean> call = cr.calculateDistance(String.valueOf(lat) , String.valueOf(lng));

                call.enqueue(new Callback<loginBean>() {
                    @Override
                    public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                        code = "-" + response.body().getMessage();

                        toolbar.setTitle(pid + code);
                        propid.setText(pid + code);
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<loginBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });

                Geocoder geocoder = new Geocoder(this);
                try
                {
                    List<Address> addresses = geocoder.getFromLocation(lat,lng, 1);
                    String cii = addresses.get(0).getLocality();
                    String stat = addresses.get(0).getAdminArea();

                    Log.i(TAG, "Place: " + addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getAdminArea() + " , " + addresses.get(0).getLocality() + " , " + addresses.get(0).getPremises()  + " , " + addresses.get(0).getSubLocality() + " , " + addresses.get(0).getAddressLine(0));

                    city.setText(cii);
                    state.setText(stat);

                    st = stat;
                    ci = cii;


                } catch (IOException e){
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
