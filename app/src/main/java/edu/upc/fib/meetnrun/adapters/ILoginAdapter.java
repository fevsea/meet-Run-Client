package edu.upc.fib.meetnrun.adapters;

import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface ILoginAdapter {
    String login(String username, String password) throws AuthorizationException;

    User getCurrentUser() throws AuthorizationException;

    boolean logout() throws AuthorizationException;

    boolean changePassword(String oldPassword, String newPassword) throws AuthorizationException, ForbiddenException;


    String getFirebaseToken() throws AuthorizationException, NotFoundException;

    boolean uppdateFirebaseToken(String token) throws AuthorizationException, NotFoundException;

}
