package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ImageAdapter mMoviesAdapter;
    //private ArrayList<Movie> movies;
    //private String sort_by;
    public Sorted sortStatus;
    private enum Sorted {RATING, POPULARITY};

    //private String[] posterUrls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sortStatus == Sorted.RATING){
            String string = "onCreate ran and sortStatus = )" + sortStatus.toString();
            Log.d("RESUME_BEHAVIOR", string);
        }
        else if (sortStatus == Sorted.POPULARITY){
            String string = "onCreate ran and sortStatus = )" + sortStatus.toString();
            Log.d("RESUME_BEHAVIOR", string);
        }else{
            Log.d("RESUME_BEHAVIOR", "sortStatus is null");
            //sortStatus = Sorted.RATING;
            //String string = "onCreate ran and and set sortStatus = )" + sortStatus.toString();
            //Log.d("RESUME_BEHAVIOR", string);
        }


        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(sortStatus != null)
        {
            Log.d("RESUME_BEHAVIOR", "onDestroy ran and sortStatus is not null");
            if (sortStatus == Sorted.RATING)
            {
                sortStatus = Sorted.RATING;
                Log.d("RESUME_BEHAVIOR", "onDestroy ran and sortStatus RATING");
            }
            else
            {
                //sortStatus = Sorted.POPULARITY;
                Log.d("RESUME_BEHAVIOR", "onDestroy ran and sortStatus POPULARITY");
            }
        }else{
            Log.d("RESUME_BEHAVIOR", "onDestroy ran and sortStatus is null set sortStatus to POPULARITY");
            sortStatus = Sorted.POPULARITY;
        }


    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        Log.d("RESUME_BEHAVIOR", "onPause is running and sortStatus = " + sortStatus.toString());
        if (sortStatus == Sorted.RATING)
        {

            mMoviesAdapter.sortByRating();
            mMoviesAdapter.notifyDataSetChanged();
            Log.d("RESUME_BEHAVIOR","Sorted.RATING in onPause() this happened");
        }
        else{
            sortStatus = Sorted.POPULARITY;
            mMoviesAdapter.notifyDataSetChanged();
            Log.d("RESUME_BEHAVIOR", "sortStatus was changed to " + sortStatus.toString());
        }

    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.d("RESUME_BEHAVIOR", "onResume is running and sortStatus is unknown");
        if (sortStatus == Sorted.RATING)
        {

            mMoviesAdapter.sortByRating();
            mMoviesAdapter.notifyDataSetChanged();
            Log.d("RESUME_BEHAVIOR", "Sorted.RATING in onResume() this happened");

        }
        else if (sortStatus == Sorted.POPULARITY){
            //sortStatus = Sorted.POPULARITY;
            mMoviesAdapter.notifyDataSetChanged();
            Log.d("RESUME_BEHAVIOR", "Sorted.POPULARITY in onResume() this happened");
        }else
        {
            Log.d("RESUME_BEHAVIOR", "sortStuts is null in onResume() ... set to POPULARITY");
            //sortStatus=Sorted.POPULARITY;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id) {
            case R.id.action_sort_by_rating_desc:
                //sort_by = this.getResources().getString(R.string.sort_by_ranking_desc);
                //populateMovies();
                mMoviesAdapter.sortByRating();
                mMoviesAdapter.notifyDataSetChanged();
                Log.d("RESUME_BEHAVIOR", "sortStatus (before change): " + sortStatus.toString());
                sortStatus = Sorted.RATING;
                Log.d("RESUME_BEHAVIOR", "sortStatus = " + sortStatus.toString());


                break;
            case R.id.action_sort_by_popularity_desc:
                //sort_by = this.getResources().getString(R.string.sort_by_popularity_desc);
                populateMovies();
                Log.d("RESUME_BEHAVIOR", "sortStatus (before change): " + sortStatus.toString());
                sortStatus=Sorted.POPULARITY;
                Log.d("RESUME_BEHAVIOR", "sortStatus = " + sortStatus.toString());
                break;
            case android.R.id.home:
                if (sortStatus == Sorted.RATING)
                {
                    mMoviesAdapter.sortByRating();
                    mMoviesAdapter.notifyDataSetChanged();
                    Log.d("RESUME_BEHAVIOR","pause this happened");
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mMoviesAdapter = new ImageAdapter(this.getActivity(), R.layout.grid_item_movie, R.id.grid_item_movie_imageview, new ArrayList<Movie>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movies);

        gridView.setAdapter(mMoviesAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = (Movie) mMoviesAdapter.getItem(position);
                ArrayList<String> movieInfo = new ArrayList<String>();
                movieInfo.add(movie.getId());
                movieInfo.add(movie.getTitle());
                movieInfo.add(movie.getDescription());
                movieInfo.add(movie.getPoster_url());
                movieInfo.add(movie.getPopularity());
                movieInfo.add(movie.getRating());
                movieInfo.add(movie.getRelease());
                movieInfo.add(movie.getBackdrop());

                Toast.makeText(getActivity(),  movieInfo.get(1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class).putStringArrayListExtra(Intent.EXTRA_TEXT, movieInfo);
                startActivity(intent);
            }
        });

        return rootView;


    }


