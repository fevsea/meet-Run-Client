package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;

import edu.upc.fib.meetnrun.views.fragments.ChallengeFragment;

public class ChallengeActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new ChallengeFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
