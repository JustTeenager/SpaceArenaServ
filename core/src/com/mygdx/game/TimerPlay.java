package com.mygdx.game;

import java.util.Locale;
import java.util.TimerTask;


public class TimerPlay extends TimerTask {

    private int seconds=120;
    private String time="-1";

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

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
