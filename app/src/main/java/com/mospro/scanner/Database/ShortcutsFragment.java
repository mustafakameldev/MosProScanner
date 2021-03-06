package com.mospro.scanner.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mospro.scanner.ProdcutAdapter;
import com.mospro.scanner.Product;
import com.mospro.scanner.R;

import java.util.ArrayList;

public class ShortcutsFragment extends Fragment {

    ArrayList<Product> products ;
    RecyclerView recyclerView ;
    ProdcutAdapter adapter;
    public ShortcutsFragment() {
        // Required empty public constructor
    }
    public static ShortcutsFragment newInstance(String param1, String param2) {
        ShortcutsFragment fragment = new ShortcutsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = new ArrayList<>() ;
        SharedPreferences myPref = getActivity().getSharedPreferences("createDB" , Context.MODE_PRIVATE) ;
        String dbName =  myPref.getString("KeyName" ,"create database");
        SQLiteDatabase myDatabase = getActivity().openOrCreateDatabase(dbName ,Context.MODE_PRIVATE , null) ;
        try{
            //  myDatabase.execSQL("SELECT * FROM "+ dbName);
            Cursor cursor = myDatabase.rawQuery("SELECT * FROM "+ dbName +" WHERE limits > units " ,null) ;
            int nameCur = cursor.getColumnIndex("name") ;
            int discCur =cursor.getColumnIndex("disc") ;
            int priceCur =cursor.getColumnIndex("price");
            int limitsCur =cursor.getColumnIndex("limits");
            int mkeyCur=cursor.getColumnIndex("mKey");
            int unitsCur=cursor.getColumnIndex("units");
            cursor.moveToFirst();
            while (cursor !=null)
            {
                Log.i("name", cursor.getString(nameCur));
                Log.i("disc", cursor.getString(discCur));
                Log.i( "mKey", cursor.getString(mkeyCur));
                Log.i("units", String.valueOf(cursor.getInt(unitsCur)));
                Log.i("limits", String.valueOf(cursor.getInt(limitsCur)));
                Log.i("price", String.valueOf(cursor.getInt(priceCur)));
                Product product = new Product(cursor.getString(nameCur));
                product.setPrice(cursor.getInt(priceCur));
                product.setmKey(cursor.getString(mkeyCur));
                product.setLimits(cursor.getInt(limitsCur));
                product.setUnits(cursor.getInt(unitsCur));
                product.setDisc(cursor.getString(discCur));
                products.add(product) ;
                Log.i("mos pro products", products.toString());
                cursor.moveToNext() ;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shortcuts, container, false);

        recyclerView =(RecyclerView)view.findViewById(R.id.recycler_shortcuts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProdcutAdapter(getActivity() , products);
        recyclerView.setAdapter(adapter);
        return view;

    }

    public void onButtonPressed(Uri uri) {

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
