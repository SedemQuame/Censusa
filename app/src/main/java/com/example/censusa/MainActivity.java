package com.example.censusa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText nameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private TextView gotoLogin;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Linking UI views, to their IDs.
        nameEditText = findViewById(R.id.signUpName);
        emailEditText = findViewById(R.id.signUpEmail);
        passwordEditText = findViewById(R.id.signUpPassword);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        gotoLogin = findViewById(R.id.gotoLogin);
        gotoLogin.setOnClickListener(this);

//         Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()){
            case R.id.signUpButton:
//                Get data from form elements, and pass them to FireBase Auth.
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                registerUser(name, email, password, intent);
                break;
            case R.id.gotoLogin:
                Toast.makeText(this, "Clicked on the gotoLogin text", Toast.LENGTH_SHORT).show();
//                Intent to change activity to LoginActivity.
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void registerUser(String name, String email, String password, Intent intent){
//                    FireBase Auth, here.
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Empty or Wrongly Formatted Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()){
            Toast.makeText(this, "Empty or Wrongly Formatted Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty() || password.length() < 8){
            Toast.makeText(this, "Password empty or shorter than required length.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information

//                Starting new activity.
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(MainActivity.this, "Email already, exists.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        // ...
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }
    }
}

