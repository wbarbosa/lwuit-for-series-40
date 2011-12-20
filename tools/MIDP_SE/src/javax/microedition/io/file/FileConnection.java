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

package javax.microedition.io.file;
/**
 * This interface is intended to access files or directories that are located on removeable media and/or file systems on a device.
 * Device internal filesystems in memory may also be accessed through this class as well, provided there is underlying hardware and OS support. If file connections are not supported to a particular media or file system, attempts to open a file connection to the media or file system through Connector.open() results in an javax.microedition.io.IOException being thrown.
 * The format of the input string used to access a FileConnection through Connector.open() must follow the format of a fully-qualified, absolute path file name as described by the file URL format in IETF RFCs 1738 2396. Further detail for the File URL format can be found in the javax.microedition.io.file package description.
 * A single connection object only references a single file or directory at a time. In some cases, a connection object can be reused to refer to a different file or directory than it was originally associated with (see setFileConnection(java.lang.String)). In general, the best approach to reference a different file or directory is by establishing a completely separate connection through the Connector.open() method.
 * File connection is different from other Generic Connection Framework connections in that a connection object can be successfully returned from the Connector.open() method without actually referencing an existing entity (in this case, a file or directory). This behavior allows the creation of new files and directories on a file system. For example, the following code can be used to create a new file on a file system, where CFCard is a valid existing file system root name for a given implementation:
 * Developers should always check for the file's or directory's existence after a connection is established to determine if the file or directory actually exists. Similarly, files or directories can be deleted using the delete() method, and developers should close the connection immediately after deletion to prevent exceptions from accessing a connection to a non-existent file or directory.
 * A file connection's open status is unaffected by the opening and closing of input and output streams from the file connection; the file connection stays open until close() is invoked on the FileConnection instance. Input and output streams may be opened and closed multiple times on a FileConnection instance.
 * All FileConnection instances have one underlying InputStream and one OutputStream. Opening a DataInputStream counts as opening an InputStream, and opening a DataOutputStream counts as opening an OutputStream. A FileConnection instance can have only one InputStream and one OutputStream open at any one time. Trying to open more than one InputStream or more than one OutputStream from a StreamConnection causes an IOException. Trying to open an InputStream or an OutputStream after the FileConnection has been closed causes an IOException.
 * The inherited StreamConnection methods in a FileConnection instance are not synchronized. The only stream method that can be called safely from another thread is close. When close is invoked on a stream that is executing in another thread, any pending I/O method MUST throw an InterruptedIOException. In the above case, implementations SHOULD try to throw the exception in a timely manner. When all open streams have been closed, and when the FileConnection is closed, any pending I/O operations MUST be interrupted in a timely manner.
 * Data written to the output streams of these FileConnection objects is not guaranteed to be flushed to the stream's destination (and subsequently made available to any input streams) until either flush() or close() is invoked on the stream.
 * Access to file connections is restricted to prevent unauthorized manipulation of data. The access security model applied to the file connection is defined by the implementing profile. The security model is applied on the invocation of the Connector.open() method with a valid file connection string. The mode provided in the open() method (Connector.READ_WRITE by default) indicates the application's request for access rights for the indicated file or directory and is therefore checked against the security scheme. All three connections modes (READ_WRITE, WRITE_ONLY, and READ_ONLY) are supported for a file connection and determine the access requested from the security model.
 * The security model is also applied during use of the returned FileConnection, specifically when the methods openInputStream(), openDataInputStream(), openOutputStream(), and openDataOutputStream() are invoked. These methods have implied request for access rights (i.e. input stream access is requesting read access, and output stream access is requesting write access). Should the application not be granted the appropriate read or write access to the file or file system by the profile authorization scheme, a java.lang.SecurityException is thrown.
 * File access through the File Connection API may be restricted to files that are within a public context and not deemed private or sensitive. This restriction is intended to protect the device's and other users' files and data from both malicious and unintentional access. RMS databases cannot be accessed using the File Connection API. Access to files and directories that are private to another application, files and directories that are private to a different user than the current user, system configuration files, and device and OS specific files and directories may be restricted. In these situations, a java.lang.SecurityException is thrown from the Connector.open() method if the file, file system, or directory is not allowed to be accessed.
 * Since: FileConnection 1.0 See Also:FileSystemRegistry
 */
public interface FileConnection extends javax.microedition.io.StreamConnection{
    /**
     * Determines the free memory that is available on the file system the file or directory resides on. This may only be an estimate and may vary based on platform-specific file system blocking and metadata information.
     */
    public abstract long availableSize();

    /**
     * Checks if the file or directory is readable. This method checks the attributes associated with a file or directory by the underlying file system. Some file systems may not support associating a read attribute with a file, in which case this method returns true.
     */
    public abstract boolean canRead();

