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

/**
 * Created by jonathanporter on 9/15/15.
 */
public class FetchMoviesExtraInfoTask extends AsyncTask<String, Void, Void> {

    //private MoviesAdapter mMoviesAdapter;
    //ArrayList<Movie> listOfMovies;
    private Context mContext;

    public FetchMoviesExtraInfoTask(Context context){
        mContext=context;
    }


    private final String LOG_TAG = FetchMoviesExtraInfoTask.class.getSimpleName();

    private void getVideosAndReviewsFromJson(String videosAndReviewsJsonString)
            throws JSONException
    {
        final String TMDB_RESULTS="results";
        final String TMDB_ID = "id";
        final String TMDB_VIDEOS="videos";
        final String TMDB_REVIEWS="reviews";
        final String TMDB_KEY="key";
        final String TMDB_NAME="name";
        final String TMDB_SITE="site";
        final String TMDB_SIZE="size";
        final String TMDB_TYPE="type";


        try {

            JSONObject videosAndReviewsJson = new JSONObject(videosAndReviewsJsonString);


            JSONObject videosJson = videosAndReviewsJson.getJSONObject(TMDB_VIDEOS);
            JSONArray videosArray = videosJson.getJSONArray(TMDB_RESULTS);

//            JSONObject reviewsJson = videosAndReviewsJson.getJSONObject(TMDB_REVIEWS);
//            JSONArray reviewsArray = reviewsJson.getJSONArray(TMDB_RESULTS);

            //Vector<ContentValues> cVVector = new Vector<ContentValues>(videosArray.length());

            Log.d(LOG_TAG, "videos Array " + videosArray.toString());

//            String[] reviews;
            String[] trailerKey = new String[videosArray.length()];


//            for (int i = 0; i < videosArray.length(); i++) {
            int i = 0;
                while(i < 5) {
                    //Strings to pull from JSON
                    String videoId, key, name, site, size, type, movieId;
                    //String[] video_urls = new String[videosArray.length()];

                    // Get the JSON object representing the movie
                    JSONObject videoInfo = videosArray.getJSONObject(i);


                    //Define the Strings
                    movieId = videosAndReviewsJson.getString(TMDB_ID);
                    videoId = videoInfo.getString(TMDB_ID);
                    key = videoInfo.getString(TMDB_KEY);
                    name = videoInfo.getString(TMDB_NAME);
                    site = videoInfo.getString(TMDB_SITE);
                    size = videoInfo.getString(TMDB_SIZE);
                    type = videoInfo.getString(TMDB_TYPE);

                    //"http://www.youtube.com/watch?v="+

                    long movieRowId;

                    Cursor movieCursor = mContext.getContentResolver().query(
                            MoviesContract.MoviesEntry.CONTENT_URI,
                            new String[]{MoviesContract.MoviesEntry._ID},
                            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{movieId},
                            null);

                    trailerKey[i] = key;
                    ContentValues videoValues = new ContentValues();

                    if (trailerKey[i] == null) {
                        Log.d(LOG_TAG, "There is no key for the " + i + "th video element");
                    } else {

                        switch (i) {
                            case 0:
                                videoValues.put(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH1, trailerKey[i]);
                                Log.d(LOG_TAG, "i = " + i);
                                break;
                            case 1:
                                videoValues.put(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH2, trailerKey[i]);
                                Log.d(LOG_TAG, "i = " + i);
                                break;
                            case 2:
                                videoValues.put(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH3, trailerKey[i]);
                                Log.d(LOG_TAG, "i = " + i);
                                break;
                            case 3:
                                videoValues.put(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH4, trailerKey[i]);
                                Log.d(LOG_TAG, "i = " + i);
                                break;
                            default:
                                Log.d(LOG_TAG, "i = " + i);
                                Log.d(LOG_TAG, "Only storing 3 videos");
                                break;

                        }
                        movieCursor.moveToFirst();
                        int index = movieCursor.getColumnIndex(MoviesContract.MoviesEntry._ID);
                        Log.d(LOG_TAG, "index = " + index + " and string = ");
                        movieRowId = movieCursor.getLong(index);

                        try {
                            Uri uri = MoviesContract.MoviesEntry.buildMoviesUri(movieRowId);
                            int updatedRows = mContext.getContentResolver().update(uri,
                                    videoValues,
                                    null,
                                    null);
                            //                        MoviesContract.MoviesEntry._ID + " = ?",
                            //                          new String[]{String.valueOf(ContentUris.parseId(uri))});
                            Log.d(LOG_TAG, "selection args = " + String.valueOf(ContentUris.parseId(uri)));
                            Log.v(LOG_TAG, "Updated Movie (id = " + movieRowId + " with trailerURL = " + videoValues.get(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH1) + " updated rows: " + updatedRows);

                        }
                        catch (Exception e)
                        {
                            Log.d(LOG_TAG, "There was an exception " + e);
                        }


                        movieCursor.close();
                    }
                    i++;
                }
//            }
//                }
//                else{
//                    //int rowsUpdated = mContext.getContentResolver().update(MoviesContract.MoviesEntry.CONTENT_URI, videoValues,null, null);
//                    Log.d(LOG_TAG, "Movie doesn't exist");
//
//                }
                //}
//            int updated = 0;
//            // add to database
//            if ( cVVector.size() > 0 ) {
//                // Student: call bulkInsert to add the weatherEntries to the database here
//                ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                cVVector.toArray(cvArray);
//                updated = mContext.getContentResolver().update(MoviesContract.MoviesEntry.CONTENT_URI, cvArray,null,null );

//            Log.d(LOG_TAG, "Videos Part Complete. " + updated + " Updated");


        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
//
//
//
    }

    @Override
    protected Void doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String videosAndReviewsJsonStr = null;
        String movieId = params[0];

        String apiKey = "42b1e5baac9dc17b1df2bc072e1c01ca";
        String append = "videos, reviews";
        //Cursor cursor = mContext.getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, new String[]{MoviesContract.MoviesEntry.COLUMN_MOVIE_ID},null,null,null);

        try {
            // Construct the URL for the query
            //URL url = new URL("http://api.themoviedb.org/3/movie/{id}&api_key="dffkfgkjgjlkjhl"&append_to_result="videos, reviews");
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APPEND_PARAM = "append_to_response";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .appendQueryParameter(APPEND_PARAM, append)
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
            videosAndReviewsJsonStr = buffer.toString();

            Log.v(LOG_TAG, "videos And Reviews JSON String: " + videosAndReviewsJsonStr);
            getVideosAndReviewsFromJson(videosAndReviewsJsonStr);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        }
          catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        finally {
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
