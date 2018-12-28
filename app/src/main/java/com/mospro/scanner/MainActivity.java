package com.mospro.scanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mospro.scanner.Database.DatabaseActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button scanBtn , createDataBaseBtn , infoBtn , searchBtn;
    SharedPreferences createPref;
    private ImageView imgView ;
    private LinearLayout  mainLayout ;
    boolean frag = false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declare();
    }
    private void declare() {

        scanBtn =(Button)findViewById(R.id.btn_scan_mainActivity) ;
        scanBtn.setOnClickListener(this);

        createDataBaseBtn=(Button)findViewById(R.id.btn_createdb_mainActivity);
        createDataBaseBtn.setOnClickListener(this);
        infoBtn=(Button)findViewById(R.id.infoButton_MainActivity);
        infoBtn.setOnClickListener(this);
        imgView=(ImageView)findViewById(R.id.imageView) ;
        createPref = getApplicationContext().getSharedPreferences("createDB" , MODE_PRIVATE);
        SharedPreferences.Editor editor = createPref.edit() ;
        editor.commit() ;
        if(createPref.getBoolean("key1", false)){
            createDataBaseBtn.setText(R.string.createDatabase);
        }  else {
            createDataBaseBtn.setText(R.string.DBActivity);
        }

        String alternativeName = getString(R.string.createDatabase) ;
        String dbName = createPref.getString("KeyName"  , alternativeName) ;
        createDataBaseBtn.setText(dbName);
        searchBtn =(Button)findViewById(R.id.btn_history_mainActivity);
        searchBtn.setOnClickListener(this);
        createHistoryDB();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_info)
        {
            Toast.makeText(this, "Info will be ready", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v== createDataBaseBtn)
        {

            createDBAction();

        }else if (v== scanBtn)
        {
            startActivity(new Intent(this , ZxingActivity.class));
        }else if (v== infoBtn)
        {


            if(!frag)
            {
                InfoFragment fragment = new InfoFragment() ;
                getSupportFragmentManager().beginTransaction().replace(R.id.container ,fragment , fragment.getClass().getSimpleName())
                        .addToBackStack(null).commit() ;
                frag = true ;
            }else if (frag)
            {
                FragmentManager fm = this.getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                frag = false ;
            }
        }else if(v==searchBtn)
        {
            HistoryFragment fragment = new HistoryFragment() ;
            getSupportFragmentManager().beginTransaction().replace(R.id.container ,fragment , fragment.getClass().getSimpleName())
                    .addToBackStack(null).commit() ;
        }
    }
    @SuppressLint("ResourceType")
    private void createDBAction() {
        if(!createPref.getBoolean("key1", false)){
            CreateDB_Fragment fragment = new CreateDB_Fragment() ;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_down, R.anim.slide_up);
            transaction.replace(R.id.container, fragment).commit();

        }  else
            {
            startActivity(new Intent(MainActivity.this , DatabaseActivity.class));
        }
    }
    void createHistoryDB ()
    {
        SQLiteDatabase myDatabase = this.openOrCreateDatabase("history" , Context.MODE_PRIVATE ,null ) ;
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY AUTOINCREMENT,mKey VARCHAR ,year INT ,month INT , day INT ) ");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
