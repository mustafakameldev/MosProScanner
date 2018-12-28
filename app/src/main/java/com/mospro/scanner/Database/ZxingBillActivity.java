package com.mospro.scanner.Database;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.google.zxing.Result;
import com.mospro.scanner.R;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ZxingBillActivity extends  AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1 ;
    private ZXingScannerView scannerView ;
    private FrameLayout container ;
    private ArrayList<String> keys ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this) ;
        setContentView(scannerView);
        keys =new ArrayList<>() ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
            }else
            {
                requestPermission();
            }
        }
    }
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(ZxingBillActivity.this ,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) ;
    }

    void requestPermission()
    {
        ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,  String[] permissions,  int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_CAMERA :
                if(grantResults.length >0 )
                {
                    boolean cameraAccepted = grantResults[0] ==PackageManager.PERMISSION_GRANTED ;
                    if(cameraAccepted)
                    {

                    }else {
                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
                        {
                            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                            {
                                displayAlertMessage("you need to allow access to both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{Manifest.permission.CAMERA} ,REQUEST_CAMERA);
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                if(scannerView == null)
                {
                    scannerView = new ZXingScannerView(this) ;
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }
    public void displayAlertMessage(String message , DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(ZxingBillActivity.this)
                .setMessage(message)
                .setPositiveButton("OK" ,listener)
                .setNegativeButton("Cancel" , null)
                .create()
                .show();
    }






    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        keys.add(scanResult);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("scan title")
                .setPositiveButton(R.string.oneMore, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.resumeCameraPreview(ZxingBillActivity.this);
                    }
                });
        builder.setNeutralButton(R.string.showList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent paid = new Intent(ZxingBillActivity.this, BillActivity.class);
                paid.putExtra("keys", keys);
                startActivity(paid);
            }
        });
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();
    }


}
