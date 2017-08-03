package com.example.android.mymoviedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.YELLOW;
import static android.os.Build.VERSION_CODES.M;
import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.API_KEY;
import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.BASE_URL;


public class MovieDetailActivity extends AppCompatActivity {

    TextView runtimeTextView, genreTextView, ratingTextView, releaseDateTextView, overviewTextView;
    WebView displayTrailer;
    ImageView backdropImageView;
    ArrayList<CastAndCrew.Cast> castArrayList;
    ArrayList<Review.ReviewResult> reviewArrayList;
    ArrayList<Trailer.TrailerResult> trailerArrayList;
    ArrayList<String> trailerNameList;
    ArrayList<Movie.Results.Genre> genreList;
    CastAdapter mCastAdapter;
    ReviewAdapter mReviewAdapter;
    RecyclerView mCastView, mReviewRecyclerView;
    ListView mTrailerView;
    ArrayAdapter<String> mTrailerAdapter;
    RetrofitHelper retrofitHelper;
    ApiInterface apiInterface;
    Movie.Results res;
    AddFavOpenHelper addFav;
    int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        runtimeTextView = (TextView) findViewById(R.id.runtime_text_view);
        releaseDateTextView = (TextView) findViewById(R.id.release_date_text_view);
        overviewTextView = (TextView) findViewById(R.id.overview_text_view);
        backdropImageView = (ImageView) findViewById(R.id.backdrop_image);
        genreTextView = (TextView) findViewById(R.id.genre_text_view);
        ratingTextView = (TextView) findViewById(R.id.rating_text_view);
        mCastView = (RecyclerView) findViewById(R.id.cast_recycler_view);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        mTrailerView = (ListView) findViewById(R.id.trailer_list_view);
        displayTrailer = (WebView) findViewById(R.id.trailer_view);

