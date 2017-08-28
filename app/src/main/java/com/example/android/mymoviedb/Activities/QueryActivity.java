package com.example.android.mymoviedb.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.mymoviedb.ApiInterface;
import com.example.android.mymoviedb.Models.CastAndCrew;
import com.example.android.mymoviedb.R;
import com.example.android.mymoviedb.RetrofitHelper;

import java.util.ArrayList;

import static com.example.android.mymoviedb.IntentConstants.MENU_ID;
import static com.example.android.mymoviedb.IntentConstants.QUERY_TERM;

public class QueryActivity extends AppCompatActivity {

    RetrofitHelper retrofitHelper;
    ApiInterface apiInterface;
    ArrayList<CastAndCrew.Cast> queryCastList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        apiInterface = retrofitHelper.getAPI();

        Intent i = getIntent();
        String query_type = "";
        String menuId = i.getStringExtra(MENU_ID);
        String query = i.getStringExtra(QUERY_TERM);
        /*
        if(menuId.trim().equals("movie")){
            query_type = "movie";
        } else if(menuId.trim().equals("keyword")){
            query_type = "keyword";
        } else if(menuId.trim().equals("cast")){
            query_type = "person";
        }
        passQuery(query_type, query);
        */
    }
/*
    private void passQuery(String query_type, String query) {
        if(query_type.equals("movie")){

        } else if(query_type.equals("person")){
            Call<CastAndCrew> call = apiInterface.getCastAndCrewList(movieId, API_KEY);
            Log.i("TAG", "Call created");
            call.enqueue(new Callback<CastAndCrew>() {
                @Override
                public void onResponse(Call<CastAndCrew> call, Response<CastAndCrew> response) {

                    if (response.isSuccessful()) {
                        Log.d("TAG", "SET CAST2");
                        Toast.makeText(QueryActivity.this, "Success with Person Query", Toast.LENGTH_SHORT).show();
                        CastAndCrew results = response.body();

                        queryCastList.clear();
                        queryCastList.addAll(results.getCast());

                        //queryAdapter.notifyDataSetChanged();
                        //mCastView.setAdapter(mCastAdapter);
                        //mCastView.setLayoutManager(new LinearLayoutManager(QueryActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    }

                }


                @Override
                public void onFailure(Call<CastAndCrew> call, Throwable t) {
                    Log.d("TAG", "SET CAST3");
                    Toast.makeText(QueryActivity.this, "Couldn't load Query - " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }
    */


}
