package com.rhosseini.movieinfo.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.view.adapter.MovieRecyclerViewAdapter;
import com.rhosseini.movieinfo.viewModel.MovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieRecyclerViewAdapter adapter;
    MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* init viewModel */
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        /* configure RecyclerView */
        configureRecyclerView();

        /* get All Movies */
        getAllMovies("home", 1);
    }

    private void getAllMovies(String searchText, Integer page) {
        viewModel.getAllMovies(searchText, page).observe(this, this::consumeResponse);
    }

    private void consumeResponse(List<Movie> response) {
        adapter.setList(response);
    }

    /* configure RecyclerView */
    private void configureRecyclerView() {
        // set recyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // set recyclerView adapter
        adapter = new MovieRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
    }
}
