package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public interface IChallengeAdapter {

    public List<Challenge> getCurrentUserChallenges() throws AutorizationException;

    public Challenge getChallenge(int challengeID) throws AutorizationException, NotFoundException;

    public boolean deleteChallenge(int challengeID) throws AutorizationException, NotFoundException;

    public Challenge createNewChallenge(User creator, User challenged, int distance, String deadlineDate,
                                        String creationDate, int creatorDistance, int challengedDistance)
            throws AutorizationException, ParamsException;
}
