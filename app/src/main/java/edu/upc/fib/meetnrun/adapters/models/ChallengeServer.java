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
    private Float distance;

    @SerializedName("deadline")
    @Expose
    private String dateDeadline;

    @SerializedName("created")
    @Expose
    private String creationDate;

    @SerializedName("creator_distance")
    @Expose
    private Float creatorDistance;

    @SerializedName("challenged_distance")
    @Expose
    private Float challengedDistance;

    @SerializedName("accepted")
    @Expose
    private Boolean accepted;

    public ChallengeServer(UserServer creator, UserServer challenged, Float distance, String dateDeadline, String creationDate,
                           Float creatorDistance, Float challengedDistance, Boolean accepted) {
        this.creator = creator;
        this.challenged = challenged;
        this.distance = distance;
        this.dateDeadline = dateDeadline;
        this.creationDate = creationDate;
        this.creatorDistance = creatorDistance;
        this.challengedDistance = challengedDistance;
        this.accepted = accepted;
    }

    public ChallengeServer(Challenge c) {
        this.creator = new UserServer(c.getCreator());
        this.challenged = new UserServer(c.getChallenged());
        this.distance = c.getDistance();
        this.dateDeadline = c.getDateDeadline();
        this.creationDate = c.getCreationDate();
        this.creatorDistance = c.getCreatorDistance();
        this.challengedDistance = c.getChallengedDistance();
        this.accepted = c.isAccepted();
    }

    @Override
    public Challenge toGenericModel() {
        Challenge c = new Challenge(this.id, creator.toGenericModel(), challenged.toGenericModel(), distance, dateDeadline, creationDate, creatorDistance, challengedDistance,this.accepted);
        return c;
    }
}
