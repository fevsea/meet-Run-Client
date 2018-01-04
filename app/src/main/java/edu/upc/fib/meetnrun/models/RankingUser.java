package edu.upc.fib.meetnrun.models;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by Javier on 03/01/2018.
 */

public class RankingUser {
    /*class userPosition{
        int position;
        int userID;
        int zip;
        float distance;
    }*/
    
     /* NO SE SI USAR STRINGS ES UNA BUENA IDEA, NO SERÁ MUY ACTUALIZABLE. SI LO VES MEJOR, HAZ UNA CLASE CON LO DE ARRIBA Y HAZ QUE LAS LISTAS DE ABAJO SEAN DE USERPOSITION */
    /* Cada elemento de las listas seguirá el siguiente formato :
        xxxxxUyyyyyZzzzzzDwwwwwwww
        Donde x es la posición, y userId, z el zip o código postal, w la distancia */
    
    /* Tots els codis postals que tenen al menys un usuari */
    private List<String> allZips;
    /* RANKINGS */
    /* Ranking global, sense importar el codi postal ni la ciutat */
    private List<String> allPositions;
    /* Ranking d'un codi postal, el donat a la funció */
    private List<String> zipPositions;
    /* Ranking d'una ciutat, la que inclou el codi postal donat, si es pot fer (DEJALA LA ULTIMA) */
    private List<String> cityPositions;
    
    /* POSICIÓ DE CURRENT USER */
    /* La posició al ranking global de CurrentUser */
    private int currentUserGlobalPosition;
    /* La posició al ranking del codi postal de CurrentUser */
    private int currentUserZipPosition;
    /* La posició al ranking de la ciutat de CurrentUser (DEJALA LA ULTIMA, no se si se hara)*/
    private int currentUserCityPosition;


    public int getCurrentUserGlobalPosition(){
        return currentUserGlobalPosition;
    }

    public int getCurrentUserZipPosition(){
        return currentUserZipPosition;
    }
    
    public int getCurrentUserCityPosition(){
        return currentUserCityPosition;
    }
    
    public List<Integer> getAllZips(){
        return allZips;
    }

    public List<String> getAllPositions(){
        return allPositions;
    }

    public List<String> getZipPositions(int zip){
        return zipPositions;
    }

    public List<String> getCityPositions(int cityZip){
        //cityZip es un dels zips de la ciutat o poble
        return cityPositions;
    }
}
