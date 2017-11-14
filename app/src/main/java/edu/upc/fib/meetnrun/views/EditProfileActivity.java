package edu.upc.fib.meetnrun.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.EditProfileFragment;
import edu.upc.fib.meetnrun.views.fragments.ProfileActivityFragment;

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
