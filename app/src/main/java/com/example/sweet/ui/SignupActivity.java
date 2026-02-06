package com.example.sweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sweet.R;
import com.example.sweet.domain.usecase.GoogleLoginUseCase;
import com.example.sweet.domain.usecase.RegisterUseCase;
import com.example.sweet.utils.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText fullName, email, password, confirmPassword;
    private Button signupButton;
    private TextView loginRedirect;
    private ImageView googleSignup;

    private RegisterUseCase registerUseCase;
    private SessionManager sessionManager;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        Log.d(TAG, "SignupActivity Started");

        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmpassword);
        signupButton = findViewById(R.id.signinButton);
        loginRedirect = findViewById(R.id.signupText);
        googleSignup = findViewById(R.id.googleLogin);

        registerUseCase = new RegisterUseCase(this);
        sessionManager = new SessionManager(this);

        setupGoogleSignIn();

        signupButton.setOnClickListener(v -> handleRegister());

        loginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        googleSignup.setOnClickListener(v -> {
            Log.d(TAG, "Google Signup Clicked");

            // 🔥 CRITICAL FIX: force sign-out before sign-in
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                googleLauncher.launch(googleSignInClient.getSignInIntent());
            });
        });

        observeRegisterResult();
    }

    // ================= NORMAL REGISTER =================

    private void handleRegister() {

        String nameText = fullName.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passText = password.getText().toString().trim();
        String confirmText = confirmPassword.getText().toString().trim();

        if (nameText.isEmpty() || emailText.isEmpty() || passText.isEmpty() || confirmText.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passText.equals(confirmText)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passText.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        registerUseCase.execute(nameText, emailText, passText);
    }

    private void observeRegisterResult() {
        registerUseCase.registerResult.observe(this, response -> {

            if (response == null) {
                Toast.makeText(this, "Server error. Try again.", Toast.LENGTH_LONG).show();
                return;
            }

            if (response.success) {

                sessionManager.saveSession(response.token, response.user.getRole());
                Toast.makeText(this, "Registration Successful 🎉", Toast.LENGTH_SHORT).show();
                redirectByRole(response.user.getRole());

            } else {
                Toast.makeText(this,
                        response.message != null ? response.message : "Registration failed",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ================= GOOGLE SIGN-IN =================

    private void setupGoogleSignIn() {

        String clientId = getString(R.string.default_web_client_id);
        Log.d(TAG, "Google WEB Client ID: " + clientId);

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(clientId) // 🔥 MUST be WEB client ID
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private final ActivityResultLauncher<Intent> googleLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() != RESULT_OK) {
                            Log.e(TAG, "Google Sign-in Cancelled");
                            return;
                        }

                        Task<GoogleSignInAccount> task =
                                GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);

                            if (account == null) {
                                Toast.makeText(this, "Google Account NULL", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String idToken = account.getIdToken();

                            if (idToken == null) {
                                Toast.makeText(this, "Google ID Token NULL", Toast.LENGTH_LONG).show();
                                return;
                            }

                            // 🔍 DEBUG (remove later)
                            Log.d(TAG, "ID TOKEN (first 30 chars): " +
                                    idToken.substring(0, Math.min(30, idToken.length())));

                            sendGoogleTokenToBackend(idToken);

                        } catch (ApiException e) {
                            Toast.makeText(this,
                                    "Google Login Failed: " + e.getStatusCode(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            );

    // ================= SEND TOKEN TO BACKEND =================

    private void sendGoogleTokenToBackend(String token) {

        GoogleLoginUseCase googleLoginUseCase = new GoogleLoginUseCase(this);

        // true = REGISTER MODE
        googleLoginUseCase.execute(token, true);

        googleLoginUseCase.loginResult.observe(this, response -> {

            if (response == null) {
                Toast.makeText(this, "Google Signup Failed", Toast.LENGTH_LONG).show();
                return;
            }

            if (response.success) {

                sessionManager.saveSession(response.token, response.user.getRole());
                Toast.makeText(this, "Google Signup Successful 🎉", Toast.LENGTH_SHORT).show();
                redirectByRole(response.user.getRole());

            } else {
                Toast.makeText(this,
                        response.message != null ? response.message : "Google Signup Failed",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ================= ROLE REDIRECT =================

    private void redirectByRole(String role) {

        if ("ADMIN".equalsIgnoreCase(role)) {
            startActivity(new Intent(this, Admin_HomeActivity.class));
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }
        finish();
    }
}
