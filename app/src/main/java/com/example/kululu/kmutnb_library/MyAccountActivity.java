package com.example.kululu.kmutnb_library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Objects;

public class MyAccountActivity extends AppCompatActivity {

    private static final String TAG = "LogServer" ;
    TextView number_id,name_id,bd_id,email_id,type_id;
    String name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detailprofile);

        Intent port_from_main =  getIntent();
        name = port_from_main.getStringExtra("name");

        email = port_from_main.getStringExtra("email");

        number_id = (TextView)findViewById(R.id.noid);
        name_id = (TextView)findViewById(R.id.nameid);
        bd_id = (TextView)findViewById(R.id.bdid);
        email_id = (TextView)findViewById(R.id.emid);
        type_id = (TextView)findViewById(R.id.typeid);

        viewAccount();

    }

    private void viewAccount(){
        String url = "http://"+getResources().getString(R.string.webserver)+"/Android/searchAccount.php";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+ response);
                try {
                    JSONObject result = new JSONObject(response);
                    int returnCode = result.getInt("code");
                    String returnMsg = result.getString("message");
                    String returnType = result.getString("type");
                    String returnName = result.getString("name");
                    String returnEmail = result.getString("email");
                    String returnId = result.getString("id");
                    String returnDay = result.getString("birthday");

                    number_id.setText(returnId);
                    name_id.setText(returnName);
                    email_id.setText(returnEmail);
                    bd_id.setText(returnDay);
                    type_id.setText(returnType);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(getBaseContext(),"onErrorResponse():"+
                                error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name",email);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

}
