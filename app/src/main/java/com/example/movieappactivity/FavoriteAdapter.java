package com.example.movieappactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>  {

    FavoriteMain favoriteMain =new FavoriteMain();
    List<MovieModelClass> movieList;
    Context mcontext;
    FirebaseAuth auth;
    FirebaseFirestore database;

    public FavoriteAdapter(Context mcontext, List<MovieModelClass> movieList) {
        //this.movieList = movieList;
        this.mcontext=mcontext;
        this.movieList = movieList;


    }


    @Override
    public FavoriteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        v = inflater.inflate(R.layout.favorite_item, parent, false);


        return new FavoriteAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(movieList.get(position).getName());
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();



       holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //System.out.println(database.collection("FavoriListFilms").document().get(Source.valueOf("name")));
               movieList.remove(position);
               notifyItemRemoved(position);
               notifyItemRangeChanged(position,movieList.size());

               String name =  movieList.get(position).getName();
               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               FirebaseFirestore.getInstance().collection("FavoriListFilms")
                       .whereEqualTo("name",name).get()
                       .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                           @Override
                           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                               WriteBatch batch = FirebaseFirestore.getInstance().batch();
                               List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                               for(DocumentSnapshot snapshot :snapshotList){

                                   batch.delete(snapshot.getReference());
                               }
                               batch.commit()
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {


                                           }
                                       });
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull  Exception e) {

                   }
               });


           }
       });

    }



    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageButton deleteBtn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameText);
            deleteBtn = itemView.findViewById(R.id.deletebtn);


        }
    }





    }

