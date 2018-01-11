package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IRankingAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetAllFriends;
import edu.upc.fib.meetnrun.asynctasks.GetRankingsUser;
import edu.upc.fib.meetnrun.asynctasks.GetRankingsUserAllZips;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.models.RankingUser;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RankingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

import static android.content.Intent.getIntent;

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
    User user;
    List<Friend> myFriends;

    RankingsAdapter rankingAdapter;
    IRankingAdapter iRankingAdapter;
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
        callGetAllFriends();
        initializePagination();

        progressBar = view.findViewById(R.id.pb_loading_ranking_users);


        setupRecyclerView();
        callGetRanking();
        setSpinner();
        return view;
    }

    private void setSpinner() {
        new GetRankingsUserAllZips() {
            @Override
            public void onResponseReceived(List<String> rankings) {
                List<String> zips=rankings;
                ArrayAdapter<String> zipsArrayAdapter= new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        zips);
                zipSpinner.setAdapter(zipsArrayAdapter);
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
            @Override
            protected Void doInBackground(List<String>... lists) {
                return null;
            }
        };
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
                PositionUser userPosition=rankingAdapter.getPosition(position);
                String userId=userPosition.getUserID();
                Log.d("fragment", "inside itemClicked");
                Log.d("fragment",userId+" "+CurrentSession.getInstance().getCurrentUser().getUsername());
                if (userId.equals(CurrentSession.getInstance().getCurrentUser().getUsername())){
                    Log.d("fragment", "is me");
                    Intent intent=new Intent(getActivity(),ProfileViewPagerFragment.class);
                    intent.putExtra("userId",CurrentSession.getInstance().getCurrentUser().getId());
                    intent.putExtra("isFriend",false);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(getActivity(),ProfileViewPagerFragment.class);
                    boolean isFriend=false;
                    for (Friend f : myFriends) {
                        String friendUsername = f.getUser().getUsername();
                        if (friendUsername.equals(userId)){
                            isFriend=true;
                            intent.putExtra("userId",f.getUser().getId());
                            intent.putExtra("isFriend",true);
                            startActivity(intent);
                            break;
                        }
                    }
                    if(!isFriend) {
                        intent.putExtra("userId", userPosition.getId());
                        intent.putExtra("isFriend", false);
                        startActivity(intent);
                    }

                }
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
                myFriends=allfriends;
            }
        }.execute();
    }
}
