package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.DeleteChat;
import edu.upc.fib.meetnrun.asynctasks.UpdateChat;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MessageAdapter;


/**
 * Created by eric on 21/11/17.
 */

public class ChatFragment extends BaseFragment {

    private View view;
    private FloatingActionButton fab;

    private RecyclerView rvMessages;
    private EditText txtMessage;
    private Button btnSend;

    private MessageAdapter adapter;
    private IChatAdapter chatDBAdapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //private DatabaseReference myRef;

    private Chat chat;
    private User currentUser;

    /*private int NUMB_MESSAGES_LOAD;
    private final int SUM_MESSAGES_LOAD = 15;
    private long MAX_MESSAGES_LOAD;

    private boolean first;

    private boolean swipe = false;
    private int itemPosition;*/

    private boolean firstTime;
    private int userPosition;

    private ChildEventListener childEventListener;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        CurrentSession cs = CurrentSession.getInstance();
        chat = cs.getChat();
        currentUser = cs.getCurrentUser();
        chatDBAdapter = cs.getChatAdapter();

        //first = true;

        for (int i = 0; i < chat.getListUsersChatSize(); i++) {
            if (currentUser.getUsername().equals(chat.getUserAtPosition(i).getUsername())) {
                Log.e(" INICIO xd"+currentUser.getUsername(), chat.getUserAtPosition(i).getUsername());
                userPosition = i;
                break;
            }
        }

        if (chat.getType() == 0) {
            if (!currentUser.getUsername().equals(chat.getUser1().getUsername())) chat.setChatName(chat.getUser1().getUsername());
            else chat.setChatName(chat.getUser2().getUsername());
        }

        getActivity().setTitle("");

        this.view = inflater.inflate(R.layout.fragment_chat, container, false);

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        /*NUMB_MESSAGES_LOAD = SUM_MESSAGES_LOAD;
        MAX_MESSAGES_LOAD = 0;*/

        rvMessages = view.findViewById(R.id.rvMensajes);
        txtMessage = view.findViewById(R.id.txtMensaje);
        btnSend = view.findViewById(R.id.btnEnviar);

