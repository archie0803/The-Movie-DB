package com.example.android.mymoviedb;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.android.mymoviedb.IntentConstants.API_KEY;
import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.BASE_URL;
import static com.example.android.mymoviedb.R.id.imageView;


public class MovieDetailActivity extends AppCompatActivity {

    private TextView runtimeTextView, genreTextView, ratingTextView, releaseDateTextView, overviewTextView, directorTextView;
    private TextView mCastTextView, mTrailerTextView, mReviewTextView, mSimilarMoviesTextView;
    private ImageView backdropImageView;
    private int mBackdropHeight, mBackdropWidth;
    private View mHLineOne, mHLineTwo, mHLineThree, mHLineFour;
    private ArrayList<CastAndCrew.Cast> mCastArrayList;
    private ArrayList<Review.ReviewResult> mReviewArrayList;
    private ArrayList<Trailer.TrailerResults> mTrailerArrayList;
    private ArrayList<Movie.Results.Genre> genreList;
    private ArrayList<Movie.Results> mSimilarMovies;
    private CastAdapter mCastAdapter;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private SimilarMoviesAdapter mSimilarMoviesAdapter;
    private RecyclerView mCastView, mReviewRecyclerView, mTrailerRecyclerView, mSimilarMoviesRecyclerView;
    private RetrofitHelper retrofitHelper;
    private ApiInterface apiInterface;
    private Movie.Results res;
    private AddFavOpenHelper addFav;
    private int movieId;
    private Call<Movie.Results> mMovieDetailsCall;
    private Call<Trailer> mMovieTrailersCall;
    private Call<SimilarMoviesResponse> mSimilarMoviesCall;
    private Call<Review> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        movieId = i.getIntExtra(IntentConstants.MOVIE_ID, -1);
        String title = i.getStringExtra(IntentConstants.MOVIE_TITLE);
        setTitle(title);

        if (movieId == -1) finish();

        backdropImageView = (ImageView) findViewById(R.id.backdrop_image);
        mBackdropWidth = getResources().getDisplayMetrics().widthPixels;
        mBackdropHeight = (int) (mBackdropWidth / 1.77);
        backdropImageView.getLayoutParams().height = mBackdropHeight;

        runtimeTextView = (TextView) findViewById(R.id.runtime_text_view);
        releaseDateTextView = (TextView) findViewById(R.id.release_date_text_view);
        ratingTextView = (TextView) findViewById(R.id.rating_text_view);
        directorTextView = (TextView) findViewById(R.id.director_text_view);
        genreTextView = (TextView) findViewById(R.id.genre_text_view);
        overviewTextView = (TextView) findViewById(R.id.overview_text_view);

        mHLineOne = findViewById(R.id.view_horizontal_line_one);

        mCastTextView = (TextView) findViewById(R.id.cast_text);
        mCastView = (RecyclerView) findViewById(R.id.cast_recycler_view);
        mCastArrayList = new ArrayList<>();
        mCastAdapter = new CastAdapter(MovieDetailActivity.this, mCastArrayList);
        mCastView.setAdapter(mCastAdapter);
        mCastView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        mHLineTwo = findViewById(R.id.view_horizontal_line_two);

