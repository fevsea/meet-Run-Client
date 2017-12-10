package edu.upc.fib.meetnrun.views;

import android.app.Activity;
import android.support.v4.app.Fragment;

import edu.upc.fib.meetnrun.views.fragments.MeetingFriendsFragment;

/**
 * Created by Javier on 09/11/2017.
 */

public class MeetingFriendsActivity extends BaseReturnActivity{


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected Fragment createFragment() {
        return new MeetingFriendsFragment();
    }

}
