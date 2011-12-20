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

package javax.microedition.io;
/**
 * The PushRegistry maintains a list of inbound connections. An application can register the inbound connections with an entry in the application descriptor file or dynamically by calling the registerConnection method.
 * While an application is running, it is responsible for all I/O operations associated with the inbound connection. When the application is not running, the application management software(AMS) listens for inbound notification requests. When a notification arrives for a registered MIDlet, the AMS will start the MIDlet via the normal invocation of MIDlet.startApp method.
 * To avoid collisions on inbound generic connections, the application descriptor file MUST include information about static connections that are needed by the MIDlet suite. If all the static Push declarations in the application descriptor can not be fulfilled during the installation, the user MUST be notified that there are conflicts and the MIDlet suite MUST NOT be installed. (See Over The Air User Initiated Provisioning Specification section for errors reported in the event of conflicts.) Conditions when the declarations can not be fulfilled include: syntax errors in the Push attributes, declaration for a connection end point (e.g. port number) that is already reserved in the device, declaration for a protocol that is not supported for Push in the device, and declaration referencing a MIDlet class that is not listed in the MIDlet-n attributes of the same application descriptor. If the MIDlet suite can function meaningfully even if a Push registration can't be fulfilled, it MUST register the Push connections using the dynamic registration methods in the PushRegistry.
 * A conflict-free installation reserves each requested connection for the exclusive use of the MIDlets in the suite. While the suite is installed, any attempt by other applications to open one of the reserved connections will fail with an IOException. A call from a MIDlet to Connector.open() on a connection reserved for its suite will always succeed, assuming the suite does not already have the connection open.
 * If two MIDlet suites have a static push connection in common, they cannot be installed together and both function correctly. The end user would typically have to uninstall one before being able to successfully install the other.
 * Each push registration entry contains the following information :
 * The MIDP 2.0 specification defines the syntax for datagram and socket inbound connections. When other specifications define push semantics for additional connection types, they must define the expected syntax for the filter field, as well as the expected format for the connection URL string.
 * The following is a sample descriptor file entry that would reserve a stream socket at port 79 and a datagram connection at port 50000. (Port numbers are maintained by IANA and cover well-known, user-registered and dynamic port numbers) [See IANA Port Number Registry]
 * The requirements for buffering of messages are specific to each protocol used for Push and are defined separately for each protocol. There is no general requirement related to buffering that would apply to all protocols. If the implementation buffers messages, these messages MUST be provided to the MIDlet when the MIDlet is started and it opens the related Connection that it has registered for Push.
 * When datagram connections are supported with Push, the implementation MUST guarantee that when a MIDlet registered for datagram Push is started in response to an incoming datagram, at least the datagram that caused the startup of the MIDlet is buffered by the implementation and will be available to the MIDlet when the MIDlet opens the UDPDatagramConnection after startup.
 * When socket connections are supported with Push, the implementation MUST guarantee that when a MIDlet registered for socket Push is started in response to an incoming socket connection, this connection can be accepted by the MIDlet by opening the ServerSocketConnection after startup, provided that the connection hasn't timed out meanwhile.
 * Not all generic connections will be appropriate for use as push application transport. Even if a protocol is supported on the device as an inbound connection type, it is not required to be enabled as a valid push mechanism. e.g. a platform might support server socket connections in a MIDlet, but might not support inbound socket connections for push launch capability. A ConnectionNotFoundException is thrown from the registerConnection and from the registerAlarm methods, when the platform does not support that optional capability.
 * Responsibility for registered push connections is shared between the AMS and the MIDlet that handles the I/O operations on the inbound connection. To prevent any data from being lost, an application is responsible for all I/O operations on the connection from the time it calls Connector.open() until it calls Connection.close().
 * The AMS listens for inbound connection notifications. This MAY be handled via a native callback or polling mechanism looking for new inbound data. The AMS is responsible for enforcing the Security of PushRegistry and presenting notifications (if any) to the user before invoking the MIDlet suite.
 * The AMS is responsible for the shutdown of any running applications (if necessary) prior to the invocation of the push MIDlet method.
 * After the AMS has started the push application, the MIDlet is responsible for opening the connections and for all subsequent I/O operations. An application that needs to perform blocking I/O operations SHOULD use a separate thread to allow for interactive user operations. Once the application has been started and the connection has been opened, the AMS is no longer responsible for listening for push notifications for that connection. The application is responsible for reading all inbound data.
 * If an application has finished with all inbound data it MAY close() the connection. If the connection is closed, then neither the AMS nor the application will be listening for push notifications. Inbound data could be lost, if the application closes the connection before all data has been received.
 * When the application is destroyed, the AMS resumes its responsiblity to watch for inbound connections.
 * A push application SHOULD behave in a predictable manner when handling asynchronous data via the push mechanism. A well behaved application SHOULD inform the user that data has been processed. (While it is possible to write applications that do not use any user visible interfaces, this could lead to a confused end user experience to launch an application that only performs a background function.)
 * There are cases when defining a well known port registered with IANA is not necessary. Simple applications may just wish to exchange data using a private protocol between a MIDlet and server application.
 * To accomodate this type of application, a mechanism is provided to dynamically allocate a connection and to register that information, as if it was known, when the application was installed. This information can then be sent to an agent on the network to use as the mechanism to communicate with the registered MIDlet.
 * For instance, if a UDPDatagramConnection is opened and a port number, was not specified, then the application is requesting a dynamic port to be allocated from the ports that are currently available. By calling PushRegistry.registerConnection() the MIDlet informs the AMS that it is the target for inbound communication, even after the MIDlet has been destroyed (See MIDlet life cycle for definition of "destroyed" state). If the application is deleted from the phone, then its dynamic communication connections are unregistered automatically.
 * During installation each MIDlet that is expecting inbound communication on a well known address has the information recorded with the AMS from the push registration attribute in the manifest or application descriptor file. Once the installation has been successfully completed, (e.g. For the OTA recommended practices - when the Installation notification message has been successfully transmitted, the application is officially installed.) the MIDlet MAY then receive inbound communication. e.g. the push notification event.
 * When the AMS is started, it checks the list of registered connections and begins listening for inbound communication. When a notification arrives the AMS starts the registered MIDlet. The MIDlet then opens the connection with Connector.open() method to perform whatever I/O operations are needed for the particular connection type. e.g. for a server socket the application uses acceptAndOpen() to get the socket connected and for a datagram connection the application uses receive() to read the delivered message.
 * For message oriented transports the inbound message MAY be read by the AMS and saved for delivery to the MIDlet when it requests to read the data. For stream oriented transports the connection MAY be lost if the connection is not accepted before the server end of the connection request timeouts.
 * When a MIDlet is started in response to a registered push connection notification, it is platform dependent what happens to the current running application. The MIDlet life cycle defines the expected behaviors that an interrupted MIDlet could see from a call to pauseApp() or from destroyApp().
 * Usage scenario 1: The suite includes a MIDlet with a well known port for communication. During the startApp processing a thread is launched to handle the incoming data. Using a separate thread is the recommended practice for avoiding conflicts between blocking I/O operations and the normal user interaction events. The thread continues to receive messages until the MIDlet is destroyed.
 * In this sample, the descriptor file includes a static push connection registration. It also includes an indication that this MIDlet requires permission to use a datagram connection for inbound push messages. (See Security of Push Functions in the package overview for details about MIDlet permissions.) Note: this sample is appropriate for bursts of datagrams. It is written to loop on the connection, processing received messages.
 * Usage scenario 2: The suite includes a MIDlet that dynamically allocates port the first time it is started.
 * In this sample, the descriptor file includes an entry indicating that the application will need permission to use the datagram connection for inbound push messages. The dynamic connection is allocated in the constructor the first time it is run. The open connection is used during this session and can be reopened in a subsequent session in response to a inbound connection notification.
 * Since: MIDP 2.0
 */
