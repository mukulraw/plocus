package com.mrtecks.primalocus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mrtecks.primalocus.loginPOJO.loginBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Warehouse extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Form";
    Toolbar toolbar;
    Spinner datasource, state, city, availability, landusage, posession, under_construction, warehouse, construction, plinth, firenoc, safety, ventilation, insulation, leveler, agreement, flooring;
    List<String> dat, sta, cit, ava, lan, pos, und, war, con, pli, fir, saf, ven, ins, lev, agr, flo;
    Button submit, add;

    double lat, lng;

    GoogleMap mMap;

    TextView change;

    private static final int DEFAULT_ZOOM = 15;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 12;

    String pid, type, date;

    String ds, st, ci, avai, lann, poss, unde, ware, cond, plin, fire, safe, vent, insu, leve, aggr, floo;
    EditText location, address, min, max, plot, covered, available, rent, security, common, eaves, center_height, opening_docks, tenantname;
    EditText fwh, large, mobile, secondary, owned, email, caretaker, caretakerphone, emailcaretaker, remarks;
    RecyclerView images;
    GridLayoutManager manager;
    RadioGroup plan, partition, tenant;
    ProgressBar progress;

    File f1;
    Uri uri;

    List<MultipartBody.Part> list;
    List<Uri> ulist;

    ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        list = new ArrayList<>();
        ulist = new ArrayList<>();

        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        pid = getIntent().getStringExtra("pid");
        type = getIntent().getStringExtra("type");
        date = getIntent().getStringExtra("date");


        dat = new ArrayList<>();
        sta = new ArrayList<>();
        cit = new ArrayList<>();
        ava = new ArrayList<>();
        lan = new ArrayList<>();
        pos = new ArrayList<>();
        und = new ArrayList<>();
        war = new ArrayList<>();
        con = new ArrayList<>();
        pli = new ArrayList<>();
        fir = new ArrayList<>();
        saf = new ArrayList<>();
        ven = new ArrayList<>();
        ins = new ArrayList<>();
        lev = new ArrayList<>();
        agr = new ArrayList<>();
        flo = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar2);
        change = findViewById(R.id.change);
        progress = findViewById(R.id.progressBar);

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
        posession = findViewById(R.id.posession);
        under_construction = findViewById(R.id.under_construction);
        warehouse = findViewById(R.id.warehouse);
        covered = findViewById(R.id.covered);
        construction = findViewById(R.id.construction);
        rent = findViewById(R.id.rent);
        security = findViewById(R.id.security);
        common = findViewById(R.id.common);
        plinth = findViewById(R.id.plinth);
        firenoc = findViewById(R.id.firenoc);
        tenantname = findViewById(R.id.tenantname);

        safety = findViewById(R.id.safety);
        ventilation = findViewById(R.id.ventilation);
        insulation = findViewById(R.id.insulation);
        leveler = findViewById(R.id.leveler);
        agreement = findViewById(R.id.agreement);
        flooring = findViewById(R.id.flooring);
        mobile = findViewById(R.id.mobile);
        secondary = findViewById(R.id.secondary);
        owned = findViewById(R.id.owned);
        email = findViewById(R.id.email);
        caretaker = findViewById(R.id.caretaker);
        caretakerphone = findViewById(R.id.caretakerphone);
        emailcaretaker = findViewById(R.id.emailcaretaker);
        remarks = findViewById(R.id.remarks);

        images = findViewById(R.id.images);
        plot = findViewById(R.id.plot);
        partition = findViewById(R.id.partition);
        tenant = findViewById(R.id.tenant);
        available = findViewById(R.id.available);
        eaves = findViewById(R.id.eaves);
        center_height = findViewById(R.id.center_height);
        opening_docks = findViewById(R.id.opening_docks);
        fwh = findViewById(R.id.fwh);
        large = findViewById(R.id.large);
        plan = findViewById(R.id.plan);

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

        adapter = new ImageAdapter(this, list);
        manager = new GridLayoutManager(this, 3);
        images.setAdapter(adapter);
        images.setLayoutManager(manager);

        dat.add("Survey");
        dat.add("Reference");
        dat.add("LL Updated");
        dat.add("Inventory Emailer");

        lan.add("Warehousing /Industrial");
        lan.add("Agricultural/Lal Dora");

        ava.add("Ready to move in (RTM)");
        ava.add("Under Construction");
        ava.add("Built to Suit (BTS)");

        pos.add("-0 to 2 months");
        pos.add("-2 to 4 months");
        pos.add("-4 to 6 months");
        pos.add(">6 months");

        und.add("-0 to 2 months");
        und.add("-2 to 4 months");
        und.add("-4 to 6 months");
        und.add(">6 months");

        war.add("stand-alone");
        war.add("campus/cluster");

        con.add("PEB Shed");
        con.add("Old Shed (Asbestos)");
        con.add("RCC");
        con.add("RCC & Old Shed Combined");

        pli.add("Ground");
        pli.add("1 ft.");
        pli.add("2, 3 ft.");
        pli.add("4 ft.");

        fir.add("Available");
        fir.add("Not Available");
        fir.add("Currently not available but agreeable be procure.");

        saf.add("Fire Sprinkler + Hydrant");
        saf.add("Only Fire Hydrant");

        ven.add("Yes");
        ven.add("No");

        ins.add("Cladding");
        ins.add("Roof");
        ins.add("Both Cladding and Roof");

        lev.add("Available");
        lev.add("Not Available");

        agr.add("Yes");
        agr.add("No");

        flo.add("Industrial");
        flo.add("VDF");
        flo.add("FM2");


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dat);
        datasource.setAdapter(adapter2);

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ava);
        availability.setAdapter(adapter1);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lan);
        landusage.setAdapter(adapter5);

        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, pos);
        posession.setAdapter(adapter6);

        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, und);
        under_construction.setAdapter(adapter7);

        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, war);
        warehouse.setAdapter(adapter8);

        ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, con);
        construction.setAdapter(adapter9);

        ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, pli);
        plinth.setAdapter(adapter10);

        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fir);
        firenoc.setAdapter(adapter11);

        ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, saf);
        safety.setAdapter(adapter12);

        ArrayAdapter<String> adapter13 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ven);
        ventilation.setAdapter(adapter13);

        ArrayAdapter<String> adapter14 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ins);
        insulation.setAdapter(adapter14);

        ArrayAdapter<String> adapter15 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lev);
        leveler.setAdapter(adapter15);

        ArrayAdapter<String> adapter16 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, agr);
        agreement.setAdapter(adapter16);

        ArrayAdapter<String> adapter17 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, flo);
        flooring.setAdapter(adapter17);


        try {

            JSONObject jsonObject = new JSONObject(getJson());
            JSONArray array = jsonObject.getJSONArray("array");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String state = object.getString("state");
                sta.add(state);
            }

            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Warehouse.this,
                    android.R.layout.simple_list_item_1, sta);
            state.setAdapter(adapter3);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Warehouse.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
                            File newdir = new File(dir);
                            try {
                                newdir.mkdirs();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";


                            f1 = new File(file);
                            try {
                                f1.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            uri = FileProvider.getUriForFile(Objects.requireNonNull(Warehouse.this), BuildConfig.APPLICATION_ID + ".provider", f1);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 1);
                            dialog.dismiss();
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                            dialog.dismiss();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


            }
        });


        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                st = sta.get(position);

                Log.d("state", st);

                try {

                    cit.clear();

                    JSONObject jsonObject = new JSONObject(getJson());
                    JSONArray array = jsonObject.getJSONArray("array");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String state = object.getString("state");

                        if (st.equals(state)) {
                            String name = object.getString("name");
                            cit.add(name);
                        }


                    }

                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(Warehouse.this,
                            android.R.layout.simple_list_item_1, cit);
                    city.setAdapter(adapter4);

                } catch (JSONException e) {
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


                final String lo = location.getText().toString();
                final String ad = address.getText().toString();
                final String mi = min.getText().toString();
                final String ma = max.getText().toString();
                final String pl = plot.getText().toString();
                final String co = covered.getText().toString();
                final String av = available.getText().toString();
                final String re = rent.getText().toString();
                final String se = security.getText().toString();
                final String com = common.getText().toString();
                final String ea = eaves.getText().toString();
                final String ce = center_height.getText().toString();
                final String op = opening_docks.getText().toString();
                final String tn = tenantname.getText().toString();
                final String fw = fwh.getText().toString();
                final String la = large.getText().toString();
                final String mo = mobile.getText().toString();
                final String sec = secondary.getText().toString();
                final String ow = owned.getText().toString();
                final String em = email.getText().toString();
                final String car = caretaker.getText().toString();
                final String cph = caretakerphone.getText().toString();
                final String cem = emailcaretaker.getText().toString();
                final String rem = remarks.getText().toString();


                final String par, ten , pla;


                if (lo.length() > 0) {
                    if (ad.length() > 0) {
                        if (mi.length() > 0) {
                            if (ma.length() > 0) {
                                if (pl.length() > 0) {
                                    if (co.length() > 0) {
                                        if (av.length() > 0) {
                                            if (re.length() > 0) {
                                                if (se.length() > 0) {
                                                    if (com.length() > 0) {
                                                        if (ea.length() > 0) {
                                                            if (ce.length() > 0) {
                                                                if (op.length() > 0) {

                                                                    RadioButton tb = tenant.findViewById(tenant.getCheckedRadioButtonId());
                                                                    ten = tb.getText().toString();

                                                                    RadioButton tb1 = partition.findViewById(partition.getCheckedRadioButtonId());
                                                                    par = tb1.getText().toString();

                                                                    RadioButton tb2 = plan.findViewById(plan.getCheckedRadioButtonId());
                                                                    pla = tb2.getText().toString();

                                                                    final Dialog dialog = new Dialog(Warehouse.this);
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

                                                                            progress.setVisibility(View.VISIBLE);

                                                                            Bean b = (Bean) getApplicationContext();

                                                                            Retrofit retrofit = new Retrofit.Builder()
                                                                                    .baseUrl(b.baseurl)
                                                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                                                    .addConverterFactory(GsonConverterFactory.create())
                                                                                    .build();

                                                                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                                            Call<loginBean> call = cr.add_warehouse(
                                                                                    SharePreferenceUtils.getInstance().getString("id"),
                                                                                    type,
                                                                                    date,
                                                                                    pid,
                                                                                    String.valueOf(lat),
                                                                                    String.valueOf(lng),
                                                                                    ds,
                                                                                    st,
                                                                                    ci,
                                                                                    lo,
                                                                                    ad,
                                                                                    avai,
                                                                                    poss,
                                                                                    unde,
                                                                                    ware,
                                                                                    cond,
                                                                                    mi,
                                                                                    ma,
                                                                                    pl,
                                                                                    co,
                                                                                    av,
                                                                                    par,
                                                                                    re,
                                                                                    se,
                                                                                    com,
                                                                                    ea,
                                                                                    ce,
                                                                                    op,
                                                                                    plin,
                                                                                    pla,
                                                                                    fire,
                                                                                    safe,
                                                                                    vent,
                                                                                    insu,
                                                                                    leve,
                                                                                    ten,
                                                                                    tn,
                                                                                    lann,
                                                                                    aggr,
                                                                                    floo,
                                                                                    fw,
                                                                                    la,
                                                                                    mo,
                                                                                    sec,
                                                                                    ow,
                                                                                    em,
                                                                                    car,
                                                                                    cph,
                                                                                    cem,
                                                                                    rem,
                                                                                    adapter.getList()
                                                                            );

                                                                            call.enqueue(new Callback<loginBean>() {
                                                                                @Override
                                                                                public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                                                                    if (response.body().getStatus().equals("1")) {
                                                                                        Intent intent = new Intent(Warehouse.this, Survey.class);
                                                                                        startActivity(intent);
                                                                                        finishAffinity();
                                                                                    }

                                                                                    Toast.makeText(Warehouse.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                                                                                    progress.setVisibility(View.GONE);
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<loginBean> call, Throwable t) {

                                                                                    progress.setVisibility(View.GONE);

                                                                                }
                                                                            });

                                                                        }
                                                                    });

                                                                    // validations done

                                                                } else {
                                                                    Toast.makeText(Warehouse.this, "Invalid no. of opening/ docks", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(Warehouse.this, "Invalid centre height", Toast.LENGTH_SHORT).show();
                                                            }

                                                        } else {
                                                            Toast.makeText(Warehouse.this, "Invalid eaves height", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } else {
                                                        Toast.makeText(Warehouse.this, "Invalid common area maintenance", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(Warehouse.this, "Invalid security deposit", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(Warehouse.this, "Invalid rent", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(Warehouse.this, "Invalid available area", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(Warehouse.this, "Invalid covered area", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Warehouse.this, "Invalid plot area", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Warehouse.this, "Invalid max. price", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Warehouse.this, "Invalid min. price", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Warehouse.this, "Invalid address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Warehouse.this, "Invalid location", Toast.LENGTH_SHORT).show();
                }


            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(Warehouse.this);
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

                avai = ava.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        landusage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lann = lan.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        posession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                poss = pos.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        under_construction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                unde = und.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        warehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ware = war.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        construction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cond = con.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        plinth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                plin = pli.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        firenoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                fire = fir.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        safety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                safe = saf.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ventilation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                vent = ven.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        insulation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                insu = ins.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        leveler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                leve = lev.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        agreement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                aggr = agr.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        flooring.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                floo = flo.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            uri = data.getData();

            Log.d("uri", String.valueOf(uri));

            String ypath = getPath(Warehouse.this, uri);
            assert ypath != null;
            f1 = new File(ypath);

            Log.d("path", ypath);

            MultipartBody.Part body = null;

            try {

                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("file[]", f1.getName(), reqFile1);


                adapter.addData(body, uri);


            } catch (Exception e1) {
                e1.printStackTrace();
            }


        } else if (requestCode == 1 && resultCode == RESULT_OK) {

            Log.d("uri", String.valueOf(uri));

            MultipartBody.Part body = null;

            try {

                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("file[]", f1.getName(), reqFile1);

                adapter.addData(body, uri);

            } catch (Exception e1) {
                e1.printStackTrace();
            }


        }


    }

    private static Bitmap decodeUriToBitmap(Context mContext, Uri sendUri) {
        Bitmap getBitmap = null;
        try {
            InputStream image_stream;
            try {
                image_stream = mContext.getContentResolver().openInputStream(sendUri);
                getBitmap = BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBitmap;
    }


    private static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {
                column
        };
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        return null;
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        Context context;
        List<MultipartBody.Part> list = new ArrayList<>();
        List<Uri> ulist = new ArrayList<>();

        ImageAdapter(Context context, List<MultipartBody.Part> list) {
            this.context = context;
            this.list = list;
        }

        void addData(MultipartBody.Part item, Uri uri) {
            list.add(item);
            ulist.add(uri);
            notifyDataSetChanged();
        }

        void removeData(int pos) {
            list.remove(pos);
            ulist.remove(pos);
            notifyDataSetChanged();
        }

        List<MultipartBody.Part> getList() {
            return list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.image_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            Uri item = ulist.get(position);

            holder.image.setImageURI(item);

            holder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeData(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            ImageButton close;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                close = itemView.findViewById(R.id.close);
            }
        }
    }

}
