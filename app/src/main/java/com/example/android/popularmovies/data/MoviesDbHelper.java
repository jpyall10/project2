package com.example.android.popularmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jonathanporter on 9/3/15.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = MoviesDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 15;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context)
    {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_POPULAR_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POPULARITY + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH +  " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_REVIEWS + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_FAVORITES + " INTEGER " +

                ");";

        db.execSQL(SQL_CREATE_POPULAR_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
