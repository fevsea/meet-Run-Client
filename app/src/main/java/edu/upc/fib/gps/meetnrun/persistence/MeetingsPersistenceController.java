package edu.upc.fib.gps.meetnrun.persistence;

import java.util.Date;
import java.util.List;

import edu.upc.fib.gps.meetnrun.models.Meeting;

/**
 * Created by Awais Iqbal on 20/10/2017.
 */

public class MeetingsPersistenceController implements IGenericController<Meeting> {


    /**
     * Given a public meeting information, crete that meeting inthe DB
     *
     * @param title Title of the meeting
     * @param description Description of the meeting
     * @param meetingDateTime Meeting dateTime in class @{@link Date}
     * @param level Meeting level
     * @param latitude Latitude where the meeting gonna happen
     * @param longitude Longitude where the meeting gonna happen
     * @return
     */
    public Meeting createMeetingPublic(String title, String description, Date meetingDateTime, int level, String latitude, String longitude){
        return null;
    }

    @Override
    public Meeting get(int id) {
        return null;
    }

    @Override
    public boolean updateObject(Meeting obj) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<Meeting> getAll() {
        return null;
    }
}
