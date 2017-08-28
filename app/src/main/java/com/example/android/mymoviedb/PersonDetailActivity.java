package com.example.android.mymoviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.mymoviedb.IntentConstants.API_KEY;
import static com.example.android.mymoviedb.MainActivity.PlaceholderFragment.BASE_URL;

public class PersonDetailActivity extends AppCompatActivity {

    private int mCastId;


    private CardView mCastImageCardView;
    private int mCastImageSideSize;
    private ImageView mCastImageView;
    private TextView mCastNameTextView;
    private TextView mCastAgeTextView;
    private TextView mCastBirthPlaceTextView;
    private TextView mCastBirthdayTextView;

    private TextView mCastBioHeaderTextView;
    private TextView mCastBioTextView;

    private TextView mMovieCastTextView;
    private RecyclerView mMovieCastRecyclerView;
    private ArrayList<PersonMovie> mMovieCastOfPersons;
    private PersonMovieAdapter mPersonMovieAdapter;
    private RetrofitHelper retrofitHelper;
    private ApiInterface apiInterface;

    private Call<Person> mPersonDetailsCall;
    private Call<PersonMovie> mMovieCastsOfPersonsCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        Intent i = getIntent();
        mCastId = i.getIntExtra(IntentConstants.CAST_ID, -1);
        if (mCastId == -1) finish();
        String mCastName = i.getStringExtra(IntentConstants.CAST_NAME);
        setTitle(mCastName);

        mCastImageCardView = (CardView) findViewById(R.id.card_view_cast_detail);
        mCastImageSideSize = (int) (getResources().getDisplayMetrics().widthPixels * 0.33);
        mCastImageCardView.getLayoutParams().height = mCastImageSideSize;
        mCastImageCardView.getLayoutParams().width = mCastImageSideSize;
        mCastImageCardView.setRadius(mCastImageSideSize / 2);
        mCastImageView = (ImageView) findViewById(R.id.image_view_cast_detail);
        mCastNameTextView = (TextView) findViewById(R.id.text_view_name_cast_detail);
        mCastNameTextView.setText(mCastName);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mCastNameTextView.getLayoutParams();
        params.setMargins(params.leftMargin, mCastImageSideSize / 3, params.rightMargin, params.bottomMargin);

        mCastAgeTextView = (TextView) findViewById(R.id.text_view_age_cast_detail);
        mCastBirthPlaceTextView = (TextView) findViewById(R.id.text_view_birthplace_cast_detail);
        mCastBirthdayTextView = (TextView) findViewById(R.id.text_view_birthday_cast_detail);

        mCastBioHeaderTextView = (TextView) findViewById(R.id.text_view_bio_header_person_detail);
        mCastBioTextView = (TextView) findViewById(R.id.text_view_bio_person_detail);

        mMovieCastTextView = (TextView) findViewById(R.id.text_view_movie_cast_person_detail);
        mMovieCastRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movie_cast_person_detail);
        mMovieCastOfPersons = new ArrayList<>();
        mPersonMovieAdapter = new PersonMovieAdapter(getApplicationContext(), mMovieCastOfPersons);
        mMovieCastRecyclerView.setAdapter(mPersonMovieAdapter);
        mMovieCastRecyclerView.setLayoutManager(new LinearLayoutManager(PersonDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        retrofitHelper = new RetrofitHelper(BASE_URL);
        apiInterface = retrofitHelper.getAPI();

        loadActivity();

    }


    public void loadActivity() {
        mPersonDetailsCall = apiInterface.getPersonDetails(mCastId, API_KEY);
        Log.i("TAG", "CALL created");
        mPersonDetailsCall.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                if (!response.isSuccessful()) {
                    Log.i("TAG", "CALL created shit");
                    mPersonDetailsCall = call.clone();
                    mPersonDetailsCall.enqueue(this);
                    return;
                }
                if (response.body() == null) {
                    Log.i("TAG", "OH NO");
                    return;
                }

                if (response.isSuccessful()) {
                    Log.i("TAG", "WE are HERE");
                    Person person = response.body();
                    setTitle(person.getName());
                    Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w1000/" + person.getProfile_path())
                            .asBitmap()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mCastImageView);
                    setAge(person.getBirthday());
                    mCastBirthPlaceTextView.setText(person.getPlace_of_birth());
                    mCastBirthdayTextView.setText(person.getBirthday());
                    mCastBioHeaderTextView.setVisibility(View.VISIBLE);
                    mCastBioTextView.setText(person.getBiography());
                    setMovieCast(person.getId());
                }

            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Log.i("TAG", "Even worse");
                Log.i("TAG", t.getMessage());
            }
        });
    }

    public void setAge(String dateOfBirthString) {
        if (dateOfBirthString != null && !dateOfBirthString.trim().isEmpty()) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(dateOfBirthString);
                mCastAgeTextView.setText((Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(sdf2.format(releaseDate))) + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMovieCast(Integer personId) {
        mMovieCastsOfPersonsCall = apiInterface.getPersonMovies(personId, API_KEY);
        mMovieCastsOfPersonsCall.enqueue(new Callback<PersonMovie>() {
            @Override
            public void onResponse(Call<PersonMovie> call, Response<PersonMovie> response) {
                if (!response.isSuccessful()) {
                    mMovieCastsOfPersonsCall = call.clone();
                    mMovieCastsOfPersonsCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getCast() == null) return;

                for (PersonMovie movieCastOfPerson : response.body().getCast()) {
                    if (movieCastOfPerson == null) return;
                    if (movieCastOfPerson.getTitle() != null && movieCastOfPerson.getPosterPath() != null) {
                        mMovieCastTextView.setVisibility(View.VISIBLE);
                        mMovieCastOfPersons.add(movieCastOfPerson);
                    }
                }
                mPersonMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PersonMovie> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPersonDetailsCall != null) mPersonDetailsCall.cancel();
        if (mMovieCastsOfPersonsCall != null) mMovieCastsOfPersonsCall.cancel();
    }
}
