package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jonathanporter on 7/13/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int resource, imageViewResourceId;
    private ArrayList<Movie> movies;
    //private ArrayList<String> urls;

    //private LayoutInflater inflater;

    public ImageAdapter(Context c, int resource, int imageViewResourceId, ArrayList<Movie> movies) {
        super();
        mContext = c;
        //this.movies = new ArrayList<Movie>();
        //this.urls = new ArrayList<String>();
        this.resource = resource;
        this.imageViewResourceId = imageViewResourceId;
        this.movies = movies;
        //this.urls = generateUrls(movies);
    }


    public int getCount() {
        return movies.size();
    }
    public void addMovie(Movie movie)
    {
        movies.add(movie);
    }

    public void clear(){
        movies.clear();
    }
    public Object getItem(int position) {
        return movies.get(position);
    }

    public void sortByRating()
    {
        //ArrayList<Movie>  sortedByRating = new ArrayList<Movie>();
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie p1, Movie p2) {
                return p2.getRating().compareTo(p1.getRating()); // Descending
            }

        });
        Log.d("RESUME_BEHAVIOR", "sortByRating ran");

/*
        double topRating = 0.0;
        Collection.sort

        for (Movie m:movies) {
            Double mRating = Double.parseDouble(m.getRating());
            if (mRating > topRating) {
                topRating = mRating;
                sortedByRating.add(0, m);

            }
            else{
                sortedByRating.add(m);
            }
        }
        movies = sortedByRating;*/
    }



    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        //= (ImageView)findViewById(R.id.grid_item_movie_imageview);

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            //imageView = new ImageView(parent.getContext());
            //imageView.setAdjustViewBounds(true);
            //imageView.setPadding(0, 0, 0, 0);
            convertView = mInflater.inflate(resource,null);
            imageView = (ImageView)convertView.findViewById(R.id.grid_item_movie_imageview);
            imageView.setAdjustViewBounds(true);

        } else {
            imageView = (ImageView) convertView;
        }

        String poster_url = movies.get(position).getPoster_url();
        Picasso.with(mContext).load(poster_url).into(imageView);
        return imageView;

    }
}

