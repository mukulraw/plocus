package com.primalocus.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class OTP extends AppCompatActivity {

    Button login;
    EditText phone;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        login = findViewById(R.id.button);
        phone = findViewById(R.id.editText);
        progress = findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OTP.this , MainActivity.class);
                startActivity(intent);
                //Toast.makeText(OTP.this, "Please verify your mobile number", Toast.LENGTH_SHORT).show();
                finish();

                /*String p = phone.getText().toString();

                if (p.length() > 0)
                {

                    Intent intent = new Intent(OTP.this , MainActivity.class);
                    startActivity(intent);
                    //Toast.makeText(OTP.this, "Please verify your mobile number", Toast.LENGTH_SHORT).show();
                    finish();

                    *//*progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<loginBean> call = cr.login(p , SharePreferenceUtils.getInstance().getString("token"));
                    call.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                            if (response.body().getStatus().equals("1"))
                            {

                                Intent intent = new Intent(Login.this , OTP.class);
                                SharePreferenceUtils.getInstance().saveString("phone" , response.body().getPhone());
                                //SharePreferenceUtils.getInstance().saveString("id" , response.body().getMessage());
                                startActivity(intent);
                                Toast.makeText(Login.this, "Please verify your phone number", Toast.LENGTH_SHORT).show();
                                finish();

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
                    });*//*

                }
                else
                {
                    Toast.makeText(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

    }
}
