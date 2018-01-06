package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IRankingAdapter;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.adapters.models.PositionServer;
import edu.upc.fib.meetnrun.adapters.models.PositionUserServer;
import edu.upc.fib.meetnrun.models.Position;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.calculateOffset;
import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 06/01/2018.
 */

public class RankingAdapterImpl implements IRankingAdapter {
    private final SOServices mServices;

    public RankingAdapterImpl(SOServices mServices) {
        this.mServices = mServices;
    }

    @Override
    public List<Position> getAvgForEachZipCode() {
        List<Position> ul = new ArrayList<>();
        try {
            Response<List<PositionServer>> ret = mServices.getAvgKMPostalCode().execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            List<PositionServer> u = ret.body();
            if (u != null) {
                for (int i = 0; i < u.size(); i++) {
                    ul.add(u.get(i).toGenericModel());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ul;
    }

    @Override
    public List<String> getAllPostalCodes() {
        List<String> ul = new ArrayList<>();
        try {
            Response<List<Forms.Zip>> ret = mServices.getAllPostCodes().execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            List<Forms.Zip> u = ret.body();
            if (u != null) {
                for (int i = 0; i < u.size(); i++) {
                    ul.add(u.get(i).getZip());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ul;
    }

    @Override
    public List<PositionUser> getUsersFromUsersRanking(int page) {
        List<PositionUser> ul = new ArrayList<>();
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<PositionUserServer>> ret =
                    mServices.getUsersInUsersRanking(SOServices.PAGELIMIT, offset).execute();

            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            PageServer<PositionUserServer> u = ret.body();
            if (u != null) {
                for (int i = 0; i < u.getResults().size(); i++) {
                    ul.add(u.getResults().get(i).toGenericModel());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ul;
    }

    @Override
    public List<PositionUser> getUsersFromUsersRankingFilterByZIP(String zip, int page) {
        List<PositionUser> ul = new ArrayList<>();
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<PositionUserServer>> ret =
                    mServices.getUsersInUsersRankingByPostCode(zip, SOServices.PAGELIMIT, offset).execute();

            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            PageServer<PositionUserServer> u = ret.body();
            if (u != null) {
                for (int i = 0; i < u.getResults().size(); i++) {
                    ul.add(u.getResults().get(i).toGenericModel());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ul;
    }

}
