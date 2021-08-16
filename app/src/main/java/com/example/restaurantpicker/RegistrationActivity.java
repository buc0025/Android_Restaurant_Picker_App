package com.example.restaurantpicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPassword;
    private TextView alreadyRegistered;
    private Button registerBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootNode = firebaseDatabase.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        alreadyRegistered = findViewById(R.id.alreadyRegistered);
        registerBtn = findViewById(R.id.registerBtn);

        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in. Send them to main activity if they are logged in.
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // email and password cannot be empty in order to register
                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Password is required.");
                    return;
                }

                if (password.length() < 6) {
                    edtPassword.setError("Password must be at least 6 characters.");
                    return;
                }

                // Create user in Firebase
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserDetails userDetails = new UserDetails(name, email);
                            String uid = FirebaseAuth.getInstance().getUid();
                            rootNode.child(uid).setValue(userDetails);
                            Toast.makeText(RegistrationActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}