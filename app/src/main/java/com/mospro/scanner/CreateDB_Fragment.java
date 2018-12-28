package com.mospro.scanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mospro.scanner.Database.DatabaseActivity;

public class CreateDB_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn;
    EditText editText ;
    LinearLayout layout;

    {
    }

    public CreateDB_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateDB_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateDB_Fragment newInstance(String param1, String param2) {
        CreateDB_Fragment fragment = new CreateDB_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view =inflater.inflate(R.layout.fragment_create_db_, container, false);
        btn = (Button)view.findViewById(R.id.btn_CreateDBFragment);
        editText =(EditText)view.findViewById(R.id.editText_CreatedDBFragment) ;
        layout = getActivity().findViewById(R.id.buttonsContainer_MainActivity);

        layout.setVisibility(LinearLayout.GONE);
        btn.setOnClickListener(this);
        return  view ;
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

    @Override
    public void onClick(View v) {
        if(v==btn)
        {
            String name  = editText.getText().toString().trim().replace(" ","_");
            if(!TextUtils.isEmpty(name)) {
                SharedPreferences createPref;
                createPref = getActivity().getSharedPreferences("createDB", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = createPref.edit();
                editor.putString("KeyName", name);
                SQLiteDatabase myDatabase = getActivity().openOrCreateDatabase(name , Context.MODE_PRIVATE ,null ) ;
                myDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+name + " (id INTEGER PRIMARY KEY AUTOINCREMENT,mKey VARCHAR ,name VARCHAR , disc VARCHAR , limits INT , units INT , price REAL ) ");
                editor.putBoolean("key1" , true) ;
                editor.apply();
                Intent intent = new Intent(getActivity() , DatabaseActivity.class) ;
                startActivity(intent);
            }
        }
    }
}
