package com.example.rideledger.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rideledger.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterScreen extends AppCompatActivity {

    TextInputEditText etName,etMobile,etEmail,etPassword;
    Button btnRegister;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        etName=findViewById(R.id.etName);
        etMobile=findViewById(R.id.etMobile);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnRegister=findViewById(R.id.btnRegister);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser(){

        String name=etName.getText().toString().trim();
        String mobile=etMobile.getText().toString().trim();
        String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            etName.setError("Enter Name");
            return;
        }

        if(TextUtils.isEmpty(mobile)){
            etMobile.setError("Enter Mobile");
            return;
        }

        if(TextUtils.isEmpty(email)){
            etEmail.setError("Enter Email");
            return;
        }

        if(TextUtils.isEmpty(password)){
            etPassword.setError("Enter Password");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        String userId = mAuth.getCurrentUser().getUid();

                        Map<String,Object> user=new HashMap<>();
                        user.put("name",name);
                        user.put("mobile",mobile);
                        user.put("email",email);

                        db.collection("users")
                                .document(userId)
                                .set(user)
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(this,
                                            "Registration Successful",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
                                    startActivity(intent);
                                    finish();


                                });

                    }else{

                        Toast.makeText(this,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                });

    }
}