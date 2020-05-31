package com.example.vpshareapp.ui.share;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.R;
import com.example.vpshareapp.School.SchoolDashBoard;
import com.example.vpshareapp.School.SchoolRegister;
import com.example.vpshareapp.SchoolLogin;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class ShareFragment extends Fragment {


    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;

    EditText school_login_name, school_login_requiredStuff, school_login_email,
            school_login_password, school_login_PhoneNo, school_login_Address,
            school_login_Area, school_login_city;
    Button submit,signIn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        progressDialog = new ProgressDialog(getContext(), 4);
        school_login_name = view.findViewById(R.id.school_login_name);
        school_login_requiredStuff = view.findViewById(R.id.school_login_requiredStuff);
        school_login_email = view.findViewById(R.id.school_login_email);
        school_login_password = view.findViewById(R.id.school_login_password);
        school_login_PhoneNo = view.findViewById(R.id.school_login_PhoneNo);
        school_login_Address = view.findViewById(R.id.school_login_Address);
        school_login_Area = view.findViewById(R.id.school_login_Area);
        school_login_city = view.findViewById(R.id.school_login_city);

        submit=view.findViewById(R.id.admin_login_submit);
        signIn=view.findViewById(R.id.admin_login_signIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign In acitvty
                startActivity(new Intent(getActivity(), SchoolLogin.class));

            }
        });

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
                    Toast.makeText(getActivity(), "Fill required Stuff", Toast.LENGTH_SHORT).show();
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
                    editor= PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                    editor.putString("School_username", email);
                    editor.putString("School_password", password);
                    editor.apply();
                    registerSchool(name,requiredStuff,email,password,phoneNo,address,area,city);

                }
            }
        });

        return view;
    }
    private void registerSchool(final String name, final String requiredStuff, String email, final String password,
                                final String phoneNo, final String address, final String area, final String city) {
        progressDialog.setMessage("Loading....");

        progressDialog.show();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(getActivity(), "Registered with "+Suser.getEmail(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getActivity(), SchoolDashBoard.class));
                            getActivity().finish();



                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());



        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String username=prefs.getString("School_username","");
        String pass=prefs.getString("School_password","");

        if(username.equals("")&&pass.equals("")) {
            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Logging...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(username, pass)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(getActivity(), SchoolDashBoard.class));
                                getActivity().finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}