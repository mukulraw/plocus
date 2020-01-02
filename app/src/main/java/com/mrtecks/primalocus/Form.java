package com.mrtecks.primalocus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Form extends AppCompatActivity implements OnMapReadyCallback {
    Toolbar toolbar;
    Spinner property , datasource , state , city , landusage;

    List<String> pro , dat , sta , cit , lan;

    EditText date;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        pro = new ArrayList<>();
        dat = new ArrayList<>();
        sta = new ArrayList<>();
        cit = new ArrayList<>();
        lan = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar2);
        property = findViewById(R.id.property);
        datasource = findViewById(R.id.datasource);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        landusage = findViewById(R.id.landusage);
        date = findViewById(R.id.date);
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

        toolbar.setTitle("Add Property");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pro.add("Warehouse");
        pro.add("Retail");
        pro.add("Office");
        pro.add("Others");

        dat.add("Survey");
        dat.add("Reference");
        dat.add("LL Updated");
        dat.add("Inventory Emailer");

        lan.add("Commercial");
        lan.add("Mix Land");
        lan.add("Institutional/ Industrial");
        lan.add("Residential");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , pro);
        property.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , dat);
        datasource.setAdapter(adapter2);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , lan);
        landusage.setAdapter(adapter5);

        date.setOnClickListener(new View.OnClickListener() {
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

                        date.setText(strDate);

                        dialog.dismiss();

                    }
                });

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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
