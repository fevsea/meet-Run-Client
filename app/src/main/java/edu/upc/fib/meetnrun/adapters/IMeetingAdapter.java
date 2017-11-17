package edu.upc.fib.meetnrun.adapters;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface IMeetingAdapter {
    public List<Meeting> getAllMeetings();

    public Meeting createMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException, AutorizationException;

    public Meeting getMeeting(int targetMeetingid) throws NotFoundException;

    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException, AutorizationException;

    public boolean deleteMeetingByID(int id) throws NotFoundException, AutorizationException;

    public List<User> getParticipantsFromMeeting(int targetMeetingId) throws AutorizationException, ParamsException;

    public boolean joinMeeting(int targetMeetingId) throws AutorizationException, ParamsException;

    public boolean leaveMeeting(int targetMeetingId) throws AutorizationException, ParamsException;

    public List<Meeting> getAllMeetingsFilteredByName(String query);

    public boolean addTracking(Integer userID, Integer meetingID, Float averageSpeed, Float distance, Integer steps, Long totalTimeMillis, Float calories, List<LatLng> routePoints) throws ForbiddenException,AutorizationException;

    public TrackingData getTracking(int userID, int meetingID) throws AutorizationException, NotFoundException;

    public boolean deleteTrackingInMeeting(int userID, int meetingID) throws AutorizationException, NotFoundException;
}
