package com.mrtecks.primalocus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.innovattic.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Form extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Form";
    Toolbar toolbar;
    Spinner datasource , state , city , availability , landusage;

    List<String> dat , sta , cit , ava , lan;

    Button submit , add;

    double lat , lng;

    GoogleMap mMap;

    TextView change;

    private static final int DEFAULT_ZOOM = 15;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 12;

    String pid , type , date;

    String ds ,st , ci , av , la;

    EditText location , address , min , max , floor , unit , chargeable , covered , carpet , rent , security , common , ceiling , facade , tenantname;
    EditText fdf , fdc , fwo , tdf , tdc , two , mobile , secondary , owned , email , caretaker , caretakerphone , emailcaretaker , remarks;
    RecyclerView images;
    RadioGroup condition , partition , tenant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        lat = getIntent().getDoubleExtra("lat" , 0);
        lng = getIntent().getDoubleExtra("lng" , 0);
        pid = getIntent().getStringExtra("pid");
        type = getIntent().getStringExtra("type");
        date = getIntent().getStringExtra("date");


        dat = new ArrayList<>();
        sta = new ArrayList<>();
        cit = new ArrayList<>();
        ava = new ArrayList<>();
        lan = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar2);
        change = findViewById(R.id.change);

        datasource = findViewById(R.id.datasource);
        availability = findViewById(R.id.availability);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        landusage = findViewById(R.id.landusage);
        add = findViewById(R.id.add);

        location = findViewById(R.id.location);
        address = findViewById(R.id.address);
        min = findViewById(R.id.min);
        max = findViewById(R.id.max);
        floor = findViewById(R.id.floor);
        unit = findViewById(R.id.unit);
        chargeable = findViewById(R.id.chargeable);
        covered = findViewById(R.id.covered);
        carpet = findViewById(R.id.carpet);
        rent = findViewById(R.id.rent);
        security = findViewById(R.id.security);
        common = findViewById(R.id.common);
        ceiling = findViewById(R.id.ceiling);
        facade = findViewById(R.id.facade);
        tenantname = findViewById(R.id.tenantname);

        fdf = findViewById(R.id.fdf);
        fdc = findViewById(R.id.fdc);
        fwo = findViewById(R.id.fwo);
        tdf = findViewById(R.id.tdf);
        tdc = findViewById(R.id.tdc);
        two = findViewById(R.id.two);
        mobile = findViewById(R.id.mobile);
        secondary = findViewById(R.id.secondary);
        owned = findViewById(R.id.owned);
        email = findViewById(R.id.email);
        caretaker = findViewById(R.id.caretaker);
        caretakerphone = findViewById(R.id.caretakerphone);
        emailcaretaker = findViewById(R.id.emailcaretaker);
        remarks = findViewById(R.id.remarks);

        images = findViewById(R.id.images);
        condition = findViewById(R.id.condition);
        partition = findViewById(R.id.partition);
        tenant = findViewById(R.id.tenant);

        submit = findViewById(R.id.button);


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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        dat.add("Survey");
        dat.add("Reference");
        dat.add("LL Updated");
        dat.add("Inventory Emailer");

        lan.add("Commercial");
        lan.add("Mix Land");
        lan.add("Institutional/ Industrial");
        lan.add("Residential");

        ava.add("Ready to move in (RTM)");
        ava.add("Under Construction");
        ava.add("Built to Suit (BTS)");



        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , dat);
        datasource.setAdapter(adapter2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , ava);
        availability.setAdapter(adapter1);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , lan);
        landusage.setAdapter(adapter5);


        try
        {

            JSONObject jsonObject=new JSONObject(getJson());
            JSONArray array=jsonObject.getJSONArray("array");
            for(int i=0;i<array.length();i++)
            {
                JSONObject object=array.getJSONObject(i);
                String state=object.getString("state");
                sta.add(state);
            }

            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Form.this,
                    android.R.layout.simple_list_item_1 , sta);
            state.setAdapter(adapter3);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                st = sta.get(position);

                Log.d("state" , st);

                try
                {

                    cit.clear();

                    JSONObject jsonObject=new JSONObject(getJson());
                    JSONArray array=jsonObject.getJSONArray("array");
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject object=array.getJSONObject(i);
                        String state=object.getString("state");

                        if (st.equals(state))
                        {
                            String name=object.getString("name");
                            cit.add(name);
                        }


                    }

                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(Form.this,
                            android.R.layout.simple_list_item_1 , cit);
                    city.setAdapter(adapter4);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ci = cit.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(Form.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.submit_popup);
                dialog.show();

                ImageButton ok = dialog.findViewById(R.id.imageButton3);
                ImageButton cancel = dialog.findViewById(R.id.imageButton4);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(Form.this , Survey.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });

            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(Form.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        landusage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                la = lan.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String lo = location.getText().toString();
                String ad = address.getText().toString();
                String fl = floor.getText().toString();
                String mi = min.getText().toString();
                String ma = max.getText().toString();
                String un = unit.getText().toString();
                String ch = chargeable.getText().toString();
                String co = covered.getText().toString();
                String ca = carpet.getText().toString();
                String re = rent.getText().toString();
                String se = security.getText().toString();
                String com = common.getText().toString();
                String ce = ceiling.getText().toString();
                String fa = facade.getText().toString();
                String tn = tenantname.getText().toString();
                String ff = fdf.getText().toString();
                String fc = fdc.getText().toString();
                String fo = fwo.getText().toString();
                String tf = tdf.getText().toString();
                String tc = tdc.getText().toString();
                String to = two.getText().toString();
                String mo = mobile.getText().toString();
                String sec = secondary.getText().toString();
                String ow = owned.getText().toString();
                String em = email.getText().toString();
                String car = caretaker.getText().toString();
                String cph = caretakerphone.getText().toString();
                String cem = emailcaretaker.getText().toString();
                String rem = remarks.getText().toString();

                String cpro , par;


                if (lo.length() > 0)
                {
                    if (ad.length() > 0)
                    {
                        if (fl.length() > 0)
                        {
                            if (mi.length() > 0)
                            {
                                if (ma.length() > 0)
                                {
                                    if (un.length() > 0)
                                    {
                                        int coid = condition.getCheckedRadioButtonId();
                                        if (coid > -1)
                                        {
                                            RadioButton cb = condition.findViewById(coid);
                                            cpro = cb.getText().toString();

                                            if (ch.length() > 0)
                                            {
                                                if (co.length() > 0)
                                                {
                                                    if (ca.length() > 0)
                                                    {
                                                        int paid = partition.getCheckedRadioButtonId();
                                                        if (paid > -1)
                                                        {
                                                            RadioButton pb = partition.findViewById(paid);
                                                            par = pb.getText().toString();

                                                            if (re.length() > 0)
                                                            {

                                                                if (se.length() > 0)
                                                                {
                                                                    if (com.length() > 0)
                                                                    {
                                                                        if (ce.length() > 0)
                                                                        {
                                                                            if (fa.length() > 0)
                                                                            {

                                                                                // validations done


                                                                            }
                                                                            else
                                                                            {
                                                                                Toast.makeText(Form.this, "Invalid facade length", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            Toast.makeText(Form.this, "Invalid ceiling height", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(Form.this, "Invalid common area maintenance", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(Form.this, "Invalid security deposit", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(Form.this, "Invalid rent", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(Form.this, "Invalid partition lease area", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(Form.this, "Invalid carpet area", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else
                                                {
                                                    Toast.makeText(Form.this, "Invalid covered area", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(Form.this, "Invalid chargeable area", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        else
                                        {
                                            Toast.makeText(Form.this, "Invalid condition property", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(Form.this, "Invalid unit number", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(Form.this, "Invalid max. price", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(Form.this, "Invalid min. price", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(Form.this, "Invalid floor", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Form.this, "Invalid address", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Form.this, "Invalid location", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat,
                        lng), DEFAULT_ZOOM));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(place.getLatLng().latitude,
                                place.getLatLng().longitude), DEFAULT_ZOOM));

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public String getJson()
    {
        String json=null;
        try
        {
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
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

}
