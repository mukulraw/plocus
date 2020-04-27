package com.primalocus.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.primalocus.app.getSurveyPOJO.getSurveyBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Survey extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private static final String TAG = Survey.class.getSimpleName();
    // The entry point to the Places API.
    private PlacesClient mPlacesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private List[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    FloatingActionButton fab;

    LinearLayout enterData , endSession;

    View bottom;
    ProgressBar progress;
    TextView total;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_survey);

        fab = findViewById(R.id.floatingActionButton);
        enterData = findViewById(R.id.enterdata);
        endSession = findViewById(R.id.endsession);
        bottom = findViewById(R.id.textView7);
        total = findViewById(R.id.total);
        progress = findViewById(R.id.progressBar);
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        mPlacesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.textView6);
        mapFragment.getMapAsync(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDeviceLocation();

            }
        });

        endSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharePreferenceUtils.getInstance().saveString("mode" , "0");

                Intent intent = new Intent(Survey.this , MainActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });

        enterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Survey.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_property_dialog);
                dialog.show();

                final Spinner property = dialog.findViewById(R.id.property);
                final EditText date = dialog.findViewById(R.id.date);
                final EditText type = dialog.findViewById(R.id.type);
                final EditText propertyid = dialog.findViewById(R.id.propertyid);
                Button submit = dialog.findViewById(R.id.button);

                final List<String> pro = new ArrayList<>();

                pro.add("Warehouse");
                pro.add("Retail");
                pro.add("Office");
                pro.add("Others");

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Survey.this ,
                        android.R.layout.simple_list_item_1 , pro);
                property.setAdapter(adapter1);

                property.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 3)
                        {
                            type.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            type.setVisibility(View.GONE);
                        }

                        String item = pro.get(position);

                        String da = date.getText().toString();

                        if (da.length() > 0)
                        {

                            String s = item.substring(0,2);

                            s = s.toUpperCase();

                            propertyid.setText(s + "-" + da + "-" + SharePreferenceUtils.getInstance().getString("id"));

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                String date2 = new SimpleDateFormat("d-M-Y", Locale.getDefault()).format(new Date());

                String sel = (String) property.getSelectedItem();

                String s = sel.substring(0,2);

                s = s.toUpperCase();

                propertyid.setText(s + "-" + date2 + "-" + SharePreferenceUtils.getInstance().getString("id"));

                date.setText(date2);

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog dialog = new Dialog(Survey.this);
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

                                SimpleDateFormat format = new SimpleDateFormat("d-M-Y");
                                String strDate = format.format(calendar.getTime());



                                String sel = (String) property.getSelectedItem();

                                String s = sel.substring(0,2);

                                s = s.toUpperCase();

                                propertyid.setText(s + "-" + strDate + "-" + SharePreferenceUtils.getInstance().getString("id"));

                                date.setText(strDate);

                                dialog.dismiss();

                            }
                        });

                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String dd = date.getText().toString();
                        String sel = (String) property.getSelectedItem();
                        if (!sel.equals("Others"))
                        {
                            if (dd.length() > 0)
                            {

                                switch (sel) {
                                    case "Retail":
                                        if (mLastKnownLocation != null)
                                        {
                                            Intent intent = new Intent(Survey.this , Form.class);
                                            intent.putExtra("lat" , mLastKnownLocation.getLatitude());
                                            intent.putExtra("lng" , mLastKnownLocation.getLongitude());
                                            intent.putExtra("pid" , propertyid.getText().toString());
                                            intent.putExtra("date" , date.getText().toString());
                                            intent.putExtra("type" , sel);
                                            dialog.dismiss();
                                            startActivity(intent);

                                        }
                                        else
                                        {
                                            Intent intent = new Intent(Survey.this , Form.class);
                                            intent.putExtra("lat" , 0);
                                            intent.putExtra("lng" , 0);
                                            intent.putExtra("pid" , propertyid.getText().toString());
                                            intent.putExtra("date" , date.getText().toString());
                                            intent.putExtra("type" , sel);
                                            dialog.dismiss();
                                            startActivity(intent);

                                        }
                                        break;
                                    case "Warehouse":
                                        if (mLastKnownLocation != null)
                                        {
                                            Intent intent = new Intent(Survey.this , Warehouse.class);
                                            intent.putExtra("lat" , mLastKnownLocation.getLatitude());
                                            intent.putExtra("lng" , mLastKnownLocation.getLongitude());
                                            intent.putExtra("pid" , propertyid.getText().toString());
                                            intent.putExtra("date" , date.getText().toString());
                                            intent.putExtra("type" , sel);
                                            dialog.dismiss();
                                            startActivity(intent);


                                        }
                                        else
                                        {
                                            Intent intent = new Intent(Survey.this , Warehouse.class);
                                            intent.putExtra("lat" , 0);
                                            intent.putExtra("lng" , 0);
                                            intent.putExtra("pid" , propertyid.getText().toString());
                                            intent.putExtra("date" , date.getText().toString());
                                            intent.putExtra("type" , sel);
                                            dialog.dismiss();
                                            startActivity(intent);

                                        }
                                        break;
                                    case "Office":
                                        if (mLastKnownLocation != null)
                                        {
                                            Intent intent = new Intent(Survey.this , Office.class);
                                            intent.putExtra("lat" , mLastKnownLocation.getLatitude());
                                            intent.putExtra("lng" , mLastKnownLocation.getLongitude());
                                            intent.putExtra("pid" , propertyid.getText().toString());
                                            intent.putExtra("date" , date.getText().toString());
                                            intent.putExtra("type" , sel);
                                            dialog.dismiss();
                                            startActivity(intent);

                                        }
                                        else
                                        {
                                            Intent intent = new Intent(Survey.this , Office.class);
                                            intent.putExtra("lat" , 0);
                                            intent.putExtra("lng" , 0);
                                            intent.putExtra("pid" , propertyid.getText().toString());
                                            intent.putExtra("date" , date.getText().toString());
                                            intent.putExtra("type" , sel);
                                            dialog.dismiss();
                                            startActivity(intent);

                                        }
                                        break;
                                    default:
                                        if (mLastKnownLocation != null)
                                        {
                                            Intent intent = new Intent(Survey.this , Form.class);
                                            intent.putExtra("lat" , mLastKnownLocation.getLatitude());
                                            intent.putExtra("lng" , mLastKnownLocation.getLongitude());
                                            intent.putExtra("pid" , propertyid.getText().toString());
                                            intent.putExtra("date" , date.getText().toString());
                                            intent.putExtra("type" , type.getText().toString());
                                            dialog.dismiss();
                                            startActivity(intent);

                                        }
                                        else
                                        {
                                            Intent intent = new Intent(Survey.this , Form.class);
                                            intent.putExtra("lat" , 0);
                                            intent.putExtra("lng" , 0);
                                            intent.putExtra("pid" , propertyid.getText().toString());
                                            intent.putExtra("date" , date.getText().toString());
                                            intent.putExtra("type" , type.getText().toString());
                                            dialog.dismiss();
                                            startActivity(intent);

                                        }
                                        break;
                                }


                            }
                            else
                            {
                                Toast.makeText(Survey.this, "Please choose a date", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {

                            String ty = type.getText().toString();

                            if (ty.length() > 0)
                            {

                                if (dd.length() > 0)
                                {

                                    switch (sel) {
                                        case "Retail":
                                            if (mLastKnownLocation != null)
                                            {
                                                Intent intent = new Intent(Survey.this , Form.class);
                                                intent.putExtra("lat" , mLastKnownLocation.getLatitude());
                                                intent.putExtra("lng" , mLastKnownLocation.getLongitude());
                                                intent.putExtra("pid" , propertyid.getText().toString());
                                                intent.putExtra("date" , date.getText().toString());
                                                intent.putExtra("type" , sel);
                                                dialog.dismiss();
                                                startActivity(intent);

                                            }
                                            else
                                            {
                                                Intent intent = new Intent(Survey.this , Form.class);
                                                intent.putExtra("lat" , 0);
                                                intent.putExtra("lng" , 0);
                                                intent.putExtra("pid" , propertyid.getText().toString());
                                                intent.putExtra("date" , date.getText().toString());
                                                intent.putExtra("type" , sel);
                                                dialog.dismiss();
                                                startActivity(intent);

                                            }
                                            break;
                                        case "Warehouse":
                                            if (mLastKnownLocation != null)
                                            {
                                                Intent intent = new Intent(Survey.this , Warehouse.class);
                                                intent.putExtra("lat" , mLastKnownLocation.getLatitude());
                                                intent.putExtra("lng" , mLastKnownLocation.getLongitude());
                                                intent.putExtra("pid" , propertyid.getText().toString());
                                                intent.putExtra("date" , date.getText().toString());
                                                intent.putExtra("type" , sel);
                                                dialog.dismiss();
                                                startActivity(intent);


                                            }
                                            else
                                            {
                                                Intent intent = new Intent(Survey.this , Warehouse.class);
                                                intent.putExtra("lat" , 0);
                                                intent.putExtra("lng" , 0);
                                                intent.putExtra("pid" , propertyid.getText().toString());
                                                intent.putExtra("date" , date.getText().toString());
                                                intent.putExtra("type" , sel);
                                                dialog.dismiss();
                                                startActivity(intent);

                                            }
                                            break;
                                        case "Office":
                                            if (mLastKnownLocation != null)
                                            {
                                                Intent intent = new Intent(Survey.this , Office.class);
                                                intent.putExtra("lat" , mLastKnownLocation.getLatitude());
                                                intent.putExtra("lng" , mLastKnownLocation.getLongitude());
                                                intent.putExtra("pid" , propertyid.getText().toString());
                                                intent.putExtra("date" , date.getText().toString());
                                                intent.putExtra("type" , sel);
                                                dialog.dismiss();
                                                startActivity(intent);

                                            }
                                            else
                                            {
                                                Intent intent = new Intent(Survey.this , Office.class);
                                                intent.putExtra("lat" , 0);
                                                intent.putExtra("lng" , 0);
                                                intent.putExtra("pid" , propertyid.getText().toString());
                                                intent.putExtra("date" , date.getText().toString());
                                                intent.putExtra("type" , sel);
                                                dialog.dismiss();
                                                startActivity(intent);

                                            }
                                            break;
                                        default:
                                            if (mLastKnownLocation != null)
                                            {
                                                Intent intent = new Intent(Survey.this , Form.class);
                                                intent.putExtra("lat" , mLastKnownLocation.getLatitude());
                                                intent.putExtra("lng" , mLastKnownLocation.getLongitude());
                                                intent.putExtra("pid" , propertyid.getText().toString());
                                                intent.putExtra("date" , date.getText().toString());
                                                intent.putExtra("type" , type.getText().toString());
                                                dialog.dismiss();
                                                startActivity(intent);

                                            }
                                            else
                                            {
                                                Intent intent = new Intent(Survey.this , Form.class);
                                                intent.putExtra("lat" , 0);
                                                intent.putExtra("lng" , 0);
                                                intent.putExtra("pid" , propertyid.getText().toString());
                                                intent.putExtra("date" , date.getText().toString());
                                                intent.putExtra("type" , type.getText().toString());
                                                dialog.dismiss();
                                                startActivity(intent);

                                            }
                                            break;
                                    }


                                }
                                else
                                {
                                    Toast.makeText(Survey.this, "Please choose a date", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(Survey.this, "Invalid property type", Toast.LENGTH_SHORT).show();
                            }

                        }



                    }
                });





            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);

                bottom.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottom.invalidate();
                        mMap.setPadding(0 , 0 , 0 , bottom.getHeight() + 18);

                    }
                }, 1);


            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {


                        if (location != null) {
                            // Logic to handle location object
                            mLastKnownLocation = location;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }


                    }
                });
                /*locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });*/
            }
        } catch(Exception e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<getSurveyBean> call = cr.getSurveyBean(SharePreferenceUtils.getInstance().getString("id"));
        call.enqueue(new Callback<getSurveyBean>() {
            @Override
            public void onResponse(Call<getSurveyBean> call, Response<getSurveyBean> response) {

                if (response.body().getStatus().equals("1"))
                {
                    total.setText(String.valueOf(response.body().getData().size()));
                }
                else
                {
                    total.setText("0");
                    Toast.makeText(Survey.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<getSurveyBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }
}
