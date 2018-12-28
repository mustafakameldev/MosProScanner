package com.mospro.scanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ZxingActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1 ;
    private ZXingScannerView scannerView ;
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
        return (ContextCompat.checkSelfPermission(ZxingActivity.this , CAMERA)== PackageManager.PERMISSION_GRANTED) ;
    }

    void requestPermission()
    {
        ActivityCompat.requestPermissions(ZxingActivity.this , new String[]{CAMERA},REQUEST_CAMERA);
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText() ;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.searchNet) ;

        builder.setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String escapedQuery = null;
                try {
                    escapedQuery = URLEncoder.encode(scanResult, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Date date = new Date(); // your date
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);




                SQLiteDatabase myDatabase = getApplicationContext().openOrCreateDatabase("history" , Context.MODE_PRIVATE ,null ) ;
                myDatabase.execSQL("INSERT INTO  history (mKey,year,month ,day)VALUES ('"+scanResult+"',"+year+" , "+month +", "+day+" )");

                Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


                // Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(scanResult));
                // startActivity(intent);
            }
        }) ;
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create() ;
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_CAMERA :
                if(grantResults.length >0 )
                {
                    boolean cameraAccepted = grantResults[0] ==PackageManager.PERMISSION_GRANTED ;
                    if(cameraAccepted)
                    {
                    }else {
                        Toast.makeText(this, "permission denied ", Toast.LENGTH_SHORT).show();
                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
                        {
                            if(shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displayAlertMessage("you need to allow access to both permissions", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{CAMERA} ,REQUEST_CAMERA);
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
    public void displayAlertMessage(String message , DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK" ,listener)
                .setNegativeButton("Cancel" , null)
                .create()
                .show();
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
}
