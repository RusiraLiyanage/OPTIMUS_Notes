package com.example.optimusnote;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeAgo {
    public String getTimeAgo(long duration){
        Date now = new Date();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime()-duration);
        long min= TimeUnit.MILLISECONDS.toMinutes(now.getTime()-duration);
        long hours= TimeUnit.MILLISECONDS.toHours(now.getTime()-duration);
        long days= TimeUnit.MILLISECONDS.toDays(now.getTime()-duration);

        String word;

        if(seconds < 60){
            word = "just now";
        }else if(min == 1){
            word = "Minutes ago";
        }else if(min > 1 && min < 60){
            word = min +"Minutes ago";
        }else if(hours ==1){
            word = "an hour ago";
        }else if(hours >1 && hours <24){
            word = hours+"Hours ago";
        }else if(days == 1){
            word = "Day ago";
        }else{
            word = days+"Days ago";
        }

        return word;

    }
}
