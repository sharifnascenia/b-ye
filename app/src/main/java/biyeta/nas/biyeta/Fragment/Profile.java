package biyeta.nas.biyeta.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biyeta.nas.biyeta.R;

/**
 * Created by user on 1/5/2017.
 */

public class Profile extends Fragment {

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("come","Profile");

        View v = inflater.inflate(R.layout.profile, null);

        return v;

    }
}
