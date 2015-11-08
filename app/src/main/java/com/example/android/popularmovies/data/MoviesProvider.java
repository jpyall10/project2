package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by jonathanporter on 9/15/15.
 */
public class MoviesProvider extends ContentProvider {

    private static final String LOG_TAG = MoviesProvider.class.getSimpleName();
    private MoviesDbHelper mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static final int MOVIES = 100;
    static final int MOVIE_WITH_ID = 101;

    private static final SQLiteQueryBuilder sMovieByIdQueryBuilder;

    static{
        sMovieByIdQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
//        sMovieByIdQueryBuilder.setTables(
//                MoviesContract.MoviesEntry.TABLE_NAME + " INNER JOIN " +
//                        MoviesContract.VideosEntry.TABLE_NAME +
//                        " ON " + MoviesContract.MoviesEntry.TABLE_NAME +
//                        "." + MoviesContract.MoviesEntry.COLUMN_VID_KEY +
//                        " = " + MoviesContract.VideosEntry.TABLE_NAME +
//                        "." + MoviesContract.VideosEntry._ID);
    }

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.


        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.


        // 3) Return the new matcher!
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        //matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/*", MOVIES_WITH_ID);

        //matcher.addURI(authority, matcher.PATH_LOCATION, LOCATION);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            case MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return retCursor;
                //break;
            }
            case MOVIE_WITH_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        MoviesContract.MoviesEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return retCursor;
                //break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

//    private Cursor getMoviesInfoById(Uri uri, String[] projection, String sortOrder) {
//        String locationSetting = MoviesContract.MoviesEntry.getLocationSettingFromUri(uri);
//        long startDate = MoviesContract.MoviesEntry.getStartDateFromUri(uri);
//
//        String[] selectionArgs;
//        String selection;
//
//        if (startDate == 0) {
//            selection = sLocationSettingSelection;
//            selectionArgs = new String[]{locationSetting};
//        } else {
//            selectionArgs = new String[]{locationSetting, Long.toString(startDate)};
//            selection = sLocationSettingWithStartDateSelection;
//        }
//
//        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder
//        );
//    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            //Student: Uncomment and fill out these two cases
            case MOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.MoviesEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        //db.close();
        Log.d(LOG_TAG, "This ran");
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection)
        {
            selection = "1";
        }
        switch(match)
        {
            case MOVIES:
                rowsDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        if (null == selection)
        {
            selection = "1";
        }
        switch(match)
        {
            case MOVIES:
                rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE_WITH_ID:
                Log.d(LOG_TAG, "movie_with_id ran in update");
                rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values,
                        //selection, selectionArgs);
                        MoviesContract.MoviesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
