package edu.upc.fib.meetnrun.persistence.internalDB.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.upc.fib.meetnrun.persistence.internalDB.entity.MeetingDB;


@Dao
public interface MeetingDao {

    @Query("SELECT * FROM "+ MeetingDB.TABLE_NAME)
    public List<MeetingDB> getAll();

    @Query("SELECT * FROM "+MeetingDB.TABLE_NAME+" where "+ MeetingDB.COLUMN_ID + " = :id")
    public MeetingDB findByID(int id);

    @Query("SELECT COUNT(*) from "+ MeetingDB.TABLE_NAME)
    public int countMeetings();

    @Query("DELETE FROM "+ MeetingDB.TABLE_NAME+" where " + MeetingDB.COLUMN_ID + " = :id")
    public int deleteByID(int id);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public long[] insertAll(MeetingDB... meetings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insert(MeetingDB meeting);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public int updateMeetings(MeetingDB... meeting);

    @Delete
    public void deleteMeetings(MeetingDB... meetings);
}
