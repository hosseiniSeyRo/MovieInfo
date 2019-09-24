package com.rhosseini.movieinfo.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.model.database.Movie;
import com.rhosseini.movieinfo.view.adapter.MovieRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Movie> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    MovieRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* set dummy data */
        setDummyData();

        /* configure RecyclerView */
        configureRecyclerView();

        /* set data in recyclerView*/
        consumeResponse(movieList);
    }

    private void consumeResponse(List<Movie> response) {
        adapter.setList(response);
    }

    private void setDummyData() {
        movieList.add(new Movie("tt2705436", "Italian Spiderman", "2007", "https://m.media-amazon.com/images/M/MV5BYjFhN2RjZTctMzA2Ni00NzE2LWJmYjMtNDAyYTllOTkyMmY3XkEyXkFqcGdeQXVyNTA0OTU0OTQ@._V1_SX300.jpg"));
        movieList.add(new Movie("tt2084949", "Superman, Spiderman or Batman", "2011", "https://m.media-amazon.com/images/M/MV5BMjQ4MzcxNDU3N15BMl5BanBnXkFtZTgwOTE1MzMxNzE@._V1_SX300.jpg"));
        movieList.add(new Movie("tt0100669", "Spiderman", "1990", "123"));
        movieList.add(new Movie("tt2705436", "Italian Spiderman", "2007", "https://m.media-amazon.com/images/M/MV5BYjFhN2RjZTctMzA2Ni00NzE2LWJmYjMtNDAyYTllOTkyMmY3XkEyXkFqcGdeQXVyNTA0OTU0OTQ@._V1_SX300.jpg"));
        movieList.add(new Movie("tt2084949", "Superman, Spiderman or Batman", "2011", "https://m.media-amazon.com/images/M/MV5BMjQ4MzcxNDU3N15BMl5BanBnXkFtZTgwOTE1MzMxNzE@._V1_SX300.jpg"));
        movieList.add(new Movie("tt0100669", "Spiderman", "1990", "https://m.media-amazon.com/images/M/MV5BMjE3Mzg0MjAxMl5BMl5BanBnXkFtZTcwNjIyODg5Mg@@._V1_SX300.jpg"));
        movieList.add(new Movie("tt2705436", "Italian Spiderman", "2007", "https://m.media-amazon.com/images/M/MV5BYjFhN2RjZTctMzA2Ni00NzE2LWJmYjMtNDAyYTllOTkyMmY3XkEyXkFqcGdeQXVyNTA0OTU0OTQ@._V1_SX300.jpg"));
        movieList.add(new Movie("tt2084949", "Superman, Spiderman or Batman", "2011", "https://m.media-amazon.com/images/M/MV5BMjQ4MzcxNDU3N15BMl5BanBnXkFtZTgwOTE1MzMxNzE@._V1_SX300.jpg"));
        movieList.add(new Movie("tt0100669", "Spiderman", "1990", "https://m.media-amazon.com/images/M/MV5BMjE3Mzg0MjAxMl5BMl5BanBnXkFtZTcwNjIyODg5Mg@@._V1_SX300.jpg"));
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
