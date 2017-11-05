package edu.upc.fib.meetnrun.persistence.internalDB.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import edu.upc.fib.meetnrun.models.Meeting;

/**
 * Created by Awais Iqbal on 31/10/2017.
 */

@Entity(tableName = MeetingDB.TABLE_NAME)
public class MeetingDB {

    /** The name of the Cheese table. */
    public static final String TABLE_NAME = "meetings";

    /** The name of the ID column. */
    public static final String COLUMN_ID = BaseColumns._ID;


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = COLUMN_ID)
    private Integer id;
    private String title;
    private String description;
    private Boolean _public;
    private Integer level;
    private String date;
    private String latitude;
    private String longitude;

    public Integer getId() {
        return id;
    }

    public MeetingDB( String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) {
        this.title = title;
        this.description = description;
        this._public = _public;
        this.level = level;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public MeetingDB(Meeting m){
        super();
        this.id=m.getId();
        this.title = m.getTitle();
        this.description = m.getDescription();
        this._public = m.getPublic();
        this.level = m.getLevel();
        this.date = m.getDate();
        this.latitude = m.getLatitude();
        this.longitude = m.getLongitude();
    }
    public Meeting toGenericModel(){
        return new Meeting(id, title, description, _public, level, date, latitude, longitude);
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean get_public() {
        return _public;
    }

    public void set_public(Boolean _public) {
        this._public = _public;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MeetingDB{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", _public=" + _public +
                ", level=" + level +
                ", date='" + date + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
