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
import edu.upc.fib.meetnrun.asynctasks.GetTrophies;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Trophie;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.TrophiesAdapter;


public class TrophiesProfileFragment extends Fragment {

    private String title;
    private int page;
    private int id;
    private View view;
    private TrophiesAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Trophie> trophiesObtained;
    private ArrayList<Trophie> createLists;

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
        id = CurrentSession.getInstance().getCurrentUser().getId();
        getTrophiesList();

        return view;
    }

    private void setupRecyclerView() {

        RecyclerView recyclerView = view.findViewById(R.id.trophiegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TrophiesAdapter(createLists, new RecyclerViewOnClickListener() {
            @Override
            public void onButtonClicked(int position) {
            }

            @Override
            public void onItemClicked(int position) {
                Trophie trophie = adapter.getTrophieAtPosition(position);
                Toast.makeText(view.getContext(), "Showing selected trophie info", Toast.LENGTH_SHORT).show();
                Intent trophieInfoIntent = new Intent();
                trophieInfoIntent.putExtra("image", trophie.getImage());
                trophieInfoIntent.putExtra("title", trophie.getTrophieTitle());
                trophieInfoIntent.putExtra("description", trophie.getTrophieDescription());
                trophieInfoIntent.putExtra("obtained", trophie.getTrophieIsObtained());
                BaseActivity.startWithFragment(getActivity(), new TrophieInfoFragment(), trophieInfoIntent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Trophie> prepareData() {

        ArrayList<Trophie> theimage = new ArrayList<>();

        for (int i = 0; i < image_Obtained.length; i++) {
            if (i != 4 && i != 28) {
                Trophie trophie = new Trophie();
                trophie.setTrophieTitle(trophie_title[i]);
                trophie.setTrophieDescription(trophie_description[i]);
                trophie.setTrophieIsObtained(trophiesObtained.get(i).getTrophieIsObtained());
                trophie.setImage_Obtained(image_Obtained[i]);
                trophie.setImage_NotObtained(image_NotObtained[i]);
                theimage.add(trophie);
            }
        }
        return theimage;
    }

    private void getTrophiesList() {
        callGetAllTrophies(id);
    }

    private void callGetAllTrophies(int id) {
        new GetTrophies() {

            @Override
            public void onResponseReceived(List<Trophie> trophies) {
                trophiesObtained = trophies;
                createLists = prepareData();
                setupRecyclerView();
            }

        }.execute(id);

    }

    private final Integer image_Obtained[] = {
            R.drawable.distance1km,
            R.drawable.distance10km,
            R.drawable.distance100km,
            R.drawable.distance1000km,
            R.drawable.distance1000km,
            R.drawable.time1h,
            R.drawable.time10h,
            R.drawable.time100h,
            R.drawable.time1000h,
            R.drawable.meetings1,
            R.drawable.meetings5,
            R.drawable.meetings10,
            R.drawable.meetings20,
            R.drawable.meetings50,
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
            R.drawable.steps10000,
            R.drawable.steps20000,
            R.drawable.steps25000,
            R.drawable.steps25000,
            R.drawable.steps100000,
            R.drawable.challenges1,
            R.drawable.challenges5,
            R.drawable.challenges10,
            R.drawable.challenges20,
            R.drawable.friends1,
            R.drawable.friends5,
            R.drawable.friends10,
            R.drawable.friends20,
    };

    private final Integer image_NotObtained[] = {
            R.drawable.not_get_distance1km,
            R.drawable.not_get_distance10km,
            R.drawable.not_get_distance100km,
            R.drawable.not_get_distance1000km,
            R.drawable.not_get_distance1000km,
            R.drawable.not_get_time1h,
            R.drawable.not_get_time10h,
            R.drawable.not_get_time100h,
            R.drawable.not_get_time1000h,
            R.drawable.not_get_meetings1,
            R.drawable.not_get_meetings5,
            R.drawable.not_get_meetings10,
            R.drawable.not_get_meetings20,
            R.drawable.not_get_meetings50,
            R.drawable.not_get_level1,
            R.drawable.not_get_level5,
            R.drawable.not_get_level10,
            R.drawable.not_get_level25,
            R.drawable.not_get_level40,
            R.drawable.not_get_level50,
            R.drawable.not_get_meeting_distance1km,
            R.drawable.not_get_meeting_distance5km,
            R.drawable.not_get_meeting_distance10km,
            R.drawable.not_get_meeting_distance21km,
            R.drawable.not_get_meeting_distance42km,
            R.drawable.not_get_steps10000,
            R.drawable.not_get_steps20000,
            R.drawable.not_get_steps25000,
            R.drawable.not_get_steps25000,
            R.drawable.not_get_steps100000,
            R.drawable.not_get_challenges1,
            R.drawable.not_get_challenges5,
            R.drawable.not_get_challenges10,
            R.drawable.not_get_challenges20,
            R.drawable.not_get_friends1,
            R.drawable.not_get_friends5,
            R.drawable.not_get_friends10,
            R.drawable.not_get_friends20,
    };

    private final String trophie_title[] = { //TODO afegir a strings
            "Your first km!",
            "Warming up",
            "Totally into it",
            "On fire!",
            "x",
            "Your first hour",
            "Ten & going forward",
            "Centenary",
            "More hours spend than a clock",
            "Just trying a meeting...",
            "Meetings are not bad",
            "Meetings are pretty good",
            "Meetings are awesome!",
            "Meeting-addicted!",
            "Just started!",
            "Getting accustomed",
            "Junior",
            "Senior",
            "Semi-professional",
            "Professional",
            "Easy beginning",
            "Medium-distance Meeting",
            "Long-distance Meeting",
            "Half Marathon Meeting",
            "Marathon Meeting",
            "First steps",
            "A few steps",
            "Some steps",
            "x",
            "A lot of steps!",
            "Your first victory",
            "Winner",
            "Champion",
            "Legend",
            "Your first Friend",
            "Being popular",
            "Well-Known",
            "Famous",
    };

    private final String trophie_description[] = { //TODO afegir a strings
            "Total distance of 1km done",
            "Total distance of 10km done",
            "Total distance of 100km done",
            "Total distance of 1000km done",
            "x",
            "Total of 1h running",
            "Total of 10h running",
            "Total of 100h running",
            "Total of 1000h running",
            "First meeting completed",
            "5 meetings completed",
            "10 meetings completed",
            "20 meetings completed",
            "50 meetings completed",
            "Level 1 accomplished",
            "Level 5 accomplished",
            "Level 10 accomplished",
            "Level 25 accomplished",
            "Level 40 accomplished",
            "Level 50 accomplished",
            "Meeting with a distance of 1km completed",
            "Meeting with a distance of 5km completed",
            "Meeting with a distance of 10km completed",
            "Meeting with a distance of 21km completed",
            "Meeting with a distance of 42km completed",
            "Total of 10000 steeps accomplished",
            "Total of 20000 steps accomplished",
            "Total of 25000 steeps accomplished",
            "x",
            "Total of 100000 steps accomplished",
            "Win 1 challenge",
            "Win 5 challenges",
            "Win 10 challenges",
            "Win 20 challenges",
            "1 friend made",
            "5 friends made",
            "10 friends made",
            "20 friends made",
    };

}
