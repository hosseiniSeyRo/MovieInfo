package com.rhosseini.movieinfo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.CustomViewHolder> {

    private Context mContext;
    private List<Movie> dataList = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;

    // for load more
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount, visibleItemCount;


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClick(int position);
    }

    public MovieRecyclerViewAdapter(Context mContext, RecyclerView recyclerView) {
        this.mContext = mContext;

        // load more
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

//                if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list, parent, false);
            return new DataViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (holder instanceof DataViewHolder) {
            Movie currentMovie = dataList.get(position);

            DataViewHolder dataViewHolder = (DataViewHolder) holder;

            Picasso.with(mContext)
                    .load(currentMovie.getPoster())
                    .centerCrop()
                    .resizeDimen(R.dimen.posterWidth, R.dimen.posterHeight)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.error_image_loading)
                    .into(dataViewHolder.poster);
            dataViewHolder.title.setText(currentMovie.getTitle());
            dataViewHolder.year.setText(currentMovie.getYear());

            dataViewHolder.container.setOnClickListener(v -> {
                onItemClickedListener.onItemClick(position);
            });
        } else {
            //Do whatever you want. Or nothing !!
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;

            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public Movie getItem(int position) {
        return dataList.get(position);
    }

    public void setList(List<Movie> movieList) {
        if (movieList != null) {
            this.dataList.addAll(movieList);
            notifyDataSetChanged();
        }
    }

    public void addNullData() {
        dataList.add(null);
        notifyItemInserted(dataList.size()-1);
    }

    public void removeNull() {
        if (isLoading) {
            dataList.remove(dataList.size() - 1);
            notifyItemRemoved(dataList.size());
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public boolean getLoading() {
        return isLoading;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class DataViewHolder extends CustomViewHolder {

        ImageView poster;
        TextView title;
        TextView year;
        View container;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            year = itemView.findViewById(R.id.year);
            container = itemView.findViewById(R.id.container);
        }
    }

    public class LoadingViewHolder extends CustomViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
