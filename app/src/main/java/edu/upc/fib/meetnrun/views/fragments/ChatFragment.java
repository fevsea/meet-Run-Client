package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

    private int numbNewMessages = 0;

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

        //getActivity().setTitle(chat.getChatName());
        getActivity().setTitle("");

        this.view = inflater.inflate(R.layout.fragment_chat, container, false);

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        rvMessages = view.findViewById(R.id.rvMensajes);
        txtMessage = view.findViewById(R.id.txtMensaje);
        btnSend = view.findViewById(R.id.btnEnviar);

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

        if (chat.getMessage().getMessage().equals("")) {
            removeProgressChat();
        }

        loadMessages();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_messages_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*NUMB_MESSAGES += 15;
                adapter.deleteMessages();
                loadMessages();*/
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return this.view;
    }

    private void loadMessages() {

        databaseReference.limitToLast(50/*NUMB_MESSAGES*/).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                removeProgressChat();
                Message m = dataSnapshot.getValue(Message.class);
                adapter.addMensaje(m);

                if (!currentUser.getUsername().equals(m.getName())) {
                    numbNewMessages++;
                }
                else {
                    if (numbNewMessages > 0) numbNewMessages = 0;
                }
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

        final String titleHistorial = getResources().getString(R.string.chat_delete_historial_title);
        final String messageHistorial = getResources().getString(R.string.chat_delete_historial_message);

        final String titleChat = getResources().getString(R.string.chat_delete_chat_title);
        final String messageChat = getResources().getString(R.string.chat_delete_chat_message);

        final String ok = getResources().getString(R.string.ok);
        final String cancel = getResources().getString(R.string.cancel);

        MenuItem itemDeleteHidtorial = menu.findItem(R.id.delete_historial);
        itemDeleteHidtorial.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                showDialog(titleHistorial, messageHistorial, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.removeValue();
                                adapter.deleteMessages();
                                rvMessages.setAdapter(adapter);
                                Message m = chat.getMessage();
                                m.setMessage("");
                                m.setName("");
                                chat.setMessage(m);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                return true;
            }
        });

        Toolbar toolbar = getActivity().findViewById(R.id.activity_toolbar);
        TextView name = toolbar.findViewById(R.id.toolbar_title);
        name.setText(chat.getChatName());
        name.setVisibility(View.VISIBLE);

        TextView icon = toolbar.findViewById(R.id.toolbar_user_icon);
        char letter = chat.getChatName().charAt(0);
        String firstLetter = String.valueOf(letter);
        icon.setBackground(getColoredCircularShape((letter)));
        icon.setText(firstLetter.toUpperCase());
        icon.setVisibility(View.VISIBLE);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileView();
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileView();
            }
        });

        MenuItem itemDeleteChat = menu.findItem(R.id.delete_chat);
        itemDeleteChat.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                showDialog(titleChat, messageChat, ok, cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.removeValue();
                                adapter.deleteMessages();
                                rvMessages.setAdapter(adapter);
                                Message m = chat.getMessage();
                                m.setMessage("");
                                m.setName("");
                                chat.setMessage(m);
                                //TODO eliminar chat
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }



    private void openProfileView() {

        Intent friendProfileIntent = new Intent(getActivity(),FriendProfileActivity.class);
        User user = null;
        int type = chat.getType();
        if (type == 0) {
            if (!currentUser.getUsername().equals(chat.getUser1().getUsername()))
                user = chat.getUser1();
            else user = chat.getUser2();
        }
        else if (type == 1) {

        }
        CurrentSession.getInstance().setFriend(user);
        startActivity(friendProfileIntent);
    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }
    protected void showDialog(String title, String message, String okButtonText, String negativeButtonText, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okButtonText, ok);
        if (negativeButtonText != null && cancel != null)
            builder.setNegativeButton(negativeButtonText, cancel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void removeProgressChat() {
        (view.findViewById(R.id.loading_layout)).setVisibility(View.GONE);
    }


}
