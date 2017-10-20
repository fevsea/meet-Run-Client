package edu.upc.fib.gps.meetnrun.persistence;

import java.util.List;

import edu.upc.fib.gps.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 20/10/2017.
 */

public class UserPersistenceController implements IGenericController<User> {
    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public boolean insert(User obj) {
        return false;
    }

    @Override
    public boolean update(User obj) {
        return false;
    }

    @Override
    public boolean delete(User obj) {
        return false;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    /**
     * Given the user information to register in the aplication, creates a User in the DB
     *
     * @param userName Username of the user
     * @param firstName first name
     * @param lastName last name of the user
     * @param email register mail
     * @param postCode register postcode
     * @param password user password
     * @return a error code described in @{@link edu.upc.fib.gps.meetnrun.utils.ErrorCodes}
     */
    public int registerUser(String userName, String firstName, String lastName, String email, int postCode,String password){
        return -1;
    }


}
