package com.rhosseini.movieinfo.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rhosseini.movieinfo.R;
import com.rhosseini.movieinfo.view.adapter.MovieRecyclerViewAdapter;
import com.rhosseini.movieinfo.viewModel.MovieViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "rHosseini -> " + this.getClass().getSimpleName();
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
        viewModel.getMoviesByTitle(searchText, page).observe(this, responseWrapper -> {
            switch (responseWrapper.getStatus()) {
                case ERROR:
                    Log.i(TAG, "ERROR: "+responseWrapper.getErrorMessage());

                    errorMessage.setText(responseWrapper.getErrorMessage());

                    errorMessage.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);

                    break;
                case CREATE:
                case SUCCESS:
                    Log.i(TAG, "CREATE||SUCCESS: "+responseWrapper.getData());

                    adapter.setList(responseWrapper.getData());

                    // if recyclerView is empty show emptyLayout
                    if (adapter.getItemCount() == 0) {
                        errorMessage.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                    } else {
                        errorMessage.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loadingLayout.setVisibility(View.GONE);
                    }

                    break;
                case LOADING:
                    Log.i(TAG, "LOADING: "+responseWrapper.getStatus());

                    errorMessage.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.VISIBLE);

                    break;
                default:
                    Log.i(TAG, "*********: "+responseWrapper.getStatus());

                    break;
            }
        });
    }

}
