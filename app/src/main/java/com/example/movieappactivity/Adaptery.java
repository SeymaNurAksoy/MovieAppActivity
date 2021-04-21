package com.example.movieappactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {

private Context mcontext;
private List<MovieModelClass> mdata;

    public Adaptery(Context mcontext, List<MovieModelClass> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater inflater =LayoutInflater.from(mcontext);
        v =inflater.inflate(R.layout.movie_item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.id.setText(mdata.get(position).getId());
        holder.name.setText(mdata.get(position).getName());

        //using  glide library to dissplay the image
        //we need  to add a link before the image string
        //https://image.tmdb.org/t/p/w500/...
        Glide.with(mcontext)
                .load("https://image.tmdb.org/t/p/w500/"+mdata.get(position).getImg())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView id;
        TextView name;
        ImageView img;
        Button btn;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.textView_id);
            name=itemView.findViewById(R.id.textView2_name);
            img=itemView.findViewById(R.id.imageView);
            btn=itemView.findViewById(R.id.kaydet);
        }
    }
}
