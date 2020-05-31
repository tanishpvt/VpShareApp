package com.example.vpshareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ThereProfileActivity extends AppCompatActivity {

    //recycle view
    RecyclerView postsRecycleView;

    FirebaseAuth firebaseAuth;

    SharedPreferences.Editor editor;


    //profile
    TextView user_name,user_email,user_phone,bag_id,barCode,allocate_commander,commander_Id,donated_stuff,user_Id,user_address,bag_Status;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);




        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("User");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        user_name=findViewById(R.id.user_name);
        user_email=findViewById(R.id.user_email);
        user_phone=findViewById(R.id.user_phone);
        bag_id=findViewById(R.id.bag_id);
        barCode=findViewById(R.id.barCode);
        allocate_commander=findViewById(R.id.allocate_commander);
        commander_Id=findViewById(R.id.commander_Id);
        donated_stuff=findViewById(R.id.donated_stuff);
        user_Id=findViewById(R.id.user_Id);
        user_address=findViewById(R.id.user_address);
        bag_Status=findViewById(R.id.bag_Status);
        //get uid of clicked posts
        Intent intent=getIntent();
        uid=intent.getStringExtra("hisId");

        //data base
        Query query= FirebaseDatabase.getInstance().getReference("Users").orderByChild("bagid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String address=ds.child("Address").getValue().toString();
                    String Barcode=ds.child("Barcode").getValue().toString();
                    String CommanderId=ds.child("CommanderId").getValue().toString();
                    String CommanderName=ds.child("CommanderName").getValue().toString();
                    String Email=ds.child("Email").getValue().toString();
                    String Name=ds.child("Name").getValue().toString();
                    String Phone=ds.child("Phone").getValue().toString();
                    String Donated=ds.child("Donated").getValue().toString();

                    user_name.setText("Name : "+Name);
                    user_email.setText("Email : "+Email);
                    user_phone.setText("Phone : "+Phone);
                    bag_id.setText("Bag Id : "+uid);
                    barCode.setText("BarCode : "+Barcode);
                    allocate_commander.setText("Commander Name : "+CommanderName);
                    commander_Id.setText("Commander Id : "+CommanderId);
                    donated_stuff.setText("Donated : "+Donated);
                    user_Id.setText("Uid : "+uid);
                    user_address.setText("Address : "+address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query query1= FirebaseDatabase.getInstance().getReference("Bags").orderByChild("bagid").equalTo(uid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String status=ds.child("status").getValue().toString();
                    if(status.equals("0")) {
                        bag_Status.setText("Status :User Allocated To Bag");
                    }
                    else if(status.equals("1")){
                        bag_Status.setText("Status :Commander Collected And Sending Bags To Admin");
                    }
                    else if(status.equals("2")){
                        //send Notification  to  User
                        bag_Status.setText("Status :Commander Sending Bags to NGO");
                    }
                    else if(status.equals("3")){
                        bag_Status.setText("Status :NGO School Collected Bags");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }




    public void checkforuserlogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            uid=user.getUid();


        }
        else{
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.action_logout){
            editor.remove("username");
            editor.remove("password");
            editor.apply();
            firebaseAuth.signOut();

            checkforuserlogin();
        }

        return super.onOptionsItemSelected(item);

    }


}
