/**
 * Created by EJ Del Rosario
 * Copyright (c) 2015
 * Personal Intellectual Property
 * All Rights Reserved
 */

package ejdelrosario.framework.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {


	/**
	 * @param format date or time format see <a href="http://developer.android.com/reference/java/text/SimpleDateFormat.html">Time Pattern Syntax</a>
	 * @param timestamp
	 * @return date or time
	 */
	public static String getDate(String format, long timestamp) {
		SimpleDateFormat dateFormat = new SimpleDateFormat( format, Locale.US );
        return dateFormat.format( new Date(timestamp) );
	}

	/**
	 *
	 * @param format date or time format see <a href="http://developer.android.com/reference/java/text/SimpleDateFormat.html">Time Pattern Syntax</a>
	 * @param timestamp
	 * @param locale
	 * @return date or time
	 */
	public static String getDate(String format, long timestamp, Locale locale ) {
		SimpleDateFormat dateFormat = new SimpleDateFormat( format, locale );
        return dateFormat.format( new Date(timestamp) );
	}
	
	public static String getDate(String format, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat( format, Locale.US );
        return dateFormat.format( date );
	}

	/**
	 * parses the date from a string formatted correctly
	 * @param format format of the string passed
	 * @param date string formatted date
	 * @return Date; returns null if format is invalid or it does not match the string date
	 */
	public static Date parseDate(String format,String date) {
		try {
			return new SimpleDateFormat(format,Locale.US).parse( date );
		}
		catch(ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * converts the Date into Calendar setting hour, minute, second and millisecond to 0
	 * @param date
	 * @return
	 */
	public static Calendar getDatePart(Date date){
	    Calendar cal = Calendar.getInstance();       // get calendar instance
	    cal.setTime(date);      
	    cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
	    cal.set(Calendar.MINUTE, 0);                 // set minute in hour
	    cal.set(Calendar.SECOND, 0);                 // set second in minute
	    cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

	    return cal;                                  // return the date part
	}

	/**
	 * computes the days between the startDate and the endDate
	 * @param startDate
	 * @param endDate
	 * @return number of days between
	 */
	public static int daysBetween(Date startDate, Date endDate) {
		  Calendar sDate = getDatePart(startDate);
		  Calendar eDate = getDatePart(endDate);

		  int daysBetween = 0;
		  while ( sDate.before(eDate) ) {
		      sDate.add( Calendar.DAY_OF_MONTH, 1 );
		      daysBetween++;
		  }
		  return daysBetween;
	}
}
