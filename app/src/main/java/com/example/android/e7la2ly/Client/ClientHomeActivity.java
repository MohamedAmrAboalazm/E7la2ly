package com.example.android.e7la2ly.Client;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.e7la2ly.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

public class ClientHomeActivity extends AppCompatActivity {


    private Fragment fragment = null;
    private FirebaseUser user;
    private Toolbar toolbar;
    private  Drawer drawer;
    private  AccountHeader headerResult;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (item.getItemId() == R.id.navigation_my_profile) {
                fragment = new ClientProfileFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.client_fragmentContainer, fragment).commit();

            }
            if (item.getItemId() == R.id.navigation_home) {
                fragment = new ChooseBarberFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.client_fragmentContainer, fragment).commit();

            }





            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_client_home);




        toolbar = findViewById(R.id.client_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();


        imageLoader();


        NavigationDrawerBuild();


        BottomNavigationView navigation = findViewById(R.id.client_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        View view = navigation.findViewById(R.id.navigation_home);
        view.performClick();

    }


    private void NavigationDrawerBuild() {


        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.side_nav_bar)
                .addProfiles(

                        user.getPhotoUrl() != null ? new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(user.getPhotoUrl()) : new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(R.drawable.default_profile_image)
                )

                .withSelectionListEnabledForSingleProfile(false)


                .build();


        //if you want to update the items at a later time it is recommended to keep it in a variable

        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName("Contact Us").withIcon(R.drawable.ic_contact_us);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Share APP").withIcon(R.drawable.ic_share);

//Now create your drawer and pass the AccountHeader.Result


        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult)


                .addDrawerItems(
                        item1,
                        item2

                )


                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch ((int) drawerItem.getIdentifier()) {


                            case 1:
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","halbas.work@gmail.com", null));
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                break;

                            case 2:
                                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Our App Now :https://www.e7la2ly.com");
                                startActivity(Intent.createChooser(intent, "Share"));
                                break;
                        }

                        return false;
                    }
                })
                .build();


    }


    private void imageLoader() {


        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                super.set(imageView, uri, placeholder, tag);
                Glide.with(imageView.getContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(placeholder).centerCrop().into(imageView);

            }

            @Override
            public void cancel(ImageView imageView) {
                super.cancel(imageView);
                Glide.with(getApplicationContext()).clear(imageView);


            }

        });


    }



    public void refreshMyData(){

        imageLoader();


        NavigationDrawerBuild();

    }


}
