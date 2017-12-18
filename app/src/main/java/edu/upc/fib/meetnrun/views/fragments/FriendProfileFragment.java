package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.CreateChat;
import edu.upc.fib.meetnrun.asynctasks.GetChat;
import edu.upc.fib.meetnrun.asynctasks.GetPrivateChat;
import edu.upc.fib.meetnrun.asynctasks.RemoveFriend;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.views.CreateChallengeActivity;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatActivity;

/**
 * Created by eric on 2/11/17.
 */

public class FriendProfileFragment extends ProfileFragmentTemplate implements View.OnClickListener {

    private String friendUsername;
    private Date dateWithoutTime;
    private List<Integer> userList;
    private Chat chat;

    @Override
    protected void setImage() {

        friendUsername = currentFriend.getUsername();

        chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getResources().getString(R.string.chat_friend_dialog_title)+" "+friendUsername;
                String message = getResources().getString(R.string.chat_friend_dialog_message)+" "+friendUsername+"?";

                String ok = getResources().getString(R.string.ok);
                String cancel = getResources().getString(R.string.cancel);
                showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callGetPrivateChat(currentFriend.getId());
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

            }
        });

    }

    @Override
    protected String setDialogTitle() {
        return getResources().getString(R.string.delete_friend_dialog_title);
    }

    @Override
    protected String setDialogMessage() {
        return getResources().getString(R.string.delete_friend_dialog_message)+" "+friendUsername+"?";
    }

    @Override
    protected void getMethod(String s) {
        callRemoveFriend(s);
    }

    @Override
    protected void configureChallengeButton() {
        challengeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), CreateChallengeActivity.class);
        i.putExtra("id",this.currentFriend.getId());
        startActivity(i);
    }


    private void callRemoveFriend(String friendId) {
        new RemoveFriend() {
            @Override
            public void onResponseReceived(boolean b) {
                if (b) {
                    Toast.makeText(getContext(), "Friend removed", Toast.LENGTH_SHORT).show();
                    currentFriend.setFriend(false);
                    getActivity().finish();
                }
            }
        }.execute(friendId);
    }


    private void callCreateChat() {
        new CreateChat(friendUsername,userList,0,null,"",0,dateWithoutTime) {
            @Override
            public void onResponseReceived(Chat chat) {
                if (chat != null) {
                    Intent i = new Intent(getContext(), ChatActivity.class);
                    CurrentSession.getInstance().setChat(chat);
                    getActivity().finish();
                    startActivity(i);
                }
            }
        }.execute();
    }


    private void callGetPrivateChat(int chatId) {
        final User user;
        user = CurrentSession.getInstance().getCurrentUser();
        new GetPrivateChat() {
            @Override
            public void onResponseReceived(Chat responseChat) {
                if (chat == null) {
                    Calendar rightNow = Calendar.getInstance();
                    dateWithoutTime = rightNow.getTime();

                    userList = new ArrayList<>();
                    userList.add(user.getId());
                    userList.add(currentFriend.getId());

                    callCreateChat();

                }
                else {
                    Intent i = new Intent(getContext(), ChatActivity.class);
                    CurrentSession.getInstance().setChat(chat);
                    getActivity().finish();
                    startActivity(i);
                }
            }
        }.execute(chatId);
    }

}
