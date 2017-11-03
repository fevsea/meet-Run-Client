package edu.upc.fib.meetnrun.views.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;

/**
 * Created by eric on 2/11/17.
 */

public class UserProfileFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_users_profile, container, false);

        Bundle profileInfo = getActivity().getIntent().getExtras();

        final TextView userName = view.findViewById(R.id.userName3);
        TextView name = view.findViewById(R.id.completeName3);
        TextView postCode = view.findViewById(R.id.userPostCode3);
        ImageView img = view.findViewById(R.id.friend_request);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("request friend", userName.getText().toString());

            }
        });
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        userName.setText(profileInfo.getString("userName"));
        name.setText(profileInfo.getString("name"));
        postCode.setText(profileInfo.getString("postCode"));

        return this.view;
    }
}
