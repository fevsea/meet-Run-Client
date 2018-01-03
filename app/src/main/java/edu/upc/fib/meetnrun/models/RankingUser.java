package edu.upc.fib.meetnrun.models;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by Javier on 03/01/2018.
 */

public class RankingUser {
    class userPosition{
        int position;
        int userID;
        int zip;
        float distance;
    }
    private int currentUserPosition;
    private List<Integer> allZips;
    private List<userPosition> allPositions;
    private List<userPosition> zipPositions;
    private List<userPosition> cityPositions;

    public int getCurrentUserPosition(){
        return currentUserPosition;
    }

    public List<Integer> getAllZips(){
        return allZips;
    }

    public List<userPosition> getAllPositions(){
        return allPositions;
    }

    public List<userPosition> getZipPositions(int zip){
        return zipPositions;
    }

    public List<userPosition> getCityPositions(int cityZip){
        return cityPositions;
    }
}
