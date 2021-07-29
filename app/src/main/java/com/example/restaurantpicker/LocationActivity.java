package com.example.restaurantpicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationActivity extends AppCompatActivity {
    private Spinner spinnerMilesRadius;
    private Button btnLocation;
    private EditText edtZipCode;
    private TextView textViewRestaurant;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        spinnerMilesRadius = findViewById(R.id.spinnerMilesRadius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.milesRadius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMilesRadius.setAdapter(adapter);

        btnLocation = findViewById(R.id.btnLocation);
        edtZipCode = findViewById(R.id.edtZipCode);
        textViewRestaurant = findViewById(R.id.textViewRestaurant);

        requestQueue = Volley.newRequestQueue(this);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
//                String zipCode = edtZipCode.getText().toString();
//                if (zipCode.length() != 5) {
//                    zipCodeDialog();
//                } else {
//                    Intent intent = new Intent(LocationActivity.this, CuisineActivity.class);
//                    startActivity(intent);
//                }
            }
        });

    }

    private void jsonParse() {
        String url = "https://api.yelp.com/v3/businesses/search?term=food&location=02343";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("businesses");
                            JSONObject business = jsonArray.getJSONObject(0);
                            String restaurant = business.getString("name");
                            textViewRestaurant.setText(restaurant);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    private void zipCodeDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Zip code is invalid.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(LocationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}