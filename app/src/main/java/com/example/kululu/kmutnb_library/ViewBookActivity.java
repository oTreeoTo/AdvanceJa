package com.example.kululu.kmutnb_library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.batch.android.json.JSONException;
import com.batch.android.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewBookActivity extends AppCompatActivity {
    EditText book_name, book_decs, book_written, book_type ;



    private static final String TAG = "LogServer" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detailbook);
        TextView title = (TextView)findViewById(R.id.title);
        TextView desc = (TextView)findViewById(R.id.desc);
        TextView author = (TextView)findViewById(R.id.author);
        TextView type = (TextView)findViewById(R.id.type);

        Intent port_from_main =  getIntent();
        int recID_book = port_from_main.getIntExtra("recID", -1);
        String book_name = port_from_main.getStringExtra("book_name");
        title.setText(book_name);

        String book_desc = port_from_main.getStringExtra("book_desc");
        desc.setText(book_desc);

        String book_written = port_from_main.getStringExtra("book_written");
        author.setText(book_written);

        String book_type = port_from_main.getStringExtra("book_type");
        type.setText(book_type);


        Log.d(TAG, "onCreate: "+ recID_book);


    }
}
