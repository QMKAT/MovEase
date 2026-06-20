package com.example.movease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        session = new UserSessionManager(this);

        // If user already signed in, go directly to appropriate home
        if (mAuth.getCurrentUser() != null) {
            checkRoleAndRoute();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    checkRoleAndRoute();
                                } else {
                                    String errorMsg = getFirebaseErrorMessage(task.getException());
                                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void checkRoleAndRoute() {
        String uid = mAuth.getCurrentUser().getUid();
        // First try cached role
        String cachedRole = session.getUserRole();
        if (cachedRole != null) {
            routeUser(cachedRole);
            return;
        }

        // Fetch from Firestore
        db.collection("users").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            String role = task.getResult().getString("role");
                            session.saveUserRole(role);
                            routeUser(role);
                        } else {
                            // Profile missing – create a default one
                            createDefaultProfile(uid);
                        }
                    }
                });
    }

    private void createDefaultProfile(String uid) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", mAuth.getCurrentUser().getEmail());
        userData.put("role", "mover");   // default role
        db.collection("users").document(uid)
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            session.saveUserRole("mover");
                            Toast.makeText(LoginActivity.this,
                                    "Profile created. Welcome!", Toast.LENGTH_SHORT).show();
                            routeUser("mover");
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Failed to create profile. Check internet.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void routeUser(String role) {
        Intent intent;
        if ("mover".equals(role)) {
            intent = new Intent(LoginActivity.this, MoverHomeActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, ProviderHomeActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private String getFirebaseErrorMessage(Exception exception) {
        if (exception == null) return "Login failed.";
        String msg = exception.getMessage();
        if (msg == null) return "Unknown error.";
        if (msg.contains("There is no user record")) return "No account found with this email.";
        if (msg.contains("The password is invalid")) return "Wrong password.";
        if (msg.contains("The email address is badly formatted")) return "Invalid email format.";
        if (msg.contains("A network error")) return "No internet connection. Check your network.";
        if (msg.contains("Too many requests")) return "Too many attempts. Try again later.";
        return "Login error: " + msg;
    }
}