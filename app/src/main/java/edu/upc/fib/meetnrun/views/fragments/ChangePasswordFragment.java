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
import edu.upc.fib.meetnrun.asynctasks.UpdatePassword;
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
        callUpdatePassword(currentPass, newPass);
    }

    private void callUpdatePassword(String currentPass, String newPass) {
        UpdatePassword updatePassword = new UpdatePassword() {
            @Override
            public void onResponseReceived(boolean b) {
                if (b) {
                    changeToUserProfile();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_change_pass), Toast.LENGTH_SHORT).show();
                }
            }
        };
        try {
            updatePassword.execute(currentPass, newPass);
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
        catch (ForbiddenException e) {
            Toast.makeText(getActivity(), R.string.forbidden_error, Toast.LENGTH_LONG).show();
        }
    }


    public int getTitle() {
        return R.string.action_change_pass;
    }
}
