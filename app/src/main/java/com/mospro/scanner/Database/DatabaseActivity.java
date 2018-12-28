package com.mospro.scanner.Database;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mospro.scanner.MainActivity;
import com.mospro.scanner.R;

public class DatabaseActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ProductsFragment fragment1 = new ProductsFragment() ;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment1,
                            fragment1.getClass().getSimpleName()).commit() ;
                    return true;
                case R.id.navigation_notifications:
                    ShortcutsFragment fragment = new ShortcutsFragment() ;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment,
                            fragment.getClass().getSimpleName()).commit() ;
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batabase);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ProductsFragment fragment = new ProductsFragment() ;
        getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment, fragment.getClass()
                        .getSimpleName()).commit() ;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.database , menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.add_action)
        {
            startActivity(new Intent(DatabaseActivity.this , ZxingAdd.class));
        }else if (item.getItemId() ==R.id.sale_action)
        {
            startActivity(new Intent(DatabaseActivity.this , ZxingBillActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DatabaseActivity.this , MainActivity.class));
    }
}
