package com.example.android.e7la2ly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.e7la2ly.Account.LoginActivity;
import com.example.android.e7la2ly.Barber.BarberHomeActivity;
import com.example.android.e7la2ly.Base.BaseActivity;
import com.example.android.e7la2ly.Client.ClientHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null && isNetworkConnected()) {
                    // User is signed in

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(user.getUid());


                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            try
                            {
                                if (dataSnapshot.child("accounttype").getValue(String.class).equals("Client")) {
                                    startActivity(new Intent(SplashScreenActivity.this, ClientHomeActivity.class));
                                    finish();
                                }

                                else if (dataSnapshot.child("accounttype").getValue(String.class).equals("Barber"))
                                {
                                    startActivity(new Intent(SplashScreenActivity.this, BarberHomeActivity.class));
                                    finish();
                                }
                            }
                            catch (NullPointerException e)
                            {
                                e.getMessage();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }
                    });


                }




                else if(!isNetworkConnected())
                {
                    showConfirmationMessage("ERROR", "NO INTERNET CONNECTION", "Ok", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            finish();

                        }
                    });
                }


                else if (user==null) {


                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, 1000);
    }


    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        return isConnected;

    }

    }

