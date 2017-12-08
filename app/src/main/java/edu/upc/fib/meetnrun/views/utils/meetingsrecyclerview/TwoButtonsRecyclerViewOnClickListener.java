package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

/**
 * Created by guillemcastro on 08/12/2017.
 */

public interface TwoButtonsRecyclerViewOnClickListener extends RecyclerViewOnClickListener {

    void onButtonAcceptClicked(int position);

    void onButtonRejectClicked(int position);

    void onButtonClicked(int position);

    void onItemClicked(int position);

}
