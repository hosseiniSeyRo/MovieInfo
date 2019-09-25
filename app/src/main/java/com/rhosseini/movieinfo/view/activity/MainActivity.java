package com.rhosseini.movieinfo.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.view.adapter.MovieRecyclerViewAdapter;
import com.rhosseini.movieinfo.viewModel.MovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieRecyclerViewAdapter adapter;
    MovieViewModel viewModel;
    TextView searchView;
    ImageView searchIcon, searchClearIcon;
    View emptyLayout, loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* init viewModel */
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        /* bind views*/
        bindViews();

        /* configure RecyclerView */
        configureRecyclerView();

        /* get All Movies */
        getAllMovies("home", 1);
    }

    /* bind views*/
    private void bindViews() {
        searchView = findViewById(R.id.searchView);
        searchIcon = findViewById(R.id.searchIcon);
        searchClearIcon = findViewById(R.id.searchClearIcon);
        emptyLayout = findViewById(R.id.emptyLayout);
        loadingLayout = findViewById(R.id.loadingLayout);

        // hide searchClearIcon
        searchClearIcon.setVisibility(View.GONE);

        // hide emptyLayout and loadingLayout
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);

        // handle searchClearIcon click listener
        searchClearIcon.setOnClickListener(v -> searchView.setText(null));

        // add textWatcher to searchView
        searchView.addTextChangedListener(searchTextWatcher);

        //  handle keyboard search btn click
        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchKeyword = v.getText().toString().trim();
                Toast.makeText(MainActivity.this, "" + searchKeyword, Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

    }

    /* search text watcher*/
    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                searchClearIcon.setVisibility(View.GONE);
            } else {
                searchClearIcon.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /* get Movies */
    private void getAllMovies(String searchText, Integer page) {
        viewModel.getAllMovies(searchText, page).observe(this, this::consumeResponse);
    }

    /* consume Response */
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