        mTrailerTextView = (TextView) findViewById(R.id.trailer_text);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler_view);
        (new LinearSnapHelper()).attachToRecyclerView(mTrailerRecyclerView);
        mTrailerArrayList = new ArrayList<>();
        mTrailerAdapter = new TrailerAdapter(MovieDetailActivity.this, mTrailerArrayList);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        mHLineThree = findViewById(R.id.view_horizontal_line_three);

        mSimilarMoviesTextView = (TextView) findViewById(R.id.similar_movie_text);
        mSimilarMoviesRecyclerView = (RecyclerView) findViewById(R.id.similar_movie_recycler_view);
        mSimilarMovies = new ArrayList<>();
        mSimilarMoviesAdapter = new SimilarMoviesAdapter(MovieDetailActivity.this, mSimilarMovies);
        mSimilarMoviesRecyclerView.setAdapter(mSimilarMoviesAdapter);
        mSimilarMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        mHLineFour = findViewById(R.id.view_horizontal_line_four);

        mReviewTextView = (TextView) findViewById(R.id.review_text);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        mReviewArrayList = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(MovieDetailActivity.this, mReviewArrayList);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));


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

        loadActivity();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mSimilarMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMovieDetailsCall != null) mMovieDetailsCall.cancel();
        if (mMovieTrailersCall != null) mMovieTrailersCall.cancel();
        if (call != null) call.cancel();
        if (mSimilarMoviesCall != null) mSimilarMoviesCall.cancel();
    }

    private void loadActivity() {

        retrofitHelper = new RetrofitHelper(BASE_URL);
        apiInterface = retrofitHelper.getAPI();

        mMovieDetailsCall = apiInterface.getMovieDetails(movieId, API_KEY);
        Log.i("TAG", "Call created");
        mMovieDetailsCall.enqueue(new Callback<Movie.Results>() {
            @Override
            public void onResponse(Call<Movie.Results> call, Response<Movie.Results> response) {
                if (!response.isSuccessful()) {
                    mMovieDetailsCall = call.clone();
                    mMovieDetailsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;

                if (response.isSuccessful()) {
                    Log.d("TAG", "SET BASIC CONTENT");
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
                    releaseDateTextView.append(result.getReleaseDate());
                    overviewTextView.append(res.getOverview());
                    ratingTextView.append(res.getVoteAverage() + "/10");
                    runtimeTextView.append(res.getRuntime() / 60 + " hr " + res.getRuntime() % 60 + " mins");
                    Picasso.with(MovieDetailActivity.this)
                            .load("https://image.tmdb.org/t/p/w780/" + res.getBackdropPath()).fit()
                            .into(backdropImageView);
                    backdropImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MovieDetailActivity.this);
                            builder.setCancelable(true);
                            View v = getLayoutInflater().inflate(R.layout.image_in_dialog, null);
                            final ImageView fullImage = v.findViewById(R.id.dialog_image);
                            Picasso.with(MovieDetailActivity.this)
                                    .load("https://image.tmdb.org/t/p/w780/" + res.getBackdropPath()).fit()
                                    .into(fullImage);
                            fullImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            int mImageWidth = getResources().getDisplayMetrics().widthPixels;
                            int mImageHeight = (int) (mImageWidth / 1.77);
                            fullImage.getLayoutParams().height = mImageHeight;
                            builder.setView(v);
                            AlertDialog dialog = builder.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                            dialog.show();
                        }
                    });


                    setCasts();

                    setTrailers();

                    setSimilarMovies();

                    setReviews();
                }
            }

            @Override
            public void onFailure(Call<Movie.Results> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, "Couldn't load data - " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void setCasts() {
        Call<CastAndCrew> call = apiInterface.getCastAndCrewList(movieId, API_KEY);
        call.enqueue(new Callback<CastAndCrew>() {
            @Override
            public void onResponse(Call<CastAndCrew> call, Response<CastAndCrew> response) {
                if (response.isSuccessful()) {
                    CastAndCrew results = response.body();
                    mCastTextView.setVisibility(View.VISIBLE);
                    mHLineOne.setVisibility(View.VISIBLE);
                    mCastArrayList.clear();
                    mCastArrayList.addAll(results.getCast());
                    mCastAdapter.notifyDataSetChanged();
                    mCastView.setAdapter(mCastAdapter);
                    mCastView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    for (CastAndCrew.Crew crew : results.getCrew()) {
                        if (crew.getJob().trim().equals("Director")) {
                            directorTextView.append(crew.getName());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CastAndCrew> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, "Couldn't load Cast - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTrailers() {
        mMovieTrailersCall = apiInterface.getTrailers(movieId, API_KEY);
        mMovieTrailersCall.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {
                if (!response.isSuccessful()) {
                    mMovieTrailersCall = call.clone();
                    mMovieTrailersCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                for (Trailer.TrailerResults video : response.body().getResults()) {
                    if (video != null && video.getSite() != null && video.getSite().trim().equals("YouTube") && video.getType() != null && video.getType().trim().equals("Trailer"))
                        mTrailerArrayList.add(video);
                }
                if (!mTrailerArrayList.isEmpty()) {
                    mTrailerTextView.setVisibility(View.VISIBLE);
                    mHLineTwo.setVisibility(View.VISIBLE);
                    mTrailerAdapter.notifyDataSetChanged();
                    mTrailerRecyclerView.setAdapter(mTrailerAdapter);
                    mTrailerRecyclerView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                Log.d("TAG", "Error: " + t.getMessage());
                Toast.makeText(MovieDetailActivity.this, "Couldn't load Trailer - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSimilarMovies() {
        mSimilarMoviesCall = apiInterface.getSimilarMovies(movieId, API_KEY);
        mSimilarMoviesCall.enqueue(new Callback<SimilarMoviesResponse>() {
            @Override
            public void onResponse(Call<SimilarMoviesResponse> call, Response<SimilarMoviesResponse> response) {
                if (!response.isSuccessful()) {
                    mSimilarMoviesCall = call.clone();
                    mSimilarMoviesCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                for (Movie.Results movieBrief : response.body().getResults()) {
                    if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                        mSimilarMovies.add(movieBrief);
                }

                if (!mSimilarMovies.isEmpty()) {
                    mSimilarMoviesTextView.setVisibility(View.VISIBLE);
                    mSimilarMoviesRecyclerView.setVisibility(View.VISIBLE);
                    mHLineThree.setVisibility(View.VISIBLE);
                    Log.i("TAG", "SUCCESS WITH SIMILAR MOVIES");
                }
                mSimilarMoviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SimilarMoviesResponse> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, "Couldn't load Similar Movies - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setReviews() {

        call = apiInterface.getReviews(movieId, API_KEY);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(MovieDetailActivity.this, "Success with Review", Toast.LENGTH_SHORT).show();
                    Review results = response.body();
                    mReviewArrayList.clear();
                    mHLineFour.setVisibility(View.VISIBLE);
                    mReviewTextView.setVisibility(View.VISIBLE);
                    mReviewArrayList.addAll(results.getResults());
                    mReviewAdapter.notifyDataSetChanged();
                    mReviewRecyclerView.setAdapter(mReviewAdapter);
                    mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, "Couldn't load reviews - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
