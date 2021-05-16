package com.example.movieappactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Button createAccountBtn,loginBtn,forgetPasswordBtn;
    EditText username,password;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        reset_alert = new AlertDialog.Builder(  this);
        inflater = this.getLayoutInflater();

        createAccountBtn  = findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        username = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginbtn);
        forgetPasswordBtn = findViewById(R.id.forgetPasswordBtn);
        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start alertdialog
                View view =  inflater.inflate(R.layout.reset_pop, null);
                reset_alert.setTitle("Şifreni sıfırlamak istermisin?")
                        .setMessage("Şifreni sıfırlamak için mail adresini gir")
                        .setPositiveButton("Sıfırla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //validate the email adress
                                EditText email  = view.findViewById(R.id.rest_email_pop);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("İşlem Başarısız");
                                    return;
                                }
                                //send the reset link
                                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText( LoginActivity.this," Sıfırlama mail gönderildi.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }).setNegativeButton("İptal",null)
                        .setView(view)
                        .create().show();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract / validate

                if(username.getText().toString().isEmpty()){
                    username.setError("Email adresi yanlış");
                    return;
                }

                if(password.getText().toString().isEmpty()) {
                    password.setError("şifre yanlış");
                    return;
                }

                // data is valid
                //Login user

                firebaseAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //login is successful
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}