package com.example.android.e7la2ly.Client;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.e7la2ly.Client.ViewBarbersData.BarberData;
import com.example.android.e7la2ly.Client.ViewBarbersData.BarbersAdapter;
import com.example.android.e7la2ly.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseBarberFragment extends Fragment {

    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<BarberData> list;
    private BarbersAdapter adapter;


    public ChooseBarberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_choose_barber, container, false);
        recyclerView=view.findViewById(R.id.recyclerView_barber);
        layoutManager = new LinearLayoutManager(getContext());
        list=new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                       if (eventSnapshot.child("accounttype").getValue(String.class).equals("Barber"))
                        {

                            BarberData data = eventSnapshot.getValue(BarberData.class);
                            list.add(data);
                       }

                    }

                adapter = new BarbersAdapter(getContext(),list);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