public class PushRegistry{
    /**
     * Retrieve the registered filter for a requested connection.
     */
    public static java.lang.String getFilter(java.lang.String connection){
        return null; //TODO codavaj!!
    }

    /**
     * Retrieve the registered MIDlet for a requested connection.
     */
    public static java.lang.String getMIDlet(java.lang.String connection){
        return null; //TODO codavaj!!
    }

    /**
     * Return a list of registered connections for the current MIDlet suite.
     */
    public static java.lang.String[] listConnections(boolean available){
        return new java.lang.String[0]; //TODO codavaj!!
    }

    /**
     * Register a time to launch the specified application. The PushRegistry supports one outstanding wake up time per MIDlet in the current suite. An application is expected to use a TimerTask for notification of time based events while the application is running.
     * If a wakeup time is already registered, the previous value will be returned, otherwise a zero is returned the first time the alarm is registered.
     */
    public static long registerAlarm(java.lang.String midlet, long time) throws java.lang.ClassNotFoundException, ConnectionNotFoundException{
        return 0l; //TODO codavaj!!
    }

    /**
     * Register a dynamic connection with the application management software. Once registered, the dynamic connection acts just like a connection preallocated from the descriptor file.
     * The arguments for the dynamic connection registration are the same as the Push Registration Attribute used for static registrations.
     * If the connection or filter arguments are null, then an IllegalArgumentException will be thrown. If the midlet argument is null a ClassNotFoundException will be thrown.
     */
    public static void registerConnection(java.lang.String connection, java.lang.String midlet, java.lang.String filter) throws java.lang.ClassNotFoundException, java.io.IOException{
        return; //TODO codavaj!!
    }

    /**
     * Remove a dynamic connection registration.
     */
    public static boolean unregisterConnection(java.lang.String connection){
        return false; //TODO codavaj!!
    }

}