//update the Image adapter according to the movies found based on the sort_by string passed in
    private void populateMovies(){
        FetchMoviesInfoTask fmit = new FetchMoviesInfoTask();
        fmit.execute();


    }

    /*public Movie getMovie(String id)
    {
        for (Movie m : movies)
        {
            if (id.equals(m.getId()))
            {
                return m;
            }
        }
        return null;
    }*/

    @Override
    public void onStart()
    {
        super.onStart();
        populateMovies();

    }

    public class FetchMoviesInfoTask extends AsyncTask<String, Void, ArrayList<Movie>> {



        private final String LOG_TAG = FetchMoviesInfoTask.class.getSimpleName();

        private ArrayList<Movie> getMoviesInfoFromJson(String moviesJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS="results";
            final String TMDB_ID = "id";
            final String TMDB_ORIGINAL_TITLE="original_title";
            final String TMDB_POSTER_PATH="poster_path";
            final String TMDB_POPULARITY="popularity";
            final String TMDB_DESCRIPTION="overview";
            final String TMDB_RATING="vote_average";
            final String TMDB_RELEASE="release_date";
            final String TMDB_BACKDROP="backdrop_path";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);
            Log.d(LOG_TAG, "movies Array " + moviesArray.toString());

            //String[] posterUrls = new String[moviesArray.length()];
            ArrayList<Movie> movies = new ArrayList<Movie>();
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            //String unitType = sharedPrefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
            for(int i = 0; i < moviesArray.length(); i++) {

                // For now, using the format "Day, description, hi/low"
                String id,title, poster, description,popularity, rating, release, backdrop;


                // Get the JSON object representing the movie
                JSONObject movieInfo = moviesArray.getJSONObject(i);


                id = movieInfo.getString(TMDB_ID);
                title = movieInfo.getString(TMDB_ORIGINAL_TITLE);
                poster = "http://image.tmdb.org/t/p/w185/" + movieInfo.getString(TMDB_POSTER_PATH);
                popularity = movieInfo.getString(TMDB_POPULARITY);
                description = movieInfo.getString(TMDB_DESCRIPTION);
                rating = movieInfo.getString(TMDB_RATING);
                release = movieInfo.getString(TMDB_RELEASE);
                backdrop = "http://image.tmdb.org/t/p/w780/" + movieInfo.getString(TMDB_BACKDROP);

                //Create move from JSON data
                Movie movie = new Movie(id, title, description, poster, popularity, rating, release, backdrop);
                //posterUrls[i] =  poster;
                Log.v(LOG_TAG, "Created Movie (id = " + id + " title = " + title + ")");

                //add movie to movies
                movies.add(movie);
                Log.v(LOG_TAG, "Added Movie (id = " + movie.getId() + " title = " + movie.getTitle() + ") to ");
            }

            for (Movie m : movies) {
                Log.v(LOG_TAG, "Movies entry url: " + m.toString());
            }
            return movies;

        }

        protected ArrayList<Movie> doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String apiKey = "42b1e5baac9dc17b1df2bc072e1c01ca";
            String sortBy = "popularity.desc";

            try {
                // Construct the URL for the query
                //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=42b1e5baac9dc17b1df2bc072e1c01ca");
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, sortBy)
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Movies JSON String: " + moviesJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try
            {
                return getMoviesInfoFromJson(moviesJsonStr);
            }catch (JSONException e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            if (movies.size() > 0)
            {
                Log.d(LOG_TAG, "Movies Array is bigger than 0");
                mMoviesAdapter.clear();


                for (Movie movie : movies)
                {

                    mMoviesAdapter.addMovie(movie);
                    mMoviesAdapter.notifyDataSetChanged();
                }


                if (sortStatus == Sorted.RATING)
                {
                    mMoviesAdapter.sortByRating();
                    mMoviesAdapter.notifyDataSetChanged();
                }else
                {
                    sortStatus = Sorted.POPULARITY;
                    mMoviesAdapter.notifyDataSetChanged();
                }
                String string = "onPostExecute ran and sortStatus = " + sortStatus.toString();
                Log.d("RESUME_BEHAVIOR", string);


            }
        }
    }

}
