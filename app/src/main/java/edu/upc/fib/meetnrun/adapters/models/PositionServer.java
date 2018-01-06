package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.upc.fib.meetnrun.models.Position;

/**
 * Created by Awais Iqbal on 06/01/2018.
 */

public class PositionServer implements IServerModel{

    @SerializedName("position")
    @Expose
    private int position;

    @SerializedName("zip")
    @Expose
    private int zip;

    @SerializedName("distance")
    @Expose
    private float distance;


    @Override
    public Position toGenericModel() {
        Position p = new Position(this.zip,this.distance);
        return p;
    }
}
