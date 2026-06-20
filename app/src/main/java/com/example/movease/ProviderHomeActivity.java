package com.example.movease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProviderHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnLogout;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home);

        mAuth = FirebaseAuth.getInstance();
        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);

        if (mAuth.getCurrentUser() != null) {
            tvWelcome.setText("Welcome Provider, " + mAuth.getCurrentUser().getEmail());
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                UserSessionManager session = new UserSessionManager(ProviderHomeActivity.this);
                session.clearSession();
                startActivity(new Intent(ProviderHomeActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}