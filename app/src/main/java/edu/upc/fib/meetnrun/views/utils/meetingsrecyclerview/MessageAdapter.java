package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Message;

/**
 * Created by eric on 21/11/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {


    private List<Message> listaMensaje = new ArrayList<>();
    private Context c;

    public MessageAdapter(Context c) {
        this.c = c;
    }

    public void addMensaje(Message m) {
        listaMensaje.add(m);
        notifyItemInserted(listaMensaje.size());
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.getNombre().setText(listaMensaje.get(position).getNombre());
        holder.getMensaje().setText(listaMensaje.get(position).getMensaje());
        holder.getHora().setText(listaMensaje.get(position).getHora());
        holder.getFotoMensaje().setText(listaMensaje.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return listaMensaje.size();
    }

}
