package com.example.vpshareapp.Admin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vpshareapp.AboutPage;
import com.example.vpshareapp.Adapter.AdapterAllCommander;
import com.example.vpshareapp.Adapter.AdapterAllocatedUser;
import com.example.vpshareapp.Model.ModelAllocatedUser;
import com.example.vpshareapp.Model.ModelCommander;
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
public class AllCommanderDetail extends Fragment {


    FirebaseAuth firebaseAuth;

    SharedPreferences.Editor editor;

    AdapterAllCommander adapterAllCommander;
    List<ModelCommander> commanderList;

    RecyclerView recyclerView;

    TextView checkLocationBtn;

    String MyID;

    public AllCommanderDetail() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_all_commander_detail, container, false);

        //init
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        checkLocationBtn=view.findViewById(R.id.checkLocationBtn);

        firebaseAuth= FirebaseAuth.getInstance();
        MyID=firebaseAuth.getUid();
        // Toast.makeText(getActivity(), ""+MyID, Toast.LENGTH_SHORT).show();

        commanderList=new ArrayList<>();

        //get all bag locations
        checkLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),getAllBagsLocation.class);
                startActivity(intent);
            }
        });


        getallCommander();
        checkforuserlogin();

        return view;
    }
    private void getallCommander() {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Commanders");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commanderList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelCommander modelCommander= ds.getValue(ModelCommander.class);


                        commanderList.add(modelCommander);


                    adapterAllCommander = new AdapterAllCommander(getActivity(), commanderList);

                    recyclerView.setAdapter(adapterAllCommander);
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
