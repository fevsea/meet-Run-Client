package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetAllFriends;
import edu.upc.fib.meetnrun.asynctasks.GetRankingsUser;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RankingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by Javier on 18/12/2017.
 */

public class RankingsUserFragment extends Fragment {
    private boolean isLoading;
    private boolean isLastPage;
    private int pageNumber;
    private int pageSize;
    private ProgressBar progressBar;
    private String title;
    private int page;
    RadioGroup rdbFilter;
    Spinner zipSpinner;
    View view;
    Context context;
    IUserAdapter userAdapter;
    User user;
    RankingsAdapter rankingAdapter;
    List<PositionUser> rankings;
    Integer zipnum;


    public static RankingsUserFragment newInstance(int page, String title) {
        RankingsUserFragment fragmentFirst = new RankingsUserFragment();
        Bundle args = new Bundle();
        args.putInt("0", page);
        args.putString("Info", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("0", 0);
        title = getArguments().getString("Info");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ranking_users, container, false);
        context = this.getActivity();
        initializePagination();
        zipSpinner = view.findViewById(R.id.rankingSpinner);
        setSpinner();
        progressBar = view.findViewById(R.id.pb_loading_ranking_users);
        rdbFilter = view.findViewById(R.id.rdGUser);
        rdbFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdbPostcode){
                    //TODO: Filter by postcode
                }else if (checkedId == R.id.rdbCity){
                    //TODO: Filter by city
                }else if (checkedId == R.id.rdbCountry || checkedId == R.id.rdbGlobal){
                    //TODO: Filter by country or world
                }

            }

        });
        setupRecyclerView();
        callGetRanking();
        return view;
    }

    private void setSpinner() {
        //TODO: catch stuff from server and put it on the spinner
    }


    protected void initializePagination() {
        pageNumber = 0;
        isLoading = false;
        isLastPage = false;
    }

    private void setupRecyclerView() {
        final RecyclerView rankingList = view.findViewById(R.id.ranking_rv);
        rankingList.setLayoutManager(new LinearLayoutManager(getActivity()));

        rankings = new ArrayList<>();
        rankingAdapter = new RankingsAdapter(rankings, new RecyclerViewOnClickListener() {

            @Override
            public void onButtonClicked(int position) {
            }

            @Override
            public void onItemClicked(int position) {
                //TODO abrir
            }
        },getContext(),false,zipnum);
        rankingList.setAdapter(rankingAdapter);

    }

    private void callGetRanking() {
        progressBar.setVisibility(View.VISIBLE);
        new GetRankingsUser(pageNumber, zipnum) {
            @Override
            public void onResponseReceived(List<PositionUser> rankingsResponse) {
                rankings = rankingsResponse;
                updateData();
            }


            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private void updateData() {
        if (rankings != null) {
            if (pageNumber == 0) {
                rankingAdapter.updateRanking(rankings);
                pageSize = rankings.size();
            }
            else {
                rankingAdapter.addRankings(rankings);
            }

            if (pageNumber != 0 && rankings.size() < pageSize) {
                isLastPage = true;
            }
            else pageNumber++;
        }
        isLoading = false;
        progressBar.setVisibility(View.INVISIBLE);
    }


    private void callGetAllFriends() {
        new GetAllFriends() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived(List<Friend> allfriends) {
                ArrayList<User> friends = new ArrayList<User>();
                Fragment frag=new UserProfileFragment();;
                Intent intent=new Intent();
                for (Friend f : allfriends) {
                    User friend = f.getFriend();
                    if (friend.equals(user)){
                        frag = new FriendProfileFragment();
                        BaseActivity.startWithFragment(getActivity(), frag, intent);

                    }
                    BaseActivity.startWithFragment(getActivity(), frag, intent);
                }
            }
        }.execute();
    }
}
