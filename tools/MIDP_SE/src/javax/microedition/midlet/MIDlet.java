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

package javax.microedition.midlet;

import java.util.HashMap;
import java.util.Map;
import javax.microedition.io.ConnectionNotFoundException;

/**
 * A MIDlet is a MID Profile application. The application must extend this class to allow the application management software to control the MIDlet and to be able to retrieve properties from the application descriptor and notify and request state changes. The methods of this class allow the application management software to create, start, pause, and destroy a MIDlet. A MIDlet is a set of classes designed to be run and controlled by the application management software via this interface. The states allow the application management software to manage the activities of multiple MIDlets within a runtime environment. It can select which MIDlets are active at a given time by starting and pausing them individually. The application management software maintains the state of the MIDlet and invokes methods on the MIDlet to notify the MIDlet of change states. The MIDlet implements these methods to update its internal activities and resource usage as directed by the application management software. The MIDlet can initiate some state changes itself and notifies the application management software of those state changes by invoking the appropriate methods.
 * Note: The methods on this interface signal state changes. The state change is not considered complete until the state change method has returned. It is intended that these methods return quickly.
 */
public abstract class MIDlet{
    private Map<String, String> values = new HashMap<String, String>();
    
    /**
     * Protected constructor for subclasses. The application management software is responsible for creating MIDlets and creation of MIDlets is restricted. MIDlets should not attempt to create other MIDlets.
     * - unless the application management software is creating the MIDlet.
     */
    protected MIDlet(){
         //TODO codavaj!!
    }

    /**
     * Get the status of the specified permission. If no API on the device defines the specific permission requested then it must be reported as denied. If the status of the permission is not known because it might require a user interaction then it should be reported as unknown.
     */
    public final int checkPermission(java.lang.String permission){
        return 0; //TODO codavaj!!
    }

    /**
     * Signals the MIDlet to terminate and enter the Destroyed state. In the destroyed state the MIDlet must release all resources and save any persistent state. This method may be called from the Paused or Active states.
     * MIDlets should perform any operations required before being terminated, such as releasing resources or saving preferences or state.
     * Note: The MIDlet can request that it not enter the Destroyed state by throwing an MIDletStateChangeException. This is only a valid response if the unconditional flag is set to false. If it is true the MIDlet is assumed to be in the Destroyed state regardless of how this method terminates. If it is not an unconditional request, the MIDlet can signify that it wishes to stay in its current state by throwing the MIDletStateChangeException. This request may be honored and the destroy() method called again at a later time.
     * If a Runtime exception occurs during destroyApp then they are ignored and the MIDlet is put into the Destroyed state.
     */
    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;

    /**
     * Provides a MIDlet with a mechanism to retrieve named properties from the application management software. The properties are retrieved from the combination of the application descriptor file and the manifest. For trusted applications the values in the manifest MUST NOT be overridden by those in the application descriptor. If they differ, the MIDlet will not be installed on the device. For untrusted applications, if an attribute in the descriptor has the same name as an attribute in the manifest the value from the descriptor is used and the value from the manifest is ignored.
     */
    public final java.lang.String getAppProperty(java.lang.String key){
        return values.get(key);
    }

    public void putAppProperty(String key, String value) {
        values.put(key, value);
    }
    
    /**
     * Used by an MIDlet to notify the application management software that it has entered into the Destroyed state. The application management software will not call the MIDlet's destroyApp method, and all resources held by the MIDlet will be considered eligible for reclamation. The MIDlet must have performed the same operations (clean up, releasing of resources etc.) it would have if the MIDlet.destroyApp() had been called.
     */
    public final void notifyDestroyed(){
        return; //TODO codavaj!!
    }

    /**
     * Notifies the application management software that the MIDlet does not want to be active and has entered the Paused state. Invoking this method will have no effect if the MIDlet is destroyed, or if it has not yet been started.
     * It may be invoked by the MIDlet when it is in the Active state.
     * If a MIDlet calls notifyPaused(), in the future its startApp() method may be called make it active again, or its destroyApp() method may be called to request it to destroy itself.
     * If the application pauses itself it will need to call resumeRequest to request to reenter the active state.
     */
    public final void notifyPaused(){
        return; //TODO codavaj!!
    }

