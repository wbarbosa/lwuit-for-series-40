package com.nokia.mid.iapinfo;

/**
 *
 * @author Shai
 */
public abstract class IAPInfo {
    public static IAPInfo 	getIAPInfo() {
        return new IAPInfo() {

            public AccessPoint[] getAccessPoints() {
                return null;
            }

            public AccessPoint[] getConnectionPreferences() throws IAPInfoException {
                return null;
            }

            public AccessPoint getLastUsedAccessPoint() throws IAPInfoException {
                return null;
            }
        };
    }

    public abstract  AccessPoint[] 	getAccessPoints()  throws IAPInfoException;

    public abstract AccessPoint[] getConnectionPreferences()
                                                throws IAPInfoException;
    public abstract  AccessPoint 	getLastUsedAccessPoint() throws IAPInfoException;
}
