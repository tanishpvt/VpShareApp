package com.example.vpshareapp.commanderacivty;


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
import android.widget.Toast;

import com.example.vpshareapp.Adapter.AdapterAllocatedUser;
import com.example.vpshareapp.Model.ModelAllocatedUser;
import com.example.vpshareapp.R;
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
public class AllocatedBags extends Fragment {



    FirebaseAuth firebaseAuth;

    SharedPreferences.Editor editor;

    AdapterAllocatedUser adapterUsers;
    List<ModelAllocatedUser> userList;

    RecyclerView recyclerView;

    String MyID;

    public AllocatedBags() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_allocated_bags, container, false);

        //init
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseAuth= FirebaseAuth.getInstance();
        MyID=firebaseAuth.getUid();
       // Toast.makeText(getActivity(), ""+MyID, Toast.LENGTH_SHORT).show();


        userList=new ArrayList<>();


        getalluser();
        checkforuserlogin();

        return view;
    }

    private void getalluser() {


        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelAllocatedUser modelUser = ds.getValue(ModelAllocatedUser.class);

                    if (modelUser.getCommanderId().equals(MyID)) {
                        userList.add(modelUser);
                   }

                    adapterUsers = new AdapterAllocatedUser(getActivity(), userList);

                    recyclerView.setAdapter(adapterUsers);
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
            MyID=user.getUid();

        }
        else{
            startActivity(new Intent(getActivity(),AllocatedBags.class));
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
