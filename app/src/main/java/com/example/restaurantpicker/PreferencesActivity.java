package com.example.restaurantpicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PreferencesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemSelectedListener {
    private Spinner spinnerMilesRadius;
    private Button btnApply, btnClear;
    private EditText edtZipCode;
    private RequestQueue requestQueue;
    private RadioGroup radioGroup;
    private RadioButton opened, closed;
    private CheckBox chineseBox, japaneseBox, italianBox, indianBox, vegetarianBox, koreanBox;
    private ArrayList<String> cuisines;
    private String milesRadius;
    private LinearLayout linLayout1, linLayout2;
    private String zipCode;
    private List<String> cuisineList;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Creating adapter for spinner
        spinnerMilesRadius = findViewById(R.id.spinnerMilesRadius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.milesRadius, android.R.layout
                .simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMilesRadius.setAdapter(adapter);
        spinnerMilesRadius.setOnItemSelectedListener(this);

        btnApply = findViewById(R.id.btnApply);
        edtZipCode = findViewById(R.id.edtZipCode);
        radioGroup = findViewById(R.id.radioGroup);
        chineseBox = findViewById(R.id.chineseBox);
        japaneseBox = findViewById(R.id.japaneseBox);
        italianBox = findViewById(R.id.italianBox);
        indianBox = findViewById(R.id.indianBox);
        vegetarianBox = findViewById(R.id.vegetarianBox);
        koreanBox = findViewById(R.id.koreanBox);
        cuisines = new ArrayList<>();

        btnClear = findViewById(R.id.btnClear);
        linLayout1 = findViewById(R.id.linLayout1);
        linLayout2 = findViewById(R.id.linLayout2);

        restaurantId = FirebaseAuth.getInstance().getUid() + "_RESTAURANTS";
        requestQueue = Volley.newRequestQueue(this);

        btnApply.setEnabled(false);

        checkboxesClicked();

        // Zip code has to be 5 digits or else apply button is whited out
        edtZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnApply.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnApply.setEnabled(s.toString().length() == 5);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnApply.setOnClickListener(v -> {
            int radioId = radioGroup.getCheckedRadioButtonId();
            opened = findViewById(radioId);
            String openedNow = "true";
            if (opened.getText().equals("closed")) {
                openedNow = "false";
            }

            Toast.makeText(PreferencesActivity.this, "selected radio button is: " + milesRadius, Toast.LENGTH_SHORT)
                    .show();

            // Saving as sharedpreferences to test in mainactivity
//            String zipcode = edtZipCode.getText().toString();
//            String uid = FirebaseAuth.getInstance().getUid();
//            UserPreferences userPreferences = new UserPreferences(zipcode, milesRadius, cuisines, openedNow);
//            PreferenceManager preferenceManager = new PreferenceManager(PreferencesActivity.this);
//            preferenceManager.savePrefs(userPreferences, uid);
//
//
//            Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
//            intent.putExtra("radius", milesRadius);
//            intent.putExtra("zipcode", edtZipCode.getText().toString());
//            intent.putExtra("opened", openedNow);
//            intent.putExtra("cuisines", cuisines);
//
//            startActivity(intent);

            // saving sharedpref key as uId, value as ArrayList<Restaurant>
            jsonParse();
        });

        clearButtonClicked();
    }

    //Testing static inputs
    private void jsonParse() {
        String url = "https://api.yelp.com/v3/businesses/search?term=food&location=02170&categories=chinese&" +
                "open_now=true&radius=1609";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("businesses");

                            if (jsonArray.length() == 0) {
//                                testUid.append("No restaurants matches your criteria");
                                Toast.makeText(PreferencesActivity.this, "No restaurants matches your criteria",
                                        Toast.LENGTH_SHORT).show();
                            } else {
//                                Random random = new Random();
//                                int n = random.nextInt(jsonArray.length());
                                ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
                                PreferenceManager preferenceManager = new PreferenceManager(PreferencesActivity.this);

                                for (int i = 0; i < 5; i++) {
                                    JSONObject entry = jsonArray.getJSONObject(i);
                                    String name = entry.getString("name");
                                    String address = entry.getString("location");
                                    String phone = entry.getString("display_phone");

                                    Restaurant restaurant = new Restaurant(name, address, phone);
                                    restaurantArrayList.add(restaurant);
                                }

                                preferenceManager.saveRestaurants(restaurantArrayList, restaurantId);
                                Toast.makeText(PreferencesActivity.this, "saved restaurants",
                                        Toast.LENGTH_SHORT).show();
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

    private void checkboxesClicked() {
        chineseBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chineseBox.isChecked()) {
                    cuisines.add("chinese");
                } else {
                    cuisines.remove("chinese");
                }
            }
        });

        japaneseBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (japaneseBox.isChecked()) {
                    cuisines.add("japanese");
                } else {
                    cuisines.remove("japanese");
                }
            }
        });

        italianBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (italianBox.isChecked()) {
                    cuisines.add("italian");
                } else {
                    cuisines.remove("italian");
                }
            }
        });

        indianBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indianBox.isChecked()) {
                    cuisines.add("indian");
                } else {
                    cuisines.remove("indian");
                }
            }
        });

        vegetarianBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vegetarianBox.isChecked()) {
                    cuisines.add("vegetarian");
                } else {
                    cuisines.remove("vegetarian");
                }
            }
        });

        koreanBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (koreanBox.isChecked()) {
                    cuisines.add("korean");
                } else {
                    cuisines.remove("korean");
                }
            }
        });
    }

    private void clearButtonClicked() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numCheckboxes1 = linLayout1.getChildCount();
                int numCheckboxes2 = linLayout2.getChildCount();
                Toast.makeText(PreferencesActivity.this, "clear button pressed" + numCheckboxes1, Toast.LENGTH_SHORT)
                        .show();

                for (int i = 0; i < numCheckboxes1; i++) {
                    v = linLayout1.getChildAt(i);
                    if (v instanceof CheckBox) {
                        ((CheckBox) v).setChecked(false);
                    }
                }

                for (int i = 0; i < numCheckboxes2; i++) {
                    v = linLayout2.getChildAt(i);
                    if (v instanceof CheckBox) {
                        ((CheckBox) v).setChecked(false);
                    }
                }
                cuisines.clear();
                edtZipCode.getText().clear();
            }
        });
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
            Intent intent = new Intent(PreferencesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // for spinner
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    // for spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        milesRadius = parent.getItemAtPosition(position).toString();
    }

    // for spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}