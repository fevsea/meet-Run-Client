package edu.upc.fib.gps.meetnrun.persistence;

import java.util.List;

import edu.upc.fib.gps.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.gps.meetnrun.exceptions.ParamsException;
import edu.upc.fib.gps.meetnrun.models.Meeting;
import edu.upc.fib.gps.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 17/10/2017.
 */

public interface IGenericController<T> {
    public T get(int id) throws NotFoundException;
    public boolean updateObject(T obj) throws ParamsException, NotFoundException;
    public boolean delete(int id) throws NotFoundException;
    public List<T> getAll();
}
