package com.rhosseini.movieinfo.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.viewModel.DetailViewModel;
import com.rhosseini.movieinfo.viewModel.MovieViewModel;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView poster;
    TextView title, year, director, writer, actors, language, country;
    DetailViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* init viewModel */
        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

        /* bind views*/
        bindViews();

        /* get id by intent */
        String imdbId = getIntent().getStringExtra("imdbId");

        /* get movie detail */
        getMovieById(imdbId);
    }

    /* bind views*/
    private void bindViews() {
        poster = findViewById(R.id.poster);
        title = findViewById(R.id.title);
        year = findViewById(R.id.year);
        director = findViewById(R.id.director);
        writer = findViewById(R.id.writer);
        actors = findViewById(R.id.actors);
        language = findViewById(R.id.language);
        country = findViewById(R.id.country);
    }

    /* get movie detail */
    private void getMovieById(String imdbId) {
        viewModel.getMovieById(imdbId).observe(this, currentMovie -> {
            // set data in views
            Picasso.with(this)
                    .load(currentMovie.getPoster())
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.error_image_loading)
                    .into(poster);
            title.setText(currentMovie.getTitle());
            year.setText(currentMovie.getYear());
            director.setText(currentMovie.getDirector());
            writer.setText(currentMovie.getWriter());
            actors.setText(currentMovie.getActors());
            language.setText(currentMovie.getLanguage());
            country.setText(currentMovie.getCountry());
        });
    }
}
