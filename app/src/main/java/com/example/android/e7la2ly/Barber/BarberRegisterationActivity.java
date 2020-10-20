package com.example.android.e7la2ly.Barber;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.e7la2ly.Base.BaseActivity;
import com.example.android.e7la2ly.R;
import com.example.android.e7la2ly.VerifyPhoneNumberActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import mumayank.com.airlocationlibrary.AirLocation;

public class BarberRegisterationActivity extends BaseActivity {

    private EditText salonName, barberName, openTime, closeTime, hairPrice, beardPrice, othersPrice, barberEmail, barberPassWord, barberPhoneNumber;
    private Button register;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private final String egyptCode = "+20";
    private AirLocation airLocation;
    private double salonLatitude, salonLongitude;
    private MaterialDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_registeration);

        auth = FirebaseAuth.getInstance();


        salonName = findViewById(R.id.salon_name_reg_et);
        barberName = findViewById(R.id.barber_name_reg_et);
        openTime = findViewById(R.id.barber_open_time_reg_et);
        closeTime = findViewById(R.id.barber_close_time_reg_et);
        hairPrice = findViewById(R.id.barber_hair_cut_price_reg_et);
        beardPrice = findViewById(R.id.barber_beard_cut_price_reg_et);
        othersPrice = findViewById(R.id.barber_others_price_reg_et);
        barberEmail = findViewById(R.id.barber_email_reg_et);
        barberPassWord = findViewById(R.id.barber_password_reg_et);
        barberPhoneNumber = findViewById(R.id.barber_phone_reg_et);
        register = findViewById(R.id.barber_register_btn);



        getLocationWithMessage();



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sSalonName = salonName.getText().toString();
                String sBarberName = barberName.getText().toString();
                String sOpenTime = openTime.getText().toString();
                String sCloseTime = closeTime.getText().toString();
                String sHairPrice = hairPrice.getText().toString();
                String sBeardPrice = beardPrice.getText().toString();
                String sOthersPrice = othersPrice.getText().toString();
                String sBarberEmail = barberEmail.getText().toString();
                String sBarberPassword = barberPassWord.getText().toString();
                String sBarberphoneNumber = barberPhoneNumber.getText().toString();

                if (sSalonName.isEmpty()) {
                    salonName.setError("SalonName is Required");
                    salonName.requestFocus();
                    return;

                }
                if (sBarberName.isEmpty()) {
                    barberName.setError("BarberName is Required");
                    barberName.requestFocus();
                    return;

                }
                if (sOpenTime.isEmpty()) {
                    openTime.setError("OpenTime is Required");
                    openTime.requestFocus();
                    return;

                }


                if (sCloseTime.isEmpty()) {
                    closeTime.setError("CloseTime is Required");
                    closeTime.requestFocus();
                    return;

                }

                if (sBarberEmail.isEmpty()) {
                    barberEmail.setError("Email is Required");
                    barberEmail.requestFocus();
                    return;

                }

                if (!Patterns.EMAIL_ADDRESS.matcher(sBarberEmail).matches()) {
                    barberEmail.setError("Please Enter a Valid Email");
                    barberEmail.requestFocus();
                    return;
                }

                if (sBarberPassword.isEmpty()) {
                    barberPassWord.setError("Password is Required");
                    barberPassWord.requestFocus();
                    return;

                }


                if (sBarberPassword.length() < 6) {
                    barberPassWord.setError("Minimum Length is 6");
                    barberPassWord.requestFocus();
                    return;
                }

                if (sBarberphoneNumber.isEmpty()) {
                    barberPhoneNumber.setError("Phone Number is Required");
                    barberPhoneNumber.requestFocus();
                    return;

                }

                if (!Patterns.PHONE.matcher(sBarberphoneNumber).matches()) {
                    barberPhoneNumber.setError("Please Enter a Valid Phone Number");
                    barberPhoneNumber.requestFocus();
                    return;
                }
                if (sHairPrice.isEmpty()) {
                    hairPrice.setError("Hair Price is Required");
                    hairPrice.requestFocus();
                    return;

                }
                if (sBeardPrice.isEmpty()) {
                    beardPrice.setError("Beard Price is Required");
                    beardPrice.requestFocus();
                    return;

                }


                Registeration(sSalonName, sBarberName, sBarberEmail, sBarberPassword, sOpenTime, sCloseTime, sHairPrice, sBeardPrice, sOthersPrice, sBarberphoneNumber,salonLatitude,salonLongitude);


                showProgressBar(R.string.loading);


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



    private void getLocation()
    {



        // Fetch location simply like this whenever you need
        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NotNull Location location) {
                
                salonLatitude=location.getLatitude();
                salonLongitude=location.getLongitude();

               showConfirmationMessage("Confirmation","Your Salon Location Is Successfully Saved","Ok");
            }

            @Override
            public void onFailed(@NotNull AirLocation.LocationFailedEnum locationFailedEnum) {


                showConfirmationMessage("ERROR", "Your Salon Location Is Failed To Be Saved", "Retry", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        getLocationWithMessage();

                    }
                });
            }
        });
    }

    private void getLocationWithMessage()
    {
        dialog= new MaterialDialog.Builder(this)
                .title("Access Location Permission")
                .content("Do You Want The App E7la2ly To Access Your Location ?")
                .positiveText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();

                        showConfirmationMessage("ERROR", "Your Salon Location Is Failed To Be Saved", "Retry", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                getLocationWithMessage();

                            }
                        });
                    }
                })
                .negativeText("Yes")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getLocation();
                    }
                })
                .show();
    }


    private void Registeration(final String salonName, final String barberName, final String barberEmail, final String barberpassword, final String openTime, final String closeTime, final String hairPrice, final String beardPrice, final String othersPrice, final String barberphonenumber,final double salonLatitude,final double salonLongitude) {
        auth.createUserWithEmailAndPassword(barberEmail, barberpassword)
                .addOnCompleteListener(BarberRegisterationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideProgressBar();
                            showMessage(getString(R.string.success), "Registered Successsfully", getString(R.string.ok));
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);


                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("barbername", barberName);
                            map.put("salonname", salonName);
                            map.put("barberemail", barberEmail);
                            map.put("barberpassword", barberpassword);
                            map.put("opentime", openTime);
                            map.put("closetime", closeTime);
                            map.put("hairprice", hairPrice);
                            map.put("beardprice", beardPrice);
                            map.put("othersprice", othersPrice);
                            map.put("barberphonenumber", barberphonenumber);
                            map.put("accounttype", "Barber");
                            map.put("barberimagelink", "");
                            map.put("salonlatitude", salonLatitude);
                            map.put("salonlongitude", salonLongitude);

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(barberName).build();
                            user.updateProfile(profileUpdates);


                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        String phoneNumber = egyptCode + barberphonenumber;

                                        Intent intent = new Intent(BarberRegisterationActivity.this, VerifyPhoneNumberActivity.class);
                                        intent.putExtra("phonenumber", phoneNumber);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);
                                    }


                                }
                            });


                        } else {

                            hideProgressBar();
                            showMessage(getString(R.string.error), "This Email Is Already Registered Before", getString(R.string.ok));
                        }
                    }
                });
    }
}
