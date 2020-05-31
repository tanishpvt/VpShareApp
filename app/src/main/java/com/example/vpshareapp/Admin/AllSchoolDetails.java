package com.example.vpshareapp.Admin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpshareapp.Adapter.AdapterAllCommander;
import com.example.vpshareapp.Adapter.AdapterSchool;
import com.example.vpshareapp.Model.ModelCommander;
import com.example.vpshareapp.Model.ModelSchool;
import com.example.vpshareapp.R;
import com.example.vpshareapp.commanderacivty.AllocatedBags;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllSchoolDetails extends Fragment {


    FirebaseAuth firebaseAuth;

    SharedPreferences.Editor editor;

    AdapterSchool adapterSchool;
    List<ModelSchool> schoolList;

    RecyclerView recyclerView;

    String MyID;

    public AllSchoolDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_school_details, container, false);

        //init
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseAuth= FirebaseAuth.getInstance();
        MyID=firebaseAuth.getUid();
        // Toast.makeText(getActivity(), ""+MyID, Toast.LENGTH_SHORT).show();

        schoolList=new ArrayList<>();


        getallSchool();

        checkforuserlogin();

        return view;
    }

    private void getallSchool() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("School");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                schoolList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelSchool modelSchool= ds.getValue(ModelSchool.class);


                    schoolList.add(modelSchool);


                    adapterSchool = new AdapterSchool(getActivity(), schoolList);

                    recyclerView.setAdapter(adapterSchool);
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


        }
        else{
            startActivity(new Intent(getActivity(), AllocatedBags.class));
            try {
                Objects.requireNonNull(getActivity()).finish();
            }catch (NullPointerException e){

            }
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}
