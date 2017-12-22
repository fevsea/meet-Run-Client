package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetCity;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 16/11/17.
 */

public abstract class ProfileFragmentTemplate extends Fragment {

    protected View view;
    protected TextView profileImg;
    protected TextView postCode;
    protected TextView userName;
    protected TextView name;
    protected ImageView img;
    protected IFriendsAdapter friendsDBAdapter;
    protected ImageView chatImage;
    protected ImageView imgSecondary;
    protected User currentFriend;
    protected Button challengeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_friends_profile, container, false);

        this.currentFriend = CurrentSession.getInstance().getFriend();
        this.friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();

        this.userName = view.findViewById(R.id.userName2);
        this.name = view.findViewById(R.id.completeName2);
        this.postCode = view.findViewById(R.id.userPostCode2);
        this.profileImg = view.findViewById(R.id.profileImage);
        this.img = view.findViewById(R.id.action_friend);
        this.chatImage = view.findViewById(R.id.chat_friend);
        this.imgSecondary = view.findViewById(R.id.action_friend_secondary);

        char letter = currentFriend.getUsername().charAt(0);
        String firstLetter = String.valueOf(letter);
        profileImg.setBackground(getColoredCircularShape(letter));
        profileImg.setText(firstLetter);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = setDialogTitle();
                String message = setDialogMessage();

                String ok = getResources().getString(R.string.ok);
                String cancel = getResources().getString(R.string.cancel);
                showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getMethod(String.valueOf(currentFriend.getId()));
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
        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        userName.setText(currentFriend.getUsername());
        String nameFriend = currentFriend.getFirstName()+" "+currentFriend.getLastName();
        name.setText(nameFriend);
        String postcodeFriend = currentFriend.getPostalCode();
        postCode.setText(postcodeFriend);

        getCityFromPostcode(postcodeFriend);

        challengeButton = view.findViewById(R.id.challenge_button);
        configureChallengeButton();
        setImage();
        return this.view;
    }

    protected abstract void configureChallengeButton();

    protected abstract void setImage();

    protected abstract String setDialogTitle();

    protected abstract String setDialogMessage();

    protected abstract void getMethod(String s);

    void showDialog(String title, String message, String okButtonText, String negativeButtonText, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okButtonText, ok);
        if (negativeButtonText != null && cancel != null)
            builder.setNegativeButton(negativeButtonText, cancel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getCityFromPostcode(String p) {
        callGetCity(p);
    }

    private void callGetCity(String p) {
        new GetCity() {
            @Override
            public void onResponseReceived(String s) {
                postCode.setText(s);
            }
        }.execute(p);
    }

    private GradientDrawable getColoredCircularShape(char letter) {

        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }
}
