package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public interface IChallengeAdapter {

    public List<Challenge> getCurrentUserChallenges() throws AuthorizationException;

    public Challenge getChallenge(int challengeID) throws AuthorizationException, NotFoundException;

    public boolean deleteRejectChallenge(int challengeID) throws AuthorizationException, NotFoundException;

    public boolean acceptChallenge(int challengeID) throws AuthorizationException, NotFoundException;

    public Challenge createNewChallenge(User creator, User challenged, int distance, String deadlineDate)
            throws AuthorizationException, ParamsException;
}
