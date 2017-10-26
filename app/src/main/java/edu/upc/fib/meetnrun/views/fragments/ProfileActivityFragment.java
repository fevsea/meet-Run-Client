package edu.upc.fib.meetnrun.views.fragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.GenericController;


public class ProfileActivityFragment extends Fragment {

    /*public ProfileActivityFragment() {
    }*/

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        updateUser();
        return view;
    }

    private void updateUserView(User u) {

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
        User u;

        @Override
        protected String doInBackground(String... strings) {
            //Log.e("MAIN","DOINGGGG");
            u = GenericController.getInstance().getCurrentUser();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //System.err.println("FINISHED");
            updateUserView(u);
            super.onPostExecute(s);
        }
    }
}
