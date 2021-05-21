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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText registerFullName,registerEmail,registerPassword,registerConfPass;
    Button registerUserBtn,gotoLogin;
    FirebaseAuth fAuth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MovieAppActivity);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerFullName = findViewById(R.id.registerFullName);
        registerEmail  = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfPass = findViewById(R.id.confPassword);
        registerUserBtn = findViewById(R.id.registerBtn);
        gotoLogin = findViewById(R.id.gotoLogin);

        fAuth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extra the data from the from

                String FullName = registerFullName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confPass = registerConfPass.getText().toString();

                if(FullName.isEmpty()){
                    registerFullName.setError("Tam Adınızı Giriniz");
                    return;
                }
                if(email.isEmpty()){
                    registerEmail.setError("Mail adresinizi Giriniz");
                    return;
                }
                if(password.isEmpty()){
                    registerPassword.setError("Şifre Giriniz");
                    return;
                }
                if(confPass.isEmpty()){
                    registerConfPass.setError("Şifreyi Yeniden Giriniz");
                    return;
                }
                if(!password.equals(confPass)){
                    registerConfPass.setError("Şifreniz eşleşmedi");
                    return;
                }

                //data is validated
                //register the user using firebase

                Toast.makeText( RegisterActivity.this," Kayıt Onaylandı", Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //send user to next page
                        fireStoreKayıt();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });




    }

    public void fireStoreKayıt(){

        String email = registerEmail.getText().toString();
        String userName =registerFullName.getText().toString();
        Map<Object, String> yeniUser = new HashMap<Object, String>();
        yeniUser.put("email",email );
        yeniUser.put("userName",userName );


        database = FirebaseFirestore.getInstance();
        database.collection("Users").add(yeniUser).addOnCompleteListener(task ->{
            if (task.isSuccessful()){


            }
        } ).addOnFailureListener(e ->{
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
        );


    }
}