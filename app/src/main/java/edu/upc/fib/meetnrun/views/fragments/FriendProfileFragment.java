package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
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
    private IChatAdapter chatDBAdapter;
    private Date dateWithoutTime;
    private List<Integer> userList;
    private Chat chat;

    @Override
    protected void setImage() {

        friendUsername = currentFriend.getUsername();
        chatDBAdapter = CurrentSession.getInstance().getChatAdapter();

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
                                new getChat().execute();
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

    private class removeFriend extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                ok = friendsDBAdapter.removeFriend(Integer.parseInt(s[0]));
            } catch (AutorizationException | ParamsException e) {
                e.printStackTrace();
            }
            try {
                chat = chatDBAdapter.getPrivateChat(currentFriend.getId());
            } catch (AutorizationException e) {
                e.printStackTrace();
                chat = null;
            } catch (NotFoundException e) {
                e.printStackTrace();
                chat = null;
            }

            if (chat != null) {
                try {
                    FirebaseDatabase.getInstance().getReference(String.valueOf(chat.getId())).removeValue();
                    chatDBAdapter.deleteChat(chat.getId());
                } catch (AutorizationException e) {
                    e.printStackTrace();
                } catch (ParamsException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {
                Toast.makeText(getContext(), "Friend removed", Toast.LENGTH_SHORT).show();
                currentFriend.setFriend(false);
                getActivity().finish();
            }
            super.onPostExecute(s);
        }
    }

    private class createChat extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... s) {
            try {
                chat = chatDBAdapter.createChat(friendUsername, userList, 0, null, "", 0, dateWithoutTime);
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            
            if (chat != null) {
                Intent i = new Intent(getContext(), ChatActivity.class);
                CurrentSession.getInstance().setChat(chat);
                getActivity().finish();
                startActivity(i);
            }
        }
    }

    private class getChat extends AsyncTask<String,String,String> {

        User user;

        @Override
        protected void onPreExecute() {
            user = CurrentSession.getInstance().getCurrentUser();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... s) {
            try {
                chat = chatDBAdapter.getPrivateChat(currentFriend.getId());
            } catch (AutorizationException e) {
                e.printStackTrace();
                chat = null;
            } catch (NotFoundException e) {
                e.printStackTrace();
                chat = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (chat == null) {
                Calendar rightNow = Calendar.getInstance();
                dateWithoutTime = rightNow.getTime();

                userList = new ArrayList<>();
                userList.add(user.getId());
                userList.add(currentFriend.getId());

                new createChat().execute();

            }
            else {
                Intent i = new Intent(getContext(), ChatActivity.class);
                CurrentSession.getInstance().setChat(chat);
                getActivity().finish();
                startActivity(i);
            }
        }
    }

}