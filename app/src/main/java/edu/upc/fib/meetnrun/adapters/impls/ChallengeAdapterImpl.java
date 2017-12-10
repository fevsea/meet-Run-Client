package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IChallengeAdapter;
import edu.upc.fib.meetnrun.adapters.models.ChallengeServer;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.checkErrorCodeAndThowException;


/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public class ChallengeAdapterImpl implements IChallengeAdapter {

    private final SOServices mServices;

    public ChallengeAdapterImpl(SOServices soServices) {
        mServices = soServices;
    }

    @Override
    public List<Challenge> getCurrentUserChallenges() throws AutorizationException {
        List<Challenge> l = new ArrayList<>();
        try {

            Response<List<ChallengeServer>> res =
                    mServices.getAllCurrentUserChallenges().execute();
            if (!res.isSuccessful())
                checkErrorCodeAndThowException(res.code(), res.errorBody().string());
            List<ChallengeServer> psm = res.body();
            for (int i = 0; i < psm.size(); i++) {
                l.add(psm.get(i).toGenericModel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return l;
    }

    @Override
    public Challenge getChallenge(int challengeID) throws AutorizationException, NotFoundException {
        ChallengeServer m = null;
        try {
            Response<ChallengeServer> ret = mServices.getChallenge(challengeID).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) {
                throw (NotFoundException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return m.toGenericModel();
    }

    @Override
    public boolean deleteRejectChallenge(int challengeID) throws AutorizationException, NotFoundException {
        boolean ok = true;
        try {
            Response<Void> ret = mServices.deleteRejectChallenge(challengeID).execute();
            if (!ret.isSuccessful()) {
                ok = false;
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) {
                throw (NotFoundException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return ok;
    }

    @Override
    public boolean acceptChallenge(int challengeID) throws AutorizationException, NotFoundException {
        boolean ok = true;
                try {
                    Response<Void> ret = mServices.acceptChallenge(challengeID).execute();
                    if (!ret.isSuccessful()) {
                        ok = false;
                        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GenericException e) {
                    e.printStackTrace();
                    if (e instanceof NotFoundException) {
                        throw (NotFoundException) e;
                    } else if (e instanceof AutorizationException) {
                        throw (AutorizationException) e;
                    }
                }
                return ok;
    }

    @Override
    public Challenge createNewChallenge(User creator, User challenged, int distance, String deadlineDate)
            throws AutorizationException, ParamsException {
        Forms.ChallengeCreator cs = new Forms.ChallengeCreator(0, creator.getId(), challenged.getId(), distance, deadlineDate);
        ChallengeServer c = null;
        try {
            Response<ChallengeServer> ret = mServices.createNewChallenge(cs).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            c = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof ParamsException) {
                throw (ParamsException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return c.toGenericModel();
    }
}
