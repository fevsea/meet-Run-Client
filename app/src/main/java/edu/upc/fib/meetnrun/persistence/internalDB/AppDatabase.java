package edu.upc.fib.meetnrun.persistence.internalDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import edu.upc.fib.meetnrun.persistence.internalDB.dao.MeetingDao;
import edu.upc.fib.meetnrun.persistence.internalDB.dao.UserDao;
import edu.upc.fib.meetnrun.persistence.internalDB.entity.MeetingDB;
import edu.upc.fib.meetnrun.persistence.internalDB.entity.UserDB;

/**
 * Created by Awais Iqbal on 31/10/2017.
 */

@Database(entities = {UserDB.class, MeetingDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract MeetingDao meetingDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}