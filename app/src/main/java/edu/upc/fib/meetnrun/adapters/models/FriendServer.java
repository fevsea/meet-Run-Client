package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 05/12/2017.
 */

public class FriendServer implements IServerModel {

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("creator")
    @Expose
    private User user;
    @SerializedName("friend")
    @Expose
    private User friend;
    @SerializedName("accepted")
    @Expose
    private boolean accepted;

    @Override
    public Friend toGenericModel() {
        Friend f = new Friend(this.user, this.friend, accepted);
        return f;
    }
}
