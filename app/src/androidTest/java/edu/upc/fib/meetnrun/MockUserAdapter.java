package edu.upc.fib.meetnrun;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;


public class MockUserAdapter implements IUserAdapter {
    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException {
        return null;
    }

    @Override
    public User getUser(int targetUserId) throws NotFoundException {
        return null;
    }

    @Override
    public boolean updateUser(User obj) throws ParamsException, NotFoundException, AutorizationException {
        return false;
    }

    @Override
    public boolean deleteUserByID(int targetUserId) throws NotFoundException, AutorizationException {
        return false;
    }

    @Override
    public List<Meeting> getUsersFutureMeetings(int targetUserId) throws AutorizationException, ParamsException {
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

        return l;    }
}
