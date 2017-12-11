package com.example.kululu.kmutnb_library;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.batch.android.Batch;
import com.batch.android.Config;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateBookActivity extends AppCompatActivity {
    EditText book_name, book_decs, book_written, book_type ;
    TextView recID_book;
    ImageView img ;
    private static final String TAG = "LogServer" ;
    private static final int SELECT_IMAGE = 1;
    private String filename;
    private String pathfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_updatebook);
        book_name = (EditText) findViewById(R.id.editTitle);
        book_decs = (EditText) findViewById(R.id.editDetail);
        book_type = (EditText) findViewById(R.id.editType);
        book_written = (EditText) findViewById(R.id.editAuthor);
        recID_book = (TextView) findViewById(R.id.idbook);
        img = (ImageView) findViewById(R.id.img_book);

        Intent port_from_main =  getIntent();
        String ID_book = port_from_main.getStringExtra("recID");
        recID_book.setText(ID_book);

        String name = port_from_main.getStringExtra("book_name");
        book_name.setText(name);

        String type = port_from_main.getStringExtra("book_type");
        book_type.setText(type);

        String decs = port_from_main.getStringExtra("book_desc");
        book_decs.setText(decs);

        String book_author = port_from_main.getStringExtra("book_written");
        book_written.setText(book_author);

        filename = port_from_main.getStringExtra("pic");

        String imgUrl = "http://"+ getResources().getString(R.string.webserver)+"/Android/Book_Pic/"+filename ;

        Picasso.with(this)
                .load(imgUrl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .error(R.mipmap.ic_launcher)
                .into(img);

    }

    public void onUpdate(View view){

        final String str_name = book_name.getText().toString();
        final String str_decs = book_decs.getText().toString();
        final String str_type = book_type.getText().toString();
        final String str_written = book_written.getText().toString();
        final String str_id = recID_book.getText().toString();
        if (isInternetConnected())
        {

            new AlertDialog.Builder(this)
                    .setTitle("Really Update this data?")
                    .setMessage("Are you sure you want to Update!")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            requestUpdateBook(str_id,str_name,str_decs,str_type,str_written);
                        }
                    }).create().show();
        }
        else{
            Toast.makeText(getBaseContext(), "not connected to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBack(View view){
        Intent GoBack = new Intent(getBaseContext(),ManagerActivity.class);
        startActivity(GoBack);
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

    private void requestUpdateBook(final String recID_book, final String bookName, final String bookDecs, final String bookType, final String bookWritten){
        String url = "http://"+getResources().getString(R.string.webserver)+"/Android/updateBook.php";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();
        Log.d(TAG, "requestCreateBook: " + bookName + bookDecs);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+ response);
                try {
                    JSONObject result = new JSONObject(response);
                    int returnCode = result.getInt("code");
                    String returnMsg = result.getString("message");
                    Log.d(TAG, String.valueOf(result));
                    Toast.makeText(getBaseContext(),returnMsg,Toast.LENGTH_SHORT).show();
                    if (returnCode == 1){
                        Intent GoBack = new Intent(getBaseContext(),ManagerActivity.class);
                        startActivity(GoBack);

                    }


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
                params.put("book_id",recID_book);
                params.put("book_name",bookName);
                params.put("book_desc",bookDecs);
                params.put("book_written",bookWritten);
                params.put("book_type",bookType);
                params.put("book_pic",filename);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void OnSelectImage(View view) { // out on create but in Your class ...... (in xml onClick="OnSelectImage")
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMAGE);
    }


    @Override   // out on create but in Your class
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            decodeUri(data.getData(), data);
        }
    }

    public void decodeUri(Uri uri, Intent data) {   // out on create but in Your class
        ParcelFileDescriptor parcelFD = null;
        img = (ImageView) findViewById(R.id.img_book);
        try {
            parcelFD = getContentResolver().openFileDescriptor(uri, "r");
            Log.d(TAG,"parcelFD " + parcelFD);
            FileDescriptor imageSource = parcelFD.getFileDescriptor();
            Log.d(TAG,"imageSource " + imageSource);


            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            Log.d(TAG,"o " + o);
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            Uri selectedImage = data.getData();
            Log.d(TAG, "uri " + selectedImage);
            String [] filePathColumn = { MediaStore.Images.Media.DATA };
            Log.d(TAG, "filePathcolumn " + filePathColumn);

            // Read the location of the selected image
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();


            //file and path address of image
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            pathfile = cursor.getString(columnIndex);  // path picture
            filename = pathfile.substring(pathfile.lastIndexOf("/")+1);   // show file picture name

            Log.d(TAG,"pic path " + pathfile);
            Log.d(TAG,"pic name " + filename);
            cursor.close();

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Log.d(TAG,"o2 " + o2);
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);
            Log.d(TAG,"bitmap " + bitmap);
            img.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            // handle errors
        } catch (IOException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }



}