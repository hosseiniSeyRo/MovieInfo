package com.rhosseini.movieinfo.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.view.adapter.MovieRecyclerViewAdapter;
import com.rhosseini.movieinfo.viewModel.MovieViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    MovieRecyclerViewAdapter adapter;
    MovieViewModel viewModel;
    View emptyLayout, loadingLayout, errorLayout;
    TextView errorMessage;
    Button tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* init viewModel */
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        /* bind views*/
        bindViews();

        /* Clicks handler */
        clickHandler();

        /* configure RecyclerView */
        configureRecyclerView();

        /* get All Movies by Specific word in title */
        getMoviesByTitle("iran", 1);
    }

    /* bind views*/
    private void bindViews() {
        recyclerView = findViewById(R.id.recyclerView);
        emptyLayout = findViewById(R.id.emptyLayout);
        loadingLayout = findViewById(R.id.loadingLayout);
        errorLayout = findViewById(R.id.errorLayout);
        errorMessage = findViewById(R.id.errorMessage);
        tryAgain = findViewById(R.id.tryAgain);

        // show emptyLayout & hide loadingLayout and errorLayout and recyclerView
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    /* Click handler */
    private void clickHandler() {
        tryAgain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tryAgain:
                //TODO

                break;
            default:
                break;
        }
    }

    /* configure RecyclerView */
    private void configureRecyclerView() {
        // set recyclerView setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set recyclerView adapter
        adapter = new MovieRecyclerViewAdapter(this, position -> {
            //TODO
//            Intent intent = new Intent(this, DetailActivity.class);
//            intent.putExtra("imdbId", adapter.getItem(position).getImdbId());
//            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    /* get Movies */
    private void getMoviesByTitle(String searchText, Integer page) {
        // show loading layout and hide recyclerView and emptyLayout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);

        // get data
        viewModel.getMoviesByTitle(searchText, page).observe(this, movies -> {
            //TODO handle state

            adapter.setList(movies);

            // if recyclerView is empty show emptyLayout
            if (adapter.getItemCount() == 0) {
                emptyLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
            } else {
                emptyLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
            }
        });
    }

}
