package edu.upc.fib.meetnrun.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.ChallengeFragment;

public class ChallengeActivity extends BaseReturnActivity {

    protected void onCreate(Bundle savedInstanceContext) {
        super.onCreate(savedInstanceContext);
        FloatingActionButton fab = findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);
    }

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
