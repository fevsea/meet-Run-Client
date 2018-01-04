package edu.upc.fib.meetnrun.models;

import java.util.List;

/**
 * Created by Javier on 03/01/2018.
 */

public class RankingZip {
    /*
    class zipPosition{
        int position;
        int zip;
        float distance;
    }*/
    /* NO SE SI USAR STRINGS ES UNA BUENA IDEA, NO SERÁ MUY ACTUALIZABLE. SI LO VES MEJOR, HAZ UNA CLASE CON LO DE ARRIBA Y HAZ QUE LAS LISTAS DE ABAJO SEAN DE ZIPPOSITION*/
    /*Sigue la misma formula que con userPosition para las listas:
        xxxxZyyyyyyDzzzzzz
        Donde las x representa la posicion, las y el codigo postal, las z la distancia media del codigo postal(no se donde iria la ciudad, puede que tenga que ir aqui) */
    
    /* RANKINGS*/
    /* Ranking (conjunt de posicions) amb tots els codis postals que tenen almenys un usuari participant */
    private List<String> allPositions;
    /* Ranking (conjunt de posicions) amb els codis postals de la ciutat que inclou el codi postal donat a la funció que tenen almenys un usuari participant (DEJALA LA ULTIMA, NO SE SI SE HARA)*/
    private List<String> cityPositions;
    
    /* POSICIONS CODI POSTAL CURRENT USER */
    /* Posició d'el codi postal de currentUser a la ciutat d'aquest */
    private int currentUserCityZipPosition;
    /* Posició d'el codi postal de currentUser a tot el conjunt */
    private int currentUserGlobalZipPosition;

    public int getCurrentUserCityZipPosition(){
        return currentUserCityZipPosition;
    }
    
    public int getCurrentUserGlobalZipPosition(){
        return currentUserCityZipPosition;
    }

    public List<String> getAllPositions(){
        return allPositions;
    }


    public List<String> getCityPositions(int cityZip){
        /* La ciutat vindria donada per cityZip, un dels codis postals de la ciutat */
        return cityPositions;
    }
}
