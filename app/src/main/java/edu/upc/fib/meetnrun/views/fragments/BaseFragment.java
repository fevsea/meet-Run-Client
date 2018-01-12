package edu.upc.fib.meetnrun.views.fragments;

import android.support.v4.app.Fragment;
import android.view.Menu;

import edu.upc.fib.meetnrun.R;

/**
 * Created by guillemcastro on 20/12/2017.
 */

public class BaseFragment extends Fragment {

    public void onBackPressed() {}

    public int getTitle() {
        return R.string.app_name;
    }


}