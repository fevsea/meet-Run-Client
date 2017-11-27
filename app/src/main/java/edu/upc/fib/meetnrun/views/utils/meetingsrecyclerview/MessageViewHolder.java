package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;

/**
 * Created by eric on 21/11/17.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView message;
    private TextView hour;
    private TextView date;
    private TextView photo;

    public MessageViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.messageName);
        message = (TextView) itemView.findViewById(R.id.messageTxt);
        hour = (TextView) itemView.findViewById(R.id.messageHour);
        date = (TextView) itemView.findViewById(R.id.messageDate);
        photo = (TextView) itemView.findViewById(R.id.messagePhoto);

    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public TextView getHour() {
        return hour;
    }

    public void setHour(TextView hour) {
        this.hour = hour;
    }

    public TextView getPhoto() {
        return photo;
    }

    public void setPhoto(TextView photo) {
        this.photo = photo;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }
}
