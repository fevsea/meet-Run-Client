package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;

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
    private IChatAdapter chatDBAdapter;
    private Date dateWithoutTime;
    private List<Integer> userList;
    private Chat chat;
    private boolean isAccepted;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isAccepted = getActivity().getIntent().getBooleanExtra("accepted", true);
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    @Override
    protected void setImage() {

        friendUsername = currentFriend.getUsername();
        chatDBAdapter = CurrentSession.getInstance().getChatAdapter();

        if (isAccepted) {
            chatImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = getResources().getString(R.string.chat_friend_dialog_title) + " " + friendUsername;
                    String message = getResources().getString(R.string.chat_friend_dialog_message) + " " + friendUsername + "?";

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
        else {
            imgSecondary.setVisibility(View.VISIBLE);
            chatImage.setVisibility(View.GONE);
            img.setImageResource(R.drawable.send);
            imgSecondary.setImageResource(android.R.drawable.ic_delete);

            img.setOnClickListener(acceptOnClickListener);
            imgSecondary.setOnClickListener(deleteOnClickListener);
        }

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
        new removeFriend().execute(Integer.valueOf(s));
    }

    @Override
    protected void configureChallengeButton() {
        if (isAccepted) {
            challengeButton.setOnClickListener(this);
        }
        else {
            challengeButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), CreateChallengeActivity.class);
        i.putExtra("id",this.currentFriend.getId());
        startActivity(i);
    }

    private class removeFriend extends AsyncTask<Integer,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(Integer... s) {
            try {
                ok = friendsDBAdapter.removeFriend(s[0]);
            } catch (AuthorizationException | ParamsException e) {
                e.printStackTrace();
            }
            try {
                chat = chatDBAdapter.getPrivateChat(currentFriend.getId());
            } catch (AuthorizationException | NotFoundException e) {
                e.printStackTrace();
                chat = null;
            }

            if (chat != null) {
                try {
                    FirebaseDatabase.getInstance().getReference(String.valueOf(chat.getId())).removeValue();
                    chatDBAdapter.deleteChat(chat.getId());
                } catch (AuthorizationException | ParamsException | NotFoundException e) {
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
            } catch (AuthorizationException | ParamsException e) {
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
            } catch (AuthorizationException | NotFoundException e) {
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

    private class AddFriend extends AsyncTask<Integer, String, Boolean> {

        private IFriendsAdapter friendsAdapter;
        private Exception e;

        private AddFriend() {
            this.friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                return friendsAdapter.addFriend(params[0]);
            }
            catch(AuthorizationException | NotFoundException e) {
                this.e = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result && e!= null) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
            }
            getActivity().finish();
        }
    }

    private View.OnClickListener acceptOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AddFriend().execute(currentFriend.getId());
        }
    };

    private View.OnClickListener deleteOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            new removeFriend().execute(currentFriend.getId());
        }
    };

}
