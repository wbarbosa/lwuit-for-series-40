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
 * This interface defines a logical serial port connection. A "logical" serial port is defined as a logical connection through which bytes are transferring serially. The logical serial port is defined within the underlying operating system and may not necessarily correspond to a physical RS-232 serial port. For instance, IrDA IRCOMM ports can commonly be configured as a logical serial port within the operating system so that it can act as a "logical" serial port.
 * A comm port is accessed using a Generic Connection Framework string with an explicit port identifier and embedded configuration parameters, each separated with a semi-colon (;).
 * Only one application may be connected to a particular serial port at a given time. An java.io.IOException is thrown, if an attempt is made to open the serial port with Connector.open() and the connection is already open.
 * A URI with the type and parameters is used to open the connection. The scheme (defined in RFC 2396) must be: comm:port identifier[optional parameters]
 * The first parameter must be a port identifier, which is a logical device name. These identifiers are most likely device specific and should be used with care.
 * The valid identifiers for a particular device and OS can be queried through the method System.getProperty() using the key "microedition.commports". A comma separated list of ports is returned which can be combined with a comm: prefix as the URL string to be used to open a serial port connection. (See port naming convention below.)
 * Any additional parameters must be separated by a semi-colon (;) and spaces are not allowed in the string. If a particular optional parameter is not applicable to a particular port, the parameter MAY be ignored. The port identifier MUST NOT contain a semi-colon (;).
 * Legal parameters are defined by the definition of the parameters below. Illegal or unrecognized parameters cause an IllegalArgumentException. If the value of a parameter is supported by the device, it must be honored. If the value of a parameter is not supported a java.io.IOException is thrown. If a baudrate parameter is requested, it is treated in the same way that the setBaudRate method handles baudrates. e.g., if the baudrate requested is not supported the system MAY substitute a valid baudrate, which can be discovered using the getBaudRate method.
 * Parameter Default Description baudrate platform dependent The speed of the port. bitsperchar 8 The number bits per character(7 or 8). stopbits 1 The number of stop bits per char(1 or 2) parity none The parity can be odd, even, or none. blocking on If on, wait for a full buffer when reading. autocts on If on, wait for the CTS line to be on before writing. autorts on If on, turn on the RTS line when the input buffer is not full. If off, the RTS line is always on.
 * The URI must conform to the BNF syntax specified below. If the URI does not conform to this syntax, an IllegalArgumentException is thrown.
 * Access to serial ports is restricted to prevent unauthorized transmission or reception of data. The security model applied to the serial port connection is defined in the implementing profile. The security model may be applied on the invocation of the Connector.open() method with a valid serial port connection string. Should the application not be granted access to the serial port through the profile authorization scheme, a java.lang.SecurityException will be thrown from the Connector.open() method. The security model MAY also be applied during execution, specifically when the methods openInputStream(), openDataInputStream(), openOutputStream(), and openDataOutputStream() are invoked.
 * The following example shows how a CommConnection would be used to access a simple loopback program.
 * The following example shows how a CommConnection would be used to discover available comm ports.
 * Logical port names can be defined to match platform naming conventions using any combination of alphanumeric characters. However, it is recommended that ports be named consistently among the implementations of this class according to a proposed convention. VM implementations should follow the following convention: Port names contain a text abbreviation indicating port capabilities followed by a sequential number for the port. The following device name types should be used:
 * This naming scheme allows API users to generally determine the type of port that they would like to use. For instance, if a application desires to "beam" a piece of data, the app could look for "IR#" ports for opening the connection. The alternative is a trial and error approach with all available ports.
 * Since: MIDP 2.0
 */
public interface CommConnection extends StreamConnection{
    /**
     * Gets the baudrate for the serial port connection.
     */
    public abstract int getBaudRate();

    /**
     * Sets the baudrate for the serial port connection. If the requested baudrate is not supported on the platform, then the system MAY use an alternate valid setting. The alternate value can be accessed using the getBaudRate method.
     */
    public abstract int setBaudRate(int baudrate);

}
