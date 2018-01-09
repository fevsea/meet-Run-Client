package edu.upc.fib.meetnrun.models;

import java.util.List;

/**
 * Created by Javier on 03/01/2018.
 */

public class RankingZip implements RankingGeneric{

    /* RANKINGS*/
    private List<Position> Ranking;

    /* POSICIONS CODI POSTAL CURRENT USER */
    /* Posició d'el codi postal de currentUser*/
    private int currentUserZipPosition;

    public int getCurrentUserCityZipPosition(){
        return currentUserZipPosition;
    }
    public int getCurrentUserGlobalZipPosition(){
        return currentUserZipPosition;
    }

    public List<Position> getGlobalRanking(){
        return Ranking;
    }
    public List<Position> getCityRanking(int cityZip){
        /* La ciutat vindria donada per cityZip, un dels codis postals de la ciutat */
        return Ranking;
    }
}
