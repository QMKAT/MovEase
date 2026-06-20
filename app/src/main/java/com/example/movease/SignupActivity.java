package com.example.movease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private RadioGroup radioGroupRole;
    private Button btnSignup;
    private TextView tvLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirm = etConfirmPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirm)) {
                    Toast.makeText(SignupActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (radioGroupRole.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SignupActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Determine selected role
                String role = (radioGroupRole.getCheckedRadioButtonId() == R.id.radioMover) ? "mover" : "provider";

                // Create user with Firebase Auth
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Save role to Firestore
                                    String uid = mAuth.getCurrentUser().getUid();
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("email", email);
                                    userData.put("role", role);
                                    db.collection("users").document(uid)
                                            .set(userData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Cache role locally
                                                        UserSessionManager session = new UserSessionManager(SignupActivity.this);
                                                        session.saveUserRole(role);
                                                        routeUser(role);
                                                    } else {
                                                        Toast.makeText(SignupActivity.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignupActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    private void routeUser(String role) {
        if (role.equals("mover")) {
            startActivity(new Intent(SignupActivity.this, MoverHomeActivity.class));
        } else {
            startActivity(new Intent(SignupActivity.this, ProviderHomeActivity.class));
        }
        finish();
    }
}