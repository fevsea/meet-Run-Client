package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.upc.fib.meetnrun.models.PositionUser;

/**
 * Created by Awais Iqbal on 06/01/2018.
 */

public class PositionUserServer implements IServerModel {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("postal_code")
    @Expose
    private String postal_code;

    @SerializedName("distance")
    @Expose
    private Integer distance;

    @Override
    public PositionUser toGenericModel() {
        PositionUser pu = new PositionUser(Integer.valueOf(postal_code),this.distance,this.username,this.first_name,this.last_name);
        return pu;
    }
}
