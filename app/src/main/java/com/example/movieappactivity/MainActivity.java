package com.example.movieappactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

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

public class MainActivity extends AppCompatActivity {

    private static  String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=da4bad6c8fcebb12f3f937e118caf830";
    List<MovieModelClass> movieList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        movieList=new ArrayList<>();
        recyclerView=findViewById(R.id.recylerview);


        GetData getData = new GetData();
        getData.execute();
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
        Adaptery adaptery = new Adaptery(this,movieList);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));



        recyclerView.setAdapter(adaptery);
   }
}