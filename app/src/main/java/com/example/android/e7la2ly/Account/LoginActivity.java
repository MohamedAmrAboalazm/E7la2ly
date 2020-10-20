package com.example.android.e7la2ly.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.android.e7la2ly.Barber.BarberHomeActivity;
import com.example.android.e7la2ly.Base.BaseActivity;
import com.example.android.e7la2ly.Client.ClientHomeActivity;
import com.example.android.e7la2ly.R;
import com.example.android.e7la2ly.SelectClientOrBarberActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {

    private EditText email, passWord;
    private TextView signUp;
    private FirebaseAuth auth;
    private Button login;
    private String sEmail, sPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email_login_et);
        passWord = findViewById(R.id.password_login_et);
        login = findViewById(R.id.login_btn);
        signUp = findViewById(R.id.signup_tv);




        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SelectClientOrBarberActivity.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sEmail = email.getText().toString();
                sPassword = passWord.getText().toString();


                if (sEmail.isEmpty()) {
                    email.setError("Email is Required");
                    email.requestFocus();
                    return;

                }

                if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
                    email.setError("Please Enter a Valid Email");
                    email.requestFocus();
                    return;
                }

                if (sPassword.isEmpty()) {
                    passWord.setError("Password is Required");
                    passWord.requestFocus();
                    return;

                }


                if (sPassword.length() < 6) {
                    passWord.setError("Minimum Length is 6");
                    passWord.requestFocus();
                    return;
                }


                Login();

                showProgressBar(R.string.loading);

            }
        });
    }


    public void Login() {
        auth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(auth.getCurrentUser().getUid());


                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    hideProgressBar();
                                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();

                                    try {
                                        if (dataSnapshot.child("accounttype").getValue(String.class).equals("Client")) {
                                            Intent intent = new Intent(LoginActivity.this, ClientHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else if (dataSnapshot.child("accounttype").getValue(String.class).equals("Barber")) {
                                            Intent intent = new Intent(LoginActivity.this, BarberHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }


                                    } catch (NullPointerException ex) {
                                        ex.getMessage();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {


                                    hideProgressBar();
                                }
                            });


                        } else {

                            hideProgressBar();
                            showMessage(getString(R.string.error), "Wrong Email or Password", getString(R.string.ok));
                        }
                    }
                });
    }


}



