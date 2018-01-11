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
import edu.upc.fib.meetnrun.asynctasks.GetRankingsUser;
import edu.upc.fib.meetnrun.asynctasks.GetRankingsZip;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Position;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.models.RankingUser;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RankingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;

/**
 * Created by Javier on 19/12/2017.
 */

public class RankingsZipFragment extends Fragment  {
    private ProgressBar progressBar;
    private String title;
    private int page;
    Spinner zipSpinner;
    View view;
    Context context;
    List<PositionUser> rankings;
    RankingsAdapter rankingAdapter;
    Integer zipnum;


    public static RankingsZipFragment newInstance(int page, String title) {
        RankingsZipFragment fragmentFirst = new RankingsZipFragment();
        Bundle args = new Bundle();
        args.putInt("1", page);
        args.putString("Zip", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("1", 1);
        title = getArguments().getString("Zip");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ranking_zips, container, false);
        context = this.getActivity();
        progressBar = view.findViewById(R.id.pb_loading_ranking_users);
        setupRecyclerView();
        callGetRanking();
        return view;
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
                Intent i=new Intent(getContext(), RankingsUserFragment.class);
                Position p=rankingAdapter.getPosition(position);
                int zip=p.getZip();
                i.putExtra("zip",zip);
                startActivity(i);
            }
        },getContext(),true,zipnum);
        rankingList.setAdapter(rankingAdapter);
    }


    private void updateData() {
        rankingAdapter.updateRanking(rankings);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void callGetRanking() {
        progressBar.setVisibility(View.VISIBLE);
        new GetRankingsZip() {
            @Override
            public void onResponseReceived(List<Position> rankingsResponse) {
                toPositionUser(rankingsResponse);
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

    private void toPositionUser(List<Position> positions) {
        rankings = new ArrayList<>();
        for (Position p : positions) {
            PositionUser positionUser = new PositionUser(p.getZip(),p.getDistance(),"","","");
            rankings.add(positionUser);
        }
    }

    @Override
    public void onResume() {
        callGetRanking();
        super.onResume();
    }

}
