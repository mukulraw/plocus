package com.mrtecks.primalocus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncorti.slidetoact.SlideToActView;

public class MainActivity extends AppCompatActivity {

    ImageView menu, notification;
    DrawerLayout drawer;
    SlideToActView slide;

    TextView name , name2 , contact , terms , about , logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.imageButton2);
        notification = findViewById(R.id.imageButton);
        drawer = findViewById(R.id.drawer);
        slide = findViewById(R.id.button2);
        contact = findViewById(R.id.contact);
        name = findViewById(R.id.name);
        name2 = findViewById(R.id.textView3);
        terms = findViewById(R.id.terms);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);

        name.setText(SharePreferenceUtils.getInstance().getString("name"));
        name2.setText("Hello " + SharePreferenceUtils.getInstance().getString("name") + " !");

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }

            }
        });

        slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {

                Intent intent = new Intent(MainActivity.this , Survey.class);
                startActivity(intent);
                slideToActView.resetSlider();
                finish();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharePreferenceUtils.getInstance().deletePref();

                Intent intent = new Intent(MainActivity.this , Splash.class);
                startActivity(intent);
                finishAffinity();

            }
        });

    }
}
