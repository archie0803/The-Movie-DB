package com.example.android.mymoviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.category;
import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.API_KEY;
import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.BASE_URL;
import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.PAGE;
import static java.security.AccessController.getContext;

public class FavouritesActivity extends AppCompatActivity {

    RecyclerAdapter recyclerAdapter;
    RecyclerView favoriteView;
    ArrayList<String> allFavsId;
    ArrayList<Movie.Results> favoriteMovies;
    AddFavOpenHelper addFavOpenHelper;
    RetrofitHelper retrofitHelper;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        favoriteMovies = new ArrayList<>();
        favoriteView = (RecyclerView) findViewById(R.id.fav_recycler_view);
        favoriteView.setLayoutManager(new GridLayoutManager(FavouritesActivity.this, 2));
        allFavsId = new ArrayList<>();
        addFavOpenHelper = new AddFavOpenHelper(FavouritesActivity.this);
        allFavsId = addFavOpenHelper.getAllFavourites();
        retrofitHelper = new RetrofitHelper(BASE_URL);
        apiInterface = retrofitHelper.getAPI();
        getFavouriteMovies(allFavsId);


    }

    public void getFavouriteMovies(ArrayList<String> allFavsId) {
        recyclerAdapter = new RecyclerAdapter(FavouritesActivity.this, favoriteMovies);
        for (int i = 0; i < allFavsId.size(); i++) {
            int movieId = Integer.parseInt(allFavsId.get(i));
            Call<Movie.Results> call = apiInterface.getMovieDetails(movieId, API_KEY);
            Log.i("TAG", "Call created");

            call.enqueue(new Callback<Movie.Results>() {
                @Override
                public void onResponse(Call<Movie.Results> call, Response<Movie.Results> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(FavouritesActivity.this, "Success", Toast.LENGTH_LONG).show();
                        Movie.Results results = response.body();
                        favoriteMovies.add(results);
                        //recyclerAdapter.notifyDataSetChanged();
                        recyclerAdapter.notifyItemInserted(favoriteMovies.size());

                    }
                }

                @Override
                public void onFailure(Call<Movie.Results> call, Throwable t) {

                    Toast.makeText(FavouritesActivity.this, t.getMessage() + "", Toast.LENGTH_LONG).show();
                }
            });

        }

        favoriteView.setAdapter(recyclerAdapter);
    }
}
