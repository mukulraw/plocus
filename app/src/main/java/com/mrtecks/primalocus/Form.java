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
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.mbms.MbmsErrors;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.mrtecks.primalocus.loginPOJO.loginBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    GridLayoutManager manager;
    RadioGroup condition , partition , tenant;
    ProgressBar progress;

    File f1;
    Uri uri;

    List<MultipartBody.Part> list;
    List<Uri> ulist;

    ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        list = new ArrayList<>();
        ulist = new ArrayList<>();

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

        adapter = new ImageAdapter(this , list);
        manager = new GridLayoutManager(this , 3);
        images.setAdapter(adapter);
        images.setLayoutManager(manager);

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

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
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


                final String lo = location.getText().toString();
                final String ad = address.getText().toString();
                final String fl = floor.getText().toString();
                final String mi = min.getText().toString();
                final String ma = max.getText().toString();
                final String un = unit.getText().toString();
                final String ch = chargeable.getText().toString();
                final String co = covered.getText().toString();
                final String ca = carpet.getText().toString();
                final String re = rent.getText().toString();
                final String se = security.getText().toString();
                final String com = common.getText().toString();
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

                final String cpro , par , ten;


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
                                                                                                ad,
                                                                                                mi,
                                                                                                ma,
                                                                                                av,
                                                                                                fl,
                                                                                                un,
                                                                                                cpro,
                                                                                                ch,
                                                                                                co,
                                                                                                ca,
                                                                                                par,
                                                                                                re,
                                                                                                se,
                                                                                                com,
                                                                                                ce,
                                                                                                fa,
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
                                                                                                adapter.getList()
                                                                                        );

                                                                                        call.enqueue(new Callback<loginBean>() {
                                                                                            @Override
                                                                                            public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                                                                                if (response.body().getStatus().equals("1"))
                                                                                                {
                                                                                                    Intent intent = new Intent(Form.this , Survey.class);
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





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat,
                        lng), DEFAULT_ZOOM));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            uri = data.getData();

            Log.d("uri" , String.valueOf(uri));

            String ypath = getPath(Form.this , uri);
            assert ypath != null;
            f1 = new File(ypath);

            Log.d("path" , ypath);

            MultipartBody.Part body = null;

            try {

                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("file[]", f1.getName(), reqFile1);


                adapter.addData(body , uri);


            } catch (Exception e1) {
                e1.printStackTrace();
            }





        } else if (requestCode == 1 && resultCode == RESULT_OK) {

            Log.d("uri" , String.valueOf(uri));

            MultipartBody.Part body = null;

            try {

                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("file[]", f1.getName(), reqFile1);

                adapter.addData(body , uri);

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

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
    {
        Context context;
        List<MultipartBody.Part> list = new ArrayList<>();
        List<Uri> ulist = new ArrayList<>();

        ImageAdapter(Context context, List<MultipartBody.Part> list)
        {
            this.context = context;
            this.list = list;
        }

        void addData(MultipartBody.Part item , Uri uri)
        {
            list.add(item);
            ulist.add(uri);
            notifyDataSetChanged();
        }

        void removeData(int pos)
        {
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
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.image_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Uri item = ulist.get(position);

            holder.image.setImageURI(item);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            ImageView image;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
            }
        }
    }

}
