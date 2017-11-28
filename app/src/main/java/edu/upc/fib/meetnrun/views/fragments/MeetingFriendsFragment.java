package edu.upc.fib.meetnrun.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.FriendsAdapter;

import static android.R.layout.simple_list_item_1;
import static android.R.layout.simple_list_item_activated_2;
import static android.R.layout.simple_list_item_checked;


/**
 * Created by Javier on 08/11/2017.
 */

public class MeetingFriendsFragment extends Fragment {

    private View view;
    private int level;
    private int meetingId;
    private FragmentActivity context;

    private IFriendsAdapter friendsDBAdapter;
    List<User> l;
    List<User> _users;
    List<Boolean> clickedFriends;
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

        lv = view.findViewById(R.id.myFriends);

        Bundle meetingLevel= getActivity().getIntent().getExtras();
        meetingId=meetingLevel.getInt("meetingId");
        level=meetingLevel.getInt("level");
        new getFriends().execute();

        return view;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = this._li.inflate(this._resource, null);
            }

            User item = this._users.get(position);
            TextView tvTitle = convertView.findViewById(android.R.id.text1);
            TextView tvSubtitle = convertView.findViewById(android.R.id.text2);


            tvTitle.setText(item.getFirstName()+" "+item.getLastName());
            tvSubtitle.setText(item.getUsername()+" -> "+item.getPostalCode());

            return convertView;
        }
    }
    private class getFriends extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                friendsDBAdapter = CurrentSession.getInstance().getFriendsAdapter();
                l = friendsDBAdapter.getUserFriends();
            } catch (AutorizationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter<User> adapter = new ArrayAdapter<User>(context, simple_list_item_activated_2,l);
            lv.setAdapter(
                    new UsersArrayAdapter(
                            context,
                            android.R.layout.simple_list_item_activated_2,
                            l
                    )
            );
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                /* The code goes here */
                    friendClicked(pos);

                }
            });
            super.onPostExecute(s);
        }
    }

    private void friendClicked(int pos) {
        clickedFriends.set(pos,!clickedFriends.get(pos));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<User> newList = new ArrayList<User>();
                for (User friend : l) {
                    String userName = friend.getUsername().toLowerCase();
                    String name = (friend.getFirstName()+" "+friend.getLastName()).toLowerCase();
                    String postCode = friend.getPostalCode();
                    if (userName != null && name != null && postCode != null) {
                        if (name.contains(newText) || userName.contains(newText) || postCode.contains(newText)) newList.add(friend);
                    }

                }
                //friendsAdapter.updateFriendsList(newList);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}


