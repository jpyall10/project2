package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIES = "movies";
    //public static final String PATH_ID = "id";

    public static final class MoviesEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_FAVORITES = "favorites";

        public static final String COLUMN_DESC = "overview";
        public static final String COLUMN_POSTER_PATH = "posterURL";  //poster_path
        public static final String COLUMN_POPULARITY = "popularity";    //popularity

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";  //vote_average
        public static final String COLUMN_BACKDROP = "backdrop";        //backdrop_path

        public static final String COLUMN_TRAILER_PATH1 = "trailerURL1";  //trailer path
        public static final String COLUMN_TRAILER_PATH2 = "trailerURL2";  //trailer path
        public static final String COLUMN_TRAILER_PATH3 = "trailerURL3";  //trailer path
        public static final String COLUMN_TRAILER_PATH4 = "trailerURL4";  //trailer path

        public static final String COLUMN_REVIEW1 = "review1";  //review path
        public static final String COLUMN_REVIEW_AUTHOR1 = "review_author1";  //review path

        public static final String COLUMN_REVIEW2 = "review2";  //review path
        public static final String COLUMN_REVIEW_AUTHOR2 = "review_author2";  //review path

        public static final String COLUMN_REVIEW3 = "review3";  //review path
        public static final String COLUMN_REVIEW_AUTHOR3 = "review_author3";  //review path

        public static final String COLUMN_REVIEW4 = "review4";  //review path
        public static final String COLUMN_REVIEW_AUTHOR4 = "review_author4";  //review path





        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;


        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}