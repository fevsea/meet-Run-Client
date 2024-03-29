package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import edu.upc.fib.meetnrun.asynctasks.AddFriend;
import edu.upc.fib.meetnrun.asynctasks.CreateChat;
import edu.upc.fib.meetnrun.asynctasks.GetChat;
import edu.upc.fib.meetnrun.asynctasks.GetPrivateChat;
import edu.upc.fib.meetnrun.asynctasks.RemoveFriend;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;

import edu.upc.fib.meetnrun.asynctasks.ReportUser;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.utils.UtilsViews;
import edu.upc.fib.meetnrun.views.BaseActivity;

/**
 * Created by eric on 2/11/17.
 */

public class FriendProfileFragment extends ProfileFragmentTemplate implements View.OnClickListener {

    private String friendUsername;
    private Date dateWithoutTime;
    private List<Integer> userList;
    private Chat chat;
    private boolean isAccepted;


    public static FriendProfileFragment newInstance(String id, String userName, String name, String postalCode) {
        FriendProfileFragment fragmentFirst = new FriendProfileFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("userName", userName);
        args.putString("name",name);
        args.putString("postalCode",postalCode);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isAccepted = getActivity().getIntent().getBooleanExtra("accepted", true);
        View v = super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    protected void setImage() {

        friendUsername = currentFriend.getUsername();

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
        callRemoveFriend(s);
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
        Intent i = new Intent();
        i.putExtra("id",this.currentFriend.getId());
        BaseActivity.startWithFragment(getActivity(), new CreateChallengeFragment(), i);
    }

    private void callAddFriend(String s) {
        new AddFriend() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof ParamsException) {
                    Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived(boolean b) {
                if (b) {
                    Toast.makeText(getContext(), "Friend request sent", Toast.LENGTH_SHORT).show();
                    //currentFriend.setFriend(true);
                    getActivity().finish();
                }
            }
        }.execute(s);
    }

    private void callRemoveFriend(String friendId) {
        new RemoveFriend() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof ParamsException) {
                    Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                }
            }

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
        new CreateChat(friendUsername,userList,0,null,"",CurrentSession.getInstance().getCurrentUser().getUsername(),dateWithoutTime) {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof ParamsException) {
                    Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived(Chat chat) {
                if (chat != null) {
                    Intent i = new Intent();
                    CurrentSession.getInstance().setChat(chat);
                    BaseActivity.startWithFragment(getActivity(), new ChatFragment(), i);
                    getActivity().finish();
                }
            }
        }.execute();
    }


    private void callGetPrivateChat(int chatId) {
        final User user;
        user = CurrentSession.getInstance().getCurrentUser();
        new GetPrivateChat() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Calendar rightNow = Calendar.getInstance();
                    dateWithoutTime = rightNow.getTime();

                    userList = new ArrayList<>();
                    userList.add(user.getId());
                    userList.add(currentFriend.getId());

                    callCreateChat();
                }
            }

            @Override
            public void onResponseReceived(Chat responseChat) {
                chat = responseChat;
                if (chat == null) {
                    Calendar rightNow = Calendar.getInstance();
                    dateWithoutTime = rightNow.getTime();

                    userList = new ArrayList<>();
                    userList.add(user.getId());
                    userList.add(currentFriend.getId());

                    callCreateChat();

                } else {
                    Intent i = new Intent();
                    CurrentSession.getInstance().setChat(chat);
                    BaseActivity.startWithFragment(getActivity(), new ChatFragment(), i);
                    getActivity().finish();
                }            }
        }.execute(chatId);
    }

    private View.OnClickListener acceptOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callAddFriend(currentFriend.getId().toString());
        }
    };

    private View.OnClickListener deleteOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            callRemoveFriend(currentFriend.getId().toString());
        }
    };

    public int getTitle() {
        return R.string.friend_profile_label;
    }


    public void showDialog(String title, String okButtonText, String negativeButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setPositiveButton(okButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callReportUser(Integer.valueOf(getArguments().getString("id")));
            }
        });
        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callReportUser(int userId) {
        new ReportUser(userId) {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof ForbiddenException) {
                    Toast.makeText(getActivity(), R.string.forbidden_banned, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived(boolean b) {
                Toast.makeText(getActivity(), R.string.report_success, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

}