    /**
     * Signals the MIDlet to enter the Paused state. In the Paused state the MIDlet must release shared resources and become quiescent. This method will only be called called when the MIDlet is in the Active state.
     * If a Runtime exception occurs during pauseApp the MIDlet will be destroyed immediately. Its destroyApp will be called allowing the MIDlet to cleanup.
     */
    protected abstract void pauseApp();

    /**
     * Requests that the device handle (for example, display or install) the indicated URL.
     * If the platform has the appropriate capabilities and resources available, it SHOULD bring the appropriate application to the foreground and let the user interact with the content, while keeping the MIDlet suite running in the background. If the platform does not have appropriate capabilities or resources available, it MAY wait to handle the URL request until after the MIDlet suite exits. In this case, when the requesting MIDlet suite exits, the platform MUST then bring the appropriate application (if one exists) to the foreground to let the user interact with the content.
     * This is a non-blocking method. In addition, this method does NOT queue multiple requests. On platforms where the MIDlet suite must exit before the request is handled, the platform MUST handle only the last request made. On platforms where the MIDlet suite and the request can be handled concurrently, each request that the MIDlet suite makes MUST be passed to the platform software for handling in a timely fashion.
     * If the URL specified refers to a MIDlet suite (either an Application Descriptor or a JAR file), the application handling the request MUST interpret it as a request to install the named package. In this case, the platform's normal MIDlet suite installation process SHOULD be used, and the user MUST be allowed to control the process (including cancelling the download and/or installation). If the MIDlet suite being installed is an update of the currently running MIDlet suite, the platform MUST first stop the currently running MIDlet suite before performing the update. On some platforms, the currently running MIDlet suite MAY need to be stopped before any installations can occur.
     * If the URL specified is of the form tel:number, as specified in RFC2806, then the platform MUST interpret this as a request to initiate a voice call. The request MUST be passed to the phone application to handle if one is present in the platform. The phone application, if present, MUST be able to set up local and global phone calls and also perform DTMF post dialing. Not all elements of RFC2806 need be implemented, especially the area-specifier or any other requirement on the terminal to know its context. The isdn-subaddress, service-provider and future-extension may also be ignored. Pauses during dialing are not relevant in some telephony services.
     * Devices MAY choose to support additional URL schemes beyond the requirements listed above.
     * Many of the ways this method will be used could have a financial impact to the user (e.g. transferring data through a wireless network, or initiating a voice call). Therefore the platform MUST ask the user to explicitly acknowlege each request before the action is taken. Implementation freedoms are possible so that a pleasant user experience is retained. For example, some platforms may put up a dialog for each request asking the user for permission, while other platforms may launch the appropriate application and populate the URL or phone number fields, but not take the action until the user explicitly clicks the load or dial buttons.
     */
    public final boolean platformRequest(java.lang.String URL) throws ConnectionNotFoundException {
        return false; //TODO codavaj!!
    }

    /**
     * Provides a MIDlet with a mechanism to indicate that it is interested in entering the Active state. Calls to this method can be used by the application management software to determine which applications to move to the Active state.
     * When the application management software decides to activate this application it will call the startApp method.
     * The application is generally in the Paused state when this is called. Even in the paused state the application may handle asynchronous events such as timers or callbacks.
     */
    public final void resumeRequest(){
        return; //TODO codavaj!!
    }

    /**
     * Signals the MIDlet that it has entered the Active state. In the Active state the MIDlet may hold resources. The method will only be called when the MIDlet is in the Paused state.
     * Two kinds of failures can prevent the service from starting, transient and non-transient. For transient failures the MIDletStateChangeException exception should be thrown. For non-transient failures the notifyDestroyed method should be called.
     * If a Runtime exception occurs during startApp the MIDlet will be destroyed immediately. Its destroyApp will be called allowing the MIDlet to cleanup.
     */
    protected abstract void startApp() throws MIDletStateChangeException;

}
