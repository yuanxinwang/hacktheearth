package com.example.endangeredfish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DetailActivity extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Button button;
    private Location mLastLocation;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 200;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION};
    private ImageView imageView;
    TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        final String name = intent.getStringExtra(MainActivity.FISH_NAME);
        textViewName = findViewById(R.id.name);
        final TextView textViewDetail = findViewById(R.id.detail);
        textViewName.setText(name);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url ="https://endangeredfish.wn.r.appspot.com/detail/";
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        try {
                            for(int i = 0; i < responses.length(); i++) {
                                JSONObject fish = (JSONObject) responses.get(i);
                                Log.d("Response", name);
                                Log.d("Response", fish.get("name").toString());
                                if (name.equals(fish.get("name").toString())) {
                                    textViewDetail.setText(fish.get("description").toString());
                                    Log.d("Response", fish.get("description").toString());
                                    new DownloadImageTask(imageView).execute(fish.get("img").toString());
                                    break;
                                }
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(
                this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLastLocation = location;
                            Log.d("location", Double.toString(mLastLocation.getLatitude()));
                        } else {
                            Log.d("loc", "no location. create one");
                            mLastLocation = new Location("dummyprovider");
                            Random r = new Random();
                            mLastLocation.setLatitude(35 + 10 * r.nextDouble());
                            mLastLocation.setLongitude(-75 + 10 * r.nextDouble());
                        }
                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mLastLocation != null) {
                    Log.d("loc", Double.toString(mLastLocation.getLatitude()));
                    String url = "https://endangeredfish.wn.r.appspot.com/location/";
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);
                                    Toast.makeText(DetailActivity.this, "Thank you!",
                                            Toast.LENGTH_LONG).show();
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Log.d("Error.Response", error.toString());
                                    Toast.makeText(getApplicationContext(), error.toString(),
                                            Toast.LENGTH_SHORT);
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("name", textViewName.getText().toString());
                            params.put("lat", Double.toString(mLastLocation.getLatitude()));
                            params.put("lon", Double.toString(mLastLocation.getLongitude()));
                            params.put("score", "1");
                            return params;
                        }
                    };

                    queue.add(postRequest);
                } else {
                    Log.d("loc", "no location");
                }
            }});

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
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
            bmImage.setImageBitmap(result);
        }
    }
}
