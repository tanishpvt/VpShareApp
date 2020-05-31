package com.example.vpshareapp.ui.commander;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vpshareapp.R;
import com.example.vpshareapp.commanderacivty.CommanderDashBorad;
import com.example.vpshareapp.commanderacivty.CommanderQrScan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CommanderRegisterFrag extends Fragment {

    private SendViewModel sendViewModel;

    Button commander_scan;
    String barcodes="";
    TextView barcode_id;
    Button admin_login_submit,commander_SignIn;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;


    EditText name_Et,email_Et,address_Et,phone_Et,city_Et,country_Et,password_Et,user_login_Area;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_send, container, false);

        editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

        commander_scan=root.findViewById(R.id.commander_scan);
        barcode_id=root.findViewById(R.id.barcode_id);
        admin_login_submit=root.findViewById(R.id.admin_login_submit);
        commander_SignIn=root.findViewById(R.id.commander_SignIn);
        name_Et=root.findViewById(R.id.user_login_name);
        email_Et=root.findViewById(R.id.user_login_email);
        address_Et=root.findViewById(R.id.user_login_Address);
        phone_Et=root.findViewById(R.id.user_login_PhoneNo);
        city_Et=root.findViewById(R.id.user_login_city);
        country_Et=root.findViewById(R.id.user_login_Country);
        password_Et=root.findViewById(R.id.user_login_password);
        user_login_Area=root.findViewById(R.id.user_login_Area);
        progressDialog=new ProgressDialog(getActivity(),4);


        commander_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), CommanderSignIn.class));
            }
        });

        admin_login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user info
                String name,email,address,phone,city,country,password,area;
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
                area=user_login_Area.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


                String alocated_Bags=prefs.getString("bags","");



                if(email.equals("")||name.equals("")||address.equals("")||phone.equals("")||
                        city.equals("")||country.equals("")||password.equals("")){
                    Toast.makeText(getActivity(), "fill all", Toast.LENGTH_SHORT).show();
                }
                else if(alocated_Bags.equals("")){
                    Toast.makeText(getActivity(), "scan all bags Qr codes", Toast.LENGTH_SHORT).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
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
                    editor.putString("Com_username", email.trim());
                    editor.putString("Com_password", password.trim());
                    editor.apply();
                    registerCommander(name,email,address,phone,city,country,password,alocated_Bags,area);

                }
            }
        });

        commander_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scan qr
                Intent intent=new Intent(getActivity(), CommanderQrScan.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void registerCommander(final String name, String email, final String address, final String phone,
                                   final String city, final String country, final String password, final String
                                           alocated_bags, final String area) {
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String zero="0";
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            String email= user.getEmail();
                            String uid=user.getUid();
                            HashMap<Object,String> hashMap=new HashMap<>();


                            hashMap.put("Name",name);
                            hashMap.put("Email",email);
                            hashMap.put("Address",address);
                            hashMap.put("Phone",phone);
                            hashMap.put("city",city);
                            hashMap.put("country",country);
                            hashMap.put("area",area);
                            hashMap.put("Password",password);
                            hashMap.put("Allocated_Bags",alocated_bags);
                            hashMap.put("isAuthorized",zero);
                            hashMap.put("uid",uid);

                            FirebaseDatabase database=FirebaseDatabase.getInstance();

                            DatabaseReference reference=database.getReference("Commanders");
                            reference.child(name).setValue(hashMap);
                            //sucess
                            Toast.makeText(getActivity(), "Registered with "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            editor.remove("bags");
                            editor.apply();

                            startActivity(new Intent(getActivity(), CommanderDashBorad.class));
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
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String alocated_Bags=prefs.getString("bags","");

        if(alocated_Bags.equals("")){
            barcode_id.setText("scan qr and set Qr");
        }
        else {
            barcode_id.setText(alocated_Bags);
        }

        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String username=prefs.getString("Com_username","");
        String pass=prefs.getString("Com_password","");

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
                                startActivity(new Intent(getActivity(), CommanderDashBorad.class));
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

    }
}