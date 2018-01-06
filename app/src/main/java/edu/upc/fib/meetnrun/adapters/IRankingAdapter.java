package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.models.Position;

/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public interface IRankingAdapter {

    public List<Position> getAvgForEachZipCode() throws AuthorizationException;

    public List<String> getAllPostalCodes() throws AuthorizationException;;

}
