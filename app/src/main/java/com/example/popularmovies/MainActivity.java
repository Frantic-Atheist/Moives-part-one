package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.emergency.EmergencyNumber;

import com.example.popularmovies.JsonUtils.JsonViewModel;
import com.example.popularmovies.JsonUtils.JsonViewModel;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncResponse{
    JsonViewModel mViewModel;
    private RecyclerView mPosterList;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> movieArrayList;
    private RecyclerView.LayoutManager mlayoutManager;
    private static final int NUMBER_OF_COLUMNS = 2;
    private JsonViewModel.DownloadData downloadtask = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = new ViewModelProvider(this).get(JsonViewModel.class);
        try {
            mViewModel.getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mPosterList = findViewById(R.id.movie_recycler_view);
        mlayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mPosterList.setLayoutManager(mlayoutManager);
        downloadtask = mViewModel.getDownloadtask();
        downloadtask.result = this;
        downloadtask.execute();


    }

    @Override
    public void processFinish(ArrayList<Movie> movieArrayList) {
        mAdapter = new MovieAdapter(movieArrayList, this);
        mPosterList.setAdapter(mAdapter);
    }
}