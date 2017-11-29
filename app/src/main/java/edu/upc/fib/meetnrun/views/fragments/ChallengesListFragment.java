package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChallengeActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.ChallengesAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Challenge> challenges;
    private ChallengesAdapter challengesAdapter;

    public ChallengesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.challenges);
        View view =  inflater.inflate(R.layout.fragment_challenges_list, container, false);
        recyclerView = view.findViewById(R.id.fragment_challenge_recycler);
        swipeRefreshLayout = view.findViewById(R.id.fragment_challenge_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        setupRecyclerView();

        FloatingActionButton fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        challenges = new ArrayList<>();
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
    }

    private void updateChallengesList() {
        new GetChallenges().execute();
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

    private class GetChallenges extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            User current = CurrentSession.getInstance().getCurrentUser();
            User other = new User(56, "aUsername", "Name", "Surname", "08001", "A?", 0);
            Date deadline = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(deadline);
            c.add(Calendar.DATE, 6);
            deadline = c.getTime();
            challenges = new ArrayList<>();
            challenges.add(new Challenge(0, current, other, 50, deadline.toString(), new Date().toString(), 25, 32));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            challengesAdapter.updateChallengeList(challenges);
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(s);
        }
    }
}