    /**
     * Checks if the file or directory is writable. This method checks the attributes associated with a file or directory by the underlying file system. Some file systems may not support associating a write attribute with a file, in which case this method returns true.
     */
    public abstract boolean canWrite();

    /**
     * Creates a file corresponding to the file string provided in the Connector.open() method for this FileConnection. The file is created immediately on the actual file system upon invocation of this method. Files are created with zero length and data can be put into the file through output streams opened on the file. This method does not create any directories specified in the file's path.
     */
    public abstract void create() throws java.io.IOException;

    /**
     * Deletes the file or directory specified in the Connector.open() URL. The file or directory is deleted immediately on the actual file system upon invocation of this method. All open input and output streams are automatically flushed and closed. Attempts to further use those streams result in an IOException. The FileConnection instance object remains open and available for use.
     */
    public abstract void delete() throws java.io.IOException;

    /**
     * Determines the size in bytes on a file system of all of the files that are contained in a directory.
     */
    public abstract long directorySize(boolean includeSubDirs) throws java.io.IOException;

    /**
     * Checks if the file or directory specified in the URL passed to the Connector.open() method exists.
     */
    public abstract boolean exists();

    /**
     * Determines the size of a file on the file system. The size of a file always represents the number of bytes contained in the file; there is no pre-allocated but empty space in a file. Users should perform an explicit flush() on any open output streams to the file prior to invoking this method to ensure accurate results.
     */
    public abstract long fileSize() throws java.io.IOException;

    /**
     * Returns the name of a file or directory excluding the URL schema and all paths. Directories are denoted with a trailing slash "/" in their returned name. The String resulting from this method looks as follows:
     * directory
     * / or
     * filename.extension
     * or if no file extension
     * filename
     */
    public abstract java.lang.String getName();

    /**
     * Returns the path excluding the file or directory name and the "file" URL schema and host from where the file or directory specified in the Connector.open() method is opened.
     * can be appended to this value to get a fully qualified path filename. The String resulting from this method looks as follows: /
     * root
     * /
     * directory
     * /
     */
    public abstract java.lang.String getPath();

    /**
     * Returns the full file URL including the scheme, host, and path from where the file or directory specified in the Connector.open() method is opened. The string returned is in an escaped ASCII format as defined by RFC 2396. The resulting String looks as follows: file://
     * host
     * /
     * root
     * /
     * directory
     * /
     * filename.extension
     * or file://
     * host
     * /
     * root
     * /
     * directory
     * /
     * directoryname
     * /
     */
    public abstract java.lang.String getURL();

    /**
     * Checks if the URL passed to the Connector.open() is a directory.
     */
    public abstract boolean isDirectory();

    /**
     * Checks if the file is hidden. The exact definition of hidden is system-dependent. For example, on UNIX systems a file is considered to be hidden if its name begins with a period character ('.'). On Win32 and FAT file systems, a file is considered to be hidden if it has been marked as such in the file's attributes. If hidden files are not supported on the referenced file system, this method always returns false.
     */
    public abstract boolean isHidden();

    /**
     * Returns an indication of whether the file connection is currently open or not.
     */
    public abstract boolean isOpen();

    /**
     * Returns the time that the file denoted by the URL specified in the Connector.open() method was last modified.
     */
    public abstract long lastModified();

    /**
     * Gets a list of all visible files and directories contained in a directory. The directory is the connection's target as specified in Connector.open().
     */
    public abstract java.util.Enumeration list() throws java.io.IOException;

    /**
     * Gets a filtered list of files and directories contained in a directory. The directory is the connection's target as specified in Connector.open().
     */
    public abstract java.util.Enumeration list(java.lang.String filter, boolean includeHidden) throws java.io.IOException;

    /**
     * Creates a directory corresponding to the directory string provided in the Connector.open() method. The directory is created immediately on the actual file system upon invocation of this method. Directories in the specified path are not recursively created and must be explicitly created before subdirectories can be created.
     */
    public abstract void mkdir() throws java.io.IOException;

    /**
     * Open and return a data input stream for a connection. The connection's target must already exist and be accessible for the input stream to be created.
     */
    public abstract java.io.DataInputStream openDataInputStream() throws java.io.IOException;

    /**
     * Open and return a data output stream for a connection. The output stream is positioned at the start of the file. Writing data to the output stream overwrites the contents of the files (i.e. does not insert data). Writing data to output streams beyond the current end of file automatically extends the file size. The connection's target must already exist and be accessible for the output stream to be created.
     * should be used to position an output stream to a different position in the file.
     * Changes made to a file through an output stream may not be immediately made to the actual file residing on the file system because platform and implementation specific use of caching and buffering of the data. Stream contents and file length extensions are not necessarily visible outside of the application immediately unless flush() is called on the stream. The returned output stream is automatically and synchronously flushed when it is closed.
     */
    public abstract java.io.DataOutputStream openDataOutputStream() throws java.io.IOException;

