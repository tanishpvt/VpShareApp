package com.example.vpshareapp.User;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.R;
import com.example.vpshareapp.commanderacivty.CommanderDashBorad;
import com.example.vpshareapp.commanderacivty.UpdateBagStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class User_QrScan extends AppCompatActivity implements ZXingScannerView.ResultHandler {



    ProgressDialog progressDialog;

    //check box
    CheckBox book_cb,pencil_cn,marker_cn,sarnpner_cn,other_cn;

    //start scaning;
    Button startScan;
    //user donated
    String donated =" ";


    String uid;
    FirebaseAuth mAtuh;
    FirebaseUser user;
    private ZXingScannerView scannerView;
    private TextView textView;
    final String commanderId="Not Allocated";
    final String ComanderName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__qr_scan);
        mAtuh=FirebaseAuth.getInstance();


        //init
        progressDialog=new ProgressDialog(this);
        scannerView=findViewById(R.id.zxscan);
        textView=findViewById(R.id.txt_result);
        //init check box
        book_cb=findViewById(R.id.checkBox_book);
        pencil_cn=findViewById(R.id.checkBox_pencil);
        marker_cn=findViewById(R.id.checkBox_Markers);
        sarnpner_cn=findViewById(R.id.checkBox_Pencil_sharpener);
        other_cn=findViewById(R.id.checkBox_other);
        startScan=findViewById(R.id.startscan);


        //request permission
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {


                        scannerView.setResultHandler(User_QrScan.this);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(User_QrScan.this, "acces deined", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView.startCamera();
                if(book_cb.isChecked()){
                    donated +="Books ";
                }
                if(pencil_cn.isChecked()){
                    donated +="Pencil ";
                }
                if(marker_cn.isChecked()){
                    donated +="Maker ";
                }
                if(sarnpner_cn.isChecked()){
                    donated +="Pencil Sarpner ";
                }
                if(other_cn.isChecked()){
                    donated +="Other Stuff ";
                }
                if(donated.equals("")) {

                    donated+="Not Selected Yet";
                }
                scannerView.setResultHandler(User_QrScan.this);
                scannerView.startCamera();



            }
        });


    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        user=mAtuh.getCurrentUser();
        if(user!=null){
            uid=user.getUid();
        }
        else {
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(User_QrScan.this,User_Register.class));
            finish();

        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        scannerView.stopCamera();
        super.onStop();
    }



    @Override
    public void handleResult(final Result rawResult) {
        final String barcode=rawResult.getText();
        textView.setText(barcode);


        if(donated.equals("")) {
            Toast.makeText(this, "please select donated stuff", Toast.LENGTH_SHORT).show();
        }
        else {

            //do data update in user about donated and barcode
            final Intent intent = new Intent(User_QrScan.this, User_Dasboard.class);
            intent.putExtra("barcode", rawResult.getText());
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Commanders");

            //code that will  take commander  name id and will allocate to user
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String commander_alocate = String.valueOf(ds.child("Allocated_Bags").getValue());


                        if (commander_alocate.indexOf(barcode) != -1) {
                            String commanderId1 = String.valueOf(ds.child("uid").getValue());
                            Toast.makeText(User_QrScan.this, "reached" + commanderId, Toast.LENGTH_SHORT).show();

                            String comanderName = ds.child("Name").getValue() + "";

                            setallcommandervalues(commanderId1, comanderName, barcode, intent);
                        } else {
                            Toast.makeText(User_QrScan.this, "not found", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }









    }

    private void setallcommandervalues(final String comanderid1, final String comanderName, final String barcode, Intent intent) {

        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("Users");




        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");


        final Query query=reference.orderByChild("bagid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Toast.makeText(User_QrScan.this, "Status Updated..", Toast.LENGTH_SHORT).show();
                    String child=ds.getKey();
                    dataSnapshot.getRef().child(uid).child("Barcode").setValue(barcode);
                    dataSnapshot.getRef().child(uid).child("Donated").setValue(donated);

                    dataSnapshot.getRef().child(uid).child("CommanderId").setValue(comanderid1);
                    dataSnapshot.getRef().child(uid).child("CommanderName").setValue(comanderName);

                    // startActivity(new Intent(User_QrScan.this, User_Dasboard.class));
                    //finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference bagRef=FirebaseDatabase.getInstance().getReference("Bags");
        //making bag section it will store bag loction status commanderId userId
        final HashMap<Object,String> hashMap2=new HashMap<>();
        hashMap2.put("Barcode",barcode);
        hashMap2.put("bagid",uid);
        //default status 0 = bags allocated to User
        hashMap2.put("status","0");
        hashMap2.put("CommanderId",comanderid1);
        hashMap2.put("location","0");
        hashMap2.put("longitude","");
        hashMap2.put("latitude","");
        bagRef.child(barcode).setValue(hashMap2);
        startActivity(intent);


    }
}
