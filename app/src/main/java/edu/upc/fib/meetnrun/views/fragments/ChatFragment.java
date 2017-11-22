package edu.upc.fib.meetnrun.views.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MessageAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.RecyclerViewOnClickListener;


/**
 * Created by eric on 21/11/17.
 */

public class ChatFragment extends Fragment {

    private View view;
    private FloatingActionButton fab;

    private RecyclerView rvMessages;
    private EditText txtMessage;
    private Button btnSend;

    private MessageAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private Chat chat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        chat = CurrentSession.getInstance().getChat();

        getActivity().setTitle(chat.getFriendUsername());

        this.view = inflater.inflate(R.layout.fragment_chat, container, false);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        rvMessages = (RecyclerView) view.findViewById(R.id.rvMensajes);
        txtMessage = (EditText) view.findViewById(R.id.txtMensaje);
        btnSend = (Button) view.findViewById(R.id.btnEnviar);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(chat.getChat()); //Chat name

        adapter = new MessageAdapter(getContext());
        LinearLayoutManager l = new LinearLayoutManager(getContext());
        rvMessages.setLayoutManager(l);
        rvMessages.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userName = CurrentSession.getInstance().getCurrentUser().getUsername();
                Calendar rightNow = Calendar.getInstance();
                StringBuilder sb = new StringBuilder();
                String hour = String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
                String minute = String.valueOf(rightNow.get(Calendar.MINUTE));
                sb.append(hour);
                sb.append(":");
                sb.append(minute);
                String txt = txtMessage.getText().toString();
                databaseReference.push().setValue(new Message(txt, userName, sb.toString()));
                txtMessage.setText("");
                chat.setLast_hour(sb.toString());
                chat.setLast_converse(txt);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message m = dataSnapshot.getValue(Message.class);
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return this.view;
    }

    private void setScrollbar() {
        rvMessages.scrollToPosition(adapter.getItemCount()-1);
    }


}
