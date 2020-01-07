package com.mrtecks.primalocus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mrtecks.primalocus.loginPOJO.loginBean;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Splash extends AppCompatActivity {

    Timer t;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    ProgressBar progress;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress = findViewById(R.id.progressBar);

        if (hasPermissions(this, PERMISSIONS)) {

            startApp();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {

            Log.d("permmm", "1");

            if (hasPermissions(this, PERMISSIONS)) {

                Log.d("permmm", "2");

                startApp();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                ) {

                    Log.d("permmm", "3");

                    Toast.makeText(getApplicationContext(), "Permissions are required for this app", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Log.d("permmm", "4");
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                            .show();
                    finish();
                    //                            //proceed with logic by disabling the related features or quit the app.
                }
            }
        }

    }

    void startApp()
    {

        String id = SharePreferenceUtils.getInstance().getString("id");
        String username = SharePreferenceUtils.getInstance().getString("username");
        String password = SharePreferenceUtils.getInstance().getString("password");

        if (id.length() > 0)
        {

            progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

            Call<loginBean> call = cr.login(username , password , SharePreferenceUtils.getInstance().getString("token"));
            call.enqueue(new Callback<loginBean>() {
                @Override
                public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                    if (response.body().getStatus().equals("1"))
                    {

                        Intent intent = new Intent(Splash.this , MainActivity.class);
                        SharePreferenceUtils.getInstance().saveString("id" , response.body().getData().getId());
                        SharePreferenceUtils.getInstance().saveString("name" , response.body().getData().getName());
                        SharePreferenceUtils.getInstance().saveString("phone" , response.body().getData().getPhone());
                        SharePreferenceUtils.getInstance().saveString("username" , response.body().getData().getUsername());
                        SharePreferenceUtils.getInstance().saveString("password" , response.body().getData().getPassword());

                        startActivity(intent);
                        Toast.makeText(Splash.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        finishAffinity();

                    }
                    else
                    {
                        SharePreferenceUtils.getInstance().deletePref();
                        Intent intent = new Intent(Splash.this , Login.class);
                        Toast.makeText(Splash.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<loginBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });

        }
        else
        {
            t = new Timer();

            t.schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent intent = new Intent(Splash.this , Login.class);
                    startActivity(intent);
                    finish();

                }
            } , 1200);
        }


    }


}
