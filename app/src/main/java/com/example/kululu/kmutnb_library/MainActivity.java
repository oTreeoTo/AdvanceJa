package com.example.kululu.kmutnb_library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.batch.android.Batch;
import com.batch.android.Config;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView recyclerView;

    private static final String TAG = "LogServer";

    private List<Data> datas = new ArrayList<>();

    public static final String REQUEST_TAG = "myrequest";
    private RequestQueue mQueue;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navbar);
        navigationView.setNavigationItemSelectedListener(this);

        ViewBook();

        // TODO : switch to live Batch Api Key before shipping
        Batch.setConfig(new Config("DEV59FF2879A522785ED8BC2972054")); // devloppement

    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null){
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    public void displayListView() {
        MyAdapterBook adapterBook = new MyAdapterBook(this,datas);
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapterBook);

    }

    public void ViewBook(){
        String url = "http://192.168.2.101/Android/viewBookAll.php";
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
                        JSONArray result = new JSONArray(response);
                        Log.d(TAG, String.valueOf(result));
                        for (int i = 0; i < result.length(); i++) {
                            jsonData = result.getJSONObject(i);
                            Data data_book = gson.fromJson(String.valueOf(jsonData),Data.class);
                            datas.add(data_book);
                            Log.d(TAG, data_book.getBook_desc());
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                if (datas.size() > 0) {
                    displayListView();
                }
                pDialog.hide();
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
                String value = "0";
                Map<String, String> params = new HashMap<String, String>();
                params.put("book_detail_search",value);
                return params;
            }
        };

        mQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(REQUEST_TAG);
        mQueue.add(stringRequest);
    }

    public void OpenLogin (View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int id = datas.get(i).getId();
        String book_name = datas.get(i).getBook_name();
        String book_desc = datas.get(i).getBook_desc();
        String book_written = datas.get(i).getBook_written();
        String book_type = datas.get(i).getBook_type();

        Intent changeToDetailBook = new Intent(this, ViewBookActivity.class);
        changeToDetailBook.putExtra("recID",id);
        changeToDetailBook.putExtra("book_name",book_name);
        changeToDetailBook.putExtra("book_desc",book_desc);
        changeToDetailBook.putExtra("book_written",book_written);
        changeToDetailBook.putExtra("book_type",book_type);
        startActivity(changeToDetailBook);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_login) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        } else if (id == R.id.nav_reg) {
            Intent register = new Intent(this, RegisterActivity.class);
            startActivity(register);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;    }
}


//if (id == R.id.nav_home) {
//        Intent book_all = new Intent(this, ViewBookActivity.class);
//        startActivity(book_all);
//        } else if (id == R.id.nav_login) {
//        Intent book_recommend = new Intent(this, ViewBookActivity.class);
//        startActivity(book_recommend);
//        } else if (id == R.id.nav_reg) {
//        Intent book_new = new Intent(this, ViewBookActivity.class);
//        startActivity(book_new);
//        }