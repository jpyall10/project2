package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.data.MoviesContract;

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
import java.util.Vector;

/**
 * Created by jonathanporter on 9/15/15.
 */
public class FetchMoviesInfoTask extends AsyncTask<String, Void, Void> {

    //private MoviesAdapter mMoviesAdapter;
    ArrayList<Movie> listOfMovies;
    private Context mContext;

    public FetchMoviesInfoTask(Context context){
        mContext=context;
    }


    private final String LOG_TAG = FetchMoviesInfoTask.class.getSimpleName();

//    long addMovie(String id, String title, String poster, String popularity, String description, String vote_average, String release, String backdrop) {
//        // Students: First, check if the location with this city name exists in the db
//        // If it exists, return the current ID
//        // Otherwise, insert it using the content resolver and the base URI
//
//        long movieRowId;
//
//        Cursor movieCursor = mContext.getContentResolver().query(
//                MoviesContract.MoviesEntry.CONTENT_URI,
//                //new String[]{MoviesContract.MoviesEntry._ID},
//                null,
//                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",
//                new String[]{id},
//                null);
//
//        if (movieCursor.moveToFirst()) {
//            int movieRowIdIndex = movieCursor.getColumnIndex(MoviesContract.MoviesEntry._ID);
//            movieRowId = movieCursor.getLong(movieRowIdIndex);
//        } else {
//            // Now that the content provider is set up, inserting rows of data is pretty simple.
//            // First create a ContentValues object to hold the data you want to insert.
//            ContentValues movieValues = new ContentValues();
//
//            // Then add the data, along with the corresponding name of the data type,
//            // so the content provider knows what kind of value is being inserted.
//            movieValues.put(MoviesContract.MoviesEntry._ID, movieRowId);
//            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, id);
//            movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, title);
//            movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, poster);
//            movieValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, popularity);
//            movieValues.put(MoviesContract.MoviesEntry.COLUMN_DESC, description);
//            movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, vote_average);
//            movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, release);
//            movieValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP, backdrop);
//            // Finally, insert movie data into the database.
//            Uri insertedUri = mContext.getContentResolver().insert(
//                    MoviesContract.MoviesEntry.CONTENT_URI,
//                    movieValues
//            );
//
//            // The resulting URI contains the ID for the row.  Extract the movieId from the Uri.
//            movieRowId = ContentUris.parseId(insertedUri);
//        }
//
//        movieCursor.close();
//        return movieRowId;
//    }

    private void getMoviesInfoFromJson(String moviesJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TMDB_RESULTS="results";
        final String TMDB_ID = "id";
        final String TMDB_ORIGINAL_TITLE="original_title";
        final String TMDB_POSTER_PATH="poster_path";
        final String TMDB_POPULARITY="popularity";
        final String TMDB_DESCRIPTION="overview";
        final String TMDB_VOTE_AVERAGE="vote_average";
        final String TMDB_RELEASE="release_date";
        final String TMDB_BACKDROP="backdrop_path";

        try {

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);
            Log.d(LOG_TAG, "movies Array " + moviesArray.toString());

            //ArrayList<Movie> movies = new ArrayList<Movie>();
            //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            Vector<ContentValues> cVVector = new Vector<ContentValues>(moviesArray.length());

            for (int i = 0; i < moviesArray.length(); i++) {

                //Strings to pull from JSON
                String id, title, poster, description, popularity, vote_average, release, backdrop;


                // Get the JSON object representing the movie
                JSONObject movieInfo = moviesArray.getJSONObject(i);


                //Define the Strings
                id = movieInfo.getString(TMDB_ID);
                title = movieInfo.getString(TMDB_ORIGINAL_TITLE);
                poster = "http://image.tmdb.org/t/p/w185/" + movieInfo.getString(TMDB_POSTER_PATH);
                popularity = movieInfo.getString(TMDB_POPULARITY);
                description = movieInfo.getString(TMDB_DESCRIPTION);
                vote_average = movieInfo.getString(TMDB_VOTE_AVERAGE);

                release = movieInfo.getString(TMDB_RELEASE);
                backdrop = "http://image.tmdb.org/t/p/w780/" + movieInfo.getString(TMDB_BACKDROP);

                //long movieRowId = addMovie(id,title,poster,popularity,description,vote_average,release,backdrop);
                //Log.d(LOG_TAG,"movieRowId = " + movieRowId);

                long movieRowId;

                Cursor movieCursor = mContext.getContentResolver().query(
                        MoviesContract.MoviesEntry.CONTENT_URI,
                        new String[]{MoviesContract.MoviesEntry.COLUMN_MOVIE_ID},
                        MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{id},
                        null);
                //int movieIdIndex = movieCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID);
                //String titleFromDb = movieCursor.getString(movieIdIndex);
                //Log.d(LOG_TAG,"Movie Cursor " + movieCursor.toString());

                if(!movieCursor.moveToFirst())
                {
                    ContentValues movieValues = new ContentValues();

                    //                movieValues.put(MoviesContract.MoviesEntry._ID, movieRowId);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, id);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, title);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, poster);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, popularity);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_DESC, description);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, vote_average);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, release);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP, backdrop);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_FAVORITES, "false");

                    cVVector.add(movieValues);

                    Uri insertedUri = mContext.getContentResolver().insert(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    movieValues);

                    movieRowId = ContentUris.parseId(insertedUri);


                    Log.v(LOG_TAG, "Added Movie (id = " + movieRowId + " title = " + movieValues.get(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE) + ") to ");
                    movieCursor.close();
                }
                else{
                    Log.d(LOG_TAG, "Movie already exists");
                }
            }
            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                // Student: call bulkInsert to add the weatherEntries to the database here
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchMoviesInfoTask Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }


    @Override
    protected Void doInBackground(String... params) {

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
            //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=dffkfgkjgjlkjhl");
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
            getMoviesInfoFromJson(moviesJsonStr);
            Log.v(LOG_TAG, "Movies JSON String: " + moviesJsonStr);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }
}
