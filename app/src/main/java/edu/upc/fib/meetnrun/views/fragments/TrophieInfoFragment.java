package edu.upc.fib.meetnrun.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import edu.upc.fib.meetnrun.R;

public class TrophieInfoFragment extends BaseFragment {

    private View view;
    private FloatingActionButton fab;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trophie_info,container,false);
        //this.view = view;

        Bundle trophieInfo = getActivity().getIntent().getExtras();

        ImageView image = view.findViewById(R.id.trophie_image);
        TextView title = view.findViewById(R.id.trophie_title);
        TextView description = view.findViewById(R.id.trophie_description);
        TextView obtained = view.findViewById(R.id.trophie_obtained);

        image.setImageResource(trophieInfo.getInt("image"));
        title.setText(trophieInfo.getString("title"));
        description.setText(trophieInfo.getString("description"));
        boolean isObtained = trophieInfo.getBoolean("obtained");
        if (isObtained) {
            obtained.setText(R.string.trophie_obtained);
        } else {
            obtained.setText(R.string.trophie_not_obtained);
        }

        fab = getActivity().findViewById(R.id.activity_fab);
        fab.setVisibility(View.INVISIBLE);

        return view;
    }
}
