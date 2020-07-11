package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.JsonUtils.JsonViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{



    //    private static int viewHolderCount;
    private int mNumberItems;
    private ArrayList<Movie> mMovieArrayList;
    private JsonViewModel mViewModel;
    private ViewModelStoreOwner mMainActivity;

    MovieAdapter(ArrayList<Movie> movieArrayList, ViewModelStoreOwner viewModelStoreOwner){
        if (movieArrayList != null){
        mNumberItems = movieArrayList.size();
        }else{
            mNumberItems = 0;
        }
        mMovieArrayList = movieArrayList;
        mMainActivity = viewModelStoreOwner;
//        viewHolderCount = 0;


    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdforGridItem = R.layout.image_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
//        mViewModel = new ViewModelProvider(mMainActivity).get(JsonViewModel.class);
        View view = inflater.inflate(layoutIdforGridItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        try {
            holder.bind(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePosterView;

        MovieViewHolder(View itemView){
            super(itemView);
            moviePosterView = itemView.findViewById(R.id.iv_poster);
        }
        void bind(int listIndex) throws JSONException {

            Movie currentMovie = mMovieArrayList.get(listIndex);
            Picasso.get().load(currentMovie.getmImgUrl().toString()).error(R.drawable.ic_launcher_background).into(moviePosterView);
        }
    }
}
