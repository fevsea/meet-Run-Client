package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;
import edu.upc.fib.meetnrun.views.fragments.CreateMeetingFragment;


/**
 * Created by Javier on 14/10/2017.
 */



public class CreateMeetingActivity extends BaseReturnActivity{


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected Fragment createFragment() {
        return new CreateMeetingFragment();
    }

}