package com.example.kululu.kmutnb_library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

/**
 * Created by Kululu on 11/12/2017.
 */

public class RegisterActivity extends AppCompatActivity {
    EditText studentID, studentName, studentSurName, studentAge, studentEmail, studentPassword;
    TextView tv;
    String url = "http://192.168.1.39/Android/register.php";
    private static final String TAG = "LogServer" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        studentID = (EditText) findViewById(R.id.studentid);
        studentName = (EditText) findViewById(R.id.Firstname);
        studentSurName = (EditText) findViewById(R.id.Surname);
        studentAge = (EditText) findViewById(R.id.birthday);
        studentEmail = (EditText) findViewById(R.id.email);
        studentPassword = (EditText) findViewById(R.id.passwordregis);

    }

    public void OnRegister(View view){
        String str_id = studentID.getText().toString();
        String str_name = studentName.getText().toString();
        String str_surname = studentSurName.getText().toString();
        String str_age = studentAge.getText().toString();
        String str_email = studentEmail.getText().toString();
        String str_password = studentPassword.getText().toString();
        if (!str_id.isEmpty() && !str_name.isEmpty() && !str_surname.isEmpty() && !str_age.isEmpty() && !str_email.isEmpty() && !str_password.isEmpty()){
//            if (){
//                requestRegister(str_id,str_name,str_surname,str_age,str_email,str_password);
//            }
        }else {
            Toast.makeText(getBaseContext(),"Please Insert Your Information",Toast.LENGTH_SHORT).show();
        }
    }

    private void requestRegister(final String studentID, final String studentName, final String studentSurName, final String studentAge, final String studentEmail, final String studentPassword){

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

                    if (returnCode == 1) {
                        Toast.makeText(getBaseContext(),returnMsg,Toast.LENGTH_SHORT).show();
                        Intent sendToLogin = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(sendToLogin);
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
                params.put("user_id",studentID);
                params.put("user_name",studentName);
                params.put("user_surname",studentSurName);
                params.put("user_birthday",studentAge);
                params.put("user_email",studentEmail);
                params.put("password",studentPassword);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
