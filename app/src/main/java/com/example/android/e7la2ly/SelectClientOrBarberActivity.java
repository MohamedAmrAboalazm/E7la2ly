package com.example.android.e7la2ly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.e7la2ly.Barber.BarberRegisterationActivity;
import com.example.android.e7la2ly.Client.ClientRegisterationActivity;

public class SelectClientOrBarberActivity extends AppCompatActivity {

    Button selectClient,selectBarber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_client_or_barber);

        selectClient=findViewById(R.id.select_client);
        selectBarber=findViewById(R.id.select_barber);

        selectClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SelectClientOrBarberActivity.this, ClientRegisterationActivity.class));

            }
        });


        selectBarber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SelectClientOrBarberActivity.this, BarberRegisterationActivity.class));

            }
        });
    }
}
