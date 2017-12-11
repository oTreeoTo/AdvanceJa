package com.example.kululu.kmutnb_library;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManageActivity extends AppCompatActivity {
    private static final String TAG = "LogServer" ;
    TextView title, desc, author, type, id;
    ImageView book_img;
    String pic;
    public static final String REQUEST_TAG = "myrequest";
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manager);

         title = (TextView)findViewById(R.id.title);
         desc = (TextView)findViewById(R.id.desc);
         author = (TextView)findViewById(R.id.author);
         type = (TextView)findViewById(R.id.type);
         id = (TextView)findViewById(R.id.id);
         book_img = (ImageView)findViewById(R.id.img_book);

        Intent port_from_main =  getIntent();
        int recID_book = port_from_main.getIntExtra("recID", -1);
        id.setText(String.valueOf(recID_book));

        String book_name = port_from_main.getStringExtra("book_name");
        title.setText(book_name);

        String book_desc = port_from_main.getStringExtra("book_desc");
        desc.setText(book_desc);

        String book_written = port_from_main.getStringExtra("book_written");
        author.setText(book_written);

        String book_type = port_from_main.getStringExtra("book_type");
        type.setText(book_type);

        pic = port_from_main.getStringExtra("pic");

        String imgUrl = "http://"+ getResources().getString(R.string.webserver)+"/Android/Book_Pic/"+pic ;

        Picasso.with(this)
                .load(imgUrl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .error(R.drawable.blankbook)
                .into(book_img);

    }

    public void OpenUpdate(View view){
        Intent intent = new Intent(this,UpdateBookActivity.class);
        intent.putExtra("book_name",title.getText().toString());
        intent.putExtra("book_desc",desc.getText().toString());
        intent.putExtra("book_written",author.getText().toString());
        intent.putExtra("book_type",type.getText().toString());
        intent.putExtra("recID",id.getText().toString());
        intent.putExtra("pic",pic);
        startActivity(intent);
        finish();
    }

    public void OnDelete(View view){
        if (isInternetConnected())
        {
            final String str_id = id.getText().toString();

            new AlertDialog.Builder(this)
                    .setTitle("Really Delete?")
                    .setMessage("Are you sure you want to delete!")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            requestDelete(str_id);
                        }
                    }).create().show();
        }
        else{
            Toast.makeText(getBaseContext(), "not connected to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            return false;
        }
    }

    public void requestDelete(final String id){
        String url = "http://"+getResources().getString(R.string.webserver)+"/Android/deleteBook.php";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Log.d(TAG, "onResponse:" + response);
                JSONObject jsonData = null ;

                try {
                    JSONObject result = new JSONObject(response);
                    int returnCode = result.getInt("code");
                    String returnMsg = result.getString("message");
                    Log.d(TAG, String.valueOf(result));
                    Toast.makeText(getBaseContext(),returnMsg,Toast.LENGTH_SHORT).show();
                    if (returnCode == 1){
                        Intent GoBack = new Intent(getBaseContext(),ManagerActivity.class);
                        startActivity(GoBack);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,error.toString());

                        Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        pDialog.hide();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("book_id",id);
                return params;
            }
        };

        mQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(REQUEST_TAG);
        mQueue.add(stringRequest);
    }
}
