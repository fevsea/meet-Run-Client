package edu.upc.fib.gps.meetnrun.persistence;

import java.util.List;

import edu.upc.fib.gps.meetnrun.models.Meeting;
import edu.upc.fib.gps.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 17/10/2017.
 */

public interface IGenericController<T> {
    public T get(int id);
    public boolean insert(T obj);//TODO possibly need to be deleted in the future
    public boolean update(T obj);
    public boolean delete(T obj);
    public List<T> getAll();
}
