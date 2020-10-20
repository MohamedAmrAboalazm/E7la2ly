package com.example.android.e7la2ly.Barber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.e7la2ly.Account.LoginActivity;
import com.example.android.e7la2ly.Client.ClientHomeActivity;
import com.example.android.e7la2ly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BarberProfileFragment extends Fragment {

    private EditText salonName,barberName,openTime,closeTime,hairPrice,beardPrice,othersPrice, barberEmail, barberPhoneNumber;
    private Button saveBarberBtn, deleteBarberAccountBtn, barberLogOutBtn;
    private ImageView barberProfileImage;
    private final int GALLERY_REQUEST = 1;
    private String imageLink, currEmail, currPassword;
    private FirebaseUser user;
    private DatabaseReference reference;
    private MaterialDialog dialog;


    public BarberProfileFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_barber_profile, container, false);




        barberProfileImage=view.findViewById(R.id.barber_profile_image);
        salonName=view.findViewById(R.id.salon_name_edit_et);
        barberName=view.findViewById(R.id.barber_name_edit_et);
        openTime=view.findViewById(R.id.barber_open_time_edit_et);
        closeTime=view.findViewById(R.id.barber_close_time_edit_et);
        hairPrice=view.findViewById(R.id.barber_hair_cut_price_edit_et);
        beardPrice=view.findViewById(R.id.barber_beard_cut_price_edit_et);
        othersPrice=view.findViewById(R.id.barber_others_price_edit_et);
        barberEmail=view.findViewById(R.id.barber_email_edit_et);
        barberPhoneNumber=view.findViewById(R.id.barber_phone_edit_et);
        saveBarberBtn=view.findViewById(R.id.save_barber_btn);
        deleteBarberAccountBtn=view.findViewById(R.id.delete_barber_account_btn);
        barberLogOutBtn=view.findViewById(R.id.barber_log_out_btn);



        showProgressBar(R.string.loading);


        user = FirebaseAuth.getInstance().getCurrentUser();


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                salonName.setText(dataSnapshot.child("salonname").getValue(String.class));
                barberName.setText(dataSnapshot.child("barbername").getValue(String.class));
                barberEmail.setText(dataSnapshot.child("barberemail").getValue(String.class));
                openTime.setText(dataSnapshot.child("opentime").getValue(String.class));
                closeTime.setText(dataSnapshot.child("closetime").getValue(String.class));
                hairPrice.setText(dataSnapshot.child("hairprice").getValue(String.class));
                beardPrice.setText(dataSnapshot.child("beardprice").getValue(String.class));
                othersPrice.setText(dataSnapshot.child("othersprice").getValue(String.class));
                barberPhoneNumber.setText(dataSnapshot.child("barberphonenumber").getValue(String.class));


                currEmail = barberEmail.getText().toString();
                currPassword = dataSnapshot.child("barberpassword").getValue(String.class);

                Glide.with(getActivity()).load(user.getPhotoUrl()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.refresh).apply(new RequestOptions().circleCrop()).error(R.drawable.default_profile_image).into(barberProfileImage);


                hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        saveBarberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sSalonName = salonName.getText().toString();
                String sBarberName = barberName.getText().toString();
                String sOpenTime = openTime.getText().toString();
                String sCloseTime = closeTime.getText().toString();
                String sHairPrice = hairPrice.getText().toString();
                String sBeardPrice = beardPrice.getText().toString();
                String sOthersPrice = othersPrice.getText().toString();
                final String sBarberEmail = barberEmail.getText().toString();
                String sBarberphoneNumber = barberPhoneNumber.getText().toString();

                if (sSalonName.isEmpty()) {
                    salonName.setError("SalonName is Required");
                    salonName.requestFocus();
                    return;

                } if (sBarberName.isEmpty()) {
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

                reference.child("salonname").setValue(sSalonName);
                reference.child("barbername").setValue(sBarberName);
                reference.child("barberemail").setValue(sBarberEmail);
                reference.child("opentime").setValue(sOpenTime);
                reference.child("closetime").setValue(sCloseTime);
                reference.child("hairprice").setValue(sHairPrice);
                reference.child("beardprice").setValue(sBeardPrice);
                reference.child("othersprice").setValue(sOthersPrice);
                reference.child("barberphonenumber").setValue(sBarberphoneNumber);


                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider
                        .getCredential(currEmail, currPassword); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                showProgressBar(R.string.loading);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                //----------------Code for Changing Email Address----------\\
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(sBarberEmail);
                                hideProgressBar();


                            }
                        });

            }
        });


        barberProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);


            }
        });


        deleteBarberAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider
                        .getCredential(currEmail, currPassword); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                //----------------Code for Changing Email Address----------\\
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                                    getActivity().finish();
                                                }
                                            }
                                        });

                            }
                        });


                reference.setValue(null);

            }
        });


        barberLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();

            }
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = intent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        uploadImage(bitmap);


                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
    }


    private void uploadImage(Bitmap bitmap) {
        showProgressBar(R.string.loading);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://e7la2ly-bb604.appspot.com");
        final StorageReference profileImagesRef = storageRef.child("barberProfileImages/" + user.getUid() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageLink = uri.toString();


                        reference.child("barberimagelink").setValue(imageLink);

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(imageLink)).build();
                        user.updateProfile(profileUpdates);

                        Glide.with(getActivity()).load(user.getPhotoUrl()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.refresh).apply(new RequestOptions().circleCrop()).error(R.drawable.default_profile_image).into(barberProfileImage);



                        updateNavigationDrawer();



                        hideProgressBar();

                    }





                }).

                        addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

            }
        });

    }



    public MaterialDialog showProgressBar(int message){
        dialog=new MaterialDialog.Builder(getActivity())
                .progress(true,0)
                .content(message)
                .cancelable(false)
                .show();

        return dialog;
    }
    public void hideProgressBar(){
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();
    }


    public void updateNavigationDrawer(){
        BarberHomeActivity barberHomeActivity = (BarberHomeActivity) getActivity();
        if(barberHomeActivity!=null){
            barberHomeActivity.refreshMyData();
        }
    }



}
