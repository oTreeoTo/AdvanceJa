package com.example.kululu.kmutnb_library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.batch.android.json.JSONException;
import com.batch.android.json.JSONObject;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewBookActivity extends AppCompatActivity {

    private static final String TAG = "LogServer" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detailbook);
        TextView title = (TextView)findViewById(R.id.title);
        TextView desc = (TextView)findViewById(R.id.desc);
        TextView author = (TextView)findViewById(R.id.author);
        TextView type = (TextView)findViewById(R.id.type);
        ImageView book_img = (ImageView)findViewById(R.id.img_book);

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

        String pic = port_from_main.getStringExtra("pic");

        String imgUrl = "http://"+ getResources().getString(R.string.webserver)+"/Android/Book_Pic/"+pic ;

        Picasso.with(this)
                .load(imgUrl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .error(R.drawable.blankbook)
                .into(book_img);



    }
}
