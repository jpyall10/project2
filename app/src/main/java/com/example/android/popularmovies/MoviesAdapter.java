package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by jonathanporter on 7/13/15.
 */
public class MoviesAdapter extends CursorAdapter {
    private int taskCount = 0;
    private Context mContext;
    private static int sLoaderID;
    private final String LOG_TAG = this.getClass().getSimpleName();
    /**
     * Cache of the children views for a movies grid view item.
     */
    public static class ViewHolder {
        public final ImageView iconView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.grid_item_movie_imageview);
        }
    }

    public MoviesAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "MoviesAdapter constructor ran");
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.grid_item_movie;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        Log.d(LOG_TAG, "newView ran");
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder)view.getTag();
        int posterIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
        String poster_url = cursor.getString(posterIndex);
        Log.d(LOG_TAG, "poster_url = " + poster_url);
        Picasso.with(context).load(poster_url).into(viewHolder.iconView);
        Log.d(LOG_TAG, "bindView ran");

    }
}

