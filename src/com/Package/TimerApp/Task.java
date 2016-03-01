package com.Package.TimerApp;

import android.os.Handler;

import java.util.TimerTask;

/**
 * Created by Sean on 2014-12-19.
 *
 * This class is used by the Interval class
 */
public class Task extends TimerTask {

    private TimerFragment tFrag;
    private boolean running, finished;
    private int seconds, minutes, hours;
    private String clockString;
    private Task next;


    final Handler myHandler = new Handler();
    final Runnable myRunnable = new Runnable() {
        public void run() {
            tFrag.timerText.setText(String.valueOf(clockString));
        }
    };

    public Task(int min, TimerFragment tFrag){
        hours = 0;
        minutes = min;
        seconds = 0;
        finished = false;
        clockString = "00:" + min + ":00";
    }
    public void run() {
        running = true;
        //Finish Conditions
        if(hours == 0 && minutes == 0 && seconds == 0) {
            running = false;
            finished = true;
            this.cancel();
        }
        if (seconds > 0)
            seconds--;
        else {
            minutes--;
            seconds = 59;

            if (minutes == 0) {
                hours--;
                minutes = 59;
            }
        }
        //clock tick sound goes in here
        updateGUI();
    }

    public void extend() {
        minutes = minutes + 5;
        updateGUI();
    }

    public void stop() {
        hours = 0;
        minutes = 0;
        seconds = 0;
        updateGUI();
        finished = true;
        this.cancel();
    }

    public boolean isFinished() {
        return finished;
    }

    private void updateGUI() {
        clockString = hours + ":" + minutes + ":" + seconds;
        myHandler.post(myRunnable);
    }
    public Task getNext() {return next;}
}