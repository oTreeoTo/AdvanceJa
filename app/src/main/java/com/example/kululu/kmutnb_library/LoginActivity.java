package com.example.kululu.kmutnb_library;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Created by Kululu on 11/12/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LogServer" ;
    EditText UsernameEt, PasswordEt;
    TextView tv;
    String url = "http://10.60.6.165/Android/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UsernameEt = (EditText) findViewById(R.id.etUserName);
        PasswordEt = (EditText) findViewById(R.id.etPassword);
        tv =  (TextView)findViewById(R.id.textView);

    }

    public void OnLogin(View view){
        String username = UsernameEt.getText().toString().trim();
        String password = PasswordEt.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()){
            requestSignIn(username,password);
        }else{
            Toast.makeText(getBaseContext(),"Please enter login info..",Toast.LENGTH_SHORT).show();
        }
    }

    public void OpenReg(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    private void requestSignIn(final String user, final String password){

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
                    String returnType = result.getString("type");

                    if (Objects.equals(returnType, "student") && returnCode == 1) {
                        Intent sendHomeStudent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(sendHomeStudent);
                    } else if (Objects.equals(returnType, "Manager") && returnCode == 1 ) {
                        Toast.makeText(getBaseContext(),returnMsg,Toast.LENGTH_SHORT).show();
                        Intent sendHomeManager = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(sendHomeManager);
                    } else {
                        Log.d(TAG, String.valueOf(result));
                        Toast.makeText(getBaseContext(),returnMsg,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
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
                params.put("user_name",user);
                params.put("password",password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
