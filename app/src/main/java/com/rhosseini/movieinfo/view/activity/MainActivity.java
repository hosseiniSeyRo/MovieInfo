package com.rhosseini.movieinfo.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.model.database.entity.SearchHistory;
import com.rhosseini.movieinfo.view.adapter.MovieRecyclerViewAdapter;
import com.rhosseini.movieinfo.viewModel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    MovieRecyclerViewAdapter adapter;
    MovieViewModel viewModel;
    AutoCompleteTextView searchView;
    ImageView searchIcon, searchClearIcon;
    ArrayAdapter<String> searchSuggestionAdapter;
    View emptyLayout, loadingLayout;

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

        /* configure Search */
        configureSearch();

        /* configure RecyclerView */
        configureRecyclerView();

//        /* get All Movies */
//        getAllMovies("home", 1);
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

        // show emptyLayout and hide loadingLayout
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    /* Click handler */
    private void clickHandler() {
        searchClearIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchClearIcon:
                searchView.setText(null);

                break;
            default:
                break;
        }
    }

    /* configure Search */
    private void configureSearch() {
        // add text change listener for searchView
        searchView.addTextChangedListener(searchTextWatcher);


        // set search Suggestion Adapter
        searchSuggestionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        searchView.setAdapter(searchSuggestionAdapter);
        searchView.setThreshold(2);
        searchView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(this, "selected: "+searchSuggestionAdapter.getItem(position), Toast.LENGTH_SHORT).show();
//            getAllMovies(searchSuggestionAdapter.getItem(position), 1);
        });


        // get suggestion data from db
        getAllSearchHistories();


        // handle keyboard search btn click
        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // get search keyword
                String searchKeyword = v.getText().toString().trim();
                Toast.makeText(this, "selected: "+searchKeyword, Toast.LENGTH_SHORT).show();

                // save search keyword in db
                saveSearchKeywordInDb(searchKeyword);

//                // get data for search keyword
//                getAllMovies(searchKeyword, 1);

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

    /* get all search histories from db */
    private void getAllSearchHistories() {
        final List<String> suggestions = new ArrayList<>();

        viewModel.getAllSearchHistories().observe(this, searchHistories -> {
            for (SearchHistory searchHistory : searchHistories) {
                suggestions.add(searchHistory.getTitle());
            }
            searchSuggestionAdapter.clear();
            searchSuggestionAdapter.addAll(suggestions);
            searchSuggestionAdapter.notifyDataSetChanged();
        });
    }

    /* save search keyword in db */
    private void saveSearchKeywordInDb(String searchKeyword) {
        SearchHistory searchHistory = new SearchHistory(searchKeyword, System.currentTimeMillis());
        viewModel.insertSearchHistory(searchHistory);
    }

    /* get Movies */
    private void getAllMovies(String searchText, Integer page) {
        viewModel.getAllMovies(searchText, page).observe(this, movies -> {
            adapter.setList(movies);
        });
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
