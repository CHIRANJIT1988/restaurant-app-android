package restaurent.menu.demo.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dell on 18-06-2015.
 */

public class GenerateUniqueId
{

    public static String generateOrderId(int table_no)
    {
        return String.valueOf(getDate() + getTime() + "-" + table_no);
    }


    public static String getDate()
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");

        return dateFormat.format(new Date());
    }


    private static String getTime()
    {

        Calendar calendar = Calendar.getInstance();

        String hour = dayMonthFormat(calendar.get(Calendar.HOUR_OF_DAY));
        String min = dayMonthFormat(calendar.get(Calendar.MINUTE));
        String sec = dayMonthFormat(calendar.get(Calendar.SECOND));

        return new StringBuilder().append(hour).append(min).append(sec).toString();
    }


    public static String dayMonthFormat(int value)
    {

        if(value < 10)
        {
            return String.valueOf("0" + value);
        }

        else
        {
            return String.valueOf(value);
        }
    }
}
