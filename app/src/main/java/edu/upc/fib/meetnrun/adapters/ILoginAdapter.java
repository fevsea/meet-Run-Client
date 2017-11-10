package edu.upc.fib.meetnrun.adapters;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface ILoginAdapter {
    public String login(String username, String password) throws AutorizationException;

    public User getCurrentUser() throws AutorizationException;

    public boolean logout() throws AutorizationException;

    public boolean changePassword(String oldPassword, String newPassword) throws AutorizationException, ForbiddenException;
    
}
