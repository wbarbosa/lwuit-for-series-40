/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */

package javax.microedition.lcdui;
/**
 * A DateField is an editable component for presenting date and time (calendar) information that may be placed into a Form. Value for this field can be initially set or left unset. If value is not set then the UI for the field shows this clearly. The field value for not initialized state is not valid value and getDate() for this state returns null.
 * Instance of a DateField can be configured to accept date or time information or both of them. This input mode configuration is done by DATE, TIME or DATE_TIME static fields of this class. DATE input mode allows to set only date information and TIME only time information (hours, minutes). DATE_TIME allows to set both clock time and date values.
 * In TIME input mode the date components of Date object must be set to the zero epoch value of January 1, 1970.
 * Calendar calculations in this field are based on default locale and defined time zone. Because of the calculations and different input modes date object may not contain same millisecond value when set to this field and get back from this field.
 * Since: MIDP 1.0
 */
public class DateField extends Item{
    /**
     * Input mode for date information (day, month, year). With this mode this DateField presents and allows only to modify date value. The time information of date object is ignored.
     * Value 1 is assigned to DATE.
     * See Also:Constant Field Values
     */
    public static final int DATE=1;

    /**
     * Input mode for date (day, month, year) and time (minutes, hours) information. With this mode this DateField presents and allows to modify both time and date information.
     * Value 3 is assigned to DATE_TIME.
     * See Also:Constant Field Values
     */
    public static final int DATE_TIME=3;

    /**
     * Input mode for time information (hours and minutes). With this mode this DateField presents and allows only to modify time. The date components should be set to the
     * zero epoch
     * value of January 1, 1970 and should not be accessed.
     * Value 2 is assigned to TIME.
     * See Also:Constant Field Values
     */
    public static final int TIME=2;

    /**
     * Creates a DateField object with the specified label and mode. This call is identical to DateField(label, mode, null).
     * label - item labelmode - the input mode, one of DATE, TIME or DATE_TIME
     * - if the input mode's value is invalid
     */
    public DateField(java.lang.String label, int mode){
         //TODO codavaj!!
    }

    /**
     * Creates a date field in which calendar calculations are based on specific TimeZone object and the default calendaring system for the current locale. The value of the DateField is initially in the
     * uninitialized
     * state. If timeZone is null, the system's default time zone is used.
     * label - item labelmode - the input mode, one of DATE, TIME or DATE_TIMEtimeZone - a specific time zone, or null for the default time zone
     * - if the input mode's value is invalid
     */
    public DateField(java.lang.String label, int mode, java.util.TimeZone timeZone){
         //TODO codavaj!!
    }

    /**
     * Returns date value of this field. Returned value is null if field value is not initialized. The date object is constructed according the rules of locale specific calendaring system and defined time zone. In TIME mode field the date components are set to the
     * zero epoch
     * value of January 1, 1970. If a date object that presents time beyond one day from this
     * zero epoch
     * then this field is in
     * not initialized
     * state and this method returns null. In DATE mode field the time component of the calendar is set to zero when constructing the date object.
     */
    public java.util.Date getDate(){
        return null; //TODO codavaj!!
    }

    /**
     * Gets input mode for this date field. Valid input modes are DATE, TIME and DATE_TIME.
     */
    public int getInputMode(){
        return 0; //TODO codavaj!!
    }

    /**
     * Sets a new value for this field. null can be passed to set the field state to
     * not initialized
     * state. The input mode of this field defines what components of passed Date object is used.
     * In TIME input mode the date components must be set to the zero epoch value of January 1, 1970. If a date object that presents time beyond one day then this field is in not initialized state. In TIME input mode the date component of Date object is ignored and time component is used to precision of minutes.
     * In DATE input mode the time component of Date object is ignored.
     * In DATE_TIME input mode the date and time component of Date are used but only to precision of minutes.
     */
    public void setDate(java.util.Date date){
        return; //TODO codavaj!!
    }

    /**
     * Set input mode for this date field. Valid input modes are DATE, TIME and DATE_TIME.
     */
    public void setInputMode(int mode){
        return; //TODO codavaj!!
    }

}
