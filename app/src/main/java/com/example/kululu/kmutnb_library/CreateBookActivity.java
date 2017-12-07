package com.example.kululu.kmutnb_library;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.batch.android.json.JSONException;
import com.batch.android.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateBookActivity extends AppCompatActivity {
    EditText book_name, book_decs, book_written, book_type ;
    TextView tv;
    String url = "http://10.40.23.5/Android/createBook.php";
    private static final String TAG = "LogServer" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addbook);
        book_name = (EditText) findViewById(R.id.name_book);
        book_decs = (EditText) findViewById(R.id.type_book);
        book_type = (EditText) findViewById(R.id.detail_book);
        book_written = (EditText) findViewById(R.id.author_book);
    }

    public void onCreateBook(View view){
        Log.d(TAG, String.valueOf(book_name));
        String str_name = book_name.getText().toString();
        String str_decs = book_decs.getText().toString();
        String str_type = book_type.getText().toString();
        String str_written = book_written.getText().toString();
        requestRegister(str_name,str_decs,str_type,str_written);
    }

    private void requestRegister(final String bookName, final String bookDecs, final String bookType, final String bookWritten){

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject result = new JSONObject(response);
                    int returnCode = result.getInt("code");
                    String returnMsg = result.getString("message");
                    Log.d(TAG, String.valueOf(result));
                    tv.setText(returnMsg);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv.setText("onErrorResponse():"+
                                error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("book_name",bookName);
                params.put("book_decs",bookDecs);
                params.put("book_written",bookWritten);
                params.put("book_type",bookType);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }



}

