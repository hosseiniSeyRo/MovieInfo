package com.rhosseini.movieinfo.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    MovieRecyclerViewAdapter adapter;
    MovieViewModel viewModel;
    AutoCompleteTextView searchView;
    ImageView searchIcon, searchClearIcon;
    ArrayAdapter<String> searchSuggestionAdapter;
    View emptyLayout, loadingLayout;
    final List<String> suggestions = new ArrayList<>();

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

        /* get All Movies */
        getMoviesByTitle("tehran", 1);
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
            // get data for search keyword
            getMoviesByTitle(searchSuggestionAdapter.getItem(position), 1);
        });


        // get suggestion data from db
        getAllSearchHistories();


        // handle keyboard search btn click
        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // disappear suggestions box
                searchView.dismissDropDown();

                // close keyboard
                closeKeyboard();

                // get search keyword
                String searchKeyword = v.getText().toString().trim();

                // save search keyword in db
                saveSearchKeywordInDb(searchKeyword);

                // get data for search keyword
                getMoviesByTitle(searchKeyword, 1);

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
        viewModel.getAllSearchHistories().observe(this, searchHistoryList -> {
            suggestions.clear();
            for (SearchHistory searchHistory : searchHistoryList) {
                suggestions.add(searchHistory.getTitle());
            }
            searchSuggestionAdapter.clear();
            searchSuggestionAdapter.addAll(suggestions);
        });
    }

    /* save search keyword in db */
    private void saveSearchKeywordInDb(String searchKeyword) {
        SearchHistory searchHistory = new SearchHistory(searchKeyword, System.currentTimeMillis());
        viewModel.insertSearchHistory(searchHistory);
    }

    /* close keyboard */
    private void closeKeyboard() {
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /* get Movies */
    private void getMoviesByTitle(String searchText, Integer page) {
        viewModel.getMoviesByTitle(searchText, page).observe(this, movies -> adapter.setList(movies));
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
