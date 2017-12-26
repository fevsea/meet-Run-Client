package edu.upc.fib.meetnrun.views.fragments;


import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public class ChangePasswordFragment extends BaseFragment {

    private final ILoginAdapter controller = CurrentSession.getInstance().getLoginAdapter();
    private EditText currentPassText;
    private EditText newPassText;
    private EditText newPass2Text;

    private String currentPass;
    private String newPass;
    private String newPass2;

    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_change_password, container, false);

        boolean b = checkParameters();

        if (b) {
            Button button = v.findViewById(R.id.save_button);
            button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveNewPass();
            }
        });
        }

        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.GONE);

        return v;
    }

    private boolean checkParameters() {

        boolean b = false;

        currentPassText = v.findViewById(R.id.currentPass_enter);
        newPassText = v.findViewById(R.id.newPass_enter);
        newPass2Text = v.findViewById(R.id.repeatNewPass_enter);

        currentPass = currentPassText.getText().toString();
        newPass = newPassText.getText().toString();
        newPass2 = newPass2Text.getText().toString();

        if (currentPass.equals("")) {
            Toast.makeText(getContext(), "Current password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (newPass.equals("")) {
            Toast.makeText(getContext(), "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (newPass2.equals("")) {
            Toast.makeText(getContext(), "Repeat password field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (newPass.length() < 5) {
            Toast.makeText(getContext(), "Password field is too short", Toast.LENGTH_SHORT).show();
        }
        else if (!newPass.equals(newPass2)) {
            Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
        }
        else {
            b = true;
        }

        return b;
    }

    private void changeToUserProfile() {
       getActivity().finish();
    }

    private void saveNewPass() {
        new updatePass().execute(currentPass, newPass);
    }


    private class updatePass extends AsyncTask<String, String, Boolean> {

        Exception exception = null;
        Boolean actualitzat_correctament;

        @Override
        protected Boolean doInBackground(String... params) {

                try {
                    actualitzat_correctament = controller.changePassword(params[0], params[1]);
                } catch (AuthorizationException | ForbiddenException e) {
                    e.printStackTrace();
                }

            return actualitzat_correctament;
        }

        @Override
        protected void onPostExecute(Boolean b) {

            if(b) {
                changeToUserProfile();
            }
            else {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_change_pass), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(b);
        }
    }

    public int getTitle() {
        return R.string.action_change_pass;
    }
}
