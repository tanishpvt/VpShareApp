package com.example.vpshareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.vpshareapp.Admin.AdminLogin;
import com.example.vpshareapp.School.SchoolRegister;
import com.example.vpshareapp.User.User_Login;
import com.example.vpshareapp.User.User_QrScan;
import com.example.vpshareapp.User.User_Register;
import com.example.vpshareapp.commanderacivty.CommanderLogin;

public class MainActivity extends AppCompatActivity {

    TextView are_u_admin;
    ImageView userLogin_img;
    ImageView commander_login;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        userLogin_img=(ImageView)findViewById(R.id.user_login);
        are_u_admin=findViewById(R.id.are_u_admin);
        commander_login=findViewById(R.id.commander_login);

        // Create the object of
        // AlertDialog Builder class

               builder = new AlertDialog.Builder(this,4);


        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this, 4)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to acess this feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                        android.Manifest.permission.ACCESS_FINE_LOCATION
                                }, 10);
                            }
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
        }
        //commander login activity
        //noinspection deprecation
        commander_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("Do you want to Login As?");

                // Set Alert Title
                builder.setTitle("Alert !");
                // the Dialog Box then it will remain show
                builder.setCancelable(true);
                builder
                        .setPositiveButton(
                                "Commander Login",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        Intent intent=new Intent(MainActivity.this, CommanderLogin.class);
                                        startActivity(intent);

                                        // When the user click yes button
                                        // then app will close
                                        finish();
                                    }
                                }).setNegativeButton(
                        "School Login",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                Intent intent=new Intent(MainActivity.this, SchoolRegister.class);
                                startActivity(intent);
                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });
                builder.create().show();

            }
            });

        //admin login activity
        //noinspection deprecation
        are_u_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AdminLogin.class);
                startActivity(intent);
            }
        });


        //user login activity
        //noinspection deprecation
        userLogin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, User_Register.class);
                startActivity(intent);
            }
        });
    }

    private void displayOption() {


    }
}
