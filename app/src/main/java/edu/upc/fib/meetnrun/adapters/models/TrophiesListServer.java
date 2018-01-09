package edu.upc.fib.meetnrun.adapters.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.models.Trophie;

public class TrophiesListServer implements IServerModel {

    @SerializedName("km_1")
    @Expose
    private boolean km_1;

    @SerializedName("km_10")
    @Expose
    private boolean km_10;

    @SerializedName("km_100")
    @Expose
    private boolean km_100;

    @SerializedName("km_1000")
    @Expose
    private boolean km_1000;

    @SerializedName("km_10000")
    @Expose
    private boolean km_10000;

    @SerializedName("h_1")
    @Expose
    private boolean h_1;

    @SerializedName("h_10")
    @Expose
    private boolean h_10;

    @SerializedName("h_100")
    @Expose
    private boolean h_100;

    @SerializedName("h_1000")
    @Expose
    private boolean h_1000;


    @SerializedName("meetings_1")
    @Expose
    private boolean meetings_1;

    @SerializedName("meetings_5")
    @Expose
    private boolean meetings_5;

    @SerializedName("meetings_10")
    @Expose
    private boolean meetings_10;

    @SerializedName("meetings_20")
    @Expose
    private boolean meetings_20;

    @SerializedName("meetings_50")
    @Expose
    private boolean meetings_50;

    @SerializedName("level_1")
    @Expose
    private boolean level_1;

    @SerializedName("level_5")
    @Expose
    private boolean level_5;

    @SerializedName("level_10")
    @Expose
    private boolean level_10;

    @SerializedName("level_25")
    @Expose
    private boolean level_25;

    @SerializedName("level_40")
    @Expose
    private boolean level_40;

    @SerializedName("level_50")
    @Expose
    private boolean level_50;

    @SerializedName("max_distance_1")
    @Expose
    private boolean max_distance_1;

    @SerializedName("max_distance_5")
    @Expose
    private boolean max_distance_5;

    @SerializedName("max_distance_10")
    @Expose
    private boolean max_distance_10;

    @SerializedName("max_distance_21")
    @Expose
    private boolean max_distance_21;

    @SerializedName("max_distance_42")
    @Expose
    private boolean max_distance_42;

    @SerializedName("steps_10000")
    @Expose
    private boolean steps_10000;

    @SerializedName("steps_20000")
    @Expose
    private boolean steps_20000;

    @SerializedName("steps_25000")
    @Expose
    private boolean steps_25000;

    @SerializedName("steps_50000")
    @Expose
    private boolean steps_50000;

    @SerializedName("steps_100000")
    @Expose
    private boolean steps_100000;

    @SerializedName("challenges_1")
    @Expose
    private boolean challenges_1;

    @SerializedName("challenges_5")
    @Expose
    private boolean challenges_5;

    @SerializedName("challenges_10")
    @Expose
    private boolean challenges_10;

    @SerializedName("challenges_20")
    @Expose
    private boolean challenges_20;

    @SerializedName("friends_1")
    @Expose
    private boolean friends_1;

    @SerializedName("friends_5")
    @Expose
    private boolean friends_5;

    @SerializedName("friends_10")
    @Expose
    private boolean friends_10;

    @SerializedName("friends_20")
    @Expose
    private boolean friends_20;


    /**
     * No args constructor for use in serialization
     */
    public TrophiesListServer() {
    }

    public List<Trophie> toGenericModel() {
        List<Trophie> lt = new ArrayList<>();
        Trophie t = null;
        t = new Trophie(km_1);
        lt.add(t);
        t = new Trophie(km_10);
        lt.add(t);
        t = new Trophie(km_100);
        lt.add(t);
        t = new Trophie(km_1000);
        lt.add(t);
        t = new Trophie(km_10000);
        lt.add(t);
        t = new Trophie(h_1);
        lt.add(t);
        t = new Trophie(h_10);
        lt.add(t);
        t = new Trophie(h_100);
        lt.add(t);
        t = new Trophie(h_1000);
        lt.add(t);
        t = new Trophie(meetings_1);
        lt.add(t);
        t = new Trophie(meetings_5);
        lt.add(t);
        t = new Trophie(meetings_10);
        lt.add(t);
        t = new Trophie(meetings_20);
        lt.add(t);
        t = new Trophie(meetings_50);
        lt.add(t);
        t = new Trophie(level_1);
        lt.add(t);
        t = new Trophie(level_5);
        lt.add(t);
        t = new Trophie(level_10);
        lt.add(t);
        t = new Trophie(level_25);
        lt.add(t);
        t = new Trophie(level_40);
        lt.add(t);
        t = new Trophie(level_50);
        lt.add(t);
        t = new Trophie(max_distance_1);
        lt.add(t);
        t = new Trophie(max_distance_5);
        lt.add(t);
        t = new Trophie(max_distance_10);
        lt.add(t);
        t = new Trophie(max_distance_21);
        lt.add(t);
        t = new Trophie(max_distance_42);
        lt.add(t);
        t = new Trophie(steps_10000);
        lt.add(t);
        t = new Trophie(steps_20000);
        lt.add(t);
        t = new Trophie(steps_25000);
        lt.add(t);
        t = new Trophie(steps_50000);
        lt.add(t);
        t = new Trophie(steps_100000);
        lt.add(t);
        t = new Trophie(challenges_1);
        lt.add(t);
        t = new Trophie(challenges_5);
        lt.add(t);
        t = new Trophie(challenges_10);
        lt.add(t);
        t = new Trophie(challenges_20);
        lt.add(t);
        t = new Trophie(friends_1);
        lt.add(t);
        t = new Trophie(friends_5);
        lt.add(t);
        t = new Trophie(friends_10);
        lt.add(t);
        t = new Trophie(friends_20);
        lt.add(t);

        for (int i = 0; i < lt.size(); i++) {
            Log.e("TROPHIE", "i:" + i + "\t isObtained: " + lt.get(i).getTrophieIsObtained());
        }
        return lt;
    }
}
