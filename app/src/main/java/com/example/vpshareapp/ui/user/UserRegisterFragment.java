package com.example.vpshareapp.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vpshareapp.R;
import com.example.vpshareapp.User.User_Dasboard;
import com.example.vpshareapp.User.User_Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserRegisterFragment extends Fragment {


    String barcode="";
    TextView barcode_txt,commander_id;

    EditText name_Et,email_Et,address_Et,phone_Et,city_Et,country_Et,password_Et,user_login_Area;
    CheckBox book_cb,pencil_cn,marker_cn,sarnpner_cn,other_cn;
    String commanderName="";
    //process dialog
    ProgressDialog progressDialog;
    String ComanderName="";


    Button submit,singIn;

    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);




        //init edit text
        //    Bundle intent=getIntent().getExtras();
        //    barcode=intent.getString("barcode");
        barcode_txt=root.findViewById(R.id.barcode_id);
        barcode_txt.setText(barcode);
        name_Et=root.findViewById(R.id.user_login_name);
        email_Et=root.findViewById(R.id.user_login_email);
        address_Et=root.findViewById(R.id.user_login_Address);
        phone_Et=root.findViewById(R.id.user_login_PhoneNo);
        city_Et=root.findViewById(R.id.user_login_city);
        country_Et=root.findViewById(R.id.user_login_Country);
        password_Et=root.findViewById(R.id.user_login_password);
        commander_id=root.findViewById(R.id.commander_id);
        user_login_Area=root.findViewById(R.id.user_login_Area);
        submit=root.findViewById(R.id.user_login_submit);
        progressDialog=new ProgressDialog(getActivity(),4);

        //init check box
        book_cb=root.findViewById(R.id.checkBox_book);
        pencil_cn=root.findViewById(R.id.checkBox_pencil);
        marker_cn=root.findViewById(R.id.checkBox_Markers);
        sarnpner_cn=root.findViewById(R.id.checkBox_Pencil_sharpener);
        other_cn=root.findViewById(R.id.checkBox_other);
        singIn=root.findViewById(R.id.user_login_signIn);

        //init firebase
        mAuth = FirebaseAuth.getInstance();

        progressDialog.setMessage("Registering user...");







        //handle btn if user is already register and he/she want to just sign in
        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), User_Login.class));
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

                if(book_cb.isChecked()){
                    donated +=" Books";
                }
                if(pencil_cn.isChecked()){
                    donated +=" Pencil";
                }
                if(marker_cn.isChecked()){
                    donated +=" Maker";
                }
                if(sarnpner_cn.isChecked()){
                    donated +=" Pencil Sarpner";
                }
                if(other_cn.isChecked()){
                    donated +=" Other Stuff";
                }
                if(donated.equals("")) {

                    donated+="Not Selected Yet";
                }


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
                    editor= PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                    editor.putString("username", email.trim());
                    editor.putString("password", password.trim());
                    editor.apply();

                    registerUser(name,email,address,phone,city,country,password,donated,barcode,commander_id1,area);

                }

            }
        });


        return root;
    }
    private void registerUser(final String name, String email, final String address, final String phone,
                              final String city, final String country, final String password, final String donated
            , final String barcode, final String commander_id1, final String area) {


        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {



                            progressDialog.dismiss();
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
                            Toast.makeText(getActivity(), "Registered with "+user.getEmail(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getActivity(), User_Dasboard.class));
                            getActivity().finish();


                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });





    }



    @Override
    public void onStart() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String username=prefs.getString("username","");
        String pass=prefs.getString("password","");

        if(username.equals("")&&pass.equals("")) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
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
                                startActivity(new Intent(getActivity(), User_Dasboard.class));
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
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        super.onStart();
    }
}