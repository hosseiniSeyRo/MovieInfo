package com.rhosseini.movieinfo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MyViewHolder> {


    Context mContext;
    List<Movie> movieList = new ArrayList<>();
    private OnItemClicked listener;


    public MovieRecyclerViewAdapter(Context mContext, OnItemClicked listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie currentMovie = movieList.get(position);

        Picasso.with(mContext)
                .load(currentMovie.getPoster())
                .centerCrop()
                .resizeDimen(R.dimen.posterWidth, R.dimen.posterHeight)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_image_loading)
                .into(holder.poster);
        holder.title.setText(currentMovie.getTitle());
        holder.year.setText(currentMovie.getYear());

        holder.container.setOnClickListener(v -> {
            listener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        if (movieList != null)
            return movieList.size();
        return 0;
    }

    public Movie getItem(int position) {
        return movieList.get(position);
    }

    public void setList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView title;
        TextView year;
        View container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            year = itemView.findViewById(R.id.year);
            container = itemView.findViewById(R.id.container);
        }
    }

    public interface OnItemClicked {
        void onItemClick(int position);
    }
}
