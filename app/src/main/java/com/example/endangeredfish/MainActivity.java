package com.example.endangeredfish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String> detailsMap = new HashMap<>();
    HashMap<String, Bitmap> imgMap = new HashMap<>();
    public static final String FISH_NAME = "NAME";
    public static final String FISH_DETAIL = "DETAIL";
    public static final String FISH_IMAGE = "IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url ="https://endangeredfish.wn.r.appspot.com/detail/";

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        try {
                            for(int i = 0; i < responses.length(); i++) {
                                JSONObject fish = (JSONObject) responses.get(i);
                                String name = fish.get("name").toString();
                                Log.d("Response", name);
                                detailsMap.put(name, fish.get("description").toString());
                                new MainActivity.DownloadImageTask(name).execute(fish.get("img").toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", "That didn't work!");
            }
        });

        queue.add(stringRequest);
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public void showAcadian(View view) {
        launchDetail(getString(R.string.acadian_whitefish));
    }

    public void showAurora(View view) {
        launchDetail(getString(R.string.aurora_trout));
    }

    public void showSalish(View view) {
        launchDetail(getString(R.string.salish_sucker));
    }

    private void launchDetail(final String name) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(FISH_NAME, name);
        intent.putExtra(FISH_DETAIL, detailsMap.get(name));
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        imgMap.get(name).compress(Bitmap.CompressFormat.JPEG, 50, bs);
        intent.putExtra(FISH_IMAGE, bs.toByteArray());
        startActivity(intent);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        String key;
        public DownloadImageTask(String key) {
            this.key = key;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            imgMap.put(key, result);
        }
    }
}
