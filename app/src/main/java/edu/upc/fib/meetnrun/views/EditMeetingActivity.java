package edu.upc.fib.meetnrun.views;


import android.content.DialogInterface;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import android.widget.EditText;
import android.widget.ScrollView;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.Marker;

import edu.upc.fib.meetnrun.R;

import edu.upc.fib.meetnrun.adapters.IGenericController;
import edu.upc.fib.meetnrun.views.fragments.EditMeetingFragment;

import edu.upc.fib.meetnrun.models.Meeting;

public class EditMeetingActivity extends BaseReturnActivity {

    private GoogleMap map;
    private Marker marker;
    private Meeting meeting; //= new Meeting(1, "HOLA", "Descr \n ipcion \n rand \n om", false, 5, new Date().toString(), "41", "2");
    private IGenericController controller;
    private boolean thereWasAnAttemptToSave = false;
    EditText titleText;
    EditText descriptionText;
    EditText levelText;
    ScrollView scrollView;

    @Override
    protected Fragment createFragment() {
        return new EditMeetingFragment();
    }

    @Override
    public void onBackPressed() {
        if (!thereWasAnAttemptToSave) {
            String title = getResources().getString(R.string.edit_meeting_close_dialog_title);
            String message = getResources().getString(R.string.edit_meeting_close_dialog_message);
            String ok = getResources().getString(R.string.ok);
            String cancel = getResources().getString(R.string.cancel);
            showDialog(title, message, ok, cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditMeetingActivity.super.onBackPressed();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );
        }
        else {
            super.onBackPressed();
        }
    }

    private void showDialog(String title, String message, String okButtonText, String negativeButtonText, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okButtonText, ok);
        if (negativeButtonText != null && cancel != null)
            builder.setNegativeButton(negativeButtonText, cancel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
