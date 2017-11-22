package com.example.android.mymoviedb.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.mymoviedb.Adapters.SearchResultAdapter;
import com.example.android.mymoviedb.ApiInterface;
import com.example.android.mymoviedb.Models.SearchResult;
import com.example.android.mymoviedb.R;
import com.example.android.mymoviedb.RetrofitHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.mymoviedb.IntentConstants.API_KEY;
import static com.example.android.mymoviedb.IntentConstants.BASE_URL;
import static com.example.android.mymoviedb.IntentConstants.MENU_ID;
import static com.example.android.mymoviedb.IntentConstants.QUERY_TERM;

public class QueryActivity extends AppCompatActivity {

    RetrofitHelper retrofitHelper;
    ApiInterface apiInterface;
    String query;
    int query_type;
    private ArrayList<SearchResult.SearchRes> mSearchResultList, mPersonList, mMovieList;
    private SearchResultAdapter mSearchAdapter, mMovieAdapter, mPersonAdapter;
    private RecyclerView mSearchRecyclerView;
    private Call<SearchResult> mSearchCall;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        retrofitHelper = new RetrofitHelper(BASE_URL);
        apiInterface = retrofitHelper.getAPI();

        Intent i = getIntent();
        String menuId = i.getStringExtra(MENU_ID);
        query = i.getStringExtra(QUERY_TERM);

        setTitle(query);

        if (menuId.trim().equals("movie")) {
            query_type = 1;
        } else if (menuId.trim().equals("cast")) {
            query_type = 2;
        } else if (menuId.trim().equalsIgnoreCase("Search")) {
            query_type = 3;
        }

        Log.i("TAG", query_type + query + " Query");
        mSearchResultList = new ArrayList<>();
        mMovieList = new ArrayList<>();
        mPersonList = new ArrayList<>();
        mSearchAdapter = new SearchResultAdapter(QueryActivity.this, mSearchResultList);
        mPersonAdapter = new SearchResultAdapter(QueryActivity.this, mPersonList);
        mMovieAdapter = new SearchResultAdapter(QueryActivity.this, mMovieList);
        mSearchRecyclerView = (RecyclerView) findViewById(R.id.search_result_recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QueryActivity.this, LinearLayoutManager.VERTICAL, false);
        mSearchRecyclerView.setLayoutManager(linearLayoutManager);
        mSearchRecyclerView.setAdapter(mSearchAdapter);
        mSearchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loadSearchResults(query_type, query);
                    loading = true;
                }

            }
        });

        loadSearchResults(query_type, query);
    }

    private void loadSearchResults(int query_type, String query) {
        final int q_type = query_type;
        mSearchCall = apiInterface.searchQuery(API_KEY, query, presentPage);
        mSearchCall.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (!response.isSuccessful()) {
                    mSearchCall = call.clone();
                    mSearchCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (response.body().getResults() == null) return;


                for (SearchResult.SearchRes res : response.body().getResults()) {
                    Log.i("TAG", "ohkay.." + res.getName());
                    if ((res.getMediaType().equals("movie") || res.getMediaType().equals("person")) && (res.getPosterPath() != null || res.getProfilePath() != null))
                        mSearchResultList.add(res);
                    if (res.getMediaType() != null || !res.getMediaType().equals("tv")) {
                        if (q_type == 1 && res.getPosterPath() != null) {
                            if (res.getMediaType().equalsIgnoreCase("movie"))
                                mMovieList.add(res);
                            mMovieAdapter.notifyDataSetChanged();
                            mSearchRecyclerView.setAdapter(mMovieAdapter);
                            if (response.body().getPage() == response.body().getTotal_pages())
                                pagesOver = true;
                            else
                                presentPage++;

                        } else if (q_type == 2 && res.getProfilePath() != null) {
                            if (res.getMediaType().equalsIgnoreCase("person"))
                                mPersonList.add(res);
                            mPersonAdapter.notifyDataSetChanged();
                            mSearchRecyclerView.setAdapter(mPersonAdapter);
                            if (response.body().getPage() == response.body().getTotal_pages())
                                pagesOver = true;
                            else
                                presentPage++;
                        }
                    }
                    mSearchAdapter.notifyDataSetChanged();
                }
                Log.i("TAG", "size" + mSearchResultList.size());

            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.i("TAG", "Ohoo");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchCall != null) mSearchCall.cancel();
    }
}
