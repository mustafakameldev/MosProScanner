package com.mospro.scanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private LinearLayout layout;
    ArrayList<ScanItem> items ;
    RecyclerView recyclerView ;
    CustomAdapter adapter ;
    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items =new ArrayList<>() ;
        layout = getActivity().findViewById(R.id.buttonsContainer_MainActivity);
        layout.setVisibility(LinearLayout.GONE);
        retrieveData();

    }

    void  retrieveData()
    {
        SQLiteDatabase myDatabase = getActivity().openOrCreateDatabase("history" ,Context.MODE_PRIVATE , null) ;
        try{
            //  myDatabase.execSQL("SELECT * FROM "+ dbName);
            Cursor cursor = myDatabase.rawQuery("SELECT * FROM  history" ,null) ;


            int yearCur =cursor.getColumnIndex("year");
            int monthCur =cursor.getColumnIndex("month");
            int dayCur=cursor.getColumnIndex("day");
            int mkeyCur=cursor.getColumnIndex("mKey");
            int idCur =cursor.getColumnIndex("id") ;
            cursor.moveToFirst();
            while (cursor !=null)
            {
                Log.i("id", String.valueOf(cursor.getInt(idCur)));
                Log.i( "mKey", cursor.getString(mkeyCur));
                Log.i("year", String.valueOf(cursor.getInt(yearCur)));
                Log.i("month", String.valueOf(cursor.getInt(monthCur)));
                Log.i("day", String.valueOf(cursor.getInt(dayCur)));

                ScanItem item = new ScanItem(cursor.getString(mkeyCur));

                item.setYear(cursor.getInt(yearCur));
                item.setMonth(cursor.getInt(monthCur));
                item.setDay(cursor.getInt(dayCur));

                items.add(item) ;
                Log.i("mos pro products", items.toString());
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        adapter = new CustomAdapter(items) ;
        recyclerView =(RecyclerView)view.findViewById(R.id.historyRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        layout.setVisibility(LinearLayout.VISIBLE);
    }



    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        ArrayList<ScanItem> scanItems ;

        public CustomAdapter(ArrayList<ScanItem> items) {
            scanItems =items ;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().from(getActivity()).inflate(R.layout.scan_item_layout , null) ;
            return new MyViewHolder(view) ;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.name.setText(scanItems.get(i).getName());
            String date =scanItems.get(i).getDay() +"/"+scanItems.get(i).getMonth() ;
            myViewHolder.time.setText(date);
           final int i2 = i ;
            myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String escapedQuery = null;
                    try {
                        escapedQuery = URLEncoder.encode(scanItems.get(i2).getName(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    getActivity().startActivity(intent1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return scanItems.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name , time ;
            LinearLayout layout ;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name =(TextView) itemView.findViewById(R.id.name_ScanLayout);
                 time= (TextView)itemView.findViewById(R.id.time_ScanLayout);
                layout=(LinearLayout) itemView.findViewById(R.id.item_Layout);
            }
        }
    }
    class ScanItem
    {
      String name ;
      ScanItem (String name )
      {
         this. name= name ;
      }

      int year , month , day ;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


}
