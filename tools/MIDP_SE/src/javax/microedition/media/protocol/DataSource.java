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

package javax.microedition.media.protocol;
/**
 * A DataSource is an abstraction for media protocol-handlers. It hides the details of how the data is read from source--whether the data is coming from a file, streaming server or proprietary delivery mechanism. It provides the methods for a Player to access the input data.
 * An application-defined protocol can be implemented with a custom DataSource. A Player can then be created for playing back the media from the custom DataSource using the Manager.createPlayer method.
 * There are a few reasons why one would choose to implement a DataSource as opposed to an InputStream for a custom protocol: DataSource/SourceStream provides the random seeking API that is not supported by an InputStream. i.e., if the custom protocol requires random seeking capabilities, a custom DataSource can be used. DataSource/SourceStream supports the concept of transfer size that is more suited for frame-delimited data, e.g. video.
 * A DataSource contains a set of SourceStreams. Each SourceStream represents one elementary data stream of the source. In the most common case, a DataSource only provides one SourceStream. A DataSource may provide multiple SourceStreams if it encapsulates multiple elementary data streams.
 * Each of the SourceStreams provides the methods to allow a Player to read data for processing.
 * DataSource manages the life-cycle of the media source by providing a simple connection protocol.
 * DataSource implements Controllable which provides extra controls via some type-specific Control interfaces. getControl and getControls can only be called when the DataSource is connected. An IllegalStateException will be thrown otherwise.
 * See Also:Manager, SourceStream, ContentDescriptor
 */
public abstract class DataSource implements javax.microedition.media.Controllable{
    /**
     * Construct a DataSource from a locator. This method should be overloaded by subclasses; the default implementation just keeps track of the locator.
     * Parameters:locator - The locator that describes the DataSource.
     */
    public DataSource(java.lang.String locator){
         //TODO codavaj!!
    }

    /**
     * Open a connection to the source described by the locator and initiate communication.
     */
    public abstract void connect() throws java.io.IOException;

    /**
     * Close the connection to the source described by the locator and free resources used to maintain the connection.
     * If no resources are in use, disconnect is ignored. If stop hasn't already been called, calling disconnect implies a stop.
     */
    public abstract void disconnect();

    /**
     * Get a string that describes the content-type of the media that the source is providing.
     */
    public abstract java.lang.String getContentType();

    /**
     * Get the locator that describes this source. Returns null if the locator hasn't been set.
     */
    public java.lang.String getLocator(){
        return null; //TODO codavaj!!
    }

    /**
     * Get the collection of streams that this source manages. The collection of streams is entirely content dependent. The MIME type of this DataSource provides the only indication of what streams may be available on this connection.
     */
    public abstract javax.microedition.media.protocol.SourceStream[] getStreams();

    /**
     * Initiate data-transfer. The start method must be called before data is available for reading.
     */
    public abstract void start() throws java.io.IOException;

    /**
     * Stop the data-transfer. If the DataSource has not been connected and started, stop is ignored.
     */
    public abstract void stop() throws java.io.IOException;

}
