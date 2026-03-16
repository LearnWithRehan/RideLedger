package com.example.rideledger.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rideledger.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {
    TextInputEditText etEmail, etPassword;
    Button btnLogin;
    private TextView SignUp;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        SignUp = findViewById(R.id.SignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Enter Email");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Enter Password");
                    return;
                }

                loginUser(email,password);

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this,RegisterScreen.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email,String password){

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        Toast.makeText(LoginScreen.this,
                                "Login Successful",
                                Toast.LENGTH_SHORT).show();

                        Intent intent =
                                new Intent(LoginScreen.this, MainActivity.class);

                        startActivity(intent);
                        finish();

                    }else{

                        Toast.makeText(LoginScreen.this,
                                "Invalid Email or Password",
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }
}