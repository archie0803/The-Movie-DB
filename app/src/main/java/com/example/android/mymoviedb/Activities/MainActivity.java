package com.example.android.mymoviedb.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.mymoviedb.ApiInterface;
import com.example.android.mymoviedb.IntentConstants;
import com.example.android.mymoviedb.Models.Movie;
import com.example.android.mymoviedb.R;
import com.example.android.mymoviedb.Adapters.RecyclerAdapter;
import com.example.android.mymoviedb.RetrofitHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.mymoviedb.IntentConstants.API_KEY;

import static com.example.android.mymoviedb.IntentConstants.CATEGORY_A;
import static com.example.android.mymoviedb.IntentConstants.CATEGORY_B;
import static com.example.android.mymoviedb.IntentConstants.LANGUAGE;
import static com.example.android.mymoviedb.IntentConstants.MOST_RATED_MOVIES_TYPE;
import static com.example.android.mymoviedb.IntentConstants.NOW_SHOWING_MOVIES_TYPE;
import static com.example.android.mymoviedb.IntentConstants.POPULAR_MOVIES_TYPE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
//        searchView.setQueryHint("Search Movies, People, Keywords");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                intent.putExtra(IntentConstants.QUERY, query);
//                startActivity(intent);
//                searchMenuItem.collapseActionView();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.name_of_cast) {
            String menu_id = "cast";
            searchMovie(menu_id);

        } else if (id == R.id.name_of_keyword) {
            String menu_id = "keyword";
            searchMovie(menu_id);

        } else if (id == R.id.name_of_movie) {
            String menu_id = "movie";
            searchMovie(menu_id);

        } else if (id == R.id.fav) {
            Intent i = new Intent(MainActivity.this, FavouritesActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void searchMovie(String menu_id) {
        final String idMenu = menu_id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search");
        builder.setMessage("Enter the movie to search");
        builder.setCancelable(false);
        View v = getLayoutInflater().inflate(R.layout.search_item_view, null);
        builder.setView(v);
        final EditText searchTerm = (EditText) v.findViewById(R.id.search_edit_text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String query = searchTerm.getText().toString();
                if (query.trim().isEmpty()) {
                    return;
                } else {
                    String newquery = query.replace(" ", "+");
                    sendQuery(idMenu, newquery);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendQuery(String id, String newQuery) {

        Intent i = new Intent(MainActivity.this, QueryActivity.class);
        i.putExtra(IntentConstants.MENU_ID, id);
        i.putExtra(IntentConstants.QUERY_TERM, newQuery);
        startActivity(i);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static final String BASE_URL = "https://api.themoviedb.org/";
        public static int PAGE = 1;
        ArrayList<Movie.Results> PopularMoviesList = new ArrayList<>();
        ArrayList<Movie.Results> NowPlayingMoviesList = new ArrayList<>();
        ArrayList<Movie.Results> MostRatedMoviesList = new ArrayList<>();
        RecyclerView mRecyclerView;
        public RecyclerAdapter mRecyclerAdapter;
        RetrofitHelper retrofitHelper;
        ApiInterface apiInterface;

        private int mMovieType;

        Call<Movie> mPopularMoviesCall;
        Call<Movie> mMostRatedMoviesCall;
        Call<Movie> mNowPlayingMoviesCall;

        private boolean pagesOver = false;
        public int presentPage = 1;
        private boolean loading = true;
        private int previousTotal = 0;
        private int visibleThreshold = 5;

        boolean firstTime = true;
        boolean secondTime = true;
        boolean thirdTime = true;

        GridLayoutManager gridLayoutManager;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final int section = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mRecyclerView = rootView.findViewById(R.id.recycler_view);
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            retrofitHelper = new RetrofitHelper(BASE_URL);
            apiInterface = retrofitHelper.getAPI();


            if (section == 1) {
                loadMovies(section);
                mRecyclerAdapter = new RecyclerAdapter(getContext(), PopularMoviesList);
                mRecyclerView.setAdapter(mRecyclerAdapter);
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();
                        int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();


                        if (loading) {
                            if (totalItemCount > previousTotal) {
                                loading = false;
                                previousTotal = totalItemCount;
                            }
                        }
                        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                            loadMovies(section);
                            loading = true;
                        }
                    }
                });
                //loadMovies(section);

                return rootView;
            } else if (section == 2) {
                loadMovies(section);
                mRecyclerAdapter = new RecyclerAdapter(getContext(), MostRatedMoviesList);
                mRecyclerView.setAdapter(mRecyclerAdapter);
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();
                        int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if (totalItemCount > previousTotal) {
                                loading = false;
                                previousTotal = totalItemCount;
                            }
                        }
                        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                            loadMovies(section);
                            loading = true;
                        }
                    }
                });
                return rootView;
            } else if (section == 3) {
                loadMovies(section);
                mRecyclerAdapter = new RecyclerAdapter(getContext(), NowPlayingMoviesList);
                mRecyclerView.setAdapter(mRecyclerAdapter);
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();
                        int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if (totalItemCount > previousTotal) {
                                loading = false;
                                previousTotal = totalItemCount;
                            }
                        }
                        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                            loadMovies(section);
                            loading = true;
                        }
                    }
                });
            }

            return rootView;
        }

        private void loadMovies(int movieType) {

            if (pagesOver) return;

            switch (movieType) {

                case POPULAR_MOVIES_TYPE:
                    mPopularMoviesCall = apiInterface.MoviesList(CATEGORY_A, API_KEY, LANGUAGE, presentPage);
                    mPopularMoviesCall.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call1, Response<Movie> response) {
                            if (!response.isSuccessful()) {
                                mPopularMoviesCall = call1.clone();
                                mPopularMoviesCall.enqueue(this);
                                return;
                            }

                            if (response.body() == null) return;
                            if (response.body().getResults() == null) return;

                            for (Movie.Results movieBrief : response.body().getResults()) {
                                if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                    PopularMoviesList.add(movieBrief);
                            }
                            mRecyclerAdapter.notifyDataSetChanged();
                            if (response.body().getPage() == response.body().getTotalPages())
                                pagesOver = true;
                            else
                                presentPage++;
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }

                    });
                    break;
                case MOST_RATED_MOVIES_TYPE:
                    mMostRatedMoviesCall = apiInterface.MoviesList(CATEGORY_B, API_KEY, LANGUAGE, presentPage);
                    mMostRatedMoviesCall.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (!response.isSuccessful()) {
                                mMostRatedMoviesCall = call.clone();
                                mMostRatedMoviesCall.enqueue(this);
                                return;
                            }

                            if (response.body() == null) return;
                            if (response.body().getResults() == null) return;

                            for (Movie.Results movieBrief : response.body().getResults()) {
                                if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                    MostRatedMoviesList.add(movieBrief);
                            }
                            mRecyclerAdapter.notifyDataSetChanged();
                            if (response.body().getPage() == response.body().getTotalPages())
                                pagesOver = true;
                            else
                                presentPage++;
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case NOW_SHOWING_MOVIES_TYPE:
                    mNowPlayingMoviesCall = apiInterface.MoviesList(IntentConstants.CATEGORY_C, API_KEY, LANGUAGE, PAGE);
                    Log.i("TAG", "Call created NP");
                    mNowPlayingMoviesCall.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (!response.isSuccessful()) {
                                mNowPlayingMoviesCall = call.clone();
                                mNowPlayingMoviesCall.enqueue(this);
                                return;
                            }

                            if (response.body() == null) return;
                            if (response.body().getResults() == null) return;

                            for (Movie.Results movieBrief : response.body().getResults()) {
                                if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null)
                                    NowPlayingMoviesList.add(movieBrief);
                            }
                            mRecyclerAdapter.notifyDataSetChanged();
                            if (response.body().getPage() == response.body().getTotalPages())
                                pagesOver = true;
                            else
                                presentPage++;
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "POPULAR";
                case 1:
                    return "MOST RATED";
                case 2:
                    return "NOW PLAYING";
            }
            return null;
        }


    }
}
