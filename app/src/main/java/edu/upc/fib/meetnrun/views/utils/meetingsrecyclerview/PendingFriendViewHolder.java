package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.User;


public class PendingFriendViewHolder extends UsersViewHolder {

    public PendingFriendViewHolder(View itemView, RecyclerViewOnClickListener listener, Context context) {
        super(itemView, listener, context);
    }

    @Override
    public void bindMeeting(User user) {
        super.bindMeeting(user);

        ImageButton accept = view.findViewById(R.id.accept);
        accept.setOnClickListener(this);

        ImageButton reject = view.findViewById(R.id.reject);
        reject.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.accept: {
                ((TwoButtonsRecyclerViewOnClickListener)(listener.get())).onButtonAcceptClicked(getAdapterPosition());
                break;
            }
            case R.id.reject: {
                ((TwoButtonsRecyclerViewOnClickListener)(listener.get())).onButtonRejectClicked(getAdapterPosition());
                break;
            }
            default: {
                super.onClick(view);
            }

        }
    }

}
