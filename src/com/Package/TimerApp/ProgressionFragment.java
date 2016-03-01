package com.Package.TimerApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sean on 2014-12-22.
 */
public class ProgressionFragment extends Fragment{

    //Initialize elements that we want to keep until the app is destroyed
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate( R.layout.progression_fragment, container, false );
        return rootView;
    }
}
