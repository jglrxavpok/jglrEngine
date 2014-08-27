package org.jge;

import java.util.Calendar;

public class Time
{

	private static String[] days   = new String[7];
	private static String[] months = new String[12];

	private static Calendar calendar;
	private static double   delta;

	static
	{
		calendar = Calendar.getInstance();
		days[Calendar.MONDAY - 1] = "Monday";
		days[Calendar.TUESDAY - 1] = "Tuesday";
		days[Calendar.WEDNESDAY - 1] = "Wednesday";
		days[Calendar.THURSDAY - 1] = "Thursday";
		days[Calendar.FRIDAY - 1] = "Friday";
		days[Calendar.SATURDAY - 1] = "Saturday";
		days[Calendar.SUNDAY - 1] = "Sunday";

		months[Calendar.JANUARY] = "January";
		months[Calendar.FEBRUARY] = "February";
		months[Calendar.MARCH] = "March";
		months[Calendar.APRIL] = "April";
		months[Calendar.MAY] = "May";
		months[Calendar.JUNE] = "June";
		months[Calendar.JULY] = "July";
		months[Calendar.AUGUST] = "August";
		months[Calendar.SEPTEMBER] = "September";
		months[Calendar.OCTOBER] = "October";
		months[Calendar.NOVEMBER] = "November";
		months[Calendar.DECEMBER] = "December";
	}

	public static long getTime()
	{
		return System.currentTimeMillis();
	}

	public static String getTimeAsText()
	{
		String second = "" + calendar.get(Calendar.SECOND);
		String minute = "" + calendar.get(Calendar.MINUTE);
		String hour = "" + calendar.get(Calendar.HOUR_OF_DAY);
		String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
		String month = months[calendar.get(Calendar.MONTH)];
		String year = "" + calendar.get(Calendar.YEAR);

		if(second.length() == 1) second = "0" + second;

		if(minute.length() == 1) minute = "0" + minute;

		if(hour.length() == 1) hour = "0" + hour;

		return month + ", " + day + " " + year + " at " + hour + "h" + minute + " and " + second + "seconds";
	}

	public static String getTimeAsString()
	{
		String second = "" + calendar.get(Calendar.SECOND);
		String minute = "" + calendar.get(Calendar.MINUTE);
		String hour = "" + calendar.get(Calendar.HOUR_OF_DAY);
		String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
		String month = "" + (calendar.get(Calendar.MONTH) + 1);
		String year = "" + calendar.get(Calendar.YEAR);

		if(second.length() == 1) second = "0" + second;

		if(minute.length() == 1) minute = "0" + minute;

		if(hour.length() == 1) hour = "0" + hour;

		if(month.length() == 1) month = "0" + month;
		return year + "/" + month + "/" + day + " @ " + hour + ":" + minute + ":" + second;
	}

	static void setDelta(double newDelta)
	{
		delta = newDelta;
	}

	public static double getDelta()
	{
		return delta;
	}
}
