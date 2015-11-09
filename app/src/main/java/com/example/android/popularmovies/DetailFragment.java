package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
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
    private int mPosition;
    private Uri mUri;
    private static final int DETAILS_LOADER_ID = 0;
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
    private ImageView mTrailerView1, mTrailerView2, mTrailerView3, mTrailerView4;
    private TextView mReviewAuthor1, mReviewAuthor2, mReviewAuthor3, mReviewAuthor4;
    private TextView mReviewContent1, mReviewContent2, mReviewContent3, mReviewContent4;
    private TextView mReleaseDateLabel, mRatingLabel, mTrailersLabel, mReviewsLabel, mIdLabel;


    private static final String YOUTUBE_IMAGE_URL_PREFIX = "http://img.youtube.com/vi/";
    private static final String YOUTUBE_IMAGE_URL_SUFFIX = "/0.jpg";

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
            MoviesContract.MoviesEntry.COLUMN_FAVORITES,
            MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH1,
            MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH2,
            MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH3,
            MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH4,
            MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR1,
            MoviesContract.MoviesEntry.COLUMN_REVIEW1,
            MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR2,
            MoviesContract.MoviesEntry.COLUMN_REVIEW2,
            MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR3,
            MoviesContract.MoviesEntry.COLUMN_REVIEW3,
            MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR4,
            MoviesContract.MoviesEntry.COLUMN_REVIEW4
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
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAILS_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mBackdrop = (ImageView) rootView.findViewById(R.id.detail_movie_backdrop);
        mPoster = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        mTitle = (TextView) rootView.findViewById(R.id.detail_movie_title);
        mReleaseDate = (TextView) rootView.findViewById(R.id.detail_movie_release);
        mDescription = (TextView) rootView.findViewById(R.id.detail_movie_description);
        mId = (TextView) rootView.findViewById(R.id.detail_movie_id);
        mUserRating = (TextView) rootView.findViewById(R.id.detail_movie_rating);
        mFavoritesButton = (Button)rootView.findViewById(R.id.detail_favorites_button);
        mTrailerView1 = (ImageView) rootView.findViewById(R.id.detail_movie_trailerURL1);
        mTrailerView2 = (ImageView) rootView.findViewById(R.id.detail_movie_trailerURL2);
        mTrailerView3 = (ImageView) rootView.findViewById(R.id.detail_movie_trailerURL3);
        mTrailerView4 = (ImageView) rootView.findViewById(R.id.detail_movie_trailerURL4);

        mReviewAuthor1 = (TextView) rootView.findViewById(R.id.detail_movie_review_author1);
        mReviewContent1 = (TextView) rootView.findViewById(R.id.detail_movie_review_content1);

        mReviewAuthor2 = (TextView) rootView.findViewById(R.id.detail_movie_review_author2);
        mReviewContent2 = (TextView) rootView.findViewById(R.id.detail_movie_review_content2);

        mReviewAuthor3 = (TextView) rootView.findViewById(R.id.detail_movie_review_author3);
        mReviewContent3 = (TextView) rootView.findViewById(R.id.detail_movie_review_content3);

        mReviewAuthor4 = (TextView) rootView.findViewById(R.id.detail_movie_review_author4);
        mReviewContent4 = (TextView) rootView.findViewById(R.id.detail_movie_review_content4);

        mReleaseDateLabel = (TextView) rootView.findViewById(R.id.detail_movie_release_label);
        mRatingLabel = (TextView) rootView.findViewById(R.id.detail_movie_rating_label);
        mTrailersLabel = (TextView) rootView.findViewById(R.id.detail_movie_trailers_label);
        mReviewsLabel = (TextView) rootView.findViewById(R.id.detail_movie_reviews_label);
        mIdLabel = (TextView) rootView.findViewById(R.id.detail_movie_id_label);

        Bundle args = this.getArguments();
        getLoaderManager().initLoader(DETAILS_LOADER_ID, args, DetailFragment.this);

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

    void onStatusChanged( ) {
        getLoaderManager().restartLoader(DETAILS_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader ran in DetailFragment");
//        String selection = null;
//        String[] selectionArgs = null;
//        if (args != null) {
//            selection = MoviesContract.MoviesEntry._ID;
//            selectionArgs = new String[]{String.valueOf(mPosition)};
//        }
//
//        Intent intent = getActivity().getIntent();
//        if(intent == null || intent.getData()==null){
//            return null;
//        }
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        if(mUri !=null)
        {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailCursor = null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.d(LOG_TAG, "onLoadFinished Ran");
        try {

            String trailerKey1, trailerKey2, trailerKey3, trailerKey4, thumbUrl1, thumbUrl2, thumbUrl3, thumbUrl4;
            int COL_MOVIE_TRAILER_URL1, COL_MOVIE_TRAILER_URL2, COL_MOVIE_TRAILER_URL3, COL_MOVIE_TRAILER_URL4;
            int COL_MOVIE_REVIEW_AUTHOR1, COL_MOVIE_REVIEW_AUTHOR2, COL_MOVIE_REVIEW_AUTHOR3, COL_MOVIE_REVIEW_AUTHOR4;
            String reviewAuthor1, reviewAuthor2, reviewAuthor3, reviewAuthor4;
            int COL_MOVIE_REVIEW_CONTENT1, COL_MOVIE_REVIEW_CONTENT2, COL_MOVIE_REVIEW_CONTENT3, COL_MOVIE_REVIEW_CONTENT4;
            String reviewContent1, reviewContent2, reviewContent3, reviewContent4;

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

            //Log.d(LOG_TAG, "trailerKey = " + thumbUrl);

            //Set Labels and button visible
            mFavoritesButton.setVisibility(View.VISIBLE);
            mReleaseDateLabel.setVisibility(View.VISIBLE);
            mRatingLabel.setVisibility(View.VISIBLE);
            mTrailersLabel.setVisibility(View.VISIBLE);
            mReviewsLabel.setVisibility(View.VISIBLE);
            mIdLabel.setVisibility(View.VISIBLE);

            mTitle.setText(title);
            mReleaseDate.setText(release);
            mId.setText(id);
            mDescription.setText(desc);
            mUserRating.setText(user);

            COL_MOVIE_TRAILER_URL1 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH1);
            trailerKey1 = mDetailCursor.getString(COL_MOVIE_TRAILER_URL1);
            final String trailerUrl1 = "http://www.youtube.com/watch?v=" + trailerKey1;
            thumbUrl1 = YOUTUBE_IMAGE_URL_PREFIX + trailerKey1 + YOUTUBE_IMAGE_URL_SUFFIX;
            Picasso.with(getActivity()).load(thumbUrl1).into(mTrailerView1);

            mTrailerView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl1)));

                }
            });

            COL_MOVIE_REVIEW_AUTHOR1 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR1);
            reviewAuthor1 = mDetailCursor.getString(COL_MOVIE_REVIEW_AUTHOR1);
            mReviewAuthor1.setText(reviewAuthor1);
            COL_MOVIE_REVIEW_CONTENT1 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW1);
            reviewContent1 = mDetailCursor.getString(COL_MOVIE_REVIEW_CONTENT1);
            mReviewContent1.setText(reviewContent1);

            try {

                COL_MOVIE_TRAILER_URL2 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH2);
                trailerKey2 = mDetailCursor.getString(COL_MOVIE_TRAILER_URL2);
                final String trailerUrl2 = "http://www.youtube.com/watch?v=" + trailerKey2;
                thumbUrl2 = YOUTUBE_IMAGE_URL_PREFIX + trailerKey2 + YOUTUBE_IMAGE_URL_SUFFIX;
                Picasso.with(getActivity()).load(thumbUrl2).into(mTrailerView2);

                COL_MOVIE_TRAILER_URL3 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH3);
                trailerKey3 = mDetailCursor.getString(COL_MOVIE_TRAILER_URL3);
                final String trailerUrl3 = "http://www.youtube.com/watch?v=" + trailerKey3;
                thumbUrl3 = YOUTUBE_IMAGE_URL_PREFIX + trailerKey3 + YOUTUBE_IMAGE_URL_SUFFIX;
                Picasso.with(getActivity()).load(thumbUrl3).into(mTrailerView3);

                COL_MOVIE_TRAILER_URL4 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TRAILER_PATH4);
                trailerKey4 = mDetailCursor.getString(COL_MOVIE_TRAILER_URL4);
                final String trailerUrl4 = "http://www.youtube.com/watch?v=" + trailerKey4;
                thumbUrl4 = YOUTUBE_IMAGE_URL_PREFIX + trailerKey4 + YOUTUBE_IMAGE_URL_SUFFIX;
                Picasso.with(getActivity()).load(thumbUrl4).into(mTrailerView4);

                mTrailerView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl2)));

                    }
                });

                mTrailerView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl3)));

                    }
                });
                mTrailerView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl4)));

                    }
                });

                COL_MOVIE_REVIEW_AUTHOR2 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR2);
                reviewAuthor2 = mDetailCursor.getString(COL_MOVIE_REVIEW_AUTHOR2);
                mReviewAuthor2.setText(reviewAuthor2);
                COL_MOVIE_REVIEW_CONTENT2 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW2);
                reviewContent2 = mDetailCursor.getString(COL_MOVIE_REVIEW_CONTENT2);
                mReviewContent2.setText(reviewContent2);

                COL_MOVIE_REVIEW_AUTHOR3 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR3);
                reviewAuthor3 = mDetailCursor.getString(COL_MOVIE_REVIEW_AUTHOR3);
                mReviewAuthor3.setText(reviewAuthor3);
                COL_MOVIE_REVIEW_CONTENT3 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW3);
                reviewContent3 = mDetailCursor.getString(COL_MOVIE_REVIEW_CONTENT3);
                mReviewContent3.setText(reviewContent3);

                COL_MOVIE_REVIEW_AUTHOR4 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR4);
                reviewAuthor4 = mDetailCursor.getString(COL_MOVIE_REVIEW_AUTHOR4);
                mReviewAuthor4.setText(reviewAuthor4);
                COL_MOVIE_REVIEW_CONTENT4 = mDetailCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW4);
                reviewContent4 = mDetailCursor.getString(COL_MOVIE_REVIEW_CONTENT4);
                mReviewContent4.setText(reviewContent4);

            }catch (Exception e)
            {
                Log.d(LOG_TAG, "Exception e = " + e);
            }

//        Uri tUri = Uri.parse(trailerUrl);
//        mTrailerView.setVideoURI(tUri);
//        mTrailerView.setVisibility(View.VISIBLE);
            final String favorites = mDetailCursor.getString(MoviesFragment.COL_MOVIE_FAVORITES);
            if (favorites == null || favorites.equals("false")) {
                mFavoritesButton.setText("Add to Favorites");
            } else {
                mFavoritesButton.setText("Remove From Favorites");
            }
            mFavoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rows;

                    ContentValues cv = new ContentValues();
                    if (favorites == null || favorites.equals("false")) {
                        cv.put(MoviesContract.MoviesEntry.COLUMN_FAVORITES, "true");
                        mFavoritesButton.setText("Remove From Favorites");
                    } else {
                        cv.put(MoviesContract.MoviesEntry.COLUMN_FAVORITES, "false");
                        mFavoritesButton.setText("Add to Favorites");
                    }
                    rows = getActivity().getContentResolver().update(MoviesContract.MoviesEntry.CONTENT_URI, cv, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?", new String[]{id});
                    Log.d(LOG_TAG, "rows update " + rows);
//                onStatusChanged();
                }
            });

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}