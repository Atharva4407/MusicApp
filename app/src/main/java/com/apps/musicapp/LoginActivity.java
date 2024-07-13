package com.apps.musicapp;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etSignUpUsername, etSignUpPassword;
    private Button btnLogin, btnSignUp, btnSwitchToSignUp, btnSwitchToLogin;
    private UserLoginHelper userLoginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLoginHelper = new UserLoginHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etSignUpUsername = findViewById(R.id.etSignUpUsername);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSwitchToSignUp = findViewById(R.id.btnSwitchToSignUp);
        btnSwitchToLogin = findViewById(R.id.btnSwitchToLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    if (userLoginHelper.checkUser(username, password)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String signUpUsername = etSignUpUsername.getText().toString().trim();
                String signUpPassword = etSignUpPassword.getText().toString().trim();

                if (TextUtils.isEmpty(signUpUsername) || TextUtils.isEmpty(signUpPassword)) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    if (userLoginHelper.checkUser(signUpUsername, signUpPassword)) {
                        Toast.makeText(LoginActivity.this, "User already exists. Please login.", Toast.LENGTH_SHORT).show();
                    } else {
                        userLoginHelper.addUser(signUpUsername, signUpPassword);
                        Toast.makeText(LoginActivity.this, "User registered successfully. Please login.", Toast.LENGTH_SHORT).show();
                        switchToLoginView();
                    }
                }
            }
        });

        btnSwitchToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignUpView();
            }
        });

        btnSwitchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLoginView();
            }
        });

        // For demonstration purposes, pre-fill the database with a user if not already exists
        if (!userLoginHelper.checkUser("user", "password")) {
            userLoginHelper.addUser("user", "password");
        }
    }

    private void switchToSignUpView() {
        findViewById(R.id.loginLayout).setVisibility(View.GONE);
        findViewById(R.id.signUpLayout).setVisibility(View.VISIBLE);
    }

    private void switchToLoginView() {
        findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.signUpLayout).setVisibility(View.GONE);
    }
}
