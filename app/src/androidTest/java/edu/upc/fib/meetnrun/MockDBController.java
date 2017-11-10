package edu.upc.fib.meetnrun;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.IGenericController;
import edu.upc.fib.meetnrun.persistence.persistenceModels.Forms;
import edu.upc.fib.meetnrun.persistence.persistenceModels.MeetingServer;
import edu.upc.fib.meetnrun.persistence.persistenceModels.UserServer;
import edu.upc.fib.meetnrun.remote.ApiUtils;
import edu.upc.fib.meetnrun.remote.SOServices;
import edu.upc.fib.meetnrun.utils.JsonUtils;
import retrofit2.Response;

public class MockDBController implements IGenericController {

    private static MockDBController instance = null;

    public MockDBController() {
    }

    public static MockDBController getInstance() {
        if (instance == null) {
            instance = new MockDBController();
        }
        return instance;
    }

    @Override
    public User getUser(int id) throws NotFoundException {
        User user = new User(1,"marcparicio","Marc","Paricio","08758", "What is the first name of the person you first kissed?", 4);
        return user;
    }


    @Override
    public Meeting getMeeting(int id) throws NotFoundException {
        Meeting meeting = new Meeting(1,"Ruta amb la fib","Sortida després de classe per la zona universitària",true,3,"yyyy-MM-ddTHH:mm:ssZ","23.0","42.0");
        return meeting;
    }

    @Override
    public boolean updateUser(User obj) throws ParamsException, NotFoundException, AutorizationException {
        //TODO
        return true;
    }

    @Override
    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException, AutorizationException {
        //TODO
        return true;
    }

    @Override
    public boolean deleteUserByID(int id) throws NotFoundException, AutorizationException {
        //TODO
        return true;
    }

    @Override
    public boolean deleteMeetingByID(int id) throws NotFoundException, AutorizationException {
        //TODO
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
    public List<User> getAllUsers() {
        List<User> l = new ArrayList<>();
        //TODO
        return l;
    }


    @Override
    public Meeting createMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException, AutorizationException {
        MeetingServer m = null;
        //TODO
        return m.toGenericModel();
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException {
        Forms.UserRegistration ur = new Forms.UserRegistration(0, userName, firstName, lastName, postCode, question, answer, password, 1);
        UserServer u = null;
        //TODO
        return u.toGenericModel();

    }

    @Override
    public String login(String username, String password) throws AutorizationException {
        String token = "";
        //TODO
        return token;
    }

    @Override
    public User getCurrentUser() throws AutorizationException {
        UserServer u = null;
        //TODO
        return u.toGenericModel();
    }

    @Override
    public boolean logout() throws AutorizationException {
        boolean ok = false;
        //TODO
        return ok;
    }

    @Override
    public List<User> getParticipantsFromMeeting(int meetingId) throws AutorizationException, ParamsException {
        List<User> ul = new ArrayList<>();
        //TODO
        return ul;
    }

    @Override
    public boolean joinMeeting(int meetingId) throws AutorizationException, ParamsException {
        boolean ok = false;
        //TODO
        return ok;
    }

    @Override
    public boolean leaveMeeting(int meetingId) throws AutorizationException, ParamsException {
        boolean ok = false;
        //TODO
        return ok;
    }

    @Override
    public List<Meeting> getUsersFutureMeetings(int userId) throws AutorizationException, ParamsException {
        List<Meeting> ul = new ArrayList<>();
        //TODO
        return ul;
    }

    @Override
    public List<User> getUserFriends() throws AutorizationException {
        List<User> ul = new ArrayList<>();
        //TODO
        return ul;
    }

    @Override
    public boolean addFriend(int targetUserId) throws AutorizationException, ParamsException {
        boolean ok = false;
        //TODO
        return ok;
    }

    @Override
    public boolean removeFriend(int targetUserId) throws AutorizationException, ParamsException {
        boolean ok = false;
        //TODO
        return ok;
    }

    @Override
    public List<User> listFriendsOfUser(int targetUserId) throws AutorizationException, ParamsException {
        List<User> ul = new ArrayList<>();
        //TODO
        return ul;
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