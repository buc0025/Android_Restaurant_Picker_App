package com.example.restaurantpicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView testUid, mainZipCode, mainMiles, mainCuisine, mainOpened;
    private RequestQueue requestQueue;
    private String zipcode, radius, opened;
    private ArrayList<String> cuisines;
    private Button btnShow, btnDeletePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testUid = findViewById(R.id.testUid);
        mainZipCode = findViewById(R.id.mainZipCode);
        mainMiles = findViewById(R.id.mainMiles);
        mainCuisine = findViewById(R.id.mainCuisine);
        mainOpened = findViewById(R.id.mainOpened);
        btnShow = findViewById(R.id.btnShow);
        btnDeletePrefs = findViewById(R.id.btnDeletePrefs);
        String uid = FirebaseAuth.getInstance().getUid();

//        testUid.setText(uid);

        requestQueue = Volley.newRequestQueue(this);

        PreferenceManager preferenceManager = new PreferenceManager(MainActivity.this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);


//         Comment lines 60-71 if compiling when user is not logged out
        Intent intent = getIntent();
        zipcode = intent.getExtras().getString("zipcode");
        radius = intent.getExtras().getString("radius");
        opened = intent.getExtras().getString("opened");
        cuisines = (ArrayList<String>) getIntent().getSerializableExtra("cuisines");

        mainZipCode.setText(zipcode);
        mainMiles.setText(radius);
        mainOpened.setText(opened);
        mainCuisine.setText(cuisines.get(0));

        jsonParse();

//        btnShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<String> cuisines = preferenceManager.getUserPrefs(uid);
//                for (int i = 0; i < cuisines.size(); i++) {
//                    testUid.append(cuisines.get(i) + "\n");
//                }
////                testUid.append(preferenceManager.getUserPrefs(uid));
//            }
//        });
//
//        btnDeletePrefs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                preferenceManager.removePrefs(uid);
//            }
//        });
    }

    private void jsonParse() {
        int milesToMeters = Integer.parseInt(radius) * 1609;
        StringBuilder stringBuilder = new StringBuilder();
        String startUrl = "https://api.yelp.com/v3/businesses/search?term=food&location=";
        stringBuilder.append(startUrl).append(zipcode);
        stringBuilder.append("&radius=").append(milesToMeters);

        if (cuisines.size() > 0) {
            stringBuilder.append("&categories=");

            for (int i = 0; i < cuisines.size(); i++) {
                if (i == cuisines.size() - 1) {
                    stringBuilder.append(cuisines.get(i));
                } else {
                    stringBuilder.append(cuisines.get(i)).append(",");
                }
            }
        }

        stringBuilder.append("&open_now=").append(opened);

        String url = stringBuilder.toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("businesses");

                            if (jsonArray.length() == 0) {
                                testUid.append("No restaurants matches your criteria");
                            } else {
                                Random random = new Random();
                                int n = random.nextInt(jsonArray.length());
//                            for (int i = 0; i < 5; i++) {
                                JSONObject entry = jsonArray.getJSONObject(n);
                                String name = entry.getString("name");
//                                String address = entry.getJSONArray("location").getString(0);
                                String address = entry.getString("location");
                                String phone = entry.getString("display_phone");
                                testUid.append(name + ": " + phone + "\n" + address + "\n");
//                                testUid.append(name + ": "  + "\n\n");
//                            }
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
                headers.put("Authorization", "Bearer y43TARnbjXmLlswBS0FdDZqIFk9KytIpXuE2gOh_5LK2yLv2OxOkIvMV-Dng0uIf66p_" +
                        "2eZtU9NZ46VrGrdUZMViBmjwySlFwbd_diB7S2dslBV4gwxw6kCQxTjRYHYx");
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