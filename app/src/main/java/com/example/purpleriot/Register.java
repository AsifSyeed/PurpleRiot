package com.example.purpleriot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.purpleriot.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    TextInputEditText textEmail, textPassword, textName;
    ProgressBar progressBar;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textEmail =  (TextInputEditText) findViewById(R.id.email_reg);
        textPassword = (TextInputEditText) findViewById(R.id.pass_reg);
        textName = (TextInputEditText) findViewById(R.id.name_reg);
        progressBar = (ProgressBar) findViewById(R.id.reg_progress);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");


    }

    public void RegisterUser (View v) {
        progressBar.setVisibility(View.VISIBLE);
        final String email = textEmail.getText().toString();
        final String password = textPassword.getText().toString();
        final String name = textName.getText().toString();

        if (!email.equals("") && !password.equals("") && password.length()>6) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        User u = new User();
                        u.setName(name);
                        u.setEmail(email);

                        reference.child(firebaseUser.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "User registered successfully!", Toast.LENGTH_SHORT).show();

                                    finish();
                                    Intent i = new Intent(Register.this, GroupChatActivity.class);
                                    startActivity(i);
                                }
                            }
                        });
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "User cannot be registered", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    public void gotoLogin (View v) {
        Intent i = new Intent(Register.this, MainActivity.class);
        startActivity(i);
    }
}