package com.rhosseini.movieinfo.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.rhosseini.movieinfo.model.database.dao.MovieDao;
import com.rhosseini.movieinfo.model.database.dao.SearchHistoryDao;
import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.model.database.entity.SearchHistory;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase INSTANCE;
    private static final String DB_NAME = "MovieDb";
    public abstract MovieDao movieDao();
    public abstract SearchHistoryDao searchHistoryDao();

    public static synchronized MovieDatabase getINSTANCE(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, DB_NAME)
                    .addCallback(callback)
                    .build();
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private SearchHistoryDao searchHistoryDao;

        private PopulateDbAsyncTask(MovieDatabase dbInstance) {
            searchHistoryDao = dbInstance.searchHistoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<SearchHistory> searchHistoryList = new ArrayList<>();

            searchHistoryList.add(new SearchHistory("home"));
            searchHistoryList.add(new SearchHistory("france"));
            searchHistoryList.add(new SearchHistory("iran"));
            searchHistoryList.add(new SearchHistory("ireland"));
            searchHistoryList.add(new SearchHistory("iraq"));
            searchHistoryList.add(new SearchHistory("war"));
            searchHistoryList.add(new SearchHistory("star"));

            searchHistoryDao.insert(searchHistoryList);

            return null;
        }
    }
}
