package com.example.android.mymoviedb;

import com.example.android.mymoviedb.Models.CastAndCrew;
import com.example.android.mymoviedb.Models.Movie;
import com.example.android.mymoviedb.Models.Person;
import com.example.android.mymoviedb.Models.PersonMovie;
import com.example.android.mymoviedb.Models.Review;
import com.example.android.mymoviedb.Models.SearchResult;
import com.example.android.mymoviedb.Models.SimilarMoviesResponse;
import com.example.android.mymoviedb.Models.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Archita on 01-08-2017.
 */

public interface ApiInterface {
    @GET("3/movie/{category}")
    Call<Movie> MoviesList(@Path("category") String category, @Query("api_key") String api_key, @Query("language") String language, @Query("page") int page);

    //get movie details
    @GET("3/movie/{movie_id}")
    Call<Movie.Results> getMovieDetails(@Path("movie_id") int movieID, @Query("api_key") String api_key);

    //get cast and crew
    @GET("3/movie/{movie_id}/credits")
    Call<CastAndCrew> getCastAndCrewList(@Path("movie_id") int movieID, @Query("api_key") String api_key);

    //get reviews
    @GET("3/movie/{movie_id}/reviews")
    Call<Review> getReviews(@Path("movie_id") int movieID, @Query("api_key") String api_key);


    //get trailers
    @GET("3/movie/{movie_id}/videos")
    Call<Trailer> getTrailers(@Path("movie_id") int movieID, @Query("api_key") String api_key);

    //get similar movies
    @GET("3/movie/{movie_id}/similar")
    Call<SimilarMoviesResponse> getSimilarMovies(@Path("movie_id") int movieID, @Query("api_key") String api_key);

    //get genre
    @GET("3/genre/movie/list")
    Call<String> getGenres(@Query("api_key") String api_key);

    //get Person details
    @GET("/3/person/{person_id}")
    Call<Person> getPersonDetails(@Path("person_id") int personId, @Query("api_key") String api_key);

    //get Movies for a person
    @GET("/3/person/{person_id}/movie_credits")
    Call<PersonMovie> getPersonMovies(@Path("person_id") int castId, @Query("api_key") String api_key);

    //search multi
    @GET("3/search/multi")
    Call<SearchResult> searchQuery(@Query("api_key") String api_key, @Query("query") String query_term, @Query("page") int page);

    /*
    //get movies by genre
    @GET("3/genre/{genre_id}/movies")
    Call<Review> getMovieByGenre(@Path("genre_id`") int genreID, @Query("api_key") String api_key);

    //search Movie
    @GET("3/search/{query_type}")
    Call<Movie> movieQuery(@Path("query_type") String query_type, @Query("api_key") String api_key, @Query("query") String query_term);

    //search Person
    @GET("3/search/{query_type}")
    Call<CastAndCrew> personQuery(@Path("query_type") String query_type, @Query("api_key") String api_key, @Query("query") String query_term);


    @GET("{type}")
    Call<ResponseComplete> getMoviesList(@Path("type") String type, @Query("api_key") String api_key);
    */

}