    /**
     * Open and return an input stream for a connection. The connection's target must already exist and be accessible for the input stream to be created.
     */
    public abstract java.io.InputStream openInputStream() throws java.io.IOException;

    /**
     * Open and return an output stream for a connection. The output stream is positioned at the start of the file. Writing data to the output stream overwrites the contents of the files (i.e. does not insert data). Writing data to output streams beyond the current end of file automatically extends the file size. The connection's target must already exist and be accessible for the output stream to be created.
     * should be used to position an output stream to a different position in the file.
     * Changes made to a file through an output stream may not be immediately made to the actual file residing on the file system because platform and implementation specific use of caching and buffering of the data. Stream contents and file length extensions are not necessarily visible outside of the application immediately unless flush() is called on the stream. The returned output stream is automatically and synchronously flushed when it is closed.
     */
    public abstract java.io.OutputStream openOutputStream() throws java.io.IOException;

    /**
     * This method opens an output stream and positions it at the indicated byte offset in the file. Data written to the returned output stream at that position overwrites any existing data until EOF is reached, and then additional data is appended. The connection's target must already exist and be accessible for the output stream to be created.
     * Changes made to a file through an output stream may not be immediately made to the actual file residing on the file system because platform and implementation specific use of caching and buffering of the data. Stream contents and file length extensions are not necessarily visible outside of the application immediately unless flush() is called on the stream. The returned output stream is automatically and synchronously flushed when it is closed.
     */
    public abstract java.io.OutputStream openOutputStream(long byteOffset) throws java.io.IOException;

    /**
     * Renames the selected file or directory to a new name in the same directory. The file or directory is renamed immediately on the actual file system upon invocation of this method. No file or directory by the original name exists after this method call. All previously open input and output streams are automatically flushed and closed. Attempts to further use those streams result in an IOException. The FileConnection instance object remains open and available for use, referring now to the file or directory by its new name.
     */
    public abstract void rename(java.lang.String newName) throws java.io.IOException;

    /**
     * Resets this FileConnection object to another file or directory. This allows reuse of the FileConnection object for directory traversal. The current FileConnection object must refer to a directory, and the new file or directory must exist within this directory, or may be the string ".." used to indicate the parent directory for the current connection). The FileConnection instance object remains open and available for use, referring now to the newly specified file or directory.
     */
    public abstract void setFileConnection(java.lang.String fileName) throws java.io.IOException;

    /**
     * Sets the hidden attribute of the selected file to the value provided. The attribute is applied to the file on the actual file system immediately upon invocation of this method if the file system and platform support it. If the file system doesn't support a hidden attribute, this method is ignored and isHidden() always returns false. Since the exact definition of hidden is system-dependent, this method only works on file systems that support a settable file attribute. For example, on Win32 and FAT file systems, a file may be considered hidden if it has been marked as such in the file's attributes; therefore this method is applicable. However on UNIX systems a file may be considered to be hidden if its name begins with a period character ('.'). In the UNIX case, this method may be ignored and the method to make a file hidden may be the rename() method.
     */
    public abstract void setHidden(boolean hidden) throws java.io.IOException;

    /**
     * Sets the file or directory readable attribute to the indicated value. The readable attribute for the file on the actual file system is set immediately upon invocation of this method. If the file system doesn't support a settable read attribute, this method is ignored and canRead() always returns true.
     */
    public abstract void setReadable(boolean readable) throws java.io.IOException;

    /**
     * Sets the selected file or directory writable attribute to the indicated value. The writable attribute for the file on the actual file system is set immediately upon invocation of the method. If the file system doesn't support a settable write attribute, this method is ignored and canWrite() always returns true.
     */
    public abstract void setWritable(boolean writable) throws java.io.IOException;

    /**
     * Determines the total size of the file system the connection's target resides on.
     */
    public abstract long totalSize();

    /**
     * Truncates the file, discarding all data from the given byte offset to the current end of the file. If the byte offset provided is greater than or equal to the file's current byte count, the method returns without changing the file. Any open streams are flushed automatically before the truncation occurs.
     */
    public abstract void truncate(long byteOffset) throws java.io.IOException;

    /**
     * Determines the used memory of a file system the connection's target resides on. This may only be an estimate and may vary based on platform-specific file system blocking and metadata information.
     */
    public abstract long usedSize();

}
