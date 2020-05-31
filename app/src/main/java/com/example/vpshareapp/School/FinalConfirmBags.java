package com.example.vpshareapp.School;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.R;
import com.example.vpshareapp.commanderacivty.CommanderDashBorad;
import com.example.vpshareapp.commanderacivty.UpdateBagStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FinalConfirmBags  extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{



    //process dialog
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bags");

    TextView updateStatus_bagName,updateStatus_bagId,updateStatus_commanderId,updateStatus_bagLocation,updateStatus_StatusOfBags;

    String barcode;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    TextView updateStatusBtn;
    String bagid;

    //user Mail that will be user when sending mail
    String mail="",subject="",message="";
    String bagStatus="3";

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_confirm_bags);



        progressDialog=new ProgressDialog(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Confirmation");
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
        updateStatus_StatusOfBags=findViewById(R.id.updateStatus_StatusOfBags);
        updateStatusBtn=findViewById(R.id.updateStatusBtn);



        //init firebase
        mAuth = FirebaseAuth.getInstance();




        //get data of bag
        final Query query=ref.orderByChild("Barcode").equalTo(barcode);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String CommanderId=""+ds.child("CommanderId").getValue();
                    bagid=""+ds.child("bagid").getValue();
                    String location=""+ds.child("location").getValue();
                    String status=""+ds.child("status").getValue();

                    updateStatus_bagName.setText(barcode);
                    updateStatus_bagId.setText(bagid);
                    updateStatus_commanderId.setText(CommanderId);
                    updateStatus_bagLocation.setText(location);
                    if(status.equals("0")){
                        //bag hold by user
                        updateStatus_StatusOfBags.setText("User Allocated To Bags");
                    }
                    else if(status.equals("1")){
                        updateStatus_StatusOfBags.setText("Commander Collected Bags");
                    }
                    else if(status.equals("2")){
                        updateStatus_StatusOfBags.setText("Commander Sending Bag To NGO");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get User info

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("Barcode").equalTo(barcode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get info
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    //
                    mail=ds.child("Email").getValue()+"";

                    Toast.makeText(FinalConfirmBags.this, ""+mail, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(FinalConfirmBags.this, "Status Updated..", Toast.LENGTH_SHORT).show();
                            String child=ds.getKey();
                            dataSnapshot.getRef().child(child).child("status").setValue(bagStatus);
                            //now send mail to user

                           sendMailToUser();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private void sendMailToUser() {

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        String nameOfSchool=user.getEmail();
        Toast.makeText(this, ""+mail, Toast.LENGTH_SHORT).show();

        subject="Donation Collected By "+nameOfSchool;
        message="Hello %DISPLAY_NAME%,\n" +
                "\n" +
                "Your Donation Collected By " +nameOfSchool+"\n"+
                "\n" +
                "Thank You " +mail+" For Contribution \n"+
                "\n" +
                "Thanks From ," +nameOfSchool+"\n"+
                "\n" +
                "Your project-576045884629 team";
        JavaMailAPI javaMailAPI=new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();


        //get location

        Intent intent=new Intent(this, getSchoolLocation.class);
        intent.putExtra("barcode",barcode);
        this.startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
