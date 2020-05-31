package com.example.vpshareapp.User;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vpshareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class User_Register extends AppCompatActivity {

    String barcode="";
    TextView barcode_txt,commander_id;

    EditText name_Et,email_Et,address_Et,phone_Et,city_Et,country_Et,password_Et,user_login_Area;

    String commanderName="";
    //process dialog
    //ProgressDialog progressDialog;
    String ComanderName="";

    private FirebaseAuth mAuth;

    //loading screen
    ScrollView scrollView;
    LottieAnimationView loading;



    Button submit,singIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__register);



        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Fill Details");
        //enable button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //init edit text
    //    Bundle intent=getIntent().getExtras();
    //    barcode=intent.getString("barcode");
        barcode_txt=findViewById(R.id.barcode_id);
        barcode_txt.setText(barcode);
        name_Et=findViewById(R.id.user_login_name);
        email_Et=findViewById(R.id.user_login_email);
        address_Et=findViewById(R.id.user_login_Address);
        phone_Et=findViewById(R.id.user_login_PhoneNo);
        city_Et=findViewById(R.id.user_login_city);
        country_Et=findViewById(R.id.user_login_Country);
        password_Et=findViewById(R.id.user_login_password);
        commander_id=findViewById(R.id.commander_id);
        user_login_Area=findViewById(R.id.user_login_Area);
        submit=findViewById(R.id.user_login_submit);

        //screen loading
        loading=findViewById(R.id.loading);
        scrollView=findViewById(R.id.scrollable);
        loading.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

       // progressDialog=new ProgressDialog(this,4);



        //init firebase
        mAuth = FirebaseAuth.getInstance();

       // progressDialog.setMessage("Registering user...");







        //handle btn if user is already register and he/she want to just sign in
        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Register.this,User_Login.class));
            }
        });

        //noinspection deprecation
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(User_Register.this, "submit", Toast.LENGTH_SHORT).show();

                //user info
                String name,email,address,phone,city,country,password,commander_id1,area;
                //user donated
                String donated ="";
                //init
                email= email_Et.getText().toString();
                name= name_Et.getText().toString();
                address= address_Et.getText().toString();
                phone= phone_Et.getText().toString();
                city= city_Et.getText().toString();
                country= country_Et.getText().toString();
                password=password_Et.getText().toString();
                commander_id1=commander_id.getText().toString();
                area=user_login_Area.getText().toString();



                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    email_Et.setError("Invalided Email");
                    email_Et.setFocusable(true);

                }
                else if(password.length()<6){
                    password_Et.setError("Password length at least 6 characters");
                    password_Et.setFocusable(true);
                }
                else {
                    SharedPreferences.Editor editor;
                    editor= PreferenceManager.getDefaultSharedPreferences(User_Register.this).edit();
                    editor.putString("username", email.trim());
                    editor.putString("password", password.trim());
                    editor.apply();

                    registerUser(name,email,address,phone,city,country,password,donated,barcode,commander_id1,area);

                }

            }
        });




    }

    private void registerUser(final String name, String email, final String address, final String phone, final String city, final String country, final String password, final String donated, final String barcode, final String commander_id1, final String area) {



        //progressDialog.show();
        //to show loading screen
        loading.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {



                           // progressDialog.dismiss();
                            //back normal

                            loading.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);

                            FirebaseUser user = mAuth.getCurrentUser();

                            String email= user.getEmail();
                            String uid=user.getUid();
                            final HashMap<Object,String> hashMap=new HashMap<>();

                            //check if commander is allocated or  not
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Commanders");
                            hashMap.put("Name",name);
                            hashMap.put("Email",email);
                            hashMap.put("Address",address);
                            hashMap.put("Phone",phone);
                            hashMap.put("city",city);
                            hashMap.put("Area",area);
                            hashMap.put("country",country);
                            hashMap.put("Password",password);
                            hashMap.put("Donated",donated);
                            hashMap.put("Barcode",barcode);
                            hashMap.put("bagid",uid);
                            hashMap.put("CommanderId", "Not Allocated");
                            hashMap.put("CommanderName","Not Allocated");
                            final FirebaseDatabase database=FirebaseDatabase.getInstance();

                            DatabaseReference reference=database.getReference("Users");


                            reference.child(uid).setValue(hashMap);

                            //sucess
                            Toast.makeText(User_Register.this, "Registered with "+user.getEmail(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(User_Register.this, User_Dasboard.class));
                            finish();


                        }
                        else {
                            //progressDialog.dismiss();
                            loading.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            Toast.makeText(User_Register.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //progressDialog.dismiss();
                loading.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                Toast.makeText(User_Register.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });





    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
    @Override
    protected void onStart() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String username=prefs.getString("username","");
        String pass=prefs.getString("password","");

        if(username.equals("")&&pass.equals("")) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else {
       //     progressDialog.setMessage("Logging...");
           // progressDialog.show();
            loading.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            mAuth.signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               // progressDialog.dismiss();

                                loading.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(User_Register.this, User_Dasboard.class));
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                //progressDialog.dismiss();

                                loading.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);

                                Toast.makeText(User_Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //progressDialog.dismiss();

                    loading.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                    Toast.makeText(User_Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        super.onStart();
    }


    public boolean onSupportNavigateUp(){

        onBackPressed();//go baack

        return super.onSupportNavigateUp();
    }

}