package com.example.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private Cursor mDetailCursor;
    private View mRootView;
    private int mPosition;
    private TextView mUriText;
    private Uri mUri;
    private static final int MOVIES_LOADER_ID = 0;
    static final String DETAIL_URI = "URI";

    //member variable views in detail activity
    private ImageView mBackdrop;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mDescription;
    private TextView mId;
    private TextView mUserRating;
    private Button mFavoritesButton;

    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final String[] DETAIL_COLUMNS = {
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
            MoviesContract.MoviesEntry.COLUMN_DESC,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_POPULARITY,
            MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
            MoviesContract.MoviesEntry.COLUMN_BACKDROP,
            MoviesContract.MoviesEntry.COLUMN_FAVORITES
    };

    public static DetailFragment newInstance(int position, Uri uri) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        fragment.mPosition = position;
        fragment.mUri = uri;
        args.putInt("id", position);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
//        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mBackdrop = (ImageView) rootView.findViewById(R.id.detail_movie_backdrop);
        mPoster = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        mTitle = (TextView) rootView.findViewById(R.id.detail_movie_title);
        mReleaseDate = (TextView) rootView.findViewById(R.id.detail_movie_release);
        mDescription = (TextView) rootView.findViewById(R.id.detail_movie_description);
        mId = (TextView) rootView.findViewById(R.id.detail_movie_id);
        mUserRating = (TextView) rootView.findViewById(R.id.detail_movie_rating);
        mFavoritesButton = (Button)rootView.findViewById(R.id.detail_favorites_button);

        Bundle args = this.getArguments();
        getLoaderManager().initLoader(MOVIES_LOADER_ID, args, DetailFragment.this);

        return rootView;
    }

//    void onLocationChanged( String newLocation ) {
//        // replace the uri, since the location has changed
//        Uri uri = mUri;
//        if (null != uri) {
//            Uri updatedUri = MoviesContract.MoviesEntry.buildMoviesUri();
//            mUri = updatedUri;
//            getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
//        }
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;
        if (args != null) {
            selection = MoviesContract.MoviesEntry._ID;
            selectionArgs = new String[]{String.valueOf(mPosition)};
        }
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                mUri,
                DETAIL_COLUMNS,
                selection,
                selectionArgs,
                null
        );
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailCursor = null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mDetailCursor = data;
        mDetailCursor.moveToFirst();
        DatabaseUtils.dumpCursor(data);
        String poster_url = mDetailCursor.getString(MoviesFragment.COL_MOVIE_POSTER_PATH);
        Picasso.with(getActivity()).load(poster_url).into(mPoster);

        String backdrop_url = mDetailCursor.getString(MoviesFragment.COL_MOVIE_BACKDROP);
        Picasso.with(getActivity()).load(backdrop_url).into(mBackdrop);

        String title = mDetailCursor.getString(MoviesFragment.COL_MOVIE_TITLE);
        String release = mDetailCursor.getString(MoviesFragment.COL_MOVIE_RELEASE_DATE);
        final String id = mDetailCursor.getString(MoviesFragment.COL_MOVIE_ID);
        String desc = mDetailCursor.getString(MoviesFragment.COL_MOVIE_DESC);
        String user = mDetailCursor.getString(MoviesFragment.COL_MOVIE_USER_RATING);

        mTitle.setText(title);
        mReleaseDate.setText(release);
        mId.setText(id);
        mDescription.setText(desc);
        mUserRating.setText(user);

        mFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rows;
                String favorites = mDetailCursor.getString(MoviesFragment.COL_MOVIE_FAVORITES);
                ContentValues cv = new ContentValues();
                if(favorites.equals("false")) {

                    cv.put(MoviesContract.MoviesEntry.COLUMN_FAVORITES, "true");
                    rows = getActivity().getContentResolver().update(mUri,cv, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",new String[]{id});
                    Log.d(LOG_TAG, "rows update " + rows);
                    mFavoritesButton.setText("Remove From Favorites");
                }else
                {
                    cv.put(MoviesContract.MoviesEntry.COLUMN_FAVORITES, "false");
                    rows = getActivity().getContentResolver().update(mUri,cv, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",new String[]{id});
                    Log.d(LOG_TAG, "rows update " + rows);
                    mFavoritesButton.setText("Add to Favorites");
                }
            }
        });



    }
}

//    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
//
//    private String id, title, description, poster_url, popularity, rating, releaseDate, backdrop;
//
//    private ShareActionProvider mShareActionProvider;
//    private String mMovie;
//    private Uri mUri;
//
//    private static final int DETAIL_LOADER = 0;
//    static final String DETAIL_URI = "URI";

