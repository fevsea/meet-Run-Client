package edu.upc.fib.meetnrun.views;


import android.support.v4.app.Fragment;

import edu.upc.fib.meetnrun.views.fragments.ChangePasswordFragment;


public class ChangePasswordActivity extends BaseReturnActivity {

    @Override
    protected Fragment createFragment() {
        return new ChangePasswordFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
