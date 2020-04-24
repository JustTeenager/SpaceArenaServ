package com.mygdx.game;

import java.util.Locale;
import java.util.TimerTask;


public class TimerPlay extends TimerTask {

    static int seconds=60;
    static String time="-1";

    @Override
    public void run() {
        if (seconds>0) {
            seconds--;
            int minute = (seconds % 3600) / 60;
            int sec = seconds % 60;
            time = String.format(Locale.getDefault(), "%2d %02d", minute, sec);
            //System.out.println(seconds);
        }
    }
}
