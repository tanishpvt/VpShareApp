package com.example.vpshareapp.School;


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
import android.widget.Button;
import android.widget.TextView;

import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.R;
import com.example.vpshareapp.User.User_QrScan;
import com.example.vpshareapp.ui.home.HomeFragment;
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
public class SchoolAccount extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    String uid;
    //all acitvity component
    TextView schoolName,schoolEmail,schoolPass,schoolPhone,schoolCity,schoolArea,schoolAddress,schoolRequired;

    //logout
    TextView  logoutBtn;


    public SchoolAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_school_account, container, false);
        //init firebase
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("School");


        schoolName=view.findViewById(R.id.schoolName);
        schoolEmail=view.findViewById(R.id.schoolEmail);
        schoolPass=view.findViewById(R.id.schoolPass);
        schoolPhone=view.findViewById(R.id.schoolPhone);
        schoolCity=view.findViewById(R.id.schoolCity);
        schoolArea=view.findViewById(R.id.schoolArea);
        schoolAddress=view.findViewById(R.id.schoolAddress);
        schoolRequired=view.findViewById(R.id.schoolRequired);
        logoutBtn=view.findViewById(R.id.logout);




        //handle logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor;
                editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.remove("School_username");
                editor.remove("School_password");
                editor.apply();
                firebaseAuth.signOut();
                checkforuserlogin();
            }
        });


        Query query=databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String name=""+ds.child("Name").getValue();
                    String address=""+ds.child("Address").getValue();
                    String email=""+ds.child("Email").getValue();
                    String pass=""+ds.child("Password").getValue();
                    String phone=""+ds.child("Phone").getValue();
                    String area=""+ds.child("area").getValue();
                    String city=""+ds.child("city").getValue();
                    String requiredstuff=""+ds.child("requiredStuff").getValue();




                    //init
                    schoolName.setText(name);
                    schoolAddress.setText(address);
                    schoolEmail.setText(email);
                    schoolPass.setText(pass);
                    schoolPhone.setText(phone);
                    schoolArea.setText(area);
                    schoolCity.setText(city);
                    schoolRequired.setText(requiredstuff);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


        return view;
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
            editor.remove("School_username");
            editor.remove("School_password");
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

}
