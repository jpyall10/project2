package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private String id, title, description, poster_url, popularity, rating, releaseDate, backdrop;
    //private Movie movie;
    private ArrayList<String> movieInfo;



    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){

            movieInfo = intent.getStringArrayListExtra(Intent.EXTRA_TEXT);
            id=movieInfo.get(0);
            title=movieInfo.get(1);
            description=movieInfo.get(2);
            poster_url=movieInfo.get(3);
            popularity=movieInfo.get(4);
            rating=movieInfo.get(5);
            releaseDate=movieInfo.get(6);
            backdrop=movieInfo.get(7);




            ((TextView) rootView.findViewById(R.id.detail_movie_id)).setText(id);
            ((TextView) rootView.findViewById(R.id.detail_movie_title)).setText(title);
            ((TextView) rootView.findViewById(R.id.detail_movie_description)).setText(description);
            ImageView i = (ImageView)rootView.findViewById(R.id.detail_movie_backdrop);
            Picasso.with(getActivity()).load(backdrop).into(i);
            ((TextView) rootView.findViewById(R.id.detail_movie_rating)).setText(rating);
            ((TextView) rootView.findViewById(R.id.detail_movie_release)).setText(releaseDate);
        }



        return rootView;
    }

}