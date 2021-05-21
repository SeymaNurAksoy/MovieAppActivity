package com.example.movieappactivity;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.Reference;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> implements Filterable {


    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private FirebaseFirestore database;


    private Context mcontext;
    private List<MovieModelClass> mdata;
    private List<MovieModelClass> mdataFilter;


    MovieModelClass movieModelClass = new MovieModelClass();

    public Adaptery(Context mcontext, List<MovieModelClass> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
        this.mdataFilter = mdata;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        v = inflater.inflate(R.layout.movie_item, parent, false);
     /*   v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for click item listener
                final MyViewHolder myViewHolder = new MyViewHolder(v);
                customItemClickListener.onItemClick(mdataFilter.get(myViewHolder.getAdapterPosition()),myViewHolder.getAdapterPosition());
            }
        });*/



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(mdataFilter.get(position).getName());
        holder.description.setText(mdataFilter.get(position).getDescription());


        //using  glide library to dissplay the image
        //we need  to add a link before the image string
        //https://image.tmdb.org/t/p/w500/...
        Glide.with(mcontext)
                .load("https://image.tmdb.org/t/p/w500/" + mdataFilter.get(position).getImg())
                .into(holder.img);

       // Uri uri = Uri.parse("https://image.tmdb.org/t/p/w500/" + mdataFilter.get(position).getImg());
     //  System.out.println(uri);


        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = auth.getCurrentUser().getEmail();


                String name;
                String id;


                FirebaseUser user = auth.getCurrentUser();

                String uuid = user.getUid();

               // String name = holder.name.getText().toString();
                 name = holder.name.getText().toString();

                HashMap<String,Object> posthashMap =new HashMap<String, Object>();
                posthashMap.put("name",name);
                posthashMap.put("email",email);

                posthashMap.put("uuid",uuid);


                database.collection("FavoriListFilms").add(posthashMap).addOnCompleteListener(task ->{
                    if (task.isSuccessful()){

                        Toast.makeText(mcontext.getApplicationContext(), "Favorilere eklendi",Toast.LENGTH_SHORT).show();

                    }
                } ).addOnFailureListener(e ->{
                            Toast.makeText(mcontext.getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                        );
            }
        });





    }

    @Override
    public int getItemCount() {
        return mdataFilter.size();
    }

 public static class MyViewHolder extends RecyclerView.ViewHolder {

     TextView name;
     TextView description;
     ImageView img;
     ImageButton btn;

     public MyViewHolder(@NonNull View itemView) {
         super(itemView);

         name = itemView.findViewById(R.id.textView_id);
         description = itemView.findViewById(R.id.textView2_name);
         img = itemView.findViewById(R.id.imageView);
         btn = itemView.findViewById(R.id.kaydet);
         btn = itemView.findViewById(R.id.kaydet);

     }
 }

    @Override
        public Filter getFilter () {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String searchString = charSequence.toString();
                    if (searchString.isEmpty()) {
                        mdataFilter = mdata;
                    } else {
                        ArrayList<MovieModelClass> tempFilteredList = new ArrayList<>();
                        for (MovieModelClass station : mdata) {
                            // search for station name
                            if (station.getName().toLowerCase().contains(searchString)) {
                                tempFilteredList.add(station);
                            }
                        }
                        mdataFilter = tempFilteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mdataFilter;
                    return filterResults;

                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mdataFilter = (ArrayList<MovieModelClass>) filterResults.values;
                    notifyDataSetChanged();
                }
            };

        }
    }


