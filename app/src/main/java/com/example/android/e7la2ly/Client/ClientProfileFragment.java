package com.example.android.e7la2ly.Client;


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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientProfileFragment extends Fragment {

    private EditText clientName, clientEmail, clientPhoneNumber;
    private Button saveClientBtn, deleteClientAccountBtn, clientLogOutBtn;
    private ImageView clientProfileImage;
    private final int GALLERY_REQUEST = 1;
    private String imageLink, currEmail, currPassword;
    private FirebaseUser user;
    private DatabaseReference reference;
    private MaterialDialog dialog;


    public ClientProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);





        clientProfileImage = view.findViewById(R.id.client_profile_image);
        clientName = view.findViewById(R.id.clientname_edit_et);
        clientEmail = view.findViewById(R.id.client_email_edit_et);
        clientPhoneNumber = view.findViewById(R.id.client_phone_edit_et);
        saveClientBtn = view.findViewById(R.id.save_client_btn);
        deleteClientAccountBtn = view.findViewById(R.id.delete_client_account_btn);
        clientLogOutBtn = view.findViewById(R.id.client_log_out_btn);


        showProgressBar(R.string.loading);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                clientName.setText(dataSnapshot.child("username").getValue(String.class));
                clientEmail.setText(dataSnapshot.child("email").getValue(String.class));
                clientPhoneNumber.setText(dataSnapshot.child("phonenumber").getValue(String.class));

                currEmail = clientEmail.getText().toString();
                currPassword = dataSnapshot.child("password").getValue(String.class);

                Glide.with(getActivity()).load(user.getPhotoUrl()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.refresh).apply(new RequestOptions().circleCrop()).error(R.drawable.default_profile_image).into(clientProfileImage);


                hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        saveClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sUserName = clientName.getText().toString();
                final String sEmail = clientEmail.getText().toString();
                String sPhoneNumber = clientPhoneNumber.getText().toString();

                if (sUserName.isEmpty()) {
                    clientName.setError("UserName is Required");
                    clientName.requestFocus();
                    return;

                }

                if (sEmail.isEmpty()) {
                    clientEmail.setError("Email is Required");
                    clientEmail.requestFocus();
                    return;

                }

                if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
                    clientEmail.setError("Please Enter a Valid Email");
                    clientEmail.requestFocus();
                    return;
                }


                if (sPhoneNumber.isEmpty()) {
                    clientPhoneNumber.setError("Phone Number is Required");
                    clientPhoneNumber.requestFocus();
                    return;

                }

                if (!Patterns.PHONE.matcher(sPhoneNumber).matches()) {
                    clientPhoneNumber.setError("Please Enter a Valid Phone Number");
                    clientPhoneNumber.requestFocus();
                    return;
                }

                reference.child("username").setValue(sUserName);
                reference.child("email").setValue(sEmail);
                reference.child("phonenumber").setValue(sPhoneNumber);


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
                                user.updateEmail(sEmail);
                                hideProgressBar();


                            }
                        });

            }
        });


        clientProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);


            }
        });


        deleteClientAccountBtn.setOnClickListener(new View.OnClickListener() {
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


        clientLogOutBtn.setOnClickListener(new View.OnClickListener() {
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
        final StorageReference profileImagesRef = storageRef.child("clientProfileImages/" + user.getUid() + ".jpg");
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


                        reference.child("clientimagelink").setValue(imageLink);

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(imageLink)).build();
                        user.updateProfile(profileUpdates);

                        Glide.with(getActivity()).load(user.getPhotoUrl()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.refresh).apply(new RequestOptions().circleCrop()).error(R.drawable.default_profile_image).into(clientProfileImage);



                        updateNavigationDrawer();



                        hideProgressBar();

                    }


                    //PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("imageLink", imageLink).apply();


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
        ClientHomeActivity clientHomeActivity = (ClientHomeActivity) getActivity();
        if(clientHomeActivity!=null){
            clientHomeActivity.refreshMyData();
        }
    }





}
