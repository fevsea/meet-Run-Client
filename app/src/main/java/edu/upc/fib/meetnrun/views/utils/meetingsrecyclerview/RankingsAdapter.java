package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Position;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.models.RankingUser;
import edu.upc.fib.meetnrun.models.RankingZip;

/**
 * Created by Javier on 15/12/2017.
 */

public class RankingsAdapter extends RecyclerView.Adapter<RankingsViewHolder>{
    private List<PositionUser> rankingList;
    private final RecyclerViewOnClickListener listener;
    private View v;
    private Context context;
    private boolean zip;
    private Integer zipnum;

    public RankingsAdapter(List<PositionUser> rankinglist, RecyclerViewOnClickListener listener, Context context, boolean zip, Integer zipnum) {
        this.rankingList = rankinglist;
        this.listener = listener;
        this.context = context;
        this.zip = zip;
        this.zipnum = zipnum;
    }

    public RankingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (zip) {
            v = layoutInflater.inflate(R.layout.zip_item_rankings, parent, false);
            return new RankingsViewHolder(v,listener);
        }
        else {
            v = layoutInflater.inflate(R.layout.user_item_rankings, parent, false);
            return new RankingsViewHolder(v,listener);
        }
    }

    @Override
    public void onBindViewHolder(final RankingsViewHolder holder, int position) {
        if (zip) {
            Position zipPosition = rankingList.get(position);
            holder.bindZipRanking(zipPosition,position);
        }
        else {
            PositionUser userPosition = rankingList.get(position);
            holder.bindUserRanking(userPosition,position);
        }
    }

    public void updateRanking(List<PositionUser> rankings) {
        this.rankingList = rankings;
        notifyDataSetChanged();
    }

    public void addRankings(List<PositionUser> rankings) {
        this.rankingList.addAll(rankings);
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return rankingList.size();
    }

    public List<PositionUser> getRankingList() {
        return rankingList;
    }
}
