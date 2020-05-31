package com.example.vpshareapp.commanderacivty;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommanderAccount extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    String uid;

    //all acitvity component
    TextView commanderName,commanderEmail,commanderPhone,commanderId,commanderCity,commanderCountry,commanderAddress,commanderAllocated,commanderPassword;


    //logout
    TextView logoutBtn;
    public CommanderAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_commander_account, container, false);



        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Commanders");

        commanderName=view.findViewById(R.id.commander_name);
        commanderEmail=view.findViewById(R.id.commander_email);
        commanderPhone=view.findViewById(R.id.commander_phone);
        commanderId=view.findViewById(R.id.commander_uId);
        logoutBtn=view.findViewById(R.id.logout);
        commanderCity=view.findViewById(R.id.commander_city);
        commanderCountry=view.findViewById(R.id.commander_country);
        commanderAddress=view.findViewById(R.id.commander_address);
        commanderAllocated=view.findViewById(R.id.commander_allocate);
        commanderPassword=view.findViewById(R.id.commander_password);

        loadData();


        //handle logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor;
                editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.remove("Com_username");
                editor.remove("Com_password");
                editor.apply();
                firebaseAuth.signOut();
                checkforuserlogin();
            }
        });

        return view;
    }

    private void loadData() {
        Query query=databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name=""+ds.child("Name").getValue();
                    String address=""+ds.child("Address").getValue();
                    String email=""+ds.child("Email").getValue();
                    String allocate=""+ds.child("Allocated_Bags").getValue();
                    String phone=""+ds.child("Phone").getValue();
                    String uid=""+ds.child("uid").getValue();
                    String city=""+ds.child("city").getValue();
                    String country=""+ds.child("country").getValue();
                    String password=""+ds.child("Password").getValue();

                  //  Toast.makeText(getContext(), ""+email, Toast.LENGTH_SHORT).show();


                    //init
                    commanderName.setText(name);
                    commanderAddress.setText(address);
                    commanderEmail.setText(email);
                    commanderId.setText(uid);
                    commanderCity.setText(city);
                    commanderPhone.setText(phone);
                    commanderCountry.setText(country);
                    commanderAllocated.setText(allocate);
                    commanderPassword.setText(password);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkforuserlogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            uid=user.getUid();


        }
        else{
            startActivity(new Intent(getActivity(), MainActivity.class));
            try {
                Objects.requireNonNull(getActivity()).finish();
            }catch (NullPointerException e){

            }
        }
    }

}
