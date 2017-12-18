package edu.upc.fib.meetnrun.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.ChallengeFragment;
package edu.upc.fib.meetnrun.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.views.fragments.ChallengeFragment;
/**
 * Created by Javier on 18/12/2017.
 */



public class RankingsZonesActivity extends BaseReturnActivity {

    protected void onCreate(Bundle savedInstanceContext) {
        super.onCreate(savedInstanceContext);
        FloatingActionButton fab = findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Fragment createFragment() {
        return new RankingsUserFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}