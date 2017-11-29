package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.views.CreateChallengeActivity;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChatActivity;

/**
 * Created by eric on 2/11/17.
 */

public class FriendProfileFragment extends ProfileFragmentTemplate implements View.OnClickListener {

    private String friendUsername;

    @Override
    protected void setImage() {

        friendUsername = currentFriend.getUsername();

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getResources().getString(R.string.chat_friend_dialog_title)+" "+friendUsername;
                String message = getResources().getString(R.string.chat_friend_dialog_message)+" "+friendUsername+"?";

                String ok = getResources().getString(R.string.ok);
                String cancel = getResources().getString(R.string.cancel);
                showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Crear o cojer chat

                                User user = CurrentSession.getInstance().getCurrentUser();
                                String currentUsername = user.getUsername();
                                Chat chat = ChatListFragment.getChat(currentUsername, friendUsername);
                                if (chat == null) {
                                    Calendar rightNow = Calendar.getInstance();

                                    Date dateWithoutTime = rightNow.getTime();

                                    Message m = new Message("", currentUsername, dateWithoutTime);

                                    List<User> userList = new ArrayList<>();
                                    userList.add(user);
                                    userList.add(currentFriend);

                                    chat = new Chat(ChatListFragment.getCount(),friendUsername, userList, 0, m);
                                    ChatListFragment.addChatFake(chat);
                                }
                                Intent i = new Intent(getContext(), ChatActivity.class);
                                CurrentSession.getInstance().setChat(chat);
                                getActivity().finish();
                                startActivity(i);
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
        new removeFriend().execute(s);
    }

    /*@Override
    protected void configureChallengeButton() {
        challengeButton.setOnClickListener(this);
    }*/

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), CreateChallengeActivity.class);
        i.putExtra("id",this.currentFriend.getId());
        startActivity(i);
    }

    private class removeFriend extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                ok = friendsDBAdapter.removeFriend(Integer.parseInt(s[0]));
                //eliminar chat con amigo
                ChatListFragment.deleteChat(friendUsername);
            } catch (AutorizationException | ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {
                Toast.makeText(getContext(), "Friend removed", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            super.onPostExecute(s);
        }
    }

}