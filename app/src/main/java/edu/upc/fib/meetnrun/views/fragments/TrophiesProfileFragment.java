package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.Trophies;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.TrophiesAdapter;


public class TrophiesProfileFragment extends Fragment {

    private String title;
    private int page;
    private View view;
    private TrophiesAdapter adapter;
    private LinearLayoutManager layoutManager;

    // newInstance constructor for creating fragment with arguments
    public static TrophiesProfileFragment newInstance(int page, String title) {
        TrophiesProfileFragment fragmentFirst = new TrophiesProfileFragment();
        Bundle args = new Bundle();
        args.putInt("3", page);
        args.putString("trophies", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("3", 3);
        title = getArguments().getString("trophies");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trophies_profile, container, false);
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.trophiegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Trophies> createLists = prepareData();
        adapter = new TrophiesAdapter(createLists, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {}

            @Override
            public void onItemClicked(int position) {
                Log.e("ONCLICK", String.valueOf(position)); //TODO ni entra ???
                Trophies trophie = adapter.getTrophieAtPosition(position);
                Toast.makeText(view.getContext(), "Showing selected trophie info", Toast.LENGTH_SHORT).show();
                Intent trophieInfoIntent = new Intent();
                trophieInfoIntent.putExtra("image",trophie.getImage_ID());
                trophieInfoIntent.putExtra("title",trophie.getImage_title());
                trophieInfoIntent.putExtra("description",trophie.getImage_description());
                trophieInfoIntent.putExtra("obtained",trophie.getImage_isObtained());
                BaseActivity.startWithFragment(getActivity(), new TrophieInfoFragment(), trophieInfoIntent);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Trophies> prepareData(){

        ArrayList<Trophies> theimage = new ArrayList<>();
        for(int i = 0; i< image_ids.length; i++){
            Trophies trophies = new Trophies();
            trophies.setImage_ID(image_ids[i]);
            trophies.setImage_title(image_title[i]);
            trophies.setImage_isObtained(image_obtained[i]);
            theimage.add(trophies);
        }
        return theimage;
    }

    private final Integer image_ids[] = {
            R.drawable.challenges1,
            R.drawable.challenges5,
            R.drawable.distance1km,
            R.drawable.distance10km,
            R.drawable.friends1,
            R.drawable.friends5,
            R.drawable.level1,
            R.drawable.level5,
            R.drawable.meeting_distance1km,
            R.drawable.meeting_distance5km,
            R.drawable.meetings1,
            R.drawable.meetings5,
            R.drawable.steps10000,
            R.drawable.steps20000,
            R.drawable.time1h,
            R.drawable.time10h,
    };


    private final String image_title[] = {
            "Win 1 challenge",
            "Win 5 challenges",
            "Total distance of 1km ran",
            "Total distance of 5km ran",
            "1 friend made",
            "5 friends made",
            "Level 1 accomplished",
            "Level 5 accomplished",
            "Meeting with a distance of 1km ran",
            "Meeting with a distance of 5km ran",
            "First meeting completed",
            "5 meetings completed",
            "Total of 10000 steeps accomplished",
            "Total of 20000 steps accomplished",
            "Total of 1h ran",
            "Total of 10h ran",
    };

    private final String image_obtained[] = {
            "True",
            "False",
            "True",
            "False",
            "True",
            "False",
            "True",
            "False",
            "True",
            "False",
            "True",
            "False",
            "True",
            "False",
            "True",
            "False",

    };

    /*private final Integer image_ids[] = {
            R.drawable.challenges1,
            R.drawable.challenges5,
            R.drawable.challenges10,
            R.drawable.challenges20,
            R.drawable.distance1km,
            R.drawable.distance10km,
            R.drawable.distance100km,
            R.drawable.distance1000km,
            R.drawable.friends1,
            R.drawable.friends5,
            R.drawable.friends10,
            R.drawable.friends20,
            R.drawable.level1,
            R.drawable.level5,
            R.drawable.level10,
            R.drawable.level25,
            R.drawable.level40,
            R.drawable.level50,
            R.drawable.meeting_distance1km,
            R.drawable.meeting_distance5km,
            R.drawable.meeting_distance10km,
            R.drawable.meeting_distance21km,
            R.drawable.meeting_distance42km,
            R.drawable.meetings1,
            R.drawable.meetings5,
            R.drawable.meetings10,
            R.drawable.meetings20,
            R.drawable.meetings50,
            R.drawable.steps10000,
            R.drawable.steps20000,
            R.drawable.steps25000,
            R.drawable.steps100000,
            R.drawable.time1h,
            R.drawable.time10h,
            R.drawable.time100h,
            R.drawable.time1000h,
    };*/
}
