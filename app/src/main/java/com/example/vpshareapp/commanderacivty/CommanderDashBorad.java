package com.example.vpshareapp.commanderacivty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.AboutPage;
import com.example.vpshareapp.Admin.AdminDashBoard;
import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.MainScreen;
import com.example.vpshareapp.R;
import com.example.vpshareapp.ui.home.VpLocation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CommanderDashBorad extends AppCompatActivity {

    ActionBar actionBar;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String mUID;
    Boolean isAthurired=true;
   //display if commander is not authorized by admin
    TextView  text;

    //header
    ImageView homeBtn,Call,Website,Location,FaceBook,Instagram;

    //facebook attributes
    public static String FACEBOOK_URL = "https://www.facebook.com/Vidyalankar.VP/";
    public static String FACEBOOK_PAGE_ID = "Vidyalankar.VP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander_dash_borad);


        actionBar=getSupportActionBar();
        actionBar.setTitle("Bag Details");

        firebaseAuth= FirebaseAuth.getInstance();

        //init header
        homeBtn=findViewById(R.id.homeBtn);
        Call=findViewById(R.id.Call);
        Website=findViewById(R.id.Website);
        Location=findViewById(R.id.Location);
        FaceBook=findViewById(R.id.FaceBook);
        Instagram=findViewById(R.id.Instagram);

        //checking login
        checkforuserlogin();





        text=findViewById(R.id.text);
        text.setVisibility(View.GONE);
        final BottomNavigationView bottomNavigationView=findViewById(R.id.navigation);
        //event listner
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);
        Query query=FirebaseDatabase.getInstance().getReference("Commanders").orderByChild("Email").equalTo(user.getEmail());


        //check is user authorized;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String isAuthorized= ""+ds.child("isAuthorized").getValue();
                    if(isAuthorized.equals("0")){
                        //comander is not allocated yet
                        isAthurired=false;
                        Toast.makeText(CommanderDashBorad.this, ""+isAuthorized, Toast.LENGTH_SHORT).show();

                        bottomNavigationView.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(), "Your not authorized by admin", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // comander  is allocated by admin
                        isAthurired=true;
                        text.setVisibility(View.GONE);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     /*   //init default fragment
        actionBar.setTitle("About");
        AboutPage fragment1=new AboutPage();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content1,fragment1,"");
        ft1.commit();

      */
        //header function
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open home page
                startActivity(new Intent(CommanderDashBorad.this, MainScreen.class));
            }
        });
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open call
                CallToVp("02224161126");
            }
        });

        Website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open website page
                WebsiteToVp("https://vidyalankar.com/vidyalankar-polytechnic/");
            }
        });

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open home page
                startActivity(new Intent(CommanderDashBorad.this, VpLocation.class));
            }
        });

        Instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open instagram page
                InstagramToVp("https://www.instagram.com/vp_vidyalankar/");
            }
        });
        FaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open home page
                FacebookToVp("facebooklink");
            }
        });



        actionBar.setTitle("Bag Detail");
        AllocatedBags fragment1=new AllocatedBags();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content1,fragment1,"");
        ft1.commit();




    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.commander_nav_bagAllocated:
                            //bag details fragmentation

                            actionBar.setTitle("Bag Detail");
                            AllocatedBags fragment1=new AllocatedBags();
                            FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content1,fragment1,"");
                            ft1.commit();
                            return true;
                        case R.id.commander_nav_Account:
                            //profile fargment transcatrion

                            actionBar.setTitle("Account");
                            CommanderAccount fragment2=new CommanderAccount();
                            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content1,fragment2,"");
                            ft2.commit();
                            return true;
                        case R.id.commander_nav_comfirmation:
                            //confirm all bag are delevered to admin fragmentation
                            actionBar.setTitle("Commander Details");
                            ConfirmBagsSubmit fragment3=new ConfirmBagsSubmit();
                            FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content1,fragment3,"");
                            ft3.commit();
                            return true;
                        case R.id.user_nav_About:
                            //confirm all bag are delevered to admin fragmentation
                            actionBar.setTitle("About");
                            AboutPage fragment4=new AboutPage();
                            FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                            ft4.replace(R.id.content1,fragment4,"");
                            ft4.commit();
                            return true;

                    }

                    return false;
                }
            };

    private void checkforuserlogin() {
         user = firebaseAuth.getCurrentUser();
        if (user != null) {

            mUID=user.getUid();

            SharedPreferences sp=getSharedPreferences("SP_User",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();



        }
        else{
            startActivity(new Intent(CommanderDashBorad.this, MainActivity.class));
            finish();
        }
    }
    @Override
    protected void onStart() {
        checkforuserlogin();
        super.onStart();
    }

    //all method for header
    private void FacebookToVp(String facebooklink) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(CommanderDashBorad.this);
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);

    }
    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    private void InstagramToVp(String website) {
        Uri uri = Uri.parse(website);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(website)));
        }

    }

    private void WebsiteToVp(String s) {
        String url = s;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void CallToVp(String s) {
        String posted_by = s;

        String uri = "tel:" + posted_by.trim() ;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(CommanderDashBorad.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            new AlertDialog.Builder(CommanderDashBorad.this,4)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to acess this feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(CommanderDashBorad.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

}
