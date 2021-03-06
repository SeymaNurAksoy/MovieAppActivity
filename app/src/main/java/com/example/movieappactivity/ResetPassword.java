package com.example.movieappactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {
    EditText userPassword, userConfPassword;
    Button savePasswordBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MovieAppActivity);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userConfPassword = findViewById(R.id.newUserPassword);
        userPassword = findViewById(R.id.newConfirmPass);

        user = FirebaseAuth.getInstance().getCurrentUser();

        savePasswordBtn = findViewById(R.id.resetPasswordBtn);
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userPassword.getText().toString().isEmpty()) {
                    userPassword.setError("Alanı doldur");
                    return;
                }
                if (userConfPassword.getText().toString().isEmpty()) {
                    userConfPassword.setError("Alanı doldur");
                    return;
                }
                if (!userPassword.getText().toString().equals(userConfPassword.getText().toString())) {
                    userConfPassword.setError("Şifreler aynı değil");
                    return;
                }

                user.updatePassword(userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResetPassword.this, "Şifre güncellendi",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPassword.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
}