/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 *
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays.util;

import com.nokia.example.birthdays.data.Birthday;
import java.util.Calendar;
import java.util.Date;

/**
 * A convenience class for printing human-readable dates.
 */
public class BirthdayPrettyPrinter {
    
    public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
        "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    
    public static final long SECONDS_IN_YEAR = 31536000;
        
    public static String getFormattedAgeOnNextBirthday(Birthday birthday) {
        long secondsFromBirth =
            (new Date().getTime() - birthday.getDate().getTime()) / 1000;
        
        return "" +
            Math.max(1, (int)
            Math.ceil((double) secondsFromBirth / SECONDS_IN_YEAR));
    }
    
    /**
     * Pretty-print how long it is until the birthday,
     * e.g. "today" or "5 days".
     *
     * @return Pretty-printed birthday as a String
     */
    public static String getTimeUntilNextOccurrence(Birthday birthday) {
        // The time of the day doesn't matter: normalize days to around
        // midnight to get reliable approximations on the time until birthday
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 10);
        c.set(Calendar.MINUTE, 0);
        long now = c.getTime().getTime() / 1000;
        
        int currentYear = c.get(Calendar.YEAR);
        c.setTime(birthday.getDate());
        c.set(Calendar.YEAR, currentYear);       
        c.set(Calendar.HOUR_OF_DAY, 10);
        c.set(Calendar.MINUTE, 10);                
        long then = c.getTime().getTime() / 1000;
        
        // If birthday already occurred this year, consider the next year
        if (then < now) {
            // 60 seconds * 60 minutes * 24 hours * 365 days
            then += 31536000; 
        }
        
        // Format the time difference
        return getHumanReadableTimeUntilDate(then - now);        
    }
    
    private static String getHumanReadableTimeUntilDate(long secondsUntilBirthday) {
        if (secondsUntilBirthday < 86400) {
            return "today";
        }
        else if (secondsUntilBirthday < 172800) {
            return "tomorrow";
        }
        
        long units = 0;
        String unit = "";
        
        // Less then a week -> "x days"
        if (secondsUntilBirthday < 604800) {
            units = secondsUntilBirthday / 86400;
            unit = units + " days";            
        }
        // Less than a month -> "x weeks"
        else if (secondsUntilBirthday < 2592000) {
            units = secondsUntilBirthday / 604800;
            unit = (units > 1 ? "" + units : "a") +
                " week" + (units > 1 ? "s" : "");
        }
        // Less than a year -> "x months" (11 full months at most)
        else if (secondsUntilBirthday < 31536000) {
            units = Math.min(11, secondsUntilBirthday / 2592000);
            unit = (units > 1 ? "" + units : "a") +
                " month" + (units > 1 ? "s" : "");
        }
        
        return "in " + unit;        
    }    
    
    public static String getFormattedBirthDate(Birthday birthday) {        
        return getFormattedDate(birthday.getDate());
    }
    
    public static String getFormattedDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        
        return
            MONTHS[c.get(Calendar.MONTH)] + " " +
            c.get(Calendar.DAY_OF_MONTH) + " " +
            c.get(Calendar.YEAR);
    }
}
