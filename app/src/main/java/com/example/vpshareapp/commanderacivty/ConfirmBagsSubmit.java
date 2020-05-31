package com.example.vpshareapp.commanderacivty;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmBagsSubmit extends Fragment implements ZXingScannerView.ResultHandler {



    ProgressDialog progressDialog;

    private ZXingScannerView scannerView;
    private TextView textView;


    public ConfirmBagsSubmit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_confirm_bags_submit, container, false);

        //init
        progressDialog=new ProgressDialog(getContext());
        scannerView=view.findViewById(R.id.zxscan);
        textView=view.findViewById(R.id.txt_result);
        //request permission
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        scannerView.setResultHandler(ConfirmBagsSubmit.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(getContext(), "acces deined", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        return view;
    }


    @Override
    public void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        scannerView.stopCamera();
        super.onStop();
    }



    @Override
    public void handleResult(Result rawResult) {
        textView.setText(rawResult.getText());
        Intent intent=new Intent(getContext(), UpdateBagStatus.class);
        intent.putExtra("barcode",rawResult.getText());
        startActivity(intent);
    }
}
