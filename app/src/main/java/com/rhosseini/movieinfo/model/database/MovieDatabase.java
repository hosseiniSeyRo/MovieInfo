package com.rhosseini.movieinfo.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.rhosseini.movieinfo.model.database.dao.MovieDao;
import com.rhosseini.movieinfo.model.database.entity.Movie;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase INSTANCE;
    private static final String DB_NAME = "MovieDb";
    public abstract MovieDao movieDao();

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
        private MovieDao movieDao;

        private PopulateDbAsyncTask(MovieDatabase dbInstance) {
            movieDao = dbInstance.movieDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Movie> movieList = new ArrayList<>();

            movieList.add(new Movie("tt2705436", "Italian Spiderman", "2007", "https://m.media-amazon.com/images/M/MV5BYjFhN2RjZTctMzA2Ni00NzE2LWJmYjMtNDAyYTllOTkyMmY3XkEyXkFqcGdeQXVyNTA0OTU0OTQ@._V1_SX300.jpg"));
            movieList.add(new Movie("tt2084949", "Superman, Spiderman or Batman", "2011", "https://m.media-amazon.com/images/M/MV5BMjQ4MzcxNDU3N15BMl5BanBnXkFtZTgwOTE1MzMxNzE@._V1_SX300.jpg"));
            movieList.add(new Movie("tt0100669", "Spiderman", "1990", "123"));

            movieDao.insert(movieList);

            return null;
        }
    }
}
