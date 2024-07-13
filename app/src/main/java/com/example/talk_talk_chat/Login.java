package com.example.talk_talk_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class Login extends AppCompatActivity {

    Button loginbtn;
    EditText email,password;
    TextView logintoregister;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logintoregister = findViewById(R.id.logintoregister);
        auth = FirebaseAuth.getInstance();
        loginbtn = findViewById(R.id.loginbtn);
        email = findViewById(R.id.logemail);
        password = findViewById(R.id.logpassword);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String pass = password.getText().toString();

                if ((TextUtils.isEmpty(Email))){
                    Toast.makeText(Login.this, "Enter The Email",Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(pass))) {
                    Toast.makeText(Login.this, "Enter The Password",Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    email.setError("Give Proper Email Address");
                } else if (password.length()<6) {
                    password.setError("More Than Six Charecters");
                    Toast.makeText(Login.this, "Password Need To Be Longer Then Six Charecter",Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {
                                    Intent intent = new Intent(Login.this, Chatpage.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(Login.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        logintoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

    }
}