        //txtMessage.setEnabled(false);
        //btnSend.setEnabled(false);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(String.valueOf(chat.getId())); //Chat name
        /*myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    if (snap.getKey().equals(String.valueOf(chat.getId()))) {
                        MAX_MESSAGES_LOAD = snap.getChildrenCount();
                    }
                }
                if (NUMB_MESSAGES_LOAD > MAX_MESSAGES_LOAD) NUMB_MESSAGES_LOAD = (int)MAX_MESSAGES_LOAD;
                changeNumbMessages();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                /*if (!first)*/ loadMessages(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        databaseReference.addChildEventListener(childEventListener);
        //databaseReference.limitToLast(NUMB_MESSAGES_LOAD).addChildEventListener(childEventListener);

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
                //MAX_MESSAGES_LOAD++;
                //NUMB_MESSAGES_LOAD++;
                txtMessage.setText("");
                chat.setMessage(m);
                callUpdateChat();

            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                /*if (!swipe) setScrollbar();
                else setScrollbarUp();
                if (swipe && adapter.getItemCount() == NUMB_MESSAGES_LOAD) {
                    swipe = false;
                }
                if (firstTime && adapter.getItemCount() == MAX_MESSAGES_LOAD) {
                    firstTime = false;
                }*/
                setScrollbar();
            }
        });

        if (chat.getMessage().getMessage().equals("")) removeProgressChat();

        /*final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_messages_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                itemPosition = NUMB_MESSAGES_LOAD;
                int resta = (int)MAX_MESSAGES_LOAD-NUMB_MESSAGES_LOAD;
                if (resta > SUM_MESSAGES_LOAD) NUMB_MESSAGES_LOAD += SUM_MESSAGES_LOAD;
                else if (resta == 0) swipe = false;
                else NUMB_MESSAGES_LOAD += resta;
                itemPosition = NUMB_MESSAGES_LOAD - itemPosition;
                if (resta != 0) {
                    adapter.deleteMessages();
                    swipe = true;
                    databaseReference.limitToLast(NUMB_MESSAGES_LOAD);
                    changeNumbMessages();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });*/

        return this.view;
    }

    /*private  void changeNumbMessages() {
        first = false;
        databaseReference.limitToLast(NUMB_MESSAGES_LOAD);
        synchronized (childEventListener){childEventListener.notify();}
    }*/

    private void loadMessages(DataSnapshot dataSnapshot) {

        //if (NUMB_MESSAGES_LOAD > 0) {

            if (firstTime) {
                removeProgressChat();
                firstTime = false;
            }
            Message m = dataSnapshot.getValue(Message.class);
            adapter.addMessage(m);
            if (/*!swipe &&*/ !firstTime) {

                int size = chat.getListUsersChatSize();
                Log.e(currentUser.getUsername(), m.getName());
                if (currentUser.getUsername().equals(m.getName())) {
                    Log.e("SOY YO", m.getName());
                    for (int i = 0; i < size; i++) {
                        if (!currentUser.getUsername().equals(chat.getUserAtPosition(i).getUsername())) {
                            chat.sumNumbMessagesAtPosition(i);
                        }
                    }
                }
                else {
                    chat.setNumbMessagesAtPosition(userPosition, 0);
                    Log.e("NO SOY YO", m.getName());
                }

                for (int i = 0; i < size; i++) {
                    Log.e(chat.getUserAtPosition(i).getUsername(), chat.getNumbMessagesAtPosition(i).toString());
                }

            }
       // }
    }

    private void setScrollbar() {
        rvMessages.scrollToPosition(adapter.getItemCount()-1);
    }

    /*private void setScrollbarUp() {
        rvMessages.scrollToPosition(itemPosition);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.chat_menu, menu);

        final String titleChat = getResources().getString(R.string.chat_delete_chat_title);
        final String messageChat = getResources().getString(R.string.chat_delete_chat_message);

        final String ok = getResources().getString(R.string.ok);
        final String cancel = getResources().getString(R.string.cancel);

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
                                adapter.deleteMessages();
                                rvMessages.setAdapter(adapter);
                                //TODO eliminar chat
                                //chat.getListUsersChat().remove(currentUser);
                                //new updateChat().execute();
                                getActivity().finish();
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

        Intent chatIntent;
        Fragment chatFragment;
        User user;
        int type = chat.getType();
        Log.e("AAA","PASSA");
        if (type == 0) {
            chatIntent = new Intent();
            chatFragment = new FriendProfileFragment();
            if (!currentUser.getUsername().equals(chat.getUser1().getUsername()))
                user = chat.getUser1();
            else user = chat.getUser2();
            CurrentSession.getInstance().setFriend(user);
        }
        //else if (type == 1) {
        else {
            chatIntent = new Intent();
            chatFragment = new ChatGroupInfoFragment();
            //chatIntent.putExtra("name",chat.getChatName());
        }
        BaseActivity.startWithFragment(getActivity(), chatFragment,chatIntent);
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

    @Override
    public void onResume() {
        firstTime = true;
        chat.setNumbMessagesAtPosition(userPosition, 0);
        super.onResume();
    }

    private void callUpdateChat() {
        new UpdateChat() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof ParamsException) {
                    Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived() {
                Log.d("ChatFragment","Chat updated");
            }
        }.execute(chat);
    }


    private void callDeleteChat(int chatId) {
        new DeleteChat() {
            @Override
            public void onExceptionReceived(GenericException e) {
                if (e instanceof AuthorizationException) {
                    Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof NotFoundException) {
                    Toast.makeText(getActivity(), R.string.not_found_error, Toast.LENGTH_LONG).show();
                }
                else if (e instanceof ParamsException) {
                    Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseReceived() {
                Log.d("ChatFragment","Chat deleted");
            }
        }.execute(chatId);
    }

    public int getTitle() {
        return R.string.chat_label;
    }

}
