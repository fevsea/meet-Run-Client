package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Challenge;

/**
 * Created by guillemcastro on 26/11/2017.
 */

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesViewHolder>  {

    private List<Challenge> challenges;
    private RecyclerViewOnClickListener listener;

    public ChallengesAdapter(List<Challenge> challenges, RecyclerViewOnClickListener listener) {
        this.challenges = challenges;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public ChallengesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.challenge_item, parent, false);
        return new ChallengesViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ChallengesViewHolder holder, int position) {
        Challenge challenge = challenges.get(position);
        holder.bindChallenge(challenge);
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public void updateChallengeList(List<Challenge> challenges) {
        this.challenges = challenges;
        notifyDataSetChanged();
    }

    public Challenge getChallengeAt(int position) {
        return challenges.get(position);
    }
}