//
//    private static final String[] DETAIL_COLUMNS = {
//            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry._ID,
//            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
//            MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
//            MoviesContract.MoviesEntry.COLUMN_DESC,
//            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
//            MoviesContract.MoviesEntry.COLUMN_BACKDROP,
//            MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
//            MoviesContract.MoviesEntry.COLUMN_POPULARITY,
//            MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
//
//    };
//
//    //Database Column Header Integers
//    private static final int COL_MOVIE_ROW_ID = 0;
//    private static final int COL_MOVIE_ID = 1;
//    private static final int COL_MOVIE_TITLE = 2;
//    private static final int COL_MOVIE__DESC = 3;
//    private static final int COL_MOVIE_RELEASE_DATE = 4;
//    private static final int COL_MOVIE_BACKDROP = 5;
//    public static final int COL_MOVIE_POSTER_PATH = 6;
//    public static final int COL_MOVIE_POPULARITY = 7;
//    public static final int COL_MOVIE_VOTE_AVERAGE = 8;

//    //member variable views in detail activity
//    private ImageView mBackdrop;
//    private ImageView mPoster;
//    private TextView mTitle;
//    private TextView mReleaseDate;
//    private TextView mDescription;
//    private TextView mId;
//    private TextView mUserRating;
//
//
//
//    public DetailFragment() {
//        setHasOptionsMenu(true);
//    }
//

//
////
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.fragment_detail, menu);
//
//        // Retrieve the share menu item
//        MenuItem menuItem = menu.findItem(R.id.action_share);
//
//        // Get the provider and hold onto it to set/change the share intent.
////            ShareActionProvider mShareActionProvider =
////                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//        // Attach an intent to this ShareActionProvider.  You can update this at any time,
//        // like when the user selects a new piece of data they might like to share.
//        //if (mShareActionProvider != null ) {
//        if (mMovie != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
////            } else {
////                Log.d(LOG_TAG, "Share Action Provider is null?");
//        }
//    }
//
////    private Intent createShareForecastIntent() {
////        Intent shareIntent = new Intent(Intent.ACTION_SEND);
////        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
////        shareIntent.setType("text/plain");
////        shareIntent.putExtra(Intent.EXTRA_TEXT,
////                mMovie + FORECAST_SHARE_HASHTAG);
////        return shareIntent;
////    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
//        super.onActivityCreated(savedInstanceState);
//    }
//
//

//
//


//        Log.v(LOG_TAG, "In onLoadFinished");
//        if (data != null && data.moveToFirst()) {
//
//
//            // Read weather condition ID from cursor
//            int movieId = data.getInt(COL_WEATHER_CONDITION_ID);
//
//            mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(movieId));
//
//            // Read date from cursor and update views for day of week and date
//            long date = data.getLong(COL_WEATHER_DATE);
//            String friendlyDateText = Utility.getDayName(getActivity(), date);
//            String dateText = Utility.getFormattedMonthDay(getActivity(), date);
//            mFriendlyDateView.setText(friendlyDateText);
//            mDateView.setText(dateText);
//
//            // Read description from cursor and update view
//            String description = data.getString(COL_WEATHER_DESC);
//            mDescriptionView.setText(description);
//
//            // Read high temperature from cursor and update view
//            boolean isMetric = Utility.isMetric(getActivity());
//
//            double high = data.getDouble(COL_WEATHER_MAX_TEMP);
//            String highString = Utility.formatTemperature(getActivity(), high, isMetric);
//            mHighTempView.setText(highString);
//
//            // Read low temperature from cursor and update view
//            double low = data.getDouble(COL_WEATHER_MIN_TEMP);
//            String lowString = Utility.formatTemperature(getActivity(), low, isMetric);
//            mLowTempView.setText(lowString);
//
//            // Read humidity from cursor and update view
//            float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
//            mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));
//
//            // Read wind speed and direction from cursor and update view
//            float windSpeedStr = data.getFloat(COL_WEATHER_WIND_SPEED);
//            float windDirStr = data.getFloat(COL_WEATHER_DEGREES);
//            mWindView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));
//
//            // Read pressure from cursor and update view
//            float pressure = data.getFloat(COL_WEATHER_PRESSURE);
//            mPressureView.setText(getActivity().getString(R.string.format_pressure, pressure));
//
//            // We still need this for the share intent
//            mMovie = String.format("%s - %s - %s/%s", dateText, description, high, low);
//
//            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
//            if (mShareActionProvider != null) {
//                mShareActionProvider.setShareIntent(createShareForecastIntent());
//            }
//        }
