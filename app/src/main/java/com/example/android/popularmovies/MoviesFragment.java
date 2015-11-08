package com.example.android.popularmovies;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.data.MoviesContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {



    private MoviesAdapter mMoviesAdapter;

    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;
    private String mSelection = null;
    private String[] mSelectionArgs = null;
    private boolean favBool = false;

    private static final String SELECTED_KEY = "selected_position";
    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int MOVIES_LOADER_ID = 0;

    private String mSortOrder = MoviesContract.MoviesEntry.COLUMN_POPULARITY + " DESC";

    private static final String[] MOVIE_COLUMNS = {
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

    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_DESC = 3;
    static final int COL_MOVIE_RELEASE_DATE = 4;
    static final int COL_MOVIE_USER_RATING = 5;
    static final int COL_MOVIE_POPULARITY = 6;
    static final int COL_MOVIE_POSTER_PATH = 7;
    static final int COL_MOVIE_BACKDROP = 8;
    static final int COL_MOVIE_FAVORITES = 9;

    public MoviesFragment(){

    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri movieUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        populateMovies();
        getLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Sort order:  Ascending, by date.
        String sortOrder = MoviesContract.MoviesEntry.COLUMN_POPULARITY + " DESC";


        Cursor cur = getActivity().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                null, null, null, sortOrder);

        int curColCount = cur.getColumnCount();
        Log.d(LOG_TAG, "curColCount = " + curColCount);

        mMoviesAdapter = new MoviesAdapter(getActivity(), cur, 0, MOVIES_LOADER_ID);

        final View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.grid_view_movies);

        mGridView.setAdapter(mMoviesAdapter);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor !=null)
                {
                    int id = cursor.getInt(MoviesFragment.COL_ID);
                    Uri uri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);

                    Log.d(LOG_TAG, "uri after click is " + uri);
                    ((Callback) getActivity())
                            .onItemSelected(uri);
                    mPosition = position;
                }
            }
        });

//        if (savedInstanceState != null) {
//            listOfMovies = (ArrayList<Movie>) savedInstanceState.get(MOVIE_KEY);
//            mMoviesAdapter = new MoviesAdapter(this.getActivity(), R.layout.grid_item_movie, R.id.grid_item_movie_imageview, listOfMovies);
//            Log.d("RESUME_BEHAVIOR", "there is a saved state");
//        } else {
//            populateMovies();
//            Log.d("RESUME_BEHAVIOR", "populateMovies ran");
//            mMoviesAdapter = new MoviesAdapter(this.getActivity(), R.layout.grid_item_movie, R.id.grid_item_movie_imageview, new ArrayList<Movie>());
//
//
//        }
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }


        return rootView;


    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String s;
        switch (id)
        {
            case R.id.action_sort_by_popularity_desc:
                s = MoviesContract.MoviesEntry.COLUMN_POPULARITY + " DESC";
                setSortOrder(s);
                onStatusChanged();
                break;
            case R.id.action_sort_by_rating_desc:
                s = MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " DESC";
                setSortOrder(s);
                onStatusChanged();
                break;
            case R.id.action_show_favorites:
                //favBool = !favBool;
                if(favBool)
                {
                    item.setTitle("Show Favorites");
                    favBool = false;
                }
                else
                {
                    item.setTitle("Show All");
                    favBool = true;
                }
                setFavorites();

                onStatusChanged();
                break;
            default:
                break;

        }

//        switch (id) {
//            case R.id.action_sort_by_rating_desc:
//                mMoviesAdapter.sortByRating();
//                mMoviesAdapter.notifyDataSetChanged();
//                //sortStatus = Sorted.RATING;
//                //Log.d("RESUME_BEHAVIOR", "sortStatus = " + sortStatus.toString());
//
//
//                break;
//            case R.id.action_sort_by_popularity_desc:
//                mMoviesAdapter.sortByPopularity();
//                mMoviesAdapter.notifyDataSetChanged();
//                //sortStatus = Sorted.POPULARITY;
//                //Log.d("RESUME_BEHAVIOR", "sortStatus = " + sortStatus.toString());
//                break;
//            default:
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    //update the Image adapter according to the movies found from AsyncTask
    private void populateMovies() {

        FetchMoviesInfoTask fmit = new FetchMoviesInfoTask(this.getActivity());
        fmit.execute();


    }

    void onStatusChanged( ) {
        getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(MOVIE_KEY, listOfMovies);
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void setSortOrder(String sortOrder){
        mSortOrder = sortOrder;
    }

    public void setFavorites()
    {
        if(favBool){
            mSelection = MoviesContract.MoviesEntry.COLUMN_FAVORITES + " = ?";
            mSelectionArgs = new String[] {"true"};
        }
        else
        {
//            mSelection = MoviesContract.MoviesEntry.COLUMN_FAVORITES + " = ?";
//            mSelectionArgs = new String[]{"false"};
            mSelection = null;
            mSelectionArgs = null;
        }
        onStatusChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG,"onCreateLoader ran");
        return new CursorLoader(getActivity(),
                MoviesContract.MoviesEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                mSelection,
                mSelectionArgs,
                mSortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);

    }
}

