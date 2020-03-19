package com.primalocus.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
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
    Spinner datasource, availability, landusage, dgspace, backup;

    List<String> dat, sta, cit, ava, lan, ele;

    Button submit, add , add1 , add2;

    double lat, lng;

    EditText state, city;

    Spinner renttype , commontype , electricitytype;

    GoogleMap mMap;

    TextView change;

    private static final int DEFAULT_ZOOM = 15;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 12;

    String pid, type, date;

    String ds, st, ci, av, lann, elec, dgspa, back;

    ImageView image1;

    TextView minimumtitle, postitle , powertitle , tenantnametitle;

    EditText location, address, min, max, floor, unit, chargeable, covered, carpet, rent, security, common, ceiling, facade, tenantname, landmark, minimum, commonlumpsum, posession, power ,electricity , land;
    EditText fdf, fdc, fwo, tdf, tdc, two, mobile, secondary, owned, email, caretaker, caretakerphone, emailcaretaker, remarks;
    RecyclerView images;
    GridLayoutManager manager;
    RadioGroup condition, partition, tenant;
    ProgressBar progress;

    File f1;
    Uri uri;

    List<MultipartBody.Part> list;
    List<Uri> ulist;

    ImageAdapter adapter;


    File f2;
    Uri uri2;
    ContactAdapter adapter222;

    List<contactBean> lll;

    RecyclerView contacts;

    CheckBox monday , tuesday , wednesday , thursday , friday , saturday , sunday;
    Spinner mondayfrom , mondayto;
    Spinner tuesdayfrom , tuesdayto;
    Spinner wednesdayfrom , wednesdayto;
    Spinner thursdayfrom , thursdayto;
    Spinner fridayfrom , fridayto;
    Spinner saturdayfrom , saturdayto;
    Spinner sundayfrom , sundayto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        list = new ArrayList<>();
        ulist = new ArrayList<>();
        lll = new ArrayList<>();

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
        ele = new ArrayList<>();

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        mondayfrom = findViewById(R.id.mondayfrom);
        mondayto = findViewById(R.id.mondayto);
        tuesdayfrom = findViewById(R.id.tuesdayfrom);
        tuesdayto = findViewById(R.id.tuesdayto);
        wednesdayfrom = findViewById(R.id.wednesdayfrom);
        wednesdayto = findViewById(R.id.wednesdayto);
        thursdayfrom = findViewById(R.id.thursdayfrom);
        thursdayto = findViewById(R.id.thursdayto);
        fridayfrom = findViewById(R.id.fridayfrom);
        fridayto = findViewById(R.id.fridayto);
        saturdayfrom = findViewById(R.id.saturdayfrom);
        saturdayto = findViewById(R.id.saturdayto);
        sundayfrom = findViewById(R.id.sundayfrom);
        sundayto = findViewById(R.id.sundayto);
        electricitytype = findViewById(R.id.electricitytype);
        land = findViewById(R.id.land);

        renttype = findViewById(R.id.renttype);
        commontype = findViewById(R.id.commontype);
        tenantnametitle = findViewById(R.id.tenantnametitle);

        List<String> times = new ArrayList<>();
        times.add("12:00 AM");
        times.add("1:00 AM");
        times.add("2:00 AM");
        times.add("3:00 AM");
        times.add("4:00 AM");
        times.add("5:00 AM");
        times.add("6:00 AM");
        times.add("7:00 AM");
        times.add("8:00 AM");
        times.add("9:00 AM");
        times.add("10:00 AM");
        times.add("11:00 AM");
        times.add("12:00 PM");
        times.add("1:00 PM");
        times.add("2:00 PM");
        times.add("3:00 PM");
        times.add("4:00 PM");
        times.add("5:00 PM");
        times.add("6:00 PM");
        times.add("7:00 PM");
        times.add("8:00 PM");
        times.add("9:00 PM");
        times.add("10:00 PM");
        times.add("11:00 PM");

        ArrayAdapter<String> timeadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, times);
        mondayfrom.setAdapter(timeadapter);
        mondayto.setAdapter(timeadapter);
        tuesdayfrom.setAdapter(timeadapter);
        tuesdayto.setAdapter(timeadapter);
        wednesdayfrom.setAdapter(timeadapter);
        wednesdayto.setAdapter(timeadapter);
        thursdayfrom.setAdapter(timeadapter);
        thursdayto.setAdapter(timeadapter);
        fridayfrom.setAdapter(timeadapter);
        fridayto.setAdapter(timeadapter);
        saturdayfrom.setAdapter(timeadapter);
        saturdayto.setAdapter(timeadapter);
        sundayfrom.setAdapter(timeadapter);
        sundayto.setAdapter(timeadapter);

        toolbar = findViewById(R.id.toolbar2);
        contacts = findViewById(R.id.contacts);
        electricity = findViewById(R.id.electricity);
        power = findViewById(R.id.power);
        add2 = findViewById(R.id.add2);
        add1 = findViewById(R.id.add1);
        image1 = findViewById(R.id.image1);

        dgspace = findViewById(R.id.dgspace);
        backup = findViewById(R.id.backup);
        powertitle = findViewById(R.id.powertitle);
        change = findViewById(R.id.change);
        progress = findViewById(R.id.progressBar);
        commonlumpsum = findViewById(R.id.commonlumpsum);
        posession = findViewById(R.id.posession);
        postitle = findViewById(R.id.postitle);

        datasource = findViewById(R.id.datasource);
        availability = findViewById(R.id.availability);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        landusage = findViewById(R.id.landusage);
        minimumtitle = findViewById(R.id.minimumtitle);
        minimum = findViewById(R.id.minimum);
        add = findViewById(R.id.add);

        location = findViewById(R.id.location);
        address = findViewById(R.id.address);
        min = findViewById(R.id.min);
        max = findViewById(R.id.max);
        floor = findViewById(R.id.floor);
        unit = findViewById(R.id.unit);
        chargeable = findViewById(R.id.chargeable);
        landmark = findViewById(R.id.landmark);
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

        lan.add("Commercial");
        lan.add("Mix Land");
        lan.add("Institutional");
        lan.add("Industrial");
        lan.add("Residential");
        lan.add("Lal Dora");
        lan.add("Others");

        ava.add("Ready to move in (RTM)");
        ava.add("Under Construction");
        ava.add("Built to Suit (BTS)");

        ele.add("Yes");
        ele.add("Not Available");

        adapter222 = new ContactAdapter(this , lll);
        GridLayoutManager manager1 = new GridLayoutManager(this , 1);
        contacts.setAdapter(adapter222);
        contacts.setLayoutManager(manager1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dat);
        datasource.setAdapter(adapter2);

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ava);
        availability.setAdapter(adapter1);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lan);
        landusage.setAdapter(adapter5);



        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ele);
        dgspace.setAdapter(adapter7);


        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ele);
        backup.setAdapter(adapter8);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Form.this);
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

                            uri = FileProvider.getUriForFile(Objects.requireNonNull(Form.this), BuildConfig.APPLICATION_ID + ".provider", f1);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 1);
                            dialog.dismiss();
                        } else if (items[item].equals("Choose from Gallery")) {


                            Intent intent = new Intent(Form.this, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
                            intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 5);
                            startActivityForResult(intent, 2);

                            /*Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                            dialog.dismiss();*/



                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


            }
        });

        covered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {

                    float act = Float.parseFloat(s.toString());
                    float tp = act / 10;
                    carpet.setText(String.valueOf(act - tp));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME , Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountries(Collections.singletonList("IN"))
                        .build(Form.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Form.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_contact_dialog);
                dialog.show();

                Spinner role = dialog.findViewById(R.id.role);
                final EditText name = dialog.findViewById(R.id.name);
                final EditText mobile = dialog.findViewById(R.id.mobile);
                final EditText landline = dialog.findViewById(R.id.landline);
                final EditText email = dialog.findViewById(R.id.email);
                Button addd = dialog.findViewById(R.id.button);

                final String[] rol = new String[1];

                final List<String> ll = new ArrayList<>();

                ll.add("Landlord");
                ll.add("Caretaker/ Security Guard");
                ll.add("Leasing Manager");
                ll.add("Others");


                ArrayAdapter<String> adapter21 = new ArrayAdapter<String>(Form.this,
                        android.R.layout.simple_list_item_1, ll);
                role.setAdapter(adapter21);

                role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        rol[0] = ll.get(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                addd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String n = name.getText().toString();
                        String p = mobile.getText().toString();

                        if (n.length() > 0)
                        {

                            if (mobile.length() == 10)
                            {
                                contactBean item = new contactBean();
                                item.setRole(rol[0]);
                                item.setName(n);
                                item.setMobile(mobile.getText().toString());
                                item.setLandline(landline.getText().toString());
                                item.setEmail(email.getText().toString());

                                adapter222.addData(item);

                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(Form.this, "Invalid mobile", Toast.LENGTH_SHORT).show();
                            }




                        }
                        else
                        {
                            Toast.makeText(Form.this, "Invalid name", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Form.this);
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


                            f2 = new File(file);
                            try {
                                f2.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            uri2 = FileProvider.getUriForFile(Objects.requireNonNull(Form.this), BuildConfig.APPLICATION_ID + ".provider", f2);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 3);
                            dialog.dismiss();
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 4);
                            dialog.dismiss();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


            }
        });





        dgspace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dgspa = ele.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        backup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                {
                    power.setText("");
                    power.setVisibility(View.VISIBLE);
                    powertitle.setVisibility(View.VISIBLE);
                }
                else
                {
                    power.setText("-");
                    power.setVisibility(View.GONE);
                    powertitle.setVisibility(View.GONE);
                }

                back = ele.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        partition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.yes) {
                    minimum.setVisibility(View.VISIBLE);
                    minimumtitle.setVisibility(View.VISIBLE);
                    minimum.setText("");
                } else {
                    minimum.setVisibility(View.GONE);
                    minimumtitle.setVisibility(View.GONE);
                    minimum.setText("-");
                }

            }
        });

        tenant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.occupied) {
                    tenantnametitle.setVisibility(View.VISIBLE);
                    tenantname.setVisibility(View.VISIBLE);
                    tenantname.setText("");
                } else {
                    tenantnametitle.setVisibility(View.GONE);
                    tenantname.setVisibility(View.GONE);
                    tenantname.setText("-");
                }

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String poss = posession.getText().toString();
                final String lo = location.getText().toString();
                final String la = landmark.getText().toString();
                final String ad = address.getText().toString();
                final String fl = floor.getText().toString();
                String mi = min.getText().toString();
                final String ma = max.getText().toString();
                final String un = unit.getText().toString();
                final String ch = chargeable.getText().toString();
                final String co = covered.getText().toString();
                final String ca = carpet.getText().toString();
                final String re = rent.getText().toString();
                final String se = security.getText().toString();
                String com = common.getText().toString();
                final String lcom = commonlumpsum.getText().toString();
                final String ce = ceiling.getText().toString();
                final String fa = facade.getText().toString();
                final String tn = tenantname.getText().toString();
                final String ff = fdf.getText().toString();
                final String fc = fdc.getText().toString();
                final String fo = fwo.getText().toString();
                final String tf = tdf.getText().toString();
                final String tc = tdc.getText().toString();
                final String to = two.getText().toString();
                final String mo = mobile.getText().toString();
                final String sec = secondary.getText().toString();
                final String ow = owned.getText().toString();
                final String em = email.getText().toString();
                final String car = caretaker.getText().toString();
                final String cph = caretakerphone.getText().toString();
                final String cem = emailcaretaker.getText().toString();
                final String rem = remarks.getText().toString();
                final String mini = minimum.getText().toString();
                //final String oper = opearational.getText().toString();

                elec = electricity.getText().toString();

                mi = mi + " " + renttype.getSelectedItem().toString();

                com = com + " " + commontype.getSelectedItem().toString();

                elec = elec + " " + electricitytype.getSelectedItem().toString();

                if (!lann.equals("Others"))
                {
                    lann = land.getText().toString();
                }

                final String cpro;
                final String par;
                String ten;

                RadioButton tb = tenant.findViewById(tenant.getCheckedRadioButtonId());
                ten = tb.getText().toString();

                if (ten.equals("Occupied"))
                {
                    ten = tenantname.getText().toString();
                }

                RadioButton pb = partition.findViewById(partition.getCheckedRadioButtonId());
                par = pb.getText().toString();

                RadioButton cb = condition.findViewById(condition.getCheckedRadioButtonId());
                cpro = cb.getText().toString();

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

                final String finalMi = mi;
                final String finalCom = com;
                final String finalTen = ten;
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                        List<hoursBean> hours = new ArrayList<>();

                        if (monday.isChecked())
                        {
                            hoursBean item = new hoursBean();
                            item.setDay(monday.getText().toString());
                            item.setFrom(mondayfrom.getSelectedItem().toString());
                            item.setTo(mondayto.getSelectedItem().toString());

                            hours.add(item);
                        }

                        if (tuesday.isChecked())
                        {
                            hoursBean item = new hoursBean();
                            item.setDay(tuesday.getText().toString());
                            item.setFrom(tuesdayfrom.getSelectedItem().toString());
                            item.setTo(tuesdayto.getSelectedItem().toString());

                            hours.add(item);
                        }

                        if (wednesday.isChecked())
                        {
                            hoursBean item = new hoursBean();
                            item.setDay(wednesday.getText().toString());
                            item.setFrom(wednesdayfrom.getSelectedItem().toString());
                            item.setTo(wednesdayto.getSelectedItem().toString());

                            hours.add(item);
                        }

                        if (thursday.isChecked())
                        {
                            hoursBean item = new hoursBean();
                            item.setDay(thursday.getText().toString());
                            item.setFrom(thursdayfrom.getSelectedItem().toString());
                            item.setTo(thursdayto.getSelectedItem().toString());

                            hours.add(item);
                        }

                        if (friday.isChecked())
                        {
                            hoursBean item = new hoursBean();
                            item.setDay(friday.getText().toString());
                            item.setFrom(fridayfrom.getSelectedItem().toString());
                            item.setTo(fridayto.getSelectedItem().toString());

                            hours.add(item);
                        }

                        if (saturday.isChecked())
                        {
                            hoursBean item = new hoursBean();
                            item.setDay(saturday.getText().toString());
                            item.setFrom(saturdayfrom.getSelectedItem().toString());
                            item.setTo(saturdayto.getSelectedItem().toString());

                            hours.add(item);
                        }

                        if (sunday.isChecked())
                        {
                            hoursBean item = new hoursBean();
                            item.setDay(sunday.getText().toString());
                            item.setFrom(sundayfrom.getSelectedItem().toString());
                            item.setTo(sundayto.getSelectedItem().toString());

                            hours.add(item);
                        }


                        List<contactBean> reqlist = adapter222.getList();

                        Gson gson = new Gson();
                        String json = gson.toJson(reqlist);

                        String json2 = gson.toJson(hours);

                        Log.d("reqlist", json2);



                        MultipartBody.Part body2 = null;

                        try {

                            RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f2);
                            body2 = MultipartBody.Part.createFormData("featured", f2.getName(), reqFile1);


                            adapter.addData(body2, uri2);


                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<loginBean> call = cr.add_retail(
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
                                la,
                                ad,
                                finalMi,
                                ma,
                                av,
                                poss,
                                fl,
                                un,
                                cpro,
                                ch,
                                co,
                                ca,
                                par,
                                mini,
                                re,
                                se,
                                finalCom,
                                lcom,
                                ce,
                                fa,
                                elec,
                                dgspa,
                                back,
                                power.getText().toString(),
                                json2,
                                finalTen,
                                tn,
                                lann,
                                ff,
                                fc,
                                fo,
                                tf,
                                tc,
                                to,
                                mo,
                                sec,
                                ow,
                                em,
                                car,
                                cph,
                                cem,
                                rem,
                                json,
                                body2,
                                adapter.getList()
                        );

                        call.enqueue(new Callback<loginBean>() {
                            @Override
                            public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                if (response.body().getStatus().equals("1")) {
                                    Intent intent = new Intent(Form.this, Survey.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }

                                Toast.makeText(Form.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                                progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<loginBean> call, Throwable t) {

                                progress.setVisibility(View.GONE);

                            }
                        });

                    }
                });

               /* if (lo.length() > 0) {

                    if (la.length() > 0) {
                        if (ad.length() > 0) {
                            if (fl.length() > 0) {
                                if (mi.length() > 0) {
                                    if (ma.length() > 0) {
                                        if (un.length() > 0) {
                                            int coid = condition.getCheckedRadioButtonId();
                                            if (coid > -1) {
                                                RadioButton cb = condition.findViewById(coid);
                                                cpro = cb.getText().toString();

                                                if (ch.length() > 0) {
                                                    if (co.length() > 0) {
                                                        if (ca.length() > 0) {
                                                            int paid = partition.getCheckedRadioButtonId();
                                                            if (paid > -1) {

                                                                if (mini.length() > 0) {
                                                                    RadioButton pb = partition.findViewById(paid);
                                                                    par = pb.getText().toString();

                                                                    if (re.length() > 0) {

                                                                        if (se.length() > 0) {
                                                                            if (com.length() > 0) {

                                                                                if (lcom.length() > 0) {
                                                                                    if (ce.length() > 0) {
                                                                                        if (fa.length() > 0) {

                                                                                            RadioButton tb = tenant.findViewById(tenant.getCheckedRadioButtonId());
                                                                                            ten = tb.getText().toString();

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

                                                                                                    List<contactBean> reqlist = adapter222.getList();

                                                                                                    Gson gson = new Gson();
                                                                                                    String json = gson.toJson(reqlist);

                                                                                                    Log.d("reqlist", json);

                                                                                                    MultipartBody.Part body2 = null;

                                                                                                    try {

                                                                                                        RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f2);
                                                                                                        body2 = MultipartBody.Part.createFormData("featured", f2.getName(), reqFile1);


                                                                                                        adapter.addData(body2, uri2);


                                                                                                    } catch (Exception e1) {
                                                                                                        e1.printStackTrace();
                                                                                                    }

                                                                                                    progress.setVisibility(View.VISIBLE);

                                                                                                    Bean b = (Bean) getApplicationContext();

                                                                                                    Retrofit retrofit = new Retrofit.Builder()
                                                                                                            .baseUrl(b.baseurl)
                                                                                                            .addConverterFactory(ScalarsConverterFactory.create())
                                                                                                            .addConverterFactory(GsonConverterFactory.create())
                                                                                                            .build();

                                                                                                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                                                                    Call<loginBean> call = cr.add_retail(
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
                                                                                                            la,
                                                                                                            ad,
                                                                                                            mi,
                                                                                                            ma,
                                                                                                            av,
                                                                                                            poss,
                                                                                                            fl,
                                                                                                            un,
                                                                                                            cpro,
                                                                                                            ch,
                                                                                                            co,
                                                                                                            ca,
                                                                                                            par,
                                                                                                            mini,
                                                                                                            re,
                                                                                                            se,
                                                                                                            com,
                                                                                                            lcom,
                                                                                                            ce,
                                                                                                            fa,
                                                                                                            elec,
                                                                                                            dgspa,
                                                                                                            back,
                                                                                                            power.getText().toString(),
                                                                                                            oper,
                                                                                                            ten,
                                                                                                            tn,
                                                                                                            la,
                                                                                                            ff,
                                                                                                            fc,
                                                                                                            fo,
                                                                                                            tf,
                                                                                                            tc,
                                                                                                            to,
                                                                                                            mo,
                                                                                                            sec,
                                                                                                            ow,
                                                                                                            em,
                                                                                                            car,
                                                                                                            cph,
                                                                                                            cem,
                                                                                                            rem,
                                                                                                            json,
                                                                                                            body2,
                                                                                                            adapter.getList()
                                                                                                    );

                                                                                                    call.enqueue(new Callback<loginBean>() {
                                                                                                        @Override
                                                                                                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                                                                                            if (response.body().getStatus().equals("1")) {
                                                                                                                Intent intent = new Intent(Form.this, Survey.class);
                                                                                                                startActivity(intent);
                                                                                                                finishAffinity();
                                                                                                            }

                                                                                                            Toast.makeText(Form.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


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
                                                                                            Toast.makeText(Form.this, "Invalid facade length", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    } else {
                                                                                        Toast.makeText(Form.this, "Invalid ceiling height", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                } else {
                                                                                    Toast.makeText(Form.this, "Invalid lumpsum CAM/ month", Toast.LENGTH_SHORT).show();
                                                                                }

                                                                            } else {
                                                                                Toast.makeText(Form.this, "Invalid common area maintenance", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
                                                                            Toast.makeText(Form.this, "Invalid security deposit", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    } else {
                                                                        Toast.makeText(Form.this, "Invalid rent", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(Form.this, "Invalid minimum partition lease area", Toast.LENGTH_SHORT).show();
                                                                }


                                                            } else {
                                                                Toast.makeText(Form.this, "Invalid partition lease area", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(Form.this, "Invalid carpet area", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(Form.this, "Invalid covered area", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(Form.this, "Invalid chargeable area", Toast.LENGTH_SHORT).show();
                                                }

                                            } else {
                                                Toast.makeText(Form.this, "Invalid condition property", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(Form.this, "Invalid unit number", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Form.this, "Invalid lumpsum amount", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Form.this, "Invalid rent/ sqft.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Form.this, "Invalid floor", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Form.this, "Invalid address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Form.this, "Invalid landmark", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(Form.this, "Invalid location", Toast.LENGTH_SHORT).show();
                }*/


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


        posession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Form.this);
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
                    posession.setText("");
                    posession.setVisibility(View.VISIBLE);
                    postitle.setVisibility(View.VISIBLE);
                } else {
                    posession.setText("-");
                    posession.setVisibility(View.GONE);
                    postitle.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        landusage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 6)
                {
                    lann = "";
                    land.setVisibility(View.VISIBLE);
                }
                else
                {
                    lann = lan.get(position);
                    land.setVisibility(View.GONE);
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
                try
                {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
                    String cii = addresses.get(0).getLocality();
                    String stat = addresses.get(0).getAdminArea();

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
            if (resultCode == Activity.RESULT_OK && data != null) {

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

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {


            ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {

                String ypath = images.get(i).path;
                assert ypath != null;
                f1 = new File(ypath);

                uri = Uri.fromFile(f1);

                Log.d("path", ypath);
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


        if (requestCode == 4 && resultCode == RESULT_OK && null != data) {
            uri2 = data.getData();

            Log.d("uri1", String.valueOf(uri));

            String ypath = getPath(Form.this, uri2);
            assert ypath != null;
            f2 = new File(ypath);

            Log.d("path1", ypath);

            image1.setImageURI(uri2);



        } else if (requestCode == 3 && resultCode == RESULT_OK) {

            Log.d("uri1", String.valueOf(uri2));

            image1.setImageURI(uri2);


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

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
        Context context;
        List<contactBean> list = new ArrayList<>();

        ContactAdapter(Context context, List<contactBean> list) {
            this.context = context;
            this.list = list;
        }

        void addData(contactBean item) {
            list.add(item);
            notifyDataSetChanged();
        }

        void removeData(int pos) {
            list.remove(pos);
            notifyDataSetChanged();
        }

        List<contactBean> getList() {
            return list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.contact_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            contactBean item = list.get(position);

            holder.role.setText(item.getRole());
            holder.name.setText(item.getName());
            holder.mobile.setText(item.getMobile());
            holder.landline.setText(item.getLandline());
            holder.email.setText(item.getEmail());

            holder.delete.setOnClickListener(new View.OnClickListener() {
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
            TextView role , name , mobile , landline , email;
            ImageButton delete;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                role = itemView.findViewById(R.id.textView10);
                name = itemView.findViewById(R.id.textView15);
                mobile = itemView.findViewById(R.id.textView16);
                landline = itemView.findViewById(R.id.textView17);
                email = itemView.findViewById(R.id.textView18);
                delete = itemView.findViewById(R.id.imageButton5);
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
