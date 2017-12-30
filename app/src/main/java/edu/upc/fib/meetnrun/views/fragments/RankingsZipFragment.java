package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;

import edu.upc.fib.meetnrun.R;

/**
 * Created by Javier on 19/12/2017.
 */

public class RankingsZipFragment extends Fragment {
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
//        page = getArguments().getInt("0", 0);
//        String title = getArguments().getString("User");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ranking_users, container, false);
        context = this.getActivity();
        initializePagination();
        zipSpinner = view.findViewById(R.id.rankingSpinner);
        setSpinner();
        rdbFilter = view.findViewById(R.id.rdGZip);
        rdbFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdbCity){
                    //TODO: Filter by city
                }else if (checkedId == R.id.rdbCountry){
                    //TODO: Filter by country or world
                }

            }

        });
        users=view.findViewById(R.id.button4);
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RankingsUserFragment.class);
                startActivity(intent);
            }
        });
        zips=view.findViewById(R.id.button5);
        zips.setClickable(false);
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
}
