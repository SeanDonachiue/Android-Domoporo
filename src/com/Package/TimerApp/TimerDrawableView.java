package com.Package.TimerApp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Sean on 2015-06-02.
 *
 * Defines a ShapeDrawable for the clock face
 */
public class TimerDrawableView extends View {
    private ShapeDrawable arcDrawable;
    private int x, y, elapsed, total, radius;
    TimerFragment.Task t;
    public TimerDrawableView(Context context, TimerFragment.Task task) {
        super(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        //Play with this stuff later!
        radius = screenWidth/5;
        x = screenWidth/2;
        y = screenHeight/5;
        t = task;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        arcDrawable = new ShapeDrawable(new ArcShape(-90, ( 360*(t.getMinutes()*60 + t.getSeconds())/(t.getInterval()*60) ) ));
        //arcDrawable = new ShapeDrawable(new ArcShape(-90, 60));
        arcDrawable.getPaint().setColor(0xff00ffff);
        arcDrawable.setBounds(x - radius, y - radius, x + radius, y + radius);
        arcDrawable.draw(canvas);
        //invalidate();
        System.out.println("arcShape drawn");
    }
}
