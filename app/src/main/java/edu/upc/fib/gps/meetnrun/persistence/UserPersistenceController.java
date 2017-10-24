package edu.upc.fib.gps.meetnrun.persistence;

import java.util.List;

import edu.upc.fib.gps.meetnrun.exceptions.ParamsException;
import edu.upc.fib.gps.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 20/10/2017.
 */

public class UserPersistenceController implements IGenericController<User> {


    /**
     * Given the user information to register in the aplication, creates a User in the DB
     *
     * @param userName Username of the user
     * @param firstName first name
     * @param lastName last name of the user
     * @param email register mail
     * @param postCode register postcode
     * @param password user password
     */
    public void registerUser(String userName, String firstName, String lastName, String email, int postCode,String password) throws ParamsException {
    }


    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public boolean updateObject(User obj) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
