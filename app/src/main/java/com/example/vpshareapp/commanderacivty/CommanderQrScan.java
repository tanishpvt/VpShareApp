package com.example.vpshareapp.commanderacivty;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CommanderQrScan  extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private ZXingScannerView scannerView;
    private TextView textView;
    String result="";
    Button allocate_bags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander_qr_scan);

        //init
        scannerView=findViewById(R.id.zxscan);
        textView=findViewById(R.id.txt_result);

        //request permission
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        scannerView.setResultHandler(CommanderQrScan.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(CommanderQrScan.this, "acces deined", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();



    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        scannerView.stopCamera();
        super.onStop();
    }

    @Override
    public void handleResult(Result rawResult) {
        result=result+" "+rawResult.getText();
        textView.setText(result);
        //start acitivty
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences=getSharedPreferences("Remember",MODE_PRIVATE);
        editor= PreferenceManager.getDefaultSharedPreferences(this).edit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String alocated_Bags=prefs.getString("bags","");
        editor.putString("bags", (alocated_Bags+result));
        editor.apply();
        Intent intent=new Intent(CommanderQrScan.this, CommanderLogin.class);
        startActivity(intent);
       }
}
