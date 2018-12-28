package com.mospro.scanner.Database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.mospro.scanner.R;

public class AddProductActivity extends AppCompatActivity {
    private EditText nameEt , discEt , priceEt , limitsEt , unitsEt  ;
    private String mKey = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        declare() ;
    }
    private void declare() {
        mKey = getIntent().getStringExtra("product");
        nameEt=(EditText)findViewById(R.id.editText_name_AddProductActivity) ;
        discEt=(EditText)findViewById(R.id.editText_disc_AddProductActivity) ;
        priceEt=(EditText)findViewById(R.id.editText_price_AddProductActivity) ;
        limitsEt=(EditText)findViewById(R.id.editText_limits_AddProductActivity) ;
        unitsEt =(EditText)findViewById(R.id.editText_units_AddProductActivity);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addproduct_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_addProduct)
            {
                addProduct();
            }
        return super.onOptionsItemSelected(item);
    }
    private void addProduct() {
        String name = nameEt.getText().toString();
        String disc =discEt.getText().toString();
        String limits =limitsEt.getText().toString();
        String units =unitsEt.getText().toString();
        String price =priceEt.getText().toString();
        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(disc) || ! TextUtils.isEmpty(mKey) )
        {
            SharedPreferences createPref;
            createPref = this.getSharedPreferences("createDB", Context.MODE_PRIVATE);
            String namedb= createPref.getString("KeyName" ,"create database");
            SQLiteDatabase myDatabase = this.openOrCreateDatabase(namedb ,MODE_PRIVATE ,null) ;

           try{
               myDatabase.execSQL("INSERT INTO  "+ namedb+ "(mKey,name,disc,limits,units,price)VALUES ('"+mKey+"','"+name+"','"+disc+"',"+limits+","+units+","+price+")");

           }catch (Exception e)
           {
               e.printStackTrace();
               Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
           }
            Toast.makeText(this, name , Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddProductActivity.this , DatabaseActivity.class));
        }
    }
}
