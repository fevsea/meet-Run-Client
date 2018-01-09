package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.IRankingAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetAllFriends;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.ProfileViewPagerFragment;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RankingsUserAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by Javier on 18/12/2017.
 */

public class RankingsUserFragment extends Fragment {
    private boolean isLoading;
    private boolean isLastPage;
    private int pageNumber;
    private ProgressBar progressBar;
    private String title;
    private int page;
    RadioGroup rdbFilter;
    Spinner zipSpinner;
    View view;
    Context context;
    Button users;
    Button zips;
    IUserAdapter userAdapter;
    User user;
    RankingsUserAdapter rankingsUserAdapter;

    public static RankingsUserFragment newInstance(int page, String title) {
        RankingsUserFragment fragmentFirst = new RankingsUserFragment();
        Bundle args = new Bundle();
        Log.d("User rankings","===========================================================");
        args.putInt("0", page);
        args.putString("Info", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("User rankings","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        super.onCreate(savedInstanceState);
/*        title = getArguments().getString("Info");
        page = getArguments().getInt("0", 0); */

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ranking_users, container, false);
        context = this.getActivity();
        initializePagination();
        zipSpinner = view.findViewById(R.id.rankingSpinner);
        List<PositionUser> positionUsers = new ArrayList<>();
        rankingsUserAdapter=new RankingsUserAdapter(positionUsers,new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {
            }

            public void onItemClicked(int position) {
                User user = rankingsUserAdapter.getUserAtPosition(position);
            }
        }, getContext());
        setSpinner();
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
        zips=view.findViewById(R.id.button3);
        zips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                Fragment frag=new RankingsZipFragment();
                BaseActivity.startWithFragment(getActivity(), frag, intent);
            }
        });
        users=view.findViewById(R.id.button2);
        users.setClickable(false);
        return view;
    }

    private void setSpinner() {
        //TODO: catch stuff from server and put it on the spinner
        List<String> zips=rankingAdapter.
        ArrayAdapter<String> zipsArrayAdapter= new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                zips);
        zipSpinner.setAdapter(zipsArrayAdapter);
    }


    protected void initializePagination() {
        pageNumber = 0;
        isLoading = false;
        isLastPage = false;
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



    protected RecyclerViewOnClickListener getRecyclerViewListener() {
        return new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {

                user = userAdapter.getUser(position);
                if (user.equals(CurrentSession.getInstance().getCurrentUser())){
                    Intent i = new Intent(getActivity(),ProfileViewPagerFragment.class);
                    startActivity(i);
                }
                else{
                    callGetAllFriends();
                }

            }
        };
    }
}
