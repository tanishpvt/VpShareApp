package com.example.vpshareapp.School;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vpshareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SchoolRegister extends AppCompatActivity {



    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;

    EditText school_login_name,school_login_requiredStuff,school_login_email,school_login_password,school_login_PhoneNo,school_login_Address,school_login_Area
            ,school_login_city;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_register);
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        progressDialog=new ProgressDialog(this,4);
        school_login_name=findViewById(R.id.school_login_name);
        school_login_requiredStuff=findViewById(R.id.school_login_requiredStuff);
        school_login_email=findViewById(R.id.school_login_email);
        school_login_password=findViewById(R.id.school_login_password);
        school_login_PhoneNo=findViewById(R.id.school_login_PhoneNo);
        school_login_Address=findViewById(R.id.school_login_Address);
        school_login_Area=findViewById(R.id.school_login_Area);
        school_login_city=findViewById(R.id.school_login_city);

        submit=findViewById(R.id.admin_login_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,requiredStuff,email,password,phoneNo,address,area,city;
                        name=school_login_name.getText().toString();
                        requiredStuff=school_login_requiredStuff.getText().toString();
                        email=school_login_email.getText().toString();
                        password=school_login_password.getText().toString();
                        phoneNo=school_login_PhoneNo.getText().toString();
                        address=school_login_Address.getText().toString();
                        area=school_login_Area.getText().toString();
                        city=school_login_city.getText().toString();

                        if(name.equals("")||requiredStuff.equals("")||email.equals("")||password.equals("")||phoneNo.equals("")||
                                address.equals("")||area.equals("")||city.equals(""))
                        {

                        }
                        else if(requiredStuff.equals("")){
                            Toast.makeText(SchoolRegister.this, "Fill required Stuff", Toast.LENGTH_SHORT).show();
                        }
                        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            school_login_email.setError("Invalided Email");
                            school_login_email.setFocusable(true);

                        }
                        else if(password.length()<6){
                            school_login_password.setError("Password length at least 6 characters");
                            school_login_password.setFocusable(true);
                        }
                        else {

                            SharedPreferences.Editor editor;
                            editor= PreferenceManager.getDefaultSharedPreferences(SchoolRegister.this).edit();
                            editor.putString("School_username", email);
                            editor.putString("School_password", password);
                            editor.apply();
                            registerSchool(name,requiredStuff,email,password,phoneNo,address,area,city);

                        }
            }
        });


    }

    private void registerSchool(final String name, final String requiredStuff, String email, final String password,
                                final String phoneNo, final String address, final String area, final String city) {
        progressDialog.setMessage("Loading....");

        progressDialog.show();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser Suser = mAuth.getCurrentUser();

                            String email= Suser.getEmail();
                            String uid=Suser.getUid();
                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("Name",name);
                            hashMap.put("Email",email);
                            hashMap.put("Password",password);
                            hashMap.put("Address",address);
                            hashMap.put("Phone",phoneNo);
                            hashMap.put("city",city);
                            hashMap.put("requiredStuff",requiredStuff);
                            hashMap.put("area",area);
                            hashMap.put("uid",uid);
                            FirebaseDatabase database=FirebaseDatabase.getInstance();

                            DatabaseReference reference=database.getReference("School");
                            reference.child(uid).setValue(hashMap);
                            //sucess
                            Toast.makeText(SchoolRegister.this, "Registered with "+Suser.getEmail(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(SchoolRegister.this, SchoolDashBoard.class));
                            finish();



                        }
                        else {
                        progressDialog.dismiss();
                        Toast.makeText(SchoolRegister.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }

                }
                });


    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);



        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String username=prefs.getString("School_username","");
        String pass=prefs.getString("School_password","");

        if(username.equals("")&&pass.equals("")) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Logging...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(SchoolRegister.this, SchoolDashBoard.class));
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(SchoolRegister.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SchoolRegister.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        }
}
