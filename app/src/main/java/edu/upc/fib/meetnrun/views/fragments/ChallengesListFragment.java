package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChallengeAdapter;
import edu.upc.fib.meetnrun.asynctasks.AcceptOrRejectChallenge;
import edu.upc.fib.meetnrun.asynctasks.GetChallenges;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.views.ChallengeActivity;
import edu.upc.fib.meetnrun.views.FriendsListActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.ChallengesAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.ChallengesRequestAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.TwoButtonsRecyclerViewOnClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewRequest;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Challenge> challenges;
    private List<Challenge> challengesAccepted;
    private List<Challenge> challengesRequest;
    private ChallengesAdapter challengesAdapter;
    private ChallengesRequestAdapter challengesAdapterRequest;

    public ChallengesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_challenges_list, container, false);
        recyclerView = view.findViewById(R.id.fragment_challenge_recycler);
        recyclerViewRequest = view.findViewById(R.id.fragment_challenge_recycler_requests);
        swipeRefreshLayout = view.findViewById(R.id.fragment_challenge_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        setupRecyclerView();

        FloatingActionButton fab = getActivity().findViewById(R.id.activity_fab);
        fab.setOnClickListener(this);

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        challenges = new ArrayList<>();
        challengesAccepted = new ArrayList<>();
        challengesAdapter = new ChallengesAdapter(challenges, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {
                Challenge challenge = challengesAdapter.getChallengeAt(position);
                Intent i = new Intent(getActivity(), ChallengeActivity.class);
                i.putExtra("id", challenge.getId());
                startActivity(i);
            }
        });
        recyclerView.setAdapter(challengesAdapter);

        recyclerViewRequest.setLayoutManager(new LinearLayoutManager(getActivity()));
        challengesRequest = new ArrayList<>();
        challengesAdapterRequest = new ChallengesRequestAdapter(challengesRequest, new TwoButtonsRecyclerViewOnClickListener() {
            @Override
            public void onButtonAcceptClicked(int position) {
                Log.d("ChallengesList", "ACCEPTED REQUEST");
                Challenge challenge = challengesAdapterRequest.getChallengeAt(position);
                challenge.setAccepted(true);
                callAcceptOrRejectChallenge(challenge);
            }

            @Override
            public void onButtonRejectClicked(int position) {
                Log.d("ChallengesList", "REJECTED REQUEST");
                Challenge challenge = challengesAdapterRequest.getChallengeAt(position);
                challenge.setAccepted(false);
                callAcceptOrRejectChallenge(challenge);
            }

            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {
                Challenge challenge = challengesAdapterRequest.getChallengeAt(position);
                Intent i = new Intent(getActivity(), ChallengeActivity.class);
                i.putExtra("id", challenge.getId());
                startActivity(i);
            }
        });
        recyclerViewRequest.setAdapter(challengesAdapterRequest);
    }

    private void updateChallengesList() {
        callGetChallenges();
    }

    private void updateChallengesAdapters() {
        challengesAccepted = new ArrayList<>();
        challengesRequest = new ArrayList<>();
        for (int i = 0; i < challenges.size(); ++i) {
            Challenge ch = challenges.get(i);
            if (!ch.isAccepted()) {
                Log.d("ChallengesList", ch.getId().toString() + " is not accepted");
                if (ch.getChallenged().getId().equals(CurrentSession.getInstance().getCurrentUser().getId())) {
                    challengesRequest.add(ch);
                }
            }
            else {
                challengesAccepted.add(ch);
                Log.d("ChallengesList", ch.getId().toString() + " is accepted");
            }
        }
        challengesAdapter.updateChallengeList(challengesAccepted);
        challengesAdapterRequest.updateChallengeList(challengesRequest);
    }

    @Override
    public void onRefresh() {
        updateChallengesList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateChallengesList();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_fab) {
            Intent i = new Intent(getActivity(), FriendsListActivity.class);
            startActivity(i);
        }
    }

    private void callGetChallenges() {
        new GetChallenges() {
            @Override
            public void onResponseReceived(List<Challenge> challenges) {
                    updateChallengesAdapters();
                    swipeRefreshLayout.setRefreshing(false);
                /* TODO handle exceptions
                else if (ex instanceof AuthorizationException){

                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), R.string.error_loading, Toast.LENGTH_LONG).show();
                }*/
            }
        }.execute();
    }


    private void callAcceptOrRejectChallenge(Challenge challenge) {
        boolean accept = false;
        if (challenge.isAccepted()) {
            accept = true;
        }

        new AcceptOrRejectChallenge(challenge.getId()) {
            @Override
            public void onResponseReceived() {
                getActivity().finish();
                /* TODO handle exceptions
                else if (exception instanceof AuthorizationException){
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (exception instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), R.string.error_loading, Toast.LENGTH_LONG).show();
                }*/
            }
        }.execute(accept);
    }



}
