package edu.upc.fib.meetnrun.persistence.internalDB.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.upc.fib.meetnrun.persistence.internalDB.entity.UserDB;


@Dao
public interface UserDao {

    @Query("SELECT * FROM "+ UserDB.TABLE_NAME)
    public List<UserDB> getAll();

    @Query("SELECT * FROM "+UserDB.TABLE_NAME+" where " + UserDB.COLUMN_ID + " = :id")
    public UserDB findByID(int id);

    @Query("SELECT COUNT(*) from " + UserDB.TABLE_NAME)
    public int countUsers();

    @Query("DELETE FROM "+UserDB.TABLE_NAME+" where "+ UserDB.COLUMN_ID + " = :id")
    public int deleteByID(int id);

    @Query("SELECT * FROM "+UserDB.TABLE_NAME+" where username = :username and password = :password")
    public UserDB login(String username,String password);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public long[] insertAll(UserDB... users);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public long insert(UserDB user);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public int updateUsers(UserDB... users);

    @Delete
    public void deleteUsers(UserDB... user);
}
