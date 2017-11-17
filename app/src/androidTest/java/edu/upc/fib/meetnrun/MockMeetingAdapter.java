package edu.upc.fib.meetnrun;


import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.utils.JsonUtils;

public class MockMeetingAdapter implements IMeetingAdapter {

    private static MockMeetingAdapter instance = null;

    public MockMeetingAdapter() {
    }

    public static MockMeetingAdapter getInstance() {
        if (instance == null) {
            instance = new MockMeetingAdapter();
        }
        return instance;
    }


    @Override
    public Meeting getMeeting(int id) throws NotFoundException {
        Meeting meeting = new Meeting(1,"Ruta amb la fib","Sortida després de classe per la zona universitària",true,3,"yyyy-MM-ddTHH:mm:ssZ","23.0","42.0");
        return meeting;
    }


    @Override
    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException, AutorizationException {
        return true;
    }

    @Override
    public boolean deleteMeetingByID(int id) throws NotFoundException, AutorizationException {
        return true;
    }

    @Override
    public List<Meeting> getAllMeetings() {
        List<Meeting> l = new ArrayList<>();
        User owner = new User(1,"marcparicio","Marc","Paricio","08758", "What is the first name of the person you first kissed?", 4);
        List<User> participants = new ArrayList<>();
        Meeting meeting = new Meeting(1,"Ruta de fibers","Sortida després de classe per la zona universitària",true,3,"yyyy-MM-ddTHH:mm:ssZ","23.0","42.0",owner,participants);
        Meeting meeting1 = new Meeting(2,"Ruta1","Sortida1",true,3,"yyyy-MM-ddTHH:mm:ssZ","23.0","42.0",owner,participants);
        Meeting meeting2 = new Meeting(3,"Ruta2","Sortida2",true,3,"yyyy-MM-ddTHH:mm:ssZ","23.0","42.0",owner,participants);
        Meeting meeting3 = new Meeting(4,"Ruta3","Sortida3",true,3,"yyyy-MM-ddTHH:mm:ssZ","23.0","42.0",owner,participants);
        l.add(meeting);
        l.add(meeting1);
        l.add(meeting2);
        l.add(meeting3);

        return l;
    }


    @Override
    public Meeting createMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException, AutorizationException {
        //return m.toGenericModel();
        return new Meeting();
    }



    @Override
    public List<User> getParticipantsFromMeeting(int meetingId) throws AutorizationException, ParamsException {
        List<User> ul = new ArrayList<>();
        User user1 = new User(1,"user1","FirstName1","LastName1","08758", "What is the first name of the person you first kissed?", 4);
        User user2 = new User(2,"user2","FirstName2","LastName2","08198", "What is the first name of the person you first kissed?", 2);
        User user3 = new User(3,"user3","FirstName3","LastName3","08019", "What is the first name of the person you first kissed?", 10);
        User user4 = new User(4,"user4","FirstName4","LastName4","08830", "What is the first name of the person you first kissed?", 1);
        ul.add(user1);
        ul.add(user2);
        ul.add(user3);
        ul.add(user4);
        return ul;
    }

    @Override
    public boolean joinMeeting(int meetingId) throws AutorizationException, ParamsException {
        boolean ok = false;
        return ok;
    }

    @Override
    public boolean leaveMeeting(int meetingId) throws AutorizationException, ParamsException {
        boolean ok = false;
        return ok;
    }

    @Override
    public List<Meeting> getAllMeetingsFilteredByName(String query) {
        return null;
    }



    private void checkErrorCodeAndThowException(int code, String string) throws GenericException {
        switch (code) {
            case 400:
                throw JsonUtils.CreateParamExceptionFromJson(string);
            case 401:
                throw JsonUtils.CreateAutorizationExceptionFromJson(string);
            case 404:
                throw JsonUtils.CreateNotFoundExceptionFromJson(string);
        }
    }

}