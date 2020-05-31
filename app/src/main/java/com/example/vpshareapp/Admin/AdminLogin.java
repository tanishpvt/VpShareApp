package com.example.vpshareapp.Admin;

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

public class AdminLogin extends AppCompatActivity {

    EditText admin_username,admin_password,admin_email;
    Button loginBtn;
    FirebaseUser user;
    FirebaseAuth auth;
    //process dialog
    ProgressDialog progressDialog;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Fill Details");
        //enable button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        progressDialog=new ProgressDialog(this,4);


    admin_username=findViewById(R.id.admin_username);
    admin_password=findViewById(R.id.admin_password);
    admin_email=findViewById(R.id.admin_email);
    loginBtn=findViewById(R.id.loginBtn);
    auth=FirebaseAuth.getInstance();
    user=auth.getCurrentUser();
    loginBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //handle login
            //admin info
            String email,username,password;
            email=admin_email.getText().toString().trim();
            password=admin_password.getText().toString().trim();
            username=admin_username.getText().toString().trim();
            if(username.equals("")){
                admin_username.setError("Empty");
                admin_username.setFocusable(true);
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                admin_email.setError("Invalided Email");
                admin_email.setFocusable(true);
            }
            else if(password.length()<6){
                admin_password.setError("Password length at least 6 characters");
                admin_password.setFocusable(true);
            }
            else {

                SharedPreferences.Editor editor;
                editor= PreferenceManager.getDefaultSharedPreferences(AdminLogin.this).edit();
                editor.putString("admin_username", email.trim());
                editor.putString("admin_password", password.trim());
                editor.apply();
                LoginAdmin(username,email,password);

            }
        }
    });
    }

    private void LoginAdmin(String username, String email, final String password) {
        //checking is username pass is corrent or not

        progressDialog.setMessage("Login..");
        progressDialog.show();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Admin");
        if(username.equals("Admin")&&password.equals("Admin123")){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //sucess
                    progressDialog.dismiss();
                    Toast.makeText(AdminLogin.this, "Login with " + user.getEmail(), Toast.LENGTH_SHORT).show();

                    if (task.isSuccessful()) {

                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();

                        String email = user.getEmail();
                        String uid = user.getUid();
                        HashMap<Object, String> hashMap = new HashMap<>();

                        hashMap.put("uid", uid);
                        hashMap.put("name", "Admin");
                        hashMap.put("password", password);
                        hashMap.put("email", email);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference reference = database.getReference("Admin");
                        reference.child("Admin").setValue(hashMap);
                        startActivity(new Intent(AdminLogin.this, AdminDashBoard.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminLogin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "wrong username & password", Toast.LENGTH_SHORT).show();

        }


    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String admin_username=prefs.getString("admin_username","");
        String admin_password=prefs.getString("admin_password","");


        final FirebaseAuth mAuth=FirebaseAuth.getInstance();


        if(admin_username.equals("")||admin_password.equals("")) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        }else {
            progressDialog.setMessage("Logging...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(admin_username, admin_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(AdminLogin.this, AdminDashBoard.class));
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(AdminLogin.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminLogin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