        Intent i = getIntent();
        movieId = i.getIntExtra(IntentConstants.MOVIE_ID, -1);
        String title = i.getStringExtra(IntentConstants.MOVIE_TITLE);
        setTitle(title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_to_fav);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFav = new AddFavOpenHelper(MovieDetailActivity.this);
                if (addFav.isFavourite(movieId + "")) {
                    Snackbar.make(view, "Already in Favourites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    addFav.addToFavourites(movieId + "");
                    Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (addFav.isFavourite(movieId + "")) {
                    addFav.removeFromFavourites(movieId + "");
                    Snackbar.make(view, "Removed from Favourites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Not in Favourites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                return true;
            }
        });


        retrofitHelper = new RetrofitHelper(BASE_URL);
        apiInterface = retrofitHelper.getAPI();
        genreList = new ArrayList<>();


        Call<Movie.Results> call = apiInterface.getMovieDetails(movieId, API_KEY);
        Log.i("TAG", "Call created");
        call.enqueue(new Callback<Movie.Results>() {
            @Override
            public void onResponse(Call<Movie.Results> call, Response<Movie.Results> response) {

                if (response.isSuccessful()) {
                    Log.d("TAG", "SET REVIEW2");
                    Toast.makeText(MovieDetailActivity.this, "Success with Review", Toast.LENGTH_SHORT).show();
                    Movie.Results result = response.body();
                    res = result;
                    genreList = new ArrayList<>();
                    genreList.clear();
                    genreList.addAll(result.getGenres());

                    String genre = "";
                    if (genreList.size() - 1 > 0) {
                        for (int j = 0; j < genreList.size() - 1; j++) {
                            genre += genreList.get(j).getName() + ", ";
                        }
                        genre += genreList.get(genreList.size() - 1).getName();
                    } else {
                        genre += genreList.get(genreList.size() - 1).getName();
                    }

                    genreTextView.append(genre);
                    releaseDateTextView.append(res.getReleaseDate());
                    overviewTextView.append(res.getOverview());
                    ratingTextView.append(res.getVoteAverage() + "/10");
                    runtimeTextView.append(res.getRuntime() + " mins");
                    Picasso.with(MovieDetailActivity.this)
                            .load("https://image.tmdb.org/t/p/w500" + res.getBackdropPath()).fit()
                            .into(backdropImageView);
                }
            }

            @Override
            public void onFailure(Call<Movie.Results> call, Throwable t) {
                Log.d("TAG", "SET DETAILS3");
                Toast.makeText(MovieDetailActivity.this, "Couldn't load reviews", Toast.LENGTH_SHORT).show();

            }
        });


        setCast(movieId);

        setTrailer(movieId);

        setReviews(movieId);

        mTrailerView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String nextKey = trailerArrayList.get(pos).getKey();
                getHTML(nextKey);
            }
        });

    }

    private void setReviews(int movieId) {

        reviewArrayList = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(MovieDetailActivity.this, reviewArrayList);

        Call<Review> call = apiInterface.getReviews(movieId, API_KEY);
        Log.i("TAG", "Call created");
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {

                if (response.isSuccessful()) {
                    Log.d("TAG", "SET REVIEW2");
                    Toast.makeText(MovieDetailActivity.this, "Success with Review", Toast.LENGTH_SHORT).show();
                    Review results = response.body();
                    reviewArrayList.clear();
                    reviewArrayList.addAll(results.getResults());
                    mReviewAdapter.notifyDataSetChanged();
                    mReviewRecyclerView.setAdapter(mReviewAdapter);
                    mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Log.d("TAG", "SET REVIEW3");
                Toast.makeText(MovieDetailActivity.this, "Couldn't load reviews", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void setCast(int movieId) {

        Log.d("TAG", "SET CAST1");

        castArrayList = new ArrayList<>();
        mCastAdapter = new CastAdapter(MovieDetailActivity.this, castArrayList);

        Call<CastAndCrew> call = apiInterface.getCastAndCrewList(movieId, API_KEY);
        Log.i("TAG", "Call created");
        call.enqueue(new Callback<CastAndCrew>() {
            @Override
            public void onResponse(Call<CastAndCrew> call, Response<CastAndCrew> response) {

                if (response.isSuccessful()) {
                    Log.d("TAG", "SET CAST2");
                    Toast.makeText(MovieDetailActivity.this, "Success with Cast", Toast.LENGTH_SHORT).show();
                    CastAndCrew results = response.body();
                    castArrayList.clear();
                    castArrayList.addAll(results.getCast());
                    mCastAdapter.notifyDataSetChanged();
                    mCastView.setAdapter(mCastAdapter);
                    mCastView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }


            @Override
            public void onFailure(Call<CastAndCrew> call, Throwable t) {
                Log.d("TAG", "SET CAST3");
                Toast.makeText(MovieDetailActivity.this, "Couldn't load Cast", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setTrailer(int movieId) {

        trailerArrayList = new ArrayList<>();
        trailerNameList = new ArrayList<>();
        mTrailerAdapter = new ArrayAdapter<>(MovieDetailActivity.this, android.R.layout.simple_list_item_1, trailerNameList);
        mTrailerView.setAdapter(mTrailerAdapter);

        Call<Trailer> call = apiInterface.getTrailers(movieId, API_KEY);
        Log.i("TAG", "Call created");
        call.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {

                if (response.isSuccessful()) {
                    Log.d("TAG", "SET TRAILER2");
                    Toast.makeText(MovieDetailActivity.this, "Success with Trailer", Toast.LENGTH_SHORT).show();
                    Trailer results = response.body();
                    trailerArrayList.clear();
                    trailerArrayList.addAll(results.getResults());

                    String key = trailerArrayList.get(0).getKey();
                    //displayTrailer.setVisibility(View.VISIBLE);
                    displayTrailer.setWebViewClient(new WebViewClient());
                    displayTrailer.getSettings().setLoadWithOverviewMode(true);
                    displayTrailer.getSettings().setUseWideViewPort(true);
                    displayTrailer.getSettings().setJavaScriptEnabled(true);
                    //displayTrailer.getSettings().setPluginsEnabled(true);
                    displayTrailer.setWebChromeClient(new WebChromeClient() {
                    });
                    getHTML(key);

                    for (int i = 0; i < trailerArrayList.size(); i++) {
                        String name = trailerArrayList.get(i).getName();
                        trailerNameList.add(name);
                        mTrailerAdapter.notifyDataSetChanged();
                    }
                    mTrailerView.setAdapter(mTrailerAdapter);
                }
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                Log.d("TAG", "SET TRAILER3");
                Toast.makeText(MovieDetailActivity.this, "Couldn't load Trailer", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getHTML(String key) {
        String html = "<iframe class=\"youtube-player\" style=\"border: 0; width=\"854\" height=\"480\" padding:0px; margin:0px\"" +
                " id=\"ytplayer\" type=\"text/html\" src=\"https://www.youtube.com/embed/"
                + key
                + "\" frameborder=\"0\">\n"
                + "</iframe>\n";

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        displayTrailer.loadData(html, mimeType, encoding);
        displayTrailer.loadDataWithBaseURL("", html, mimeType, encoding, "");

    }
}
