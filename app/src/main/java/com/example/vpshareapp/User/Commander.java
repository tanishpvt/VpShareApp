package com.example.vpshareapp.User;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.Adapter.AdapterAllocatedUser;
import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.Model.ModelAllocatedUser;
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
public class Commander extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    public String commanderNametxt;
    String uid="0",comanderId="";


    //all acitvity component
    TextView commanderName,commanderEmail,commanderPhone,commanderId,commanderCity,commanderCountry,commanderAddress,commanderAllocated;

    String barcode;

    public Commander() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_commander, container, false);
        //init firebase
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();



        commanderName=view.findViewById(R.id.commander_name);
        commanderEmail=view.findViewById(R.id.commander_email);
        commanderPhone=view.findViewById(R.id.commander_phone);
        commanderId=view.findViewById(R.id.commander_uId);
        commanderCity=view.findViewById(R.id.commander_city);
        commanderCountry=view.findViewById(R.id.commander_country);
        commanderAddress=view.findViewById(R.id.commander_address);
        commanderAllocated=view.findViewById(R.id.commander_allocate);
        databaseReference=firebaseDatabase.getReference("Users");



        final Query getcomanderId=databaseReference.orderByChild("Email").equalTo(user.getEmail());
        getcomanderId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //getting  commander uid
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    comanderId = ""+ds.child("CommanderId").getValue();
                    commanderNametxt = ""+ds.child("CommanderName").getValue();
                    barcode = ""+ds.child("Barcode").getValue();
                    //   Toast.makeText(getContext(), ""+commanderId1, Toast.LENGTH_SHORT).show();
                    commanderId.setText(comanderId);
                    commanderName.setText(commanderNametxt);
                    getcommander(commanderNametxt);
                    //  Toast.makeText(getActivity(), ""+commanderNametxt, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





/*

        //updating comander  Id in user database
        Query query1=FirebaseDatabase.getInstance().getReference("Users").orderByChild("bagid").equalTo(uid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(uid).child("CommanderId").setValue(commanderId.getText().toString());
                dataSnapshot.getRef().child(uid).child("CommanderName").setValue(commanderName.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //updating comander  Id in bags database
        Query query2=FirebaseDatabase.getInstance().getReference("Bags").orderByChild("bagid").equalTo(uid);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(barcode).child("CommanderId").setValue(commanderId.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

 */






        checkforuserlogin();
    return view;
    }

    public void getcommander(String commanderName){
        //  String comName=commanderName+" Commander";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Commanders");
        // Toast.makeText(getActivity(), ""+commanderName.getText().toString(), Toast.LENGTH_SHORT).show();
        Query query=ref.orderByChild("Name").equalTo(commanderName);
        Toast.makeText(getActivity(), ""+ commanderName, Toast.LENGTH_SHORT).show();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    //   if (ds.child("uid").equals(comanderId)) {


                    String name = "" + ds.child("Name").getValue();
                    String address = "" + ds.child("Address").getValue();
                    String email = "" + ds.child("Email").getValue();
                    String allocate = "" + ds.child("Allocated_Bags").getValue();
                    String phone = "" + ds.child("Phone").getValue();
                    comanderId = "" + ds.child("uid").getValue();
                    String city = "" + ds.child("city").getValue();
                    String country = "" + ds.child("country").getValue();

                    //Toast.makeText(getContext(), ""+email, Toast.LENGTH_SHORT).show();


                    //init
                    //  commanderName.setText(name);
                    commanderAddress.setText(address);
                    commanderEmail.setText(email);
                    //  commanderId.setText(comanderId);
                    commanderCity.setText(city);
                    commanderPhone.setText(phone);
                    commanderCountry.setText(country);
                    commanderAllocated.setText(barcode);
                }
                // }
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout){
            SharedPreferences.Editor editor;
            editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editor.remove("username");
            editor.remove("password");
            editor.apply();
            firebaseAuth.signOut();
            checkforuserlogin();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();



    }
}
