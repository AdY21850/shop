package com.example.sweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sweet.R;
import com.example.sweet.domain.usecase.GoogleLoginUseCase;
import com.example.sweet.domain.usecase.LoginUseCase;
import com.example.sweet.utils.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText email, password;
    private Button loginButton;
    private ImageView googleLogin;
    private TextView signupText;

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 101;

    private LoginUseCase loginUseCase;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        googleLogin = findViewById(R.id.googleLogin);
        signupText = findViewById(R.id.signupText);

        sessionManager = new SessionManager(this);
        loginUseCase = new LoginUseCase(this);

        // Auto-login if session exists
        autoLoginCheck();

        loginButton.setOnClickListener(v -> handleLogin());

        observeLoginResult();

        setupSignupClickableText(signupText);

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Google Sign-In Setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLogin.setOnClickListener(v -> {
            Log.d(TAG, "Google login clicked");
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    // ================= NORMAL LOGIN =================

    private void handleLogin() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        Log.d(TAG, "Login attempt: " + emailText);

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        loginUseCase.execute(emailText, passwordText);
    }

    private void observeLoginResult() {
        loginUseCase.loginResult.observe(this, response -> {

            Log.d(TAG, "Backend Response: " + response);

            if (response == null) {
                Log.e(TAG, "Response NULL from backend");
                Toast.makeText(this, "Server error. Try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (response.success) {

                Log.d(TAG, "Login Success | Role: " + response.user.getRole());

                sessionManager.saveSession(response.token, response.user.getRole());

                redirectByRole(response.user.getRole());

            } else {
                Log.e(TAG, "Login Failed: " + response.message);
                Toast.makeText(this,
                        response.message != null ? response.message : "Invalid email or password",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================= GOOGLE LOGIN =================

    private void sendGoogleTokenToBackend(String token) {

        Log.d(TAG, "Google Token Sent to Backend");

        GoogleLoginUseCase googleLoginUseCase = new GoogleLoginUseCase(this);

        // false = LOGIN mode
        googleLoginUseCase.execute(token, false);

        googleLoginUseCase.loginResult.observe(this, response -> {

            Log.d(TAG, "Google Backend Response: " + response);

            if (response == null) {
                Log.e(TAG, "Google backend returned NULL");
                Toast.makeText(this, "Google login failed", Toast.LENGTH_SHORT).show();
                return;
            }

            if (response.success) {

                sessionManager.saveSession(response.token, response.user.getRole());

                redirectByRole(response.user.getRole());

            } else {
                Log.e(TAG, "Google login error: " + response.message);
                Toast.makeText(this,
                        response.message != null ? response.message : "Google login failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================= GOOGLE RESULT =================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account == null) {
                    Toast.makeText(this, "Google account null", Toast.LENGTH_SHORT).show();
                    return;
                }

                String idToken = account.getIdToken();

                if (idToken == null) {
                    Log.e(TAG, "Google ID Token NULL — SHA1 issue");
                    Toast.makeText(this, "Google Token NULL — Fix Firebase SHA1", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d(TAG, "Google SignIn Success");

                sendGoogleTokenToBackend(idToken);

            } catch (ApiException e) {
                Log.e(TAG, "Google Sign-In Failed", e);
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ================= AUTO LOGIN =================

    private void autoLoginCheck() {
        if (sessionManager.getToken() != null) {

            Log.d(TAG, "Auto login success | Role: " + sessionManager.getRole());

            redirectByRole(sessionManager.getRole());

            finish();
        }
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

    // ================= SIGNUP CLICKABLE TEXT =================

    private void setupSignupClickableText(TextView signupText) {

        String text = "Don’t have an account? Sign Up";
        SpannableString spannableString = new SpannableString(text);

        int signUpStart = text.indexOf("Sign Up");
        int signUpEnd = signUpStart + "Sign Up".length();

        ClickableSpan signUpClickable = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }

            @Override
            public void updateDrawState(android.text.TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.cream));
                ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(signUpClickable, signUpStart, signUpEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signupText.setText(spannableString);
        signupText.setMovementMethod(LinkMovementMethod.getInstance());
        signupText.setHighlightColor(android.graphics.Color.TRANSPARENT);
    }
}
