package com.example.vpshareapp.ui.commander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.R;
import com.example.vpshareapp.User.User_Dasboard;
import com.example.vpshareapp.commanderacivty.CommanderDashBorad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CommanderSignIn extends AppCompatActivity {

    Button user_login_signIn;
    TextView user_login_password,user_login_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander_sign_in);

        user_login_signIn=findViewById(R.id.user_login_signIn);
        user_login_password=findViewById(R.id.user_login_password);
        user_login_email=findViewById(R.id.user_login_email);

        user_login_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign
                String email,password;
                email=user_login_email.getText().toString();
                password=user_login_password.getText().toString();
               // Toast.makeText(CommanderSignIn.this, "sadas", Toast.LENGTH_SHORT).show();
                if(!email.equals("")||password.equals("")){
                    SharedPreferences.Editor editor;
                    editor= PreferenceManager.getDefaultSharedPreferences(CommanderSignIn.this).edit();
                    editor.putString("Com_username", email.trim());
                    editor.putString("Com_password", password.trim());
                    editor.apply();
                    signIn(email,password);
                }
            }
        });


    }

    private void signIn(String email, String password) {
        final ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_DarkActionBar);
        progressDialog.setMessage("Logging...");
        progressDialog.show();
        final FirebaseAuth mAuth;
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(CommanderSignIn.this, CommanderDashBorad.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(CommanderSignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CommanderSignIn.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
