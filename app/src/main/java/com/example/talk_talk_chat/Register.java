package com.example.talk_talk_chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    Button registerbtn;
    TextView regtolog;
    EditText rg_username,rg_email,rg_password,rg_repassword;
    CircleImageView rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerbtn = findViewById(R.id.registerbtn);
        regtolog = findViewById(R.id.registertologin);
        rg_username = findViewById(R.id.regname);
        rg_email = findViewById(R.id.regemail);
        rg_password = findViewById(R.id.regpassword);
        rg_repassword = findViewById(R.id.regpassword);
        rg_profileImg = findViewById(R.id.profilelogo);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee = rg_username.getText().toString();
                String emaill = rg_email.getText().toString();
                String Password = rg_password.getText().toString();
                String cPassword = rg_repassword.getText().toString();
                String status = "Hey i am using this app";

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword)){
                    Toast.makeText(Register.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                } else if (!emaill.matches(emailPattern)) {
                    rg_email.setError("Type a valid email here");
                } else if (Password.length()<6) {
                    rg_password.setError("Password Must Be 6 Charecter Or More");
                } else if (!Password.equals(cPassword)) {
                    rg_password.setError("The Password Does Not Match");
                }
                else {
                    auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if (imageURI != null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        Users users= new Users(id, namee, emaill, Password, imageuri, status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Intent intent = new Intent(Register.this, Chatpage.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else {
                                                                    Toast.makeText(Register.this, "Error in creating the user",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status = "Hey i am using this app";
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/talk-talk-64f8f.appspot.com/o/blankProfile.jpg?alt=media&token=e9c3ad59-73c1-40d9-a996-d81549dab9cd";
                                    Users users= new Users(id, namee, emaill, Password, imageuri, status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(Register.this, Chatpage.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(Register.this, "Error in creating the user",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else {
                                Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        regtolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10){
            if (data != null){
                imageURI = data.getData();
                rg_profileImg.setImageURI(imageURI);
            }
        }
    }
}