package com.mospro.scanner;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.mospro.scanner.Database.EditActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
public class ProdcutAdapter extends RecyclerView.Adapter<ProdcutAdapter.ProductViewHolder> {
    private Context context ;
    private ArrayList<Product> products ;
    public ProdcutAdapter(Context context, ArrayList products) {
        this.context = context;
        this.products = products;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context) ;
        View view = inflater.inflate(R.layout.product_layout , null) ;
        return new ProductViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i) {
        productViewHolder.name.setText( products.get(i).getName());
        productViewHolder.disc.setText(products.get(i).getDisc());
        productViewHolder.price.setText(String.valueOf("price : "+products.get(i).getPrice()));
        productViewHolder.limits.setText(String.valueOf("limits : "+products.get(i).getLimits()));
        productViewHolder.units.setText(String.valueOf("units : "+products.get(i).getUnits()));
        final int i2 = i ;
        productViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder((Activity) context).title(R.string.chooseAction).sheet(R.menu.products_frag_context_menu).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.action_edit:
                                    Intent intent = new Intent(context , EditActivity.class) ;
                                        intent.putExtra("productKey" ,products.get(i2).getId() );
                                    context.startActivity(intent);
                                break;
                            case R.id.action_delete :
                                 final AlertDialog.Builder builder =new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                 builder.setMessage(R.string.sureDelete);
                                 builder.setTitle(R.string.deleteEntry);
                                 builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                     }
                                 }) ;
                                 builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         products.remove(i2);
                                         notifyItemRemoved(i2);
                                         notifyItemRangeChanged(i2, products.size());
                                         deleteRow(i2);
                                     }
                                 });
                                 builder.show();
                                break ;
                            case R.id.action_search :
                                String escapedQuery = null;
                                try {
                                    escapedQuery = URLEncoder.encode(products.get(i2).getmKey(), "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                                context.startActivity(intent1);
                            }
                    }
                }).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return products.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name , disc , price , limits , units  ;
        LinearLayout mainLayout ;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name =(TextView)itemView.findViewById(R.id.name_productLayout) ;
            disc =(TextView)itemView.findViewById(R.id.disc_ProductLayout) ;
            price =(TextView)itemView.findViewById(R.id.price_productLayout) ;
            limits =(TextView)itemView.findViewById(R.id.limits_productLayout) ;
            units =(TextView)itemView.findViewById(R.id.units_productLayout) ;
            mainLayout= (LinearLayout)itemView.findViewById(R.id.product_Layout) ;
        }
    }
    void deleteRow(int i )
    {
        SharedPreferences myPref = context.getSharedPreferences("createDB" , Context.MODE_PRIVATE) ;
        String dbName =  myPref.getString("KeyName" ,"create database");
        SQLiteDatabase myDatabase = context.openOrCreateDatabase(dbName ,Context.MODE_PRIVATE , null) ;
        myDatabase.execSQL("delete from "+dbName+" where id="+i+"");
    }


}
