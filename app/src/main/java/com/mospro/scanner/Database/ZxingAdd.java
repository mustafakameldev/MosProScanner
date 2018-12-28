package com.mospro.scanner.Database;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.zxing.Result;
import com.mospro.scanner.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ZxingAdd extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1 ;
    private ZXingScannerView scannerView ;
    private FrameLayout container ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this) ;
        setContentView(scannerView);
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
        return (ContextCompat.checkSelfPermission(ZxingAdd.this ,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) ;
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
        new AlertDialog.Builder(ZxingAdd.this)
                .setMessage(message)
                .setPositiveButton("OK" ,listener)
                .setNegativeButton("Cancel" , null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        final String scanResult = result.getText() ;


        if( !checkProduct(scanResult))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.addProductDB)
                    .setPositiveButton(R.string.addProduct, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scannerView.resumeCameraPreview(ZxingAdd.this);

                            //  Toast.makeText(ZxingAdd.this, scanResult, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ZxingAdd.this, AddProductActivity.class);
                            intent.putExtra("product", scanResult);
                            startActivity(intent);
                        }
                    });
            builder.setNeutralButton( R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }) ;
            builder.setMessage(scanResult);
            AlertDialog alert = builder.create() ;
            alert.show();
        }else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.productExists)
                    .setNeutralButton( R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }) ;
            builder.setMessage(scanResult);
            AlertDialog alert = builder.create() ;
            alert.show();
        }
    }

    private boolean checkProduct( String scanResult) {
        boolean checkItem = false ;
        SharedPreferences myPref = this.getSharedPreferences("createDB" , Context.MODE_PRIVATE) ;
        String dbName =  myPref.getString("KeyName" ,"create database");
        SQLiteDatabase myDatabase = this.openOrCreateDatabase(dbName ,Context.MODE_PRIVATE , null) ;
        try{
            Cursor cursor = myDatabase.rawQuery("SELECT * FROM "+ dbName ,null) ;
            int mkeyCur=cursor.getColumnIndex("mKey");
            cursor.moveToFirst();
            while (cursor !=null)
            {
                Log.i( "mKey", cursor.getString(mkeyCur));
                if(scanResult.equals(cursor.getString(mkeyCur)))
                {
                    checkItem = true ;

                }
                cursor.moveToNext() ;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return checkItem ;
    }
}
