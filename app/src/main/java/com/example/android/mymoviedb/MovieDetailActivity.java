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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.API_KEY;
import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.BASE_URL;


public class MovieDetailActivity extends AppCompatActivity {

    TextView genreTextView, ratingTextView, releaseDateTextView, overviewTextView;
    WebView displayTrailer;
    ImageView backdropImageView;
    ArrayList<CastAndCrew.Cast> castArrayList;
    CastAdapter mCastAdapter;
    RecyclerView mCastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_to_fav);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to favourite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent i = getIntent();
        int movieId = i.getIntExtra(IntentConstants.MOVIE_ID, -1);
        String overview = i.getStringExtra(IntentConstants.MOVIE_OVERVIEW);
        String title = i.getStringExtra(IntentConstants.MOVIE_TITLE);
        String backdropPath = i.getStringExtra(IntentConstants.BACKDROP_PATH);
        String releaseDate = i.getStringExtra(IntentConstants.RELEASE_DATE);
        double rating = i.getDoubleExtra(IntentConstants.VOTE_AVERAGE, -1);
        boolean isTrailer = i.getBooleanExtra(IntentConstants.IS_TRAILER, false);

        setTitle(title);

        setCast(movieId);


        releaseDateTextView = (TextView) findViewById(R.id.release_date_textView);
        overviewTextView = (TextView) findViewById(R.id.overview_textView);
        backdropImageView = (ImageView) findViewById(R.id.backdrop_image);
        genreTextView = (TextView) findViewById(R.id.genre_textView);
        ratingTextView = (TextView) findViewById(R.id.rating_textView);
        mCastView = (RecyclerView) findViewById(R.id.cast_recycler_view);
        releaseDateTextView.append(": " + releaseDate);
        overviewTextView.append(overview);
        ratingTextView.append(": " + rating + "/10");


        Picasso.with(MovieDetailActivity.this)
                .load("https://image.tmdb.org/t/p/w500" + backdropPath).fit()
                .into(backdropImageView);

        /*
        if (isTrailer) {

            String frameVideo = "<iframe width=\"854\" height=\"480\" src=\"https://www.youtube.com/embed/2_SE2gQwXoo\" frameborder=\"0\" allowfullscreen></iframe>";

            displayTrailer = (WebView) findViewById(R.id.trailer_view);
            displayTrailer.setVisibility(View.VISIBLE);
            displayTrailer.setWebViewClient(new WebViewClient());
            displayTrailer.getSettings().setLoadWithOverviewMode(true);
            displayTrailer.getSettings().setUseWideViewPort(true);
            WebSettings webSettings = displayTrailer.getSettings();
            webSettings.setJavaScriptEnabled(true);
            displayTrailer.getSettings().setJavaScriptEnabled(true);
            displayTrailer.getSettings().setPluginsEnabled(true);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            //displayTrailer.loadData(frameVideo, mimeType, encoding);
            displayTrailer.setWebChromeClient(new WebChromeClient() {});
            String html = getHTML();
            displayTrailer.loadDataWithBaseURL("", html, mimeType, encoding, "");
        }


        }
        */


    }

    public void setCast(int movieId) {

        RetrofitHelper retrofitHelper = new RetrofitHelper(BASE_URL);
        ApiInterface apiInterface = retrofitHelper.getAPI();
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

    public String getHTML() {
        String html = "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 95%; padding:0px; margin:0px\"" +
                " id=\"ytplayer\" type=\"text/html\" src=\"http://www.youtube.com/embed/"
                + "J2fB5XWj6IE"
                + "?fs=0\" frameborder=\"0\">\n"
                + "</iframe>\n";
        return html;
    }
}
