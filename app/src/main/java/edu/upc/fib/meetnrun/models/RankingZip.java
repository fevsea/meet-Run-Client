package edu.upc.fib.meetnrun.models;

import java.util.List;

/**
 * Created by Javier on 03/01/2018.
 */

public class ZipUser {
    class zipPosition{
        int position;
        int zip;
        float distance;
    }
    private int currentUserZipPosition;
    private List<zipPosition> allPositions;
    private List<zipPosition> cityPositions;

    public int getCurrentUserZipPosition(){
        return currentUserZipPosition;
    }

    public List<zipPosition> getAllPositions(){
        return allPositions;
    }


    public List<zipPosition> getCityPositions(int cityZip){
        return cityPositions;
    }
}
