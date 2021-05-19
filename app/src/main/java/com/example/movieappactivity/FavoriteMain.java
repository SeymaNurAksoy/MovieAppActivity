package com.example.movieappactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FavoriteMain extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore database;
    RecyclerView recyclerView2;
    List<String> movielist ;
    MovieModelClass movieModelClass ;
    List<MovieModelClass> modelListe = new ArrayList<MovieModelClass>();
    FavoriteAdapter favoriteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_activity);

        movieModelClass = new MovieModelClass();
        movielist = new ArrayList<>();
        recyclerView2= findViewById(R.id.recyclerview2);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        verileriAl();



    }



    public void verileriAl(){
        database.collection("FavoriListFilms").addSnapshotListener((value, error) -> {
           if (error != null){
               Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
           }else{
               if(value != null){
                   if(value.isEmpty() == false){
                       List<DocumentSnapshot> documentSnapshot = value.getDocuments();
                       movielist.clear();
                       for( DocumentSnapshot document: documentSnapshot){
                           String id = (String) document.get("id");
                           String name = (String) document.get("name");
                           String email = (String) document.get("email");
                           System.out.println(auth.getCurrentUser().getEmail());
                         if(auth.getCurrentUser().getEmail().contains(email)) {
                           //  movielist.add(name);
                             movieModelClass= new MovieModelClass(id,name,email);
                             modelListe.add(movieModelClass);

                         }
                       }
                       for ( String name : movielist){
                           System.out.println(name);
                       }

                       FavoriteAdapter adaptery = new FavoriteAdapter(this,modelListe);
                       recyclerView2.setLayoutManager((new LinearLayoutManager(this)));
                       recyclerView2.setAdapter(adaptery);
                   }
               }
           }
        });

        System.out.println(database.collection("FavoriteListFilms").document());


    }

}



/*   private fun setOnRecyclerViewItemSwipedListener() {
        ItemTouchHelper(object : SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val swipedProduct = products[position]
                deleteProduct(position, swipedProduct)
            }

        }).attachToRecyclerView(dataBinding.productsRecyclerView)
    }

    private fun deleteProduct(position: Int, product: Product) {
        displayProgressBar()
        val isProductDeletedLiveData = viewModel.deleteProduct(product.id!!)
        isProductDeletedLiveData.observe(this) { dataOrException ->
            val isProductDeleted = dataOrException.data
            if (isProductDeleted != null) {
                if (isProductDeleted) {
                    products.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, products.size)
                    hideProgressBar()
                }
            }

            if (dataOrException.e != null) {
                logErrorMessage(dataOrException.e!!.message!!)
            }
        }
    }*/