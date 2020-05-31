package com.example.vpshareapp.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.vpshareapp.Adapter.AdapterAllocatedUser;
import com.example.vpshareapp.Model.ModelAllocatedUser;
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

public class UserUnderCommander extends AppCompatActivity {



    FirebaseAuth firebaseAuth;

    SharedPreferences.Editor editor;

    AdapterAllocatedUser adapterUsers;
    List<ModelAllocatedUser> userList;

    RecyclerView recyclerView;

    String MyID;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_under_commander);


        //init
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth= FirebaseAuth.getInstance();
        MyID=firebaseAuth.getUid();
        // Toast.makeText(getActivity(), ""+MyID, Toast.LENGTH_SHORT).show();


        userList=new ArrayList<>();


        getalluser();
        checkforuserlogin();

    }
    private void getalluser() {


        Intent intent=getIntent();
        final String commanderId=intent.getStringExtra("commanderId");
        if(commanderId.equals("")){

        }
        else {

            final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ModelAllocatedUser modelUser = ds.getValue(ModelAllocatedUser.class);

                        if (modelUser.getCommanderId().equals(commanderId)) {
                            userList.add(modelUser);
                        }

                        adapterUsers = new AdapterAllocatedUser(UserUnderCommander.this, userList);

                        recyclerView.setAdapter(adapterUsers);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkforuserlogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {


        }
        else{
            startActivity(new Intent(this, AllocatedBags.class));
            try {
                Objects.requireNonNull(this).finish();
            }catch (NullPointerException e){

            }
        }
    }


}
