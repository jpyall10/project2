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

    //public Sorted sortStatus;

    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";
    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int MOVIES_LOADER_ID = 0;

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
        Cursor c =
                getActivity().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                        new String[]{MoviesContract.MoviesEntry._ID},
                        null,
                        null,
                        null);
        if (c.getCount() == 0){
            populateMovies();
        }
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

//        populateMovies();


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Movie movie = (Movie) mMoviesAdapter.getItem(position);
//                ArrayList<String> movieInfo = new ArrayList<String>();
//                movieInfo.add(movie.getId());
//                movieInfo.add(movie.getTitle());
//                movieInfo.add(movie.getDescription());
//                movieInfo.add(movie.getPoster_url());
//                movieInfo.add(movie.getPopularity());
//                movieInfo.add(movie.getRating());
//                movieInfo.add(movie.getRelease());
//                movieInfo.add(movie.getBackdrop());
                int uriId = position + 1;
                Uri uri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, uriId);
                //Toast.makeText(getActivity(), movieInfo.get(1), Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), DetailActivity.class).putStringArrayListExtra(Intent.EXTRA_TEXT, movieInfo);
                //startActivity(intent);
                DetailFragment detailFragment = DetailFragment.newInstance(uriId, uri);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_movies, detailFragment)
                        .addToBackStack(null).commit();
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
        populateMovies();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG,"onCreateLoader ran");
        //String sortOrder = MoviesContract.MoviesEntry.COLUMN_POPULARITY + " DESC";
        return new CursorLoader(getActivity(),
                MoviesContract.MoviesEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null);
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

