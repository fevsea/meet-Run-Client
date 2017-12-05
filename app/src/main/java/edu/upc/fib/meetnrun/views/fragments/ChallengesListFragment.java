package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.ChallengeActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.ChallengesAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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
        View view =  inflater.inflate(R.layout.fragment_challenges_list, container, false);
        recyclerView = view.findViewById(R.id.fragment_challenge_recycler);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_fab) {
            Fragment friendsListFragment = new FriendsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_contentFrame, friendsListFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private class GetChallenges extends AsyncTask<String,String,Boolean> {

        private Exception ex;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                challenges = CurrentSession.getInstance().getChallengeAdapter().getCurrentUserChallenges();
            }
            catch (AutorizationException e) {
                this.ex = e;
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if (s && ex == null) {
                challengesAdapter.updateChallengeList(challenges);
                swipeRefreshLayout.setRefreshing(false);
            }
            else if (ex instanceof AutorizationException){
                Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(), R.string.error_loading, Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }
}
