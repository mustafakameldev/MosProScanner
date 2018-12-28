package com.mospro.scanner.Database;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mospro.scanner.Product;
import com.mospro.scanner.R;

import java.util.ArrayList;
public class BillActivity extends AppCompatActivity implements View.OnClickListener {
    private  ArrayList<String> keys;
    private  ArrayList<Product> products ;
    private BillAdapter adapter ;
    private ArrayList<Float> numbers;
    // activity component
    private RecyclerView recyclerView ;
    private Button btn_sum ;
    private TextView tv_sum ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill) ;
        getExtras();
        declare();
        retrieve();
    }
    private void declare() {
        products = new ArrayList<>();
        numbers = new ArrayList<>();
        recyclerView =(RecyclerView)findViewById(R.id.recycler_bill);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BillAdapter(this ,products);
        recyclerView.setAdapter(adapter);
        btn_sum =(Button)findViewById(R.id.btn_count_BillActivity) ;
        tv_sum=(TextView)findViewById(R.id.tv_count_BillActivity) ;
        tv_sum.setText(" ");
        btn_sum.setOnClickListener(this );
    }
    private void getExtras() {
        keys = new ArrayList<>();
        keys = getIntent().getExtras().getStringArrayList("keys") ;
        Log.i("Keys", keys.toString());
    }
    private void retrieve ()
    {
        SharedPreferences createPref;
        createPref = this.getSharedPreferences("createDB", Context.MODE_PRIVATE);
        String namedb= createPref.getString("KeyName" ,"create database");
        SQLiteDatabase myDatabase = this.openOrCreateDatabase(namedb ,MODE_PRIVATE ,null) ;
        for ( String mKey:keys) {
            Cursor cursor = myDatabase.rawQuery("SELECT * FROM "+ namedb +" WHERE mKey = '"+mKey+"' " ,null) ;
            if(cursor.getCount()>0) {
                int nameCur = cursor.getColumnIndex("name");
                int discCur =cursor.getColumnIndex("disc") ;
                int priceCur =cursor.getColumnIndex("price");
                int limitsCur =cursor.getColumnIndex("limits");
                int unitsCur=cursor.getColumnIndex("units");
                int idCur =cursor.getColumnIndex("id");
                cursor.moveToFirst();
                while(cursor!=null)
                {
                    Product product = new Product(cursor.getString(nameCur));
                    product.setLimits(cursor.getInt(limitsCur));
                    product.setUnits(cursor.getInt(unitsCur));
                    product.setDisc(cursor.getString(discCur));
                    product.setPrice(cursor.getInt(priceCur));
                    product.setId(cursor.getInt(idCur));
                    product.setmKey(mKey);
                    products.add(product);
                    adapter.notifyDataSetChanged();
                    break;
                }
            } else {

                Toast.makeText(this, mKey+ " not exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v==btn_sum)
        {
            tv_sum.setText(sum().toString());
        }
    }
    public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
        private Context context ;
        private  ArrayList<Product> products ;
        public BillAdapter(Context context , ArrayList<Product> products)
        {
            this.context = context ;
            this.products = products ;
        }
        @NonNull
        @Override
        public BillAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context) ;
            View view = inflater.inflate(R.layout.bill_layout , null) ;
            return new BillAdapter.BillViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull final BillViewHolder billViewHolder, final int i) {
            billViewHolder.name.setText( products.get(i).getName());
            billViewHolder.disc.setText(products.get(i).getDisc());
            billViewHolder.price.setText(String.valueOf("price : "+products.get(i).getPrice()));
            billViewHolder.limits.setText(String.valueOf("limits : "+products.get(i).getLimits()));
            billViewHolder.units.setText(String.valueOf("units : "+products.get(i).getUnits()));
            billViewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(TextUtils.isEmpty(billViewHolder.editNo.toString()))
                    {
                        billViewHolder.editNo.setText("0");
                    }else
                    {
                        Double no = Double.valueOf(billViewHolder.editNo.getText().toString());
                        no =no+1 ;
                        billViewHolder.editNo.setText(no.toString());
                    }

                }
            });
            billViewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(billViewHolder.editNo.getText().toString()))
                    {
                        Double no = Double.valueOf(billViewHolder.editNo.getText().toString());
                        if (no == 0)
                        {
                        }else if(no > 0) {
                            no = no - 1;
                        }
                        billViewHolder.editNo.setText(no.toString());
                    }
                }
            });
            billViewHolder.btnAddToTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i2 =i ;
                    float number =Float.valueOf(billViewHolder.editNo.getText().toString());
                    int edit = (int)number ;
                    numbers.add(number *products.get(i2).getPrice());
                    int position = products.get(i).getId();
                    int units = products.get(i2).getUnits() - edit ;
                    editItem(position , units);
                }
            });
        }
        @Override
        public int getItemCount() {
            return products.size();
        }

        public class BillViewHolder extends RecyclerView.ViewHolder {
            TextView name , disc , price , limits , units  ;
            ImageView add , minus ;
            EditText editNo ;
            Button btnAddToTotal ;
            public BillViewHolder(@NonNull View itemView) {
                super(itemView);
                name= (TextView)itemView.findViewById(R.id.name_BillLayout);
                disc =(TextView)itemView.findViewById(R.id.disc_BillLayout);
                price=(TextView)itemView.findViewById(R.id.price_BillLayout);
                limits=(TextView)itemView.findViewById(R.id.limits_BillLayout);
                units=(TextView)itemView.findViewById(R.id.units_BillLayout);
                add =(ImageView)itemView.findViewById(R.id._imgBtn_add_BillLayout);
                minus =(ImageView)itemView.findViewById(R.id.imgBtn_minus_BillLayout) ;
                editNo=(EditText)itemView.findViewById(R.id.itemsET_BillLayout);
                btnAddToTotal=(Button)itemView.findViewById(R.id.btn_addToBill_BillLayout);
            }
        }
    }
    Double sum() {
        Double sum = 0.0;
        for (Float i : numbers) {
            sum = sum + i;
        }
        return sum;
    }
    void editItem(int position , int number)
    {
        SharedPreferences myPref = this.getSharedPreferences("createDB" , Context.MODE_PRIVATE) ;
        String dbName =  myPref.getString("KeyName" ,"create database");
        SQLiteDatabase myDatabase = this.openOrCreateDatabase(dbName ,Context.MODE_PRIVATE , null) ;
        myDatabase.execSQL(" UPDATE "+dbName+" SET units='"+ number+"' WHERE id = "+position+"");
    }
}
