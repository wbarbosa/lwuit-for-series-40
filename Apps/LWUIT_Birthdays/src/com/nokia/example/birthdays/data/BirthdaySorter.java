/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 *
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.birthdays.data;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * Helper for sorting a Vector of birthdays in the order in which they will
 * occur (as observed from now).
 */
public class BirthdaySorter {

    private static final int BEFORE = -1;
    private static final int EQUAL = 0;
    private static final int AFTER = 1;
    
    // Helpers for date comparison operations
    private static final Calendar TODAY = Calendar.getInstance();
    private static final Calendar CAL1 = Calendar.getInstance();
    private static final Calendar CAL2 = Calendar.getInstance();
    
    private static final long NOW_MILLIS = new Date().getTime();
    private static final int CURRENT_YEAR =
        Calendar.getInstance().get(Calendar.YEAR);

    /**
     * Sort a Vector of birthdays in the order of upcoming birthdays.
     *
     * This method will alter the given Vector directly, so handle with care.
     *
     * @param birthdays Vector of Birthday objects
     */
    public void sort(Vector birthdays) {
        int count = birthdays.size();
        Birthday[] array = new Birthday[count];

        try {
            birthdays.copyInto(array);
        }
        catch (Exception e) {
            return;
        }

        sort(array, 0, count - 1);

        for (int i = 0; i < count; i++) {
            birthdays.setElementAt(array[i], i);
        }
    }

    /**
     * Quicksort algorithm based on an adaptation of James Gosling's 1995
     * demo code.
     *
     * See http://code.google.com/p/oppositelock/source/browse/jsr/trunk/src/numfum/j2me/util/QuickSort.java?r=2
     *
     * @param array
     * @param _lo
     * @param _hi
     */
    private void sort(Birthday[] array, int _lo, int _hi) {
        int lo = _lo;
        int hi = _hi;
        if (lo >= hi) {
            return;
        }

        Birthday swapped;
        
        // Special case for two elements.
        if (lo == hi - 1) {
            if (compare(array[lo], array[hi]) > 0) {
                swapped = array[lo];
                array[lo] = array[hi];
                array[hi] = swapped;
            }
            return;
        }

        int n = (lo + hi) / 2;
        Birthday pivot = array[n];
        array[n] = array[hi];
        array[hi] = pivot;

        while (lo < hi) {
            // Search forward from array[lo] until an element is found that
            // is greater than the pivot or lo >= hi.
            while (compare(array[lo], pivot) <= 0 && lo < hi) {
                lo++;
            }
            
            // Search backward from array[hi] until element is found that is
            // less than the pivot, or lo >= hi.
            while (compare(pivot, array[hi]) <= 0 && lo < hi) {
                hi--;
            }

            // Swap elements array[lo] and array[hi].
            if (lo < hi) {
                swapped = array[lo];
                array[lo] = array[hi];
                array[hi] = swapped;
            }
        }
        
        // Put the median in the 'centre' of the list.
        array[_hi] = array[hi];
        array[ hi] = pivot;
        
        // Recursive calls, elements array[_lo] to array[lo - 1] are less
        // than or equal to pivot, elements array[hi + 1] to array[_hi] are
        // greater than pivot.
        sort(array, _lo, lo - 1);
        sort(array, hi + 1, _hi);
    }

    /**
     * Compare a birth date to another, considering the current time of year.
     *
     * Suppose it's now the 1st of August:
     * 1 2 3 4 5 6 7 8 9 10 11 12
     *               ^
     * 
     * For showing the upcoming birthdays list, we need to consider whether the
     * dates compared have already happened this year or not.
     *
     * This means that if people had birthdays each month, their display order,
     * as viewed on August 1st, would be:
     * 8 9 10 11 12 1 2 3 4 5 6 7
     *
     * @param date1 First date of comparison
     * @param date2 Second date of comparison
     * @return BEFORE if date1 should appear before date2; AFTER if date1 should
     * appear after date2, EQUAL if it makes no matter
     */
    private int compare(Birthday date1, Birthday date2) {
        /*
         * Disregard the year by comparing the dates as if they belong to
         * current year: here we're only interested in the month + day.
         */
        CAL1.setTime(date1.getDate());
        CAL1.set(Calendar.YEAR, CURRENT_YEAR);
        CAL2.setTime(date2.getDate());
        CAL2.set(Calendar.YEAR, CURRENT_YEAR);

        /*
         * Because we're not interested in time of the day, today's birthdays
         * easily get mistreated as having already happened unless we consider
         * them individually. If there are multiple birthdays on the same day,
         * it doesn't matter which one of them we display first.
         */
        if (isToday(CAL1)) {
            return BEFORE;
        }
        else if (isToday(CAL2)) {
            return AFTER;
        }        
        
        /*
         * If one of the two dates has already occurred this year and the 
         * other one hasn't, the next occurrence (from now) comes first.
         */
        long time1 = CAL1.getTime().getTime();
        long time2 = CAL2.getTime().getTime();
        
        if (time1 < NOW_MILLIS && time2 > NOW_MILLIS) {
            // time1 has also happened this year, needs to go after time2
            return AFTER;
        }
        else if (time1 > NOW_MILLIS && time2 < NOW_MILLIS) {
            return BEFORE;
        }

        /*
         * If we reached this point, if means time1 and time2 are both on the
         * same side of the year, and we can compare them normally.
         */
        long diff = time1 - time2;
        // If date1 < date2, return BEFORE (-1)
        if (diff < 0) {
            return BEFORE;
        } // If date2 > date1, return AFTER (1)
        else if (diff > 0) {
            return AFTER;
        }
        return EQUAL;
    }
    
    private boolean isToday(Calendar calendar) {
        return
            calendar.get(Calendar.MONTH) == TODAY.get(Calendar.MONTH) &&
            calendar.get(Calendar.DATE) == TODAY.get(Calendar.DATE);
    }
}
