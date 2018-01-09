package edu.upc.fib.meetnrun.models;

import java.util.List;

/**
 * Created by Javier on 03/01/2018.
 */

public class RankingUser implements RankingGeneric{

    /* Tots els codis postals que tenen al menys un usuari */
    private List<Integer> allZips;
    /* RANKINGS */
    /* Ranking global, sense importar el codi postal ni la ciutat */
    private List<PositionUser> Ranking;

    /* POSICIÓ DE CURRENT USER */
    /* La posició al ranking de CurrentUser */
    private int currentUserPosition;

    public RankingUser() {
    }

/* ZIP: ranking del codigo postal, city: ranking de ciudad, global: ranking global */
    public int getCurrentUserZipPosition(){
        return currentUserPosition;
    }
    public int getCurrentUserCityPosition(){
        return currentUserPosition;
    }
    public int getCurrentUserGlobalPosition(){
        return currentUserPosition;
    }


    public List<Integer> getAllZips(){
        return allZips;
    }

    public List<PositionUser> getRanking(){
        return Ranking;
    }
    public List<PositionUser> getRankingByZip(int zip){
        return Ranking;
    }
    public List<PositionUser> getRankingByCity(int zip){ return Ranking; }
}
