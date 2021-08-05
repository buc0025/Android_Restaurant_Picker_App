package com.example.restaurantpicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView testUid;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testUid = findViewById(R.id.testUid);
        String uid = FirebaseAuth.getInstance().getUid();

//        testUid.setText(uid);

        requestQueue = Volley.newRequestQueue(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        jsonParse();


    }

    private void jsonParse() {
        String url = "https://api.yelp.com/v3/businesses/search?term=food&location=02343";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("businesses");

                            for (int i = 0; i < 5; i++) {
                                JSONObject entry = jsonArray.getJSONObject(i);
                                String api = entry.getString("name");

                                testUid.append(api + ": "  + "\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer y43TARnbjXmLlswBS0FdDZqIFk9KytIpXuE2gOh_5LK2yLv2OxOkIvMV-Dng0uIf66p_2eZtU9NZ46VrGrdUZMViBmjwySlFwbd_diB7S2dslBV4gwxw6kCQxTjRYHYx");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}