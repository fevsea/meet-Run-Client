package edu.upc.fib.meetnrun.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;

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

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("0", 0);
        String title = getArguments().getString("User");
    }

    protected void initializePagination() {
        pageNumber = 0;
        isLoading = false;
        isLastPage = false;
    }

}
