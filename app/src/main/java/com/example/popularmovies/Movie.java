package com.example.popularmovies;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;


public class Movie {

    final static String THEMOVIEDB_IMG_BASE_URL = "image.tmdb.org";
    final static String[] URL_PATH = {"t","p"};
    final static String PARAM_SCHEME = "https";
    final static String IMAGE_SIZE = "w185";
    private String mTitle;
    private URL mImgUrl;
    private String mSynopsis;
    private int mRating;
    private Date mReleaseDate;
    private long mId;



    public Movie(Long id, String title, String posterPath, String synopsis, int rating, Date releaseDate) throws MalformedURLException {
        mId = id;
        mTitle = title;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(PARAM_SCHEME).authority(THEMOVIEDB_IMG_BASE_URL).appendPath(URL_PATH[0]).appendPath(URL_PATH[1]).appendPath(IMAGE_SIZE);
        mImgUrl = new URL(builder.build() + posterPath);
        mSynopsis = synopsis;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    public String getmTitle() {
        return mTitle;
    }

    public URL getmImgUrl() {
        return mImgUrl;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public int getmRating() {
        return mRating;
    }

    public Date getmReleaseDate() {
        return mReleaseDate;
    }

}
