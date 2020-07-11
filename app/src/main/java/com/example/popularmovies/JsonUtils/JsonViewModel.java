package com.example.popularmovies.JsonUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.popularmovies.AsyncResponse;
import com.example.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class JsonViewModel extends ViewModel {


    final static String THEMOVIEDB_BASE_URL = "api.themoviedb.org";
    final static String API_KEY = "api key";
    final static String PARAM_SCHEME = "https";
    final static String SORT_POPULAR = "popular";
    final static String PARAM_QUERY = "api_key";
    final static String[] URL_PATH = {"3", "movie"};
    final static String SORT_TOP_RATED = "top_rated";
    final static String PARAM_POSTER_PATH = "poster_path";
    final static String PARAM_TITLE = "title";
    final static String PARAM_RELEASE_DATE = "release_date";
    final static String PARAM_PLOT_SYNOPSIS = "overview";
    final static String PARAM_RATING = "vote_average";
    final static String PARAM_ID = "id";
    private static String json;
    private static ArrayList<Movie> mMovieArrayList;
    private int pagenumber = 1;
    private DownloadData downloadtask = null;

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();

                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } else {
                Log.e("getResponseFromHttpUrl", "Error response code: " +
                        urlConnection.getResponseCode() + "\nError response message: " +
                        urlConnection.getResponseMessage());
            }

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (in != null) {
                in.close();
            }
        }
        return null;
    }

    public ArrayList<Movie> getData() throws JSONException {
        if (json == null) {
            json = new String();
            downloadtask = new DownloadData();
        }
        return mMovieArrayList;
    }



    public static class DownloadData extends AsyncTask<URL, Void, String> {
        public AsyncResponse result = null;
        @Override
        protected String doInBackground(URL... urls) {
            String jsonResponse = null;
            try {
                jsonResponse = getResponseFromHttpUrl(createMovieListUrl(SORT_POPULAR));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }


        @Override
        protected void onPostExecute(String s) {
            json = s;
            if (mMovieArrayList == null) {
                mMovieArrayList = new ArrayList<Movie>();
            }
            try {
                mMovieArrayList = parseJson(json, mMovieArrayList);
            } catch (JSONException | MalformedURLException | ParseException e) {
                e.printStackTrace();
            }
            result.processFinish(mMovieArrayList);

        }

    }

    private static ArrayList<Movie> parseJson(String json, ArrayList<Movie> movieArrayList) throws JSONException, MalformedURLException, ParseException {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject current_movie = (JSONObject) results.get(i);
            String title = current_movie.getString(PARAM_TITLE);
            String synapsis = current_movie.getString(PARAM_PLOT_SYNOPSIS);
            double ratings =  current_movie.getDouble(PARAM_RATING);
            String releaseDate = current_movie.getString(PARAM_RELEASE_DATE);
            long id = current_movie.getLong(PARAM_ID);
            String posterPath = current_movie.getString(PARAM_POSTER_PATH);
            int rating =(int) ratings*100;
            Movie currentMovie = new Movie(id, title, posterPath,synapsis, rating, stringToDate(releaseDate));
            movieArrayList.add(currentMovie);

        }
        return movieArrayList;
    }


    private static Date stringToDate(String sDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        return date;
    }


    public static URL createMovieListUrl(String sortBY) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(PARAM_SCHEME).authority(THEMOVIEDB_BASE_URL).appendPath(URL_PATH[0])
                .appendPath(URL_PATH[1]).appendPath(sortBY)
                .appendQueryParameter(PARAM_QUERY, API_KEY);
        Uri uri = builder.build();
        URL movieListUrl = null;
        try {
            movieListUrl = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return movieListUrl;
    }

    public DownloadData getDownloadtask() {
        return downloadtask;
    }
}
