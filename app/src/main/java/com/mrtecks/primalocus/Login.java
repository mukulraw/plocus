package com.mrtecks.primalocus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mrtecks.primalocus.loginPOJO.loginBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {

    Button login;
    EditText username , password;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.button);
        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        progress = findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String u = username.getText().toString();
                String p = password.getText().toString();

                if (u.length() > 0)
                {

                    if (p.length() > 0)
                    {
                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<loginBean> call = cr.login(u , p , SharePreferenceUtils.getInstance().getString("token"));
                        call.enqueue(new Callback<loginBean>() {
                            @Override
                            public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                if (response.body().getStatus().equals("1"))
                                {

                                    Intent intent = new Intent(Login.this , MainActivity.class);
                                    SharePreferenceUtils.getInstance().saveString("id" , response.body().getData().getId());
                                    SharePreferenceUtils.getInstance().saveString("name" , response.body().getData().getName());
                                    SharePreferenceUtils.getInstance().saveString("phone" , response.body().getData().getPhone());
                                    SharePreferenceUtils.getInstance().saveString("username" , response.body().getData().getUsername());
                                    SharePreferenceUtils.getInstance().saveString("password" , response.body().getData().getPassword());

                                    startActivity(intent);
                                    Toast.makeText(Login.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                    finishAffinity();

                                }
                                else
                                {
                                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }



                }
                else
                {
                    Toast.makeText(Login.this, "Invalid username", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
