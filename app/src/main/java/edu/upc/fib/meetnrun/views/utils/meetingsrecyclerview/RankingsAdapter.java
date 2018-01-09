package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Position;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.models.RankingGeneric;
import edu.upc.fib.meetnrun.models.RankingUser;
import edu.upc.fib.meetnrun.models.RankingZip;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Javier on 15/12/2017.
 */

public class RankingsAdapter extends RecyclerView.Adapter<RankingsViewHolder>{
    private RankingUser rankingUserList;
    private RankingZip rankingZipList;
    private final RecyclerViewOnClickListener listener;
    private View v;
    private Context context;
    private boolean zip;
    private Integer zipnum;

    public RankingsAdapter(RankingGeneric rankinglist, RecyclerViewOnClickListener listener, Context context, boolean zip, Integer zipnum) {
        if (zip) this.rankingZipList = (RankingZip)rankinglist;
        else this.rankingUserList = (RankingUser) rankinglist;
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
            Position zipPosition = rankingZipList.getGlobalRanking().get(position);
            holder.bindZipRanking(zipPosition);
        }
        else {
            PositionUser userPosition = null;
            if (zipnum == null) userPosition = rankingUserList.getRanking().get(position);
            else userPosition = rankingUserList.getRankingByZip(zipnum).get(position);
            holder.bindUserRanking(userPosition);
        }
    }

    public int getItemCount() {
        if (!zip) {
            if (zipnum == null) return rankingUserList.getRanking().size();
            else return rankingUserList.getRankingByZip(zipnum).size();
        }
        else {
            return rankingZipList.getGlobalRanking().size();
        }
    }
}
