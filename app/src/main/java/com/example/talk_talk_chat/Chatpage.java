package com.example.talk_talk_chat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chatpage extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView imglogout;
    ImageView cambut,setbut;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatpage);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        cambut = findViewById(R.id.camBut);
        setbut = findViewById(R.id.settingBut);


        DatabaseReference reference = database.getReference().child("user");

        usersArrayList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imglogout = findViewById(R.id.logoutimg);
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Chatpage.this,R.style.dialoge);
                dialog.setContentView(R.layout.dialoge_layout);
                Button no,yes;
                yes = dialog.findViewById(R.id.yesbtn);
                no = dialog.findViewById(R.id.nobtn);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Chatpage.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        setbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chatpage.this, setting.class);
                startActivity(intent);
            }
        });

        cambut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,10);
            }
        });


        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(Chatpage.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        if(auth.getCurrentUser()==null){
            Intent intent = new Intent(Chatpage.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}