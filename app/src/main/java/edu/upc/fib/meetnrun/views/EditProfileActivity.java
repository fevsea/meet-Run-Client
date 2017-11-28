package edu.upc.fib.meetnrun.views;

import android.support.v4.app.Fragment;

import edu.upc.fib.meetnrun.views.fragments.EditProfileFragment;

public class EditProfileActivity extends BaseReturnActivity {


    @Override
    protected Fragment createFragment() {
        return new EditProfileFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
