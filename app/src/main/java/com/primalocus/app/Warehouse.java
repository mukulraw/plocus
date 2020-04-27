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
import com.google.android.gms.maps.model.LatLng;
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







        /*progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<stateBean> call = cr.getStates();

        call.enqueue(new Callback<stateBean>() {
            @Override
            public void onResponse(Call<stateBean> call, Response<stateBean> response) {

                sta.clear();

                for (int i = 0 ; i < response.body().getData().size() ; i++)
                {
                    sta.add(response.body().getData().get(i).getState());
                }

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Warehouse.this,
                        android.R.layout.simple_list_item_1, sta);
                state.setAdapter(adapter3);

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<stateBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });*/



        /*try {

            JSONArray array = new JSONArray(getJson());
            //JSONArray array = jsonObject.getJSONArray("array");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String state = object.getString("admin");
                sta.add(state);
            }

            HashSet<String> sstt = new HashSet<>(sta);
            sta.clear();
            sta.addAll(sstt);
            Collections.sort(sta, new Comparator<String>() {
                @Override
                public int compare(String text1, String text2) {
                    return text1.compareToIgnoreCase(text2);
                }
            });

            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Warehouse.this,
                    android.R.layout.simple_list_item_1, sta);
            state.setAdapter(adapter3);

        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

        /*state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                st = sta.get(position);

                Log.d("state", st);


                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                Call<stateBean> call = cr.getCity(st);

                call.enqueue(new Callback<stateBean>() {
                    @Override
                    public void onResponse(Call<stateBean> call, Response<stateBean> response) {

                        cit.clear();

                        for (int i = 0 ; i < response.body().getData().size() ; i++)
                        {
                            cit.add(response.body().getData().get(i).getCityName());
                        }

                        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(Warehouse.this,
                                android.R.layout.simple_list_item_1, cit);
                        city.setAdapter(adapter4);

                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<stateBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


                *//*try {

                    cit.clear();

                    JSONArray array = new JSONArray(getJson());
                    //JSONArray array = jsonObject.getJSONArray("array");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String state = object.getString("admin");

                        if (st.equals(state)) {
                            String name = object.getString("city");
                            cit.add(name);
                        }


                    }

                    HashSet<String> sstt1 = new HashSet<>(cit);
                    cit.clear();
                    cit.addAll(sstt1);
                    Collections.sort(cit, new Comparator<String>() {
                        @Override
                        public int compare(String text1, String text2) {
                            return text1.compareToIgnoreCase(text2);
                        }
                    });


                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(Warehouse.this,
                            android.R.layout.simple_list_item_1, cit);
                    city.setAdapter(adapter4);

                } catch (JSONException e) {
                    e.printStackTrace();
                }*//*

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

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
                    toolbar.setTitle(pid);
                    propid.setText(pid);

                    wuc frag = new wuc();

                    frag.setData(Warehouse.this);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.replace, frag);
                    fragmentTransaction.commit();


                   /* posession.setText("-");
                    posession.setVisibility(View.GONE);
                    postitle.setVisibility(View.GONE);
                    postitle.setText("Date of Possession");

                    under_construction.setVisibility(View.VISIBLE);
                    under_constructiontitle.setVisibility(View.VISIBLE);
                    under_constructionlayout.setVisibility(View.VISIBLE);
                    construction.setVisibility(View.VISIBLE);
                    constructiontitle.setVisibility(View.VISIBLE);
                    constructionlayout.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    pricetitle.setVisibility(View.VISIBLE);
                    covered.setVisibility(View.VISIBLE);
                    coveredtitle.setVisibility(View.VISIBLE);
                    available.setVisibility(View.VISIBLE);
                    availabletitle.setVisibility(View.VISIBLE);
                    partition.setVisibility(View.VISIBLE);
                    partitiontitle.setVisibility(View.VISIBLE);
                    rent.setVisibility(View.GONE);
                    renttitle.setVisibility(View.GONE);
                    security.setVisibility(View.VISIBLE);
                    securitytitle.setVisibility(View.VISIBLE);
                    common.setVisibility(View.VISIBLE);
                    commontitle.setVisibility(View.VISIBLE);
                    commontype.setVisibility(View.VISIBLE);
                    minimum.setVisibility(View.VISIBLE);
                    minimumtitle.setVisibility(View.VISIBLE);
                    minimum.setText("");
                    eaves.setVisibility(View.VISIBLE);
                    eavestitle.setVisibility(View.VISIBLE);
                    center_height.setVisibility(View.VISIBLE);
                    center_heighttitle.setVisibility(View.VISIBLE);
                    opening_docks.setVisibility(View.VISIBLE);
                    opening_dockstitle.setVisibility(View.VISIBLE);
                    plinth.setVisibility(View.VISIBLE);
                    plinthtitle.setVisibility(View.VISIBLE);
                    plan.setVisibility(View.VISIBLE);
                    plantitle.setVisibility(View.VISIBLE);
                    firenoc.setVisibility(View.VISIBLE);
                    firenoclayout.setVisibility(View.VISIBLE);
                    firenoctitle.setVisibility(View.VISIBLE);
                    safety.setVisibility(View.VISIBLE);
                    safetylayout.setVisibility(View.VISIBLE);
                    safetytitle.setVisibility(View.VISIBLE);
                    ventilation.setVisibility(View.VISIBLE);
                    ventilationlayout.setVisibility(View.VISIBLE);
                    ventilationtitle.setVisibility(View.VISIBLE);
                    insulation.setVisibility(View.VISIBLE);
                    insulationtitle.setVisibility(View.VISIBLE);
                    insulationlayout.setVisibility(View.VISIBLE);
                    leveler.setVisibility(View.VISIBLE);
                    levelertitle.setVisibility(View.VISIBLE);
                    levelerlayout.setVisibility(View.VISIBLE);
                    dockleverernumber.setVisibility(View.VISIBLE);
                    dockleverernumbertitle.setVisibility(View.VISIBLE);

                    min.setText("");
                    max.setText("");
                    covered.setText("");
                    available.setText("");
                    rent.setText("");
                    security.setText("");
                    common.setText("");
                    eaves.setText("");
                    center_height.setText("");
                    opening_docks.setText("");
                    dockleverernumber.setText("");*/

                }
                else if(position == 1)
                {

                    pid = pid2 + "-" + "RTM";
                    toolbar.setTitle(pid);
                    propid.setText(pid);

                    wrtm frag = new wrtm();

                    frag.setData(Warehouse.this);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.replace, frag);
                    fragmentTransaction.commit();

                    /*posession.setText("");
                    posession.setVisibility(View.VISIBLE);
                    postitle.setVisibility(View.VISIBLE);
                    postitle.setText("Date of Possession");

                    under_construction.setVisibility(View.GONE);
                    under_constructiontitle.setVisibility(View.GONE);
                    under_constructionlayout.setVisibility(View.GONE);
                    construction.setVisibility(View.VISIBLE);
                    constructiontitle.setVisibility(View.VISIBLE);
                    constructionlayout.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    pricetitle.setVisibility(View.VISIBLE);
                    covered.setVisibility(View.VISIBLE);
                    coveredtitle.setVisibility(View.VISIBLE);
                    available.setVisibility(View.VISIBLE);
                    availabletitle.setVisibility(View.VISIBLE);
                    partition.setVisibility(View.VISIBLE);
                    partitiontitle.setVisibility(View.VISIBLE);
                    rent.setVisibility(View.GONE);
                    renttitle.setVisibility(View.GONE);
                    security.setVisibility(View.VISIBLE);
                    securitytitle.setVisibility(View.VISIBLE);
                    common.setVisibility(View.VISIBLE);
                    commontitle.setVisibility(View.VISIBLE);
                    commontype.setVisibility(View.VISIBLE);
                    eaves.setVisibility(View.VISIBLE);
                    eavestitle.setVisibility(View.VISIBLE);
                    center_height.setVisibility(View.VISIBLE);
                    center_heighttitle.setVisibility(View.VISIBLE);
                    opening_docks.setVisibility(View.VISIBLE);
                    opening_dockstitle.setVisibility(View.VISIBLE);
                    plinth.setVisibility(View.VISIBLE);
                    plinthtitle.setVisibility(View.VISIBLE);
                    minimum.setVisibility(View.VISIBLE);
                    minimumtitle.setVisibility(View.VISIBLE);
                    minimum.setText("");
                    plan.setVisibility(View.VISIBLE);
                    plantitle.setVisibility(View.VISIBLE);
                    firenoc.setVisibility(View.VISIBLE);
                    firenoclayout.setVisibility(View.VISIBLE);
                    firenoctitle.setVisibility(View.VISIBLE);
                    safety.setVisibility(View.VISIBLE);
                    safetylayout.setVisibility(View.VISIBLE);
                    safetytitle.setVisibility(View.VISIBLE);
                    ventilation.setVisibility(View.VISIBLE);
                    ventilationlayout.setVisibility(View.VISIBLE);
                    ventilationtitle.setVisibility(View.VISIBLE);
                    insulation.setVisibility(View.VISIBLE);
                    insulationtitle.setVisibility(View.VISIBLE);
                    insulationlayout.setVisibility(View.VISIBLE);
                    leveler.setVisibility(View.VISIBLE);
                    levelertitle.setVisibility(View.VISIBLE);
                    levelerlayout.setVisibility(View.VISIBLE);
                    dockleverernumber.setVisibility(View.VISIBLE);
                    dockleverernumbertitle.setVisibility(View.VISIBLE);

                    min.setText("");
                    max.setText("");
                    covered.setText("");
                    available.setText("");
                    rent.setText("");
                    security.setText("");
                    common.setText("");
                    eaves.setText("");
                    center_height.setText("");
                    opening_docks.setText("");
                    dockleverernumber.setText("");*/

                    /*posession.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (avai.equals("Ready to move in (RTM)"))
                            {
                                final Dialog dialog = new Dialog(Warehouse.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.date_dialog);
                                dialog.show();


                                final DatePicker picker = dialog.findViewById(R.id.date);
                                Button ok = dialog.findViewById(R.id.ok);

                                long now = System.currentTimeMillis() - 1000;
                                picker.setMinDate(now);

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        int year = picker.getYear();
                                        int month = picker.getMonth();
                                        int day = picker.getDayOfMonth();

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, month, day);

                                        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd, YYYY");
                                        String strDate = format.format(calendar.getTime());

                                        posession.setText(strDate);

                                        dialog.dismiss();

                                    }
                                });
                            }



                        }
                    });*/

                }
                else
                {

                    pid = pid2 + "-" + "BTS";
                    toolbar.setTitle(pid);
                    propid.setText(pid);

                    wbts frag = new wbts();

                    frag.setData(Warehouse.this);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.replace, frag);
                    fragmentTransaction.commit();

                    /*posession.setText("");
                    posession.setVisibility(View.VISIBLE);
                    postitle.setVisibility(View.VISIBLE);
                    postitle.setText("Estimated Time of Possession");


                    under_construction.setVisibility(View.GONE);
                    under_constructiontitle.setVisibility(View.GONE);
                    under_constructionlayout.setVisibility(View.GONE);
                    construction.setVisibility(View.GONE);
                    constructiontitle.setVisibility(View.GONE);
                    constructionlayout.setVisibility(View.GONE);
                    price.setVisibility(View.GONE);
                    pricetitle.setVisibility(View.GONE);
                    covered.setVisibility(View.GONE);
                    coveredtitle.setVisibility(View.GONE);
                    available.setVisibility(View.GONE);
                    availabletitle.setVisibility(View.GONE);
                    partition.setVisibility(View.GONE);
                    minimum.setVisibility(View.GONE);
                    minimumtitle.setVisibility(View.GONE);
                    minimum.setText("-");
                    partitiontitle.setVisibility(View.GONE);
                    rent.setVisibility(View.VISIBLE);
                    renttitle.setVisibility(View.VISIBLE);
                    security.setVisibility(View.GONE);
                    securitytitle.setVisibility(View.GONE);
                    common.setVisibility(View.GONE);
                    commontitle.setVisibility(View.GONE);
                    commontype.setVisibility(View.GONE);
                    eaves.setVisibility(View.GONE);
                    eavestitle.setVisibility(View.GONE);
                    center_height.setVisibility(View.GONE);
                    center_heighttitle.setVisibility(View.GONE);
                    opening_docks.setVisibility(View.GONE);
                    opening_dockstitle.setVisibility(View.GONE);
                    plinth.setVisibility(View.GONE);
                    plinthtitle.setVisibility(View.GONE);
                    plan.setVisibility(View.GONE);
                    plantitle.setVisibility(View.GONE);
                    firenoc.setVisibility(View.GONE);
                    firenoclayout.setVisibility(View.GONE);
                    firenoctitle.setVisibility(View.GONE);
                    safety.setVisibility(View.GONE);
                    safetylayout.setVisibility(View.GONE);
                    safetytitle.setVisibility(View.GONE);
                    ventilation.setVisibility(View.GONE);
                    ventilationlayout.setVisibility(View.GONE);
                    ventilationtitle.setVisibility(View.GONE);
                    insulation.setVisibility(View.GONE);
                    insulationtitle.setVisibility(View.GONE);
                    insulationlayout.setVisibility(View.GONE);
                    leveler.setVisibility(View.GONE);
                    levelertitle.setVisibility(View.GONE);
                    levelerlayout.setVisibility(View.GONE);
                    dockleverernumber.setVisibility(View.GONE);
                    dockleverernumbertitle.setVisibility(View.GONE);


                    min.setText("-");
                    max.setText("-");
                    covered.setText("-");
                    available.setText("-");
                    //rent.setText("-");
                    security.setText("-");
                    common.setText("-");
                    eaves.setText("-");
                    center_height.setText("-");
                    opening_docks.setText("-");
                    dockleverernumber.setText("-");


                    unde = "-";
                    cond = "-";
                    plin = "-";
                    fire = "-";
                    safe = "-";
                    vent = "-";
                    insu = "-";
                    leve = "-";

                    posession.setOnClickListener(null);*/

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
