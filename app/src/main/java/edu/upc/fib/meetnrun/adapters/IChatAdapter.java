package edu.upc.fib.meetnrun.adapters;

import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;

/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public interface IChatAdapter {

    /**
     * Saves a Chat element in the server
     *
     * @param chatName                    Chat name
     * @param listUsersChatIDs            @{@link List<edu.upc.fib.meetnrun.models.User>}
     * @param type                        Type of chat if is individual chat or by groups
     * @param meetingID                   ID of the relation meeting
     * @param lastMessage                 Last message send in the chat
     * @param lastMessageUserName The position in the list of the last user who send the message
     * @param lastDateTime                last datetimestamp
     * @return New instance of @{@link Chat} created in server
     * @throws AuthorizationException Throwed when the user is not logged
     * @throws ParamsException       Throwed when one or more parameters are incorrect
     */
    public Chat createChat(String chatName, List<Integer> listUsersChatIDs, int type, Integer meetingID,
                           String lastMessage, String lastMessageUserName, Date lastDateTime)
            throws AuthorizationException, ParamsException;

    /**
     * Get the chats of the current logged user, given by the page number asked
     *
     * @param page page of chats to get
     * @return @{@link List<Chat>} with all the chatsi n that page
     * @throws AuthorizationException When user is not logged in the app
     */
    public List<Chat> getChats(int page) throws AuthorizationException;


    public boolean updateChat(Chat c) throws AuthorizationException, ParamsException, NotFoundException;

    public boolean deleteChat(int id) throws AuthorizationException, ParamsException, NotFoundException;

    public Chat getChat(int id) throws AuthorizationException, NotFoundException;

    public Chat getPrivateChat(int targetUserID) throws AuthorizationException, NotFoundException;

}
