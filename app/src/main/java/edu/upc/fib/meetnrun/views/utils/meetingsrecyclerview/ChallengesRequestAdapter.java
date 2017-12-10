package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;

/**
 * Created by guillemcastro on 08/12/2017.
 */

public class ChallengesRequestAdapter extends ChallengesAdapter {

    public ChallengesRequestAdapter(List<Challenge> challenges, TwoButtonsRecyclerViewOnClickListener listener) {
        super(challenges, listener);
    }

    @Override
    public ChallengesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.challenge_request_item, parent, false);
        return new ChallengesRequestViewHolder(view, (TwoButtonsRecyclerViewOnClickListener)listener);
    }

}
