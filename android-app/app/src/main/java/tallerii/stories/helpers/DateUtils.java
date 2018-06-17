package tallerii.stories.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.text.DateFormat.getDateTimeInstance;

public class DateUtils {
    public static long getNowTime(){
        return new Date().getTime();
    }

    public static String getTimeFromTimestamp(long timeStamp){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timeStamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date fucking error";
        }
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public static String printDifference(Date startDate){

        Date endDate = new Date();

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if(elapsedDays > 0){
            return elapsedDays+" days ago";
        } else if(elapsedHours > 0){
            return elapsedHours+" hours ago";
        } else if(elapsedMinutes > 0){
            return elapsedMinutes+" minutes ago";
        } else if(elapsedSeconds > 0){
            return elapsedSeconds+" seconds ago";
        } else{
            SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMM, yyyy 'at' hh:mm:ss");
            return dateFormatter.format(startDate);
        }

    }
}
