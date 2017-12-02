package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.upc.fib.meetnrun.models.Challenge;

/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public class ChallengeServer implements IServerModel {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("creator")
    @Expose
    private UserServer creator;

    @SerializedName("challenged")
    @Expose
    private UserServer challenged;

    @SerializedName("distance")
    @Expose
    private int distance;

    @SerializedName("deadline")
    @Expose
    private String dateDeadline;

    @SerializedName("created")
    @Expose
    private String creationDate;

    @SerializedName("creatorDistance")
    @Expose
    private Integer creatorDistance;

    @SerializedName("challengedDistance")
    @Expose
    private Integer challengedDistance;

    public ChallengeServer(UserServer creator, UserServer challenged, int distance, String dateDeadline, String creationDate,
                           Integer creatorDistance, Integer challengedDistance) {
        this.creator = creator;
        this.challenged = challenged;
        this.distance = distance;
        this.dateDeadline = dateDeadline;
        this.creationDate = creationDate;
        this.creatorDistance = creatorDistance;
        this.challengedDistance = challengedDistance;
    }

    public ChallengeServer(Challenge c) {
        this.creator = new UserServer(c.getCreator());
        this.challenged = new UserServer(c.getChallenged());
        this.distance = c.getDistance();
        this.dateDeadline = c.getDateDeadline();
        this.creationDate = c.getCreationDate();
        this.creatorDistance = c.getCreatorDistance();
        this.challengedDistance = c.getChallengedDistance();
    }

    @Override
    public Challenge toGenericModel() {
        Challenge c = new Challenge(this.id, creator.toGenericModel(), challenged.toGenericModel(), distance, dateDeadline, creationDate, creatorDistance, challengedDistance);
        return c;
    }
}
