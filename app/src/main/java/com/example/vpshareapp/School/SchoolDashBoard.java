package com.example.vpshareapp.School;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.vpshareapp.AboutPage;
import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.R;
import com.example.vpshareapp.User.Account;
import com.example.vpshareapp.User.BagDetail;
import com.example.vpshareapp.User.Commander;
import com.example.vpshareapp.User.User_Dasboard;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SchoolDashBoard extends AppCompatActivity {


    ActionBar actionBar;

    FirebaseAuth firebaseAuth;

    String mUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_dash_board);

        actionBar=getSupportActionBar();
        actionBar.setTitle("Home Page");



        firebaseAuth= FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView=findViewById(R.id.navigation);
        //event listner
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        //init default fragment
        actionBar.setTitle("About");
        AboutPage fragment1=new AboutPage();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content1,fragment1,"");
        ft1.commit();


        //checking login
        checkforuserlogin();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){

                        case R.id.school_nav_comfirmation:
                            //profile fargment transcatrion

                            actionBar.setTitle("Comfirmation");
                            SchoolComfirmBags fragment2=new SchoolComfirmBags();
                            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content1,fragment2,"");
                            ft2.commit();
                            return true;
                        case R.id.school_nav_Account:
                            //user fragmentation

                            actionBar.setTitle("Account");
                            SchoolAccount fragment3=new SchoolAccount();
                            FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content1,fragment3,"");
                            ft3.commit();
                            return true;

                        case R.id.school_nav_About:
                            //user fragmentation

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
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            mUID=user.getUid();

            SharedPreferences sp=getSharedPreferences("SP_User",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();



        }
        else{
            startActivity(new Intent(SchoolDashBoard.this, MainActivity.class));
            finish();
        }
    }
    @Override
    protected void onStart() {
        checkforuserlogin();
        super.onStart();
    }
}
