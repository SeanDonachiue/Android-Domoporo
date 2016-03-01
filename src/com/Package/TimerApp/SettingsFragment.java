package com.Package.TimerApp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

/**
 * Created by Sean on 2014-12-22.
 */
public class SettingsFragment extends Fragment{

    public settingsChangedListener setter;
    //Superfluous? Maybe not for debugging
    private int workInt, breakInt, loopCount;
    //  gonna need three number pickers (I believe, maybe only 1 is needed, but the naive solution is 3)
    NumberPicker workPicker, breakPicker, loopPicker;
    //Initialize elements that we want to keep until the app is destroyed
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    //Initialize UI elements
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        // The last two arguments ensure LayoutParams are inflated properly.
        View rootView = inflater.inflate( R.layout.settings_fragment, container, false );

        /*SPINNERS!
            Currently using three spinners. This seems messy and likely can be abstracted to 1... more elegant

         */

        //Work Interval
        workPicker = (NumberPicker) rootView.findViewById(R.id.workPicker);
        workPicker.setMaxValue(60);
        workPicker.setMinValue(1);
        workPicker.setWrapSelectorWheel(true);
        workPicker.setOnValueChangedListener( new NumberPicker.
                OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int
                    oldVal, int newVal) {
                setter.onSetWork(newVal);
            }
        });
        //Break Interval
        breakPicker = (NumberPicker) rootView.findViewById(R.id.breakPicker);
        breakPicker.setMaxValue(15);
        breakPicker.setMinValue(1);
        breakPicker.setWrapSelectorWheel(true);
        breakPicker.setOnValueChangedListener( new NumberPicker.
                OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int
                    oldVal, int newVal) {
                setter.onSetBreak(newVal);
            }
        });
        //Loop Count
        loopPicker = (NumberPicker) rootView.findViewById(R.id.loopPicker);
        loopPicker.setMaxValue(5);
        loopPicker.setMinValue(1);
        loopPicker.setWrapSelectorWheel(true);
        loopPicker.setOnValueChangedListener( new NumberPicker.
                OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int
                    oldVal, int newVal) {
                setter.onSetLoop(newVal);
            }
        });

        return rootView;
    }

    //Pass the picked values to ScreenManagerActivity (Might need a different activity, we'll see.)
    public interface settingsChangedListener {
        public void onSetWork(int work);
        public void onSetBreak(int breakI);
        public void onSetLoop(int loop);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            setter = (settingsChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement settingsChangedListener");
        }
    }
}
