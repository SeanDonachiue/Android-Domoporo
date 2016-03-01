package com.Package.TimerApp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sean on 2014-12-22.
 *
 */
public class TimerFragment extends Fragment {
    private View rootView;

    //UI elements of the timer screen
    TextView timerText; //This is package local visibility so that it can be changed from TASK
    private Button startButton, abandonButton, extendButton;
    private ButtonListener buttonClicker;

    //Important Timer Elements
    private Timer clock;
    private Task currTask;
    private boolean running;

    //Timer String Variables
    private String clockString;
    private int seconds, minutes;

    private Bitmap bitM;
    private Canvas canV;
    private RelativeLayout fragLayout;
    //Settings Variables
    private int workInterval, breakInterval, numLoops, taskCount;

    //timerText update handler and runnable
    final Handler myHandler = new Handler();
    final Runnable myRunnable = new Runnable() {
        public void run() {
            timerText.setText(String.valueOf(clockString));
        }
    };

    //Initialize elements that we want to keep until the app is destroyed
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     *
     * @param inflater
     *              -"Inflates" the UI. I assume it "renders" everything.
     * @param container
     * @param savedInstanceState
     * @return rootView
     *              -The view object for the entire fragment
     */
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        // The last two arguments: container, and false, ensure LayoutParams inflates correctly
        rootView = inflater.inflate( R.layout.timer_fragment, container, false );

        //UI element instantiations
        fragLayout = (RelativeLayout) rootView.findViewById(R.id.relative_layout);
        startButton = (Button) rootView.findViewById(R.id.start_button);
        extendButton = (Button) rootView.findViewById(R.id.extend_button);
        abandonButton = (Button) rootView.findViewById(R.id.abandon_button);
        timerText = (TextView) rootView.findViewById(R.id.timer_text);

        bitM = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        canV = new Canvas(bitM);

        clock = new Timer();
        buttonClicker = new ButtonListener();

        //Set click listeners
        startButton.setOnClickListener(buttonClicker);
        startButton.setText("Start");
        abandonButton.setOnClickListener(buttonClicker);
        abandonButton.setText("Abandon Task");
        extendButton.setOnClickListener(buttonClicker);
        extendButton.setText("Extend");

        //Task Variables, these come from settings
        // TODO: load from savestate
        workInterval = 5;
        breakInterval = 1;
        taskCount = 0;
        numLoops = 1;
        currTask = new Task (workInterval);               //Initializes with a workInterval of 20 minutes.

        return rootView;
    }

    public int getMinutes() { return minutes; }
    public int getInterval() { return currTask.intervalTime; }
    /*
        Settings, called from ScreenManagerActivity
        TODO: GIVE TASKS THE APPROPRIATE INTERVAL BASED ON SETTINGS. H0W?
        add an apply button?
     */
    public void setWork(int w) {
        workInterval = w;
        currTask.updateGUI();
    }
    public void setBreak(int b) {
        breakInterval = b;
        currTask.updateGUI();
    }
    public void setNumLoops(int loops) {
        numLoops = loops;
        currTask.updateGUI();
    }

    // Button Logic is contained in here!
    public class ButtonListener implements View.OnClickListener {

        public ButtonListener() {}

        @Override
        public void onClick(View view) {
            int buttonID = view.getId();
            switch(buttonID) {
                //Button Logic For Start
                case R.id.start_button:
                    if(!running) {
                        running = true;
                        clock.scheduleAtFixedRate(currTask, 1000, 1000);
                    }
                    break;
                //Button Logic For Stop
                case R.id.abandon_button:
                    Task temp = currTask;
                    currTask = currTask.getNext();
                    temp.stop(currTask);
                    break;
                //Button Logic For Extend
                case R.id.extend_button:
                    currTask.extend();
                    break;
            }
        }
    }

    public class Task extends TimerTask {

        private Task next;
        private int intervalTime;
        TimerDrawableView arc;
        public Task(int min){
            intervalTime = min;
            minutes = min;
            seconds = 0;
            arc = new TimerDrawableView(fragLayout.getContext(), this);
            fragLayout.addView(arc);
            updateGUI();
        }

        public void run() {
            //Finish Conditions
            if(minutes == 0 && seconds == 0) {
                this.stop(this.getNext());
            }
            if (seconds > 0)
                seconds--;
            else if(minutes > 0) {
                minutes--;
                seconds = 59;
            }
            updateGUI();
        }

        public void extend() {
            minutes = minutes + 5;
            updateGUI();
        }

        public void stop(Task nextTask) {
            if(nextTask != null) {
                this.cancel();
                clock.scheduleAtFixedRate(nextTask, 1000, 1000);
                updateGUI();
            }
            else {
                //TODO: Display a message that the loop is complete (end, or long break?)
                //reset taskCount
                running = false;
                taskCount = 0;
                //load a new task
                currTask = new Task(workInterval);
                //clock will still display nothing here.
            }
        }

        private void updateGUI() {

            if(seconds < 10 && minutes < 10)                //Format with 0 in front of single digits.
                clockString = "0" + minutes + ":0" + seconds;
            else if(minutes < 10 && seconds >= 10)
                clockString = "0" + minutes + ":" + seconds;
            else if(seconds < 10)
                clockString =  minutes + ":0" + seconds;
            else
                clockString = minutes + ":" + seconds;
            arc.postInvalidate();
            System.out.println("arc invalidated");
            myHandler.post(myRunnable);
        }

        //Work Interval and Break Interval are variable in Settings
        public Task getNext() {
            taskCount ++;
            if(taskCount >= numLoops*2) {  //Base Case: Timer has finished all tasks
                running = false;
                next = null;
                this.cancel();
            }
            else if(taskCount % 2 == 0)
                next = new Task(workInterval);      //taskCount even, run workInterval
            else if(taskCount % 2 != 0)
                next = new Task(breakInterval);     //taskCount odd, run breakInterval

            return next;
        }
        public int getMinutes() { return minutes; }
        public int getSeconds() { return seconds; }
        public int getInterval() { return intervalTime; }
    }
}
