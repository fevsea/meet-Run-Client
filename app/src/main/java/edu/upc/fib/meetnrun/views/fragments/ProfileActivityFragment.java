package edu.upc.fib.meetnrun.views.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;


public class ProfileActivityFragment extends Fragment {

    /*public ProfileActivityFragment() {
    }*/

    User u;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //updateUser();

        u = CurrentSession.getInstance().getCurrentUser();



        Log.e("USER","AGAFO USER" + " " + u);

        String userName = u.getUsername(); Log.e("USER","AGAFO NAME USER" + " " + userName);
        String name = u.getFirstName() + ' ' + u.getLastName(); Log.e("USER","AGAFO NAME" + " " + name);
        String postCode = u.getPostalCode(); Log.e("USER","AGAFO POST CODE" + " " + postCode);


        TextView userNameTextView = (TextView) view.findViewById(R.id.userName);
        Log.e("view","agafo view username");
        TextView nameTextView = (TextView) view.findViewById(R.id.completeName);
        Log.e("view","agafo view name");
        TextView userPostCodeTextView = (TextView) view.findViewById(R.id.userPostCode);
        Log.e("view","agafo view postcode");

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(android.R.drawable.ic_menu_edit);

        /*
        fab.setBackgroundColor(0);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        userNameTextView.setText(userName); Log.e("Set text","poso username");
        nameTextView.setText(name);Log.e("Set text","poso name");
        userPostCodeTextView.setText(postCode); Log.e("Set text","poso post");

        return view;
    }

    /*private void updateUserView(User u) {

        TextView userNameTextView = view.findViewById(R.id.userName);
        TextView nameTextView = view.findViewById(R.id.name);
        TextView userPostCodeTextView = view.findViewById(R.id.userPostCode);


        String userName = u.getUsername();
        String name = u.getFirstName() + ' ' + u.getLastName();
        String postCode = u.getPostalCode();


        userNameTextView.setText(userName);
        nameTextView.setText(name);
        userPostCodeTextView.setText(postCode);

    }

    private void updateUser() {
        new GetUser().execute();
    }

    private class GetUser extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.e("MAIN","DOINGGGG");
            u = GenericController.getInstance().getCurrentUser();
            Log.e("MAIN","HE AGAFAT EL USER");
            Log.e("MAIN","AMB NOM USER" + u.getUsername());
            Log.e("MAIN","AMB NOM" + u.getFirstName());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("MAIN","FINISHED");
            //updateUserView(u);
            super.onPostExecute(s);
        }
    }*/
}
