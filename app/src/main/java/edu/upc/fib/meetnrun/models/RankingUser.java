package edu.upc.fib.meetnrun.models;

import java.util.List;

/**
 * Created by Javier on 03/01/2018.
 */

public class RankingUser{

    /* Tots els codis postals que tenen al menys un usuari */
    private List<String> allZips;
    /* RANKINGS */
    /* Ranking global, sense importar el codi postal ni la ciutat */
    private List<PositionUser> Ranking;


    public RankingUser() {
    }

    public List<String> getAllZips(){
        return allZips;
    }
    public List<PositionUser> getRanking(){
        return Ranking;
    }
    public List<PositionUser> getRankingByZip(int zip){
        return Ranking;
    }
}
