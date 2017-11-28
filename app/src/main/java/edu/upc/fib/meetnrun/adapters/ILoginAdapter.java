package edu.upc.fib.meetnrun.adapters;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface ILoginAdapter {
    String login(String username, String password) throws AutorizationException;

    User getCurrentUser() throws AutorizationException;

    boolean logout() throws AutorizationException;

    boolean changePassword(String oldPassword, String newPassword) throws AutorizationException, ForbiddenException;

    String getFirebaseToken() throws AutorizationException, NotFoundException;

    boolean uppdateFirebaseToken(String token) throws AutorizationException, NotFoundException;

}
