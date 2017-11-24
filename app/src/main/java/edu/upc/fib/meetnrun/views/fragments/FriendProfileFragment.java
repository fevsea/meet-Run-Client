package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.views.ChatActivity;
import edu.upc.fib.meetnrun.views.ChatListActivity;

/**
 * Created by eric on 2/11/17.
 */

public class FriendProfileFragment extends ProfileFragmentTemplate {

    @Override
    protected void setImage() {

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String friend = profileInfo.getString("userName");
                String title = getResources().getString(R.string.chat_friend_dialog_title)+" "+friend;
                String message = getResources().getString(R.string.chat_friend_dialog_message)+" "+friend+"?";

                String ok = getResources().getString(R.string.ok);
                String cancel = getResources().getString(R.string.cancel);
                showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Crear o cojer chat

                                String user = CurrentSession.getInstance().getCurrentUser().getUsername();
                                Chat chat = ChatListFragment.getChat(user, friend);
                                if (chat == null) {
                                    String chatName = user+" - "+friend;
                                    Calendar rightNow = Calendar.getInstance();
                                    StringBuilder sb = new StringBuilder();
                                    String hour = null;
                                    String minute = null;
                                    String aux = String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
                                    if (aux.length() == 1) hour = "0"+aux;
                                    else hour = aux;
                                    aux = String.valueOf(rightNow.get(Calendar.MINUTE));
                                    if (aux.length() == 1) minute = "0"+aux;
                                    else minute = aux;
                                    sb.append(hour);
                                    sb.append(":");
                                    sb.append(minute);
                                    String seconds = String.valueOf(rightNow.get(Calendar.SECOND));

                                    rightNow.set(Calendar.HOUR_OF_DAY, 0);
                                    rightNow.set(Calendar.MINUTE, 0);
                                    rightNow.set(Calendar.SECOND, 0);
                                    rightNow.set(Calendar.MILLISECOND, 0);
                                    Date dateWithoutTime = rightNow.getTime();

                                    Message m = new Message("", user, sb.toString(), dateWithoutTime.toString(), seconds);

                                    chat = new Chat(1,chatName, user, friend, m);
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
        return getResources().getString(R.string.delete_friend_dialog_message)+" "+profileInfo.getString("userName")+"?";
    }

    @Override
    protected void getMethod(String s) {
        new removeFriend().execute(s);
    }

    private class removeFriend extends AsyncTask<String,String,String> {

        boolean ok = false;

        @Override
        protected String doInBackground(String... s) {
            try {
                ok = friendsDBAdapter.removeFriend(Integer.parseInt(s[0]));
                //eliminar chat con amigo
                ChatListFragment.deleteChat(profileInfo.getString("userName"));
            } catch (AutorizationException e) {
                e.printStackTrace();
            } catch (ParamsException e) {
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