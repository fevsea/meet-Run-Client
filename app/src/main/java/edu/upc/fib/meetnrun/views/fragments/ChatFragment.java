package edu.upc.fib.meetnrun.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.FriendProfileActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MessageAdapter;


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
    private User currentUser;

    private int NUMB_MESSAGES = 15;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        chat = CurrentSession.getInstance().getChat();
        currentUser = CurrentSession.getInstance().getCurrentUser();

        if (chat.getType() == 0) {
            if (!currentUser.getUsername().equals(chat.getUser1().getUsername())) chat.setChatName(chat.getUser1().getUsername());
            else chat.setChatName(chat.getUser2().getUsername());
        }

        getActivity().setTitle(chat.getChatName());

        this.view = inflater.inflate(R.layout.fragment_chat, container, false);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        rvMessages = (RecyclerView) view.findViewById(R.id.rvMensajes);
        txtMessage = (EditText) view.findViewById(R.id.txtMensaje);
        btnSend = (Button) view.findViewById(R.id.btnEnviar);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(String.valueOf(chat.getId())); //Chat name

        adapter = new MessageAdapter(getContext());
        LinearLayoutManager l = new LinearLayoutManager(getContext());
        rvMessages.setLayoutManager(l);
        rvMessages.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userName = currentUser.getUsername();

                Calendar cal = Calendar.getInstance();

                Date dateWithoutTime = cal.getTime();

                String txt = txtMessage.getText().toString();
                Message m = new Message(txt, userName, dateWithoutTime);
                databaseReference.push().setValue(m);
                txtMessage.setText("");
                chat.setMessage(m);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        loadMessages();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_messages_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMessages();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return this.view;
    }

    private void loadMessages() {

        databaseReference.limitToLast(NUMB_MESSAGES).addChildEventListener(new ChildEventListener() {
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
    }

    private void setScrollbar() {
        rvMessages.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.chat_menu, menu);
        MenuItem item = menu.findItem(R.id.delete_historial);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                databaseReference.removeValue();
                adapter.deleteMessages();
                rvMessages.setAdapter(adapter);
                Message m = chat.getMessage();
                m.setMessage("");
                m.setName("");
                chat.setMessage(m);
                return true;
            }
        });
        /*inflater.inflate(R.menu.chat_menu, menu);
        MenuItem item = menu.findItem(R.id.chat_menu);
        item.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (chat.getType() == 0) {
                    Intent friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);
                    CurrentSession.getInstance().setFriend(chat.getUser2());
                    startActivity(friendProfileIntent);
                } else {

                }
                return true;
            }
        });*/

        super.onCreateOptionsMenu(menu, inflater);
    }


}
