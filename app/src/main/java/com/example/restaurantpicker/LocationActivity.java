package com.example.restaurantpicker;

import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Map;

public class LocationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private Spinner spinnerMilesRadius;
    private Button btnLocation;
    private EditText edtZipCode;
    private TextView textViewRestaurant;
    private RequestQueue requestQueue;
    private RadioGroup radioGroup;
    private RadioButton opened, closed;
    private CheckBox chineseBox, japaneseBox, italianBox;
    private ArrayList<String> cuisines;
    private String milesRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        spinnerMilesRadius = findViewById(R.id.spinnerMilesRadius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.milesRadius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMilesRadius.setAdapter(adapter);
        spinnerMilesRadius.setOnItemSelectedListener(this);

        btnLocation = findViewById(R.id.btnLocation);
        edtZipCode = findViewById(R.id.edtZipCode);
        textViewRestaurant = findViewById(R.id.textViewRestaurant);
        radioGroup = findViewById(R.id.radioGroup);
        chineseBox = findViewById(R.id.chineseBox);
        japaneseBox = findViewById(R.id.japaneseBox);
        italianBox = findViewById(R.id.italianBox);
        cuisines = new ArrayList<>();


        requestQueue = Volley.newRequestQueue(this);

        btnLocation.setEnabled(false);

        edtZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnLocation.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != 5) {
                    btnLocation.setEnabled(false);
                } else {
                    btnLocation.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                jsonParse();
//                String zipCode = edtZipCode.getText().toString();
//                if (zipCode.length() != 5) {
//                    zipCodeDialog();
//                } else {
//                    Intent intent = new Intent(LocationActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }
                int radioId = radioGroup.getCheckedRadioButtonId();
                opened = findViewById(radioId);
                String openedNow = "opened";
                if (opened.getText().equals("closed")) {
                    openedNow = "closed";
                }

                Toast.makeText(LocationActivity.this, "selected radio button is: " + milesRadius, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LocationActivity.this, MainActivity.class);

                intent.putExtra("radius", milesRadius);
                intent.putExtra("zipcode", edtZipCode.getText().toString());
                intent.putExtra("opened", openedNow);
                intent.putExtra("cuisines", cuisines);

                startActivity(intent);
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

                            for (int i = 0; i < 5; i++) {
                                JSONObject entry = jsonArray.getJSONObject(i);
                                String api = entry.getString("name");

                                textViewRestaurant.append(api + ": "  + "\n\n");
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

    public void checkButton(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        opened = findViewById(radioId);

        Toast.makeText(this, "selected radio button is: " + opened.getText(), Toast.LENGTH_SHORT).show();
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