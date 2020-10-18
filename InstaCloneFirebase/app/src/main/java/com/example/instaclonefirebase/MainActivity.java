package com.example.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText txtEmail, txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // firebase instance oluşturma
        firebaseAuth = FirebaseAuth.getInstance();

        // Inputlarımızı tanımlama
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
    }

    public void signInAction (View view) {

    }

    public void signUpAction (View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.matches("") || password.matches("")) {
            Toast.makeText(this, "email or password cannot be empty", Toast.LENGTH_SHORT).show();
        } else {

            // firebase email ve password kullanarak kullanıcı oluşturma
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }
}