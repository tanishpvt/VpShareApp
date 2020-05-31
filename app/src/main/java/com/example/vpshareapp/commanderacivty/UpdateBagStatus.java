package com.example.vpshareapp.commanderacivty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UpdateBagStatus extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{



    //process dialog
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bags");

    TextView updateStatus_bagName,updateStatus_bagId,updateStatus_commanderId,updateStatus_bagLocation;

    String barcode;

    TextView updateStatusBtn;

    String bagStatus="0";
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bag_status);


        progressDialog=new ProgressDialog(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Status");
        //enable button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //init edit text
        Intent intent=getIntent();
        barcode=intent.getStringExtra("barcode");
        updateStatus_bagId=findViewById(R.id.updateStatus_bagId);
        updateStatus_bagName=findViewById(R.id.updateStatus_bagName);
        updateStatus_commanderId=findViewById(R.id.updateStatus_commanderId);
        updateStatus_bagLocation=findViewById(R.id.updateStatus_bagLocation);
        updateStatusBtn= findViewById(R.id.updateStatusBtn);


        //init sippner
       spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //init firebase
        mAuth = FirebaseAuth.getInstance();


        //get data of bag
        final Query query=ref.orderByChild("Barcode").equalTo(barcode);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String CommanderId=""+ds.child("CommanderId").getValue();
                    String bagid=""+ds.child("bagid").getValue();
                    String location=""+ds.child("location").getValue();

                    updateStatus_bagName.setText(barcode);
                            updateStatus_bagId.setText(bagid);
                    updateStatus_commanderId.setText(CommanderId);
                            updateStatus_bagLocation.setText(location);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updateStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Query query=ref.orderByChild("Barcode").equalTo(barcode);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            Toast.makeText(UpdateBagStatus.this, "Status Updated..", Toast.LENGTH_SHORT).show();
                            String child=ds.getKey();
                            dataSnapshot.getRef().child(child).child("status").setValue(bagStatus);
                            //check is bag is collected by commander
                            if(bagStatus.equals("1")){
                                Toast.makeText(UpdateBagStatus.this, "update commander location to firebase server", Toast.LENGTH_SHORT).show();
                            }
                            else if(bagStatus.equals("2")){
                                Toast.makeText(UpdateBagStatus.this, "Admin location to firebase server will be updated when admin recive or open the app", Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(UpdateBagStatus.this, CommanderDashBorad.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    //setting status
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("User Allocated To Bags")){
            bagStatus="0";
            Toast.makeText(this, ""+bagStatus, Toast.LENGTH_SHORT).show();
        }
        else if (text.equals("Commander Collected Bags")){
            bagStatus="1";

            Toast.makeText(this, ""+bagStatus, Toast.LENGTH_SHORT).show();
        }
        else if (text.equals("Commander Sending Bags NGO")){
            bagStatus="2";

            Toast.makeText(this, ""+bagStatus, Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
