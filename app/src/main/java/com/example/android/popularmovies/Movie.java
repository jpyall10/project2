package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by jonathanporter on 7/14/15.
 */
public class Movie implements Parcelable {

    private final String LOG_TAG = "MOVIE_CLASS";

    private String id, title, description, poster_url, popularity, rating, release, backdrop;

    public Movie(){


    }

    public Movie(String id, String title, String description, String poster_url, String popularity, String rating, String release, String backdrop)
    {
        this.id=id;
        this.title=title;
        this.description=description;
        this.poster_url=poster_url;
        this.popularity=popularity;
        this.rating=rating;
        this.release=release;
        this.backdrop=backdrop;
        this.init();


    }

    private void init(){
        setId(id);
        setTitle(title);
        setDescription(description);
        setPoster_url(poster_url);
        setPopularity(popularity);
        setRating(rating);
        setRelease(release);
        setBackdrop(backdrop);

    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }
    public String getPoster_url(){
        return poster_url;
    }
    public String getPopularity(){
        return popularity;
    }
    public String getDescription(){
        return description;
    }
    public String getRating(){
        return rating;
    }
    public String getRelease(){
        return release;
    }
    public String getBackdrop(){
        return backdrop;
    }
    private void setId(String string){
        Log.d(LOG_TAG, "setID used");
        id = string;
    }

    private void setTitle(String string){
        title = string;
    }
    private void setPoster_url(String string){
        poster_url=string;
    }
    private void setPopularity(String string){
        popularity=string;
    }
    private void setDescription(String string){
        description=string;
    }
    private void setRating(String string){
        rating = string;
    }
    private void setRelease(String string){
        release = string;
    }
    private void setBackdrop(String string){
        backdrop = string;
    }

    public String toString(){
        return id + " " + title + " " + description + " " + popularity + " " + rating + " " + release + " " + backdrop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(poster_url);
        dest.writeString(popularity);
        dest.writeString(rating);
        dest.writeString(release);
        dest.writeString(backdrop);

    }
}
