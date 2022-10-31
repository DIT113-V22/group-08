package com.quinstedt.islandRush.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/*Source:
 https://www.geeksforgeeks.org/how-to-perform-crud-operations-in-room-database-in-android**/


@Database(entities = {PlayerScore.class}, version = 2, exportSchema = false)
public abstract class PlayerScoreDatabase extends RoomDatabase {

    private static PlayerScoreDatabase instance;

    public abstract ScoreDao Dao();

    public static synchronized PlayerScoreDatabase getInstance(Context context) {
        if (instance == null) {
            instance =
                    // for creating a instance for database
                    // we are creating a database builder and passing
                    // database class with database name.
                    Room.databaseBuilder(context.getApplicationContext(),
                            PlayerScoreDatabase.class, "scoreboard_database")
                            // below line is use to add fall back to
                            // destructive migration to database.
                            .fallbackToDestructiveMigration()
                            // below line is to add callback
                            // to database.
                            .addCallback(roomCallback)
                            // below line is to
                            // build database.
                            .build();
        }
        // after creating an instance
        // we are returning our instance
        return instance;
    }

    // below line is to create a callback for our room database.
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // this method is called when database is created
            // and below line is to populate data.
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // we are creating an async task class to perform task in background.
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(PlayerScoreDatabase instance) {
            ScoreDao dao = instance.Dao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}

