package com.example.movieappactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener{


    FirebaseAuth auth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    Adaptery adaptery;
    SwipeRefreshLayout swipeRefreshLayout;

    Button button;

    List<MovieModelClass> list = new ArrayList<>();

    private static  String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=da4bad6c8fcebb12f3f937e118caf830";
    List<MovieModelClass> movieList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        swipeRefreshLayout= findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        reset_alert = new AlertDialog.Builder(  this);
        inflater = this.getLayoutInflater();
        movieList=new ArrayList<>();
        recyclerView=findViewById(R.id.recylerview);
        GetData getData = new GetData();
        getData.execute();





    }
    @Override
    public void onRefresh() {
        list.clear();
        GetData getData = new GetData();
        getData.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);


        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)) {
                    adaptery.getFilter().filter("");
                }
                else {
                    adaptery.getFilter().filter(newText.toString());
                }
                return(true);


            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.favorite_films) {
            startActivity(new Intent(getApplicationContext(), FavoriteMain.class));

        }

  if (item.getItemId() == R.id.resetUserPassword) {
            startActivity(new Intent(getApplicationContext(), ResetPassword.class));

        }
        if (item.getItemId() == R.id.uptadeEmailMenu) {
            View view = inflater.inflate(R.layout.reset_pop, null);
            reset_alert.setTitle("Mail değiştirmek istermisin?")
                    .setMessage("Yeni mail adresini gir")
                    .setPositiveButton("Değiştir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //validate the email address
                            EditText email = view.findViewById(R.id.rest_email_pop);
                            if (email.getText().toString().isEmpty()) {
                                email.setError("İşlem Başarısız");
                                return;
                            }
                            //send the reset link
                            FirebaseUser user = auth.getCurrentUser();
                            user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Mail değiştirildi.", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("İptal", null)
                    .setView(view)
                    .create().show();

        }

        if (item.getItemId() == R.id.delete_account_menu) {
            reset_alert.setTitle(" Hesabı silmek istiyormusun?")
                    .setMessage("Eminmisin?")
                    .setPositiveButton(" Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, " Hesap  silindi.", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).setNegativeButton(" İptal", null)
                    .create().show();
        }

        if (item.getItemId() == R.id.log_out) {

            Intent ıntent = new Intent(getApplicationContext(), LogOut.class);
            //Activity başlatıyoruz bizden intent türünde nesne istiyor kendi oluşturduğumuz nesneyi kullanıyoruz.
            startActivity(ıntent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public class GetData extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {

            String current ="";
            try{
                URL url;
                HttpURLConnection urlConnection =null;
                try {
                    url = new URL((JSON_URL));
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is =urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    int data =is.read();
                    while (data != -1){
                        current += (char) data;
                        data =isr.read();
                    }
                    return  current;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieModelClass model = new MovieModelClass();
                    model.setId(jsonObject1.getString("title"));
                    model.setName(jsonObject1.getString("overview"));
                    model.setImg(jsonObject1.getString("poster_path"));

                    movieList.add(model);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecylerView( movieList);
        }
    }

   private void PutDataIntoRecylerView(List<MovieModelClass> movieList){
        adaptery = new Adaptery(this, movieList, new CustomItemClickListener() {
            @Override
            public void onItemClick(MovieModelClass movie, int position) {
                Toast.makeText(getApplicationContext(),""+movie.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));

        recyclerView.setAdapter(adaptery);

   }


}