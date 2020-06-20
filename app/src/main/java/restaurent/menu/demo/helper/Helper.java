package restaurent.menu.demo.helper;

import java.util.Calendar;

/**
 * Created by dell on 19-06-2015.
 */

public class Helper
{

    // directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "restaurent.demo.photo";


    public static String toCamelCase(String inputString)
    {

        String result = "";

        if (inputString.length() == 0)
        {
            return result;
        }

        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);

        result = result + firstCharToUpperCase;

        for (int i = 1; i < inputString.length(); i++)
        {

            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);

            if (previousChar == ' ')
            {

                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            }

            else
            {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }

        return result;
    }



    public static String dateTimeFormat(String str)
    {

        String datetime = "";

        try
        {

            String[] parts = str.split(" ")[0].split("-");

            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int date = Integer.parseInt(parts[2]);

            final Calendar c = Calendar.getInstance();


            if(c.get(Calendar.YEAR) == year)
            {

                if(c.get(Calendar.DATE) == date && (c.get(Calendar.MONTH) + 1) == month)
                {

                    String[] time = str.split(" ")[1].split(":");
                    datetime = setDateTime(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
                }

                else
                {

                    switch(month)
                    {

                        case 1:

                            datetime = "Jan " + date;
                            break;

                        case 2:

                            datetime = "Feb " + date;
                            break;

                        case 3:

                            datetime = "Mar " + date;
                            break;

                        case 4:

                            datetime = "Apr " + date;
                            break;

                        case 5:

                            datetime = "May " + date;
                            break;

                        case 6:

                            datetime = "Jun " + date;
                            break;

                        case 7:

                            datetime = "Jul " + date;
                            break;

                        case 8:

                            datetime = "Aug " + date;
                            break;

                        case 9:

                            datetime = "Sep " + date;
                            break;

                        case 10:

                            datetime = "Oct " + date;
                            break;

                        case 11:

                            datetime = "Nov " + date;
                            break;

                        case 12:

                            datetime = "Dec " + date;
                            break;
                    }
                }
            }

            else
            {
                datetime =  date + "/" + month + "/" + year;
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return datetime;
    }



    public static String setDateTime(int hour, int minute)
    {

        String timeSet = "";

        if (hour > 12)
        {
            hour -= 12;
            timeSet = "PM";
        }

        else if (hour == 0)
        {
            hour += 12;
            timeSet = "AM";
        }

        else if (hour == 12)
        {
            timeSet = "PM";
        }

        else
        {
            timeSet = "AM";
        }


        String hours = "";

        if (hour < 10)
        {
            hours = "0" + hour;
        }

        else
        {
            hours = String.valueOf(hour);
        }


        String minutes = "";

        if (minute < 10)
        {
            minutes = "0" + minute;
        }

        else
        {
            minutes = String.valueOf(minute);
        }

        return String.valueOf(hours + ":" + minutes + " " + timeSet);
    }
}