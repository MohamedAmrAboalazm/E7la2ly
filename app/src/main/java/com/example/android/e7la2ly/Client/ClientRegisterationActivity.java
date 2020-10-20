package com.example.android.e7la2ly.Client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

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

import java.util.HashMap;

public class ClientRegisterationActivity extends BaseActivity {

    private EditText clientName, clientEmail, clientPassWord, clientPhoneNumber;
    private Button register;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private final String egyptCode = "+20";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_registeration);

        auth = FirebaseAuth.getInstance();

        clientName = findViewById(R.id.client_name_reg_et);
        clientEmail = findViewById(R.id.client_email_reg_et);
        clientPassWord = findViewById(R.id.client_password_reg_et);
        clientPhoneNumber = findViewById(R.id.client_phone_reg_et);
        register = findViewById(R.id.register_btn);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String sClientName = clientName.getText().toString();
                String sClientEmail = clientEmail.getText().toString();
                String sClientPassword = clientPassWord.getText().toString();
                String sClientPhoneNumber = clientPhoneNumber.getText().toString();

                if (sClientName.isEmpty()) {
                    clientName.setError("UserName is Required");
                    clientName.requestFocus();
                    return;

                }

                if (sClientEmail.isEmpty()) {
                    clientEmail.setError("Email is Required");
                    clientEmail.requestFocus();
                    return;

                }

                if (!Patterns.EMAIL_ADDRESS.matcher(sClientEmail).matches()) {
                    clientEmail.setError("Please Enter a Valid Email");
                    clientEmail.requestFocus();
                    return;
                }

                if (sClientPassword.isEmpty()) {
                    clientPassWord.setError("Password is Required");
                    clientPassWord.requestFocus();
                    return;

                }


                if (sClientPassword.length() < 6) {
                    clientPassWord.setError("Minimum Length is 6");
                    clientPassWord.requestFocus();
                    return;
                }

                if (sClientPhoneNumber.isEmpty()) {
                    clientPhoneNumber.setError("Phone Number is Required");
                    clientPhoneNumber.requestFocus();
                    return;

                }

                if (!Patterns.PHONE.matcher(sClientPhoneNumber).matches()) {
                    clientPhoneNumber.setError("Please Enter a Valid Phone Number");
                    clientPhoneNumber.requestFocus();
                    return;
                }


                Registeration(sClientName, sClientEmail, sClientPassword, sClientPhoneNumber);


                showProgressBar(R.string.loading);


            }
        });


    }


    public void Registeration(final String clientName, final String clientEmail, final String clientPassword, final String clientPhonenumber) {
        auth.createUserWithEmailAndPassword(clientEmail, clientPassword)
                .addOnCompleteListener(ClientRegisterationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideProgressBar();
                            showMessage(getString(R.string.success), "Registered Successsfully", getString(R.string.ok));
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();


                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);


                            final HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("username", clientName);
                            map.put("email", clientEmail);
                            map.put("password", clientPassword);
                            map.put("phonenumber", clientPhonenumber);
                            map.put("accounttype", "Client");
                            map.put("clientimagelink", "");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(clientName).build();
                            user.updateProfile(profileUpdates);


                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        String phoneNumber = egyptCode + clientPhonenumber;

                                        Intent intent = new Intent(ClientRegisterationActivity.this, VerifyPhoneNumberActivity.class);
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