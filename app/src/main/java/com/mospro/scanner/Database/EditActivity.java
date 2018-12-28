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
import com.mospro.scanner.R;
public class EditActivity extends AppCompatActivity {
    private EditText name , price , limits , units , disc ;
    private int productKey ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        productKey = getIntent().getIntExtra("productKey" , 0);
        declare();
    }
    private void declare() {
        name=(EditText)findViewById(R.id.editText_name_EditProductActivity);
        disc=(EditText)findViewById(R.id.editText_disc_EditProductActivity);
        limits=(EditText)findViewById(R.id.editText_limits_EditProductActivity);
        units=(EditText)findViewById(R.id.editText_units_EditProductActivity);
        price=(EditText)findViewById(R.id.editText_price_EditProductActivity);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.action_edit1)
        {
            String nameStr = name.getText().toString();
            String discStr =disc.getText().toString();
            String limitsStr =limits.getText().toString();
            String unitsStr =units.getText().toString();
            String priceStr =price.getText().toString();
            Integer limitsInt = Integer.valueOf(limitsStr) ;
            Integer unitsInt = Integer.valueOf(unitsStr) ;
            Integer priceInt = Integer.valueOf(priceStr) ;
            if(!TextUtils.isEmpty(nameStr) || !TextUtils.isEmpty(discStr)) {
                SharedPreferences myPref = this.getSharedPreferences("createDB" , Context.MODE_PRIVATE) ;
                String dbName =  myPref.getString("KeyName" ,"create database");
                SQLiteDatabase myDatabase = this.openOrCreateDatabase(dbName ,Context.MODE_PRIVATE , null) ;
                myDatabase.execSQL(" UPDATE "+dbName+" SET name='"+ nameStr+"' WHERE id = "+productKey+"");
                myDatabase.execSQL(" UPDATE "+dbName+" SET disc='"+ discStr+"' WHERE id = "+productKey+"");
                myDatabase.execSQL(" UPDATE "+dbName+" SET limits="+ limitsInt+" WHERE id = "+productKey+"");
                myDatabase.execSQL(" UPDATE "+dbName+" SET units='"+ unitsInt+"' WHERE id = "+productKey+"");
                myDatabase.execSQL(" UPDATE "+dbName+" SET price='"+ priceInt+"' WHERE id = "+productKey+"");
                startActivity(new Intent(EditActivity.this , DatabaseActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
