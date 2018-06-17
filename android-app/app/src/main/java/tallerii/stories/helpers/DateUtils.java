package tallerii.stories.helpers;

import java.text.DateFormat;
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
}
