package com.primalocus.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.primalocus.app.getworkPOJO.Datum;
import com.primalocus.app.getworkPOJO.getWorkBean;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView menu, notification;
    DrawerLayout drawer;
    SlideToActView slide;
    RecyclerView grid;
    GridLayoutManager manager;
    TextView name , name2 , contact , terms , about , logout;
    List<Datum> list;
    GridAdapter adapter;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

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
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar);
        manager = new GridLayoutManager(this , 1);
        adapter = new GridAdapter(this , list);
        name.setText(SharePreferenceUtils.getInstance().getString("name"));
        name2.setText("Hello " + SharePreferenceUtils.getInstance().getString("name") + " !");

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

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

                SharePreferenceUtils.getInstance().saveString("mode" , "1");

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

    class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>
    {
        Context context;
        List<Datum> list;

        GridAdapter(Context context, List<Datum> list)
        {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Datum> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.work_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Datum item = list.get(position);
            holder.check.setText(item.getWork());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            CheckBox check;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                check = itemView.findViewById(R.id.checkBox);

            }
        }
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

        Call<getWorkBean> call = cr.getWork(SharePreferenceUtils.getInstance().getString("id"));
        call.enqueue(new Callback<getWorkBean>() {
            @Override
            public void onResponse(Call<getWorkBean> call, Response<getWorkBean> response) {

                if (response.body().getStatus().equals("1"))
                {
                    adapter.setData(response.body().getData());
                }
                else
                {
                    adapter.setData(response.body().getData());
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<getWorkBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }

}
