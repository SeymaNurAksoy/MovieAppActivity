package com.example.movieappactivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FavoriteMain extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore database;
    RecyclerView recyclerView2;

    List<String> movielist ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_activity);

        movielist = new ArrayList<>();
        recyclerView2= findViewById(R.id.recyclerview2);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        verileriAl();



    }

    public void verileriAl(){
        database.collection("Post").addSnapshotListener((value, error) -> {
           if (error != null){
               Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
           }else{
               if(value != null){
                   if(value.isEmpty() == false){
                       List<DocumentSnapshot> documentSnapshot = value.getDocuments();

                       for( DocumentSnapshot document: documentSnapshot){
                           String name = (String) document.get("name");


                           movielist.add(name);

                       }

                       for ( String name : movielist){
                           System.out.println(name);
                       }
                       FavoriteAdapter adaptery = new FavoriteAdapter(this,movielist);
                       recyclerView2.setLayoutManager((new LinearLayoutManager(this)));
                       recyclerView2.setAdapter(adaptery);
                   }
               }
           }
        });



    }
}
