package com.example.endangeredfish;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    public static final String FISH_NAME = "NAME";
    public static final String FISH_DETAIL = "DETAIL";

    private void launchDetail(final String name) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(FISH_NAME, name);
        startActivity(intent);
    }
}
