package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

import static android.R.layout.simple_list_item_activated_2;


/**
 * Created by Javier on 08/11/2017.
 */

public class MeetingFriendsFragment extends Fragment {

    private View view;
    private int level;
    private int meetingId;
    private FragmentActivity context;
    private UsersArrayAdapter adapter;

    private IFriendsAdapter friendsDBAdapter;
    List<User> l;
    HashMap<Integer,Boolean> selectedUsers;

    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_friends, container, false);
        this.view = view;
        context=this.getActivity();
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);
        selectedUsers = new HashMap<>();
        lv = (ListView) view.findViewById(R.id.myFriends);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Bundle meetingLevel= getActivity().getIntent().getExtras();
        meetingId=meetingLevel.getInt("meetingId");
        level=meetingLevel.getInt("level");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                User u = (User) adapter.getItemAtPosition(pos);
                boolean setSelected;
                if (selectedUsers.get(u.getId()) != null) {
                    setSelected = false;
                    selectedUsers.remove(u.getId());
                }
                else {
                    setSelected = true;
                    selectedUsers.put(u.getId(),true);
                }
              lv.setItemChecked(pos, setSelected);
            }
        });
        new getFriends().execute();

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.edit_meeting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done_button) {
            SparseBooleanArray checked = lv.getCheckedItemPositions();
            ArrayList<User> selectedItems = new ArrayList<User>();
            for (int i = 0; i < checked.size(); i++) {
                int position = checked.keyAt(i);
                if (checked.valueAt(i)) {
                    selectedItems.add(adapter.getItem(position));
                }
            }
            Toast.makeText(context,"WORK IN PROGRESS, USERS ADDED", Toast.LENGTH_LONG).show();
            //TODO crida al servidor enviar llista d'amics a enviar
        }
        return super.onOptionsItemSelected(item);
    }


    public class UsersArrayAdapter extends ArrayAdapter<User>{
        LayoutInflater _li;
        int _resource;
        List<User> _users;

        public UsersArrayAdapter(Context context, int resource, List<User> objects) {
            super(context, resource, objects);

            this._li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this._resource = resource;
            this._users = objects;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null){
                view = this._li.inflate(this._resource, null);
            }

            User user = this._users.get(position);

            TextView userPhoto = view.findViewById(R.id.meeting_item_user_photo2);
            char letter = user.getUsername().charAt(0);
            String firstLetter = String.valueOf(letter);
            userPhoto.setBackground(getColoredCircularShape((letter)));
            userPhoto.setText(firstLetter);

            TextView userName = view.findViewById(R.id.meeting_item_username);
            userName.setText(user.getUsername());

            TextView name = view.findViewById(R.id.meeting_item_name);
            name.setText(user.getFirstName()+" "+user.getLastName());

            TextView postCode = view.findViewById(R.id.meeting_item_postcode);
            postCode.setText(user.getPostalCode());

            TextView meetingLevel = view.findViewById(R.id.meeting_item_level2);
            String level = String.valueOf(user.getLevel());
            if (level.equals("null")) level = "0";
            meetingLevel.setText(String.valueOf(level));

/*
            User item = this._users.get(position);
            TextView tvTitle = (TextView)convertView.findViewById(android.R.id.text1);
            TextView tvSubtitle = (TextView)convertView.findViewById(android.R.id.text2);

            tvTitle.setText(item.getFirstName()+" "+item.getLastName());
            tvSubtitle.setText(item.getUsername()+" -> "+item.getPostalCode());
*/
            return view;
        }

        @Override
        public int getCount() {
            return _users.size();
        }
    }


    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

    private class getFriends extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();
                //TODO pagination
                l = friendsDBAdapter.getUserFriends(0);
            } catch (AutorizationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            adapter = new UsersArrayAdapter(context, R.layout.user_item_simple,l);
            lv.setAdapter(adapter);
            super.onPostExecute(s);
        }
    }

}


