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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.file.FileConnection;

/**
 * This class is factory for creating new Connection objects.
 * The creation of Connections is performed dynamically by looking up a protocol implementation class whose name is formed from the platform name (read from a system property) and the protocol name of the requested connection (extracted from the parameter string supplied by the application programmer.) The parameter string that describes the target should conform to the URL format as described in RFC 2396. This takes the general form:
 * {scheme}:[{target}][{params}]
 * where {scheme} is the name of a protocol such as http}.
 * The {target} is normally some kind of network address.
 * Any {params} are formed as a series of equates of the form ";x=y". Example: ";type=a".
 * An optional second parameter may be specified to the open function. This is a mode flag that indicates to the protocol handler the intentions of the calling code. The options here specify if the connection is going to be read (READ), written (WRITE), or both (READ_WRITE). The validity of these flag settings is protocol dependent. For instance, a connection for a printer would not allow read access, and would throw an IllegalArgumentException. If the mode parameter is not specified, READ_WRITE is used by default.
 * An optional third parameter is a boolean flag that indicates if the calling code can handle timeout exceptions. If this flag is set, the protocol implementation may throw an InterruptedIOException when it detects a timeout condition. This flag is only a hint to the protocol handler, and it does not guarantee that such exceptions will actually be thrown. If this parameter is not set, no timeout exceptions will be thrown.
 * Because connections are frequently opened just to gain access to a specific input or output stream, four convenience functions are provided for this purpose. See also: DatagramConnection for information relating to datagram addressing
 * Since: CLDC 1.0
 */
public class Connector{
    /**
     * Access mode READ.
     * See Also:Constant Field Values
     */
    public static final int READ=1;

    /**
     * Access mode READ_WRITE.
     * See Also:Constant Field Values
     */
    public static final int READ_WRITE=3;

    /**
     * Access mode WRITE.
     * See Also:Constant Field Values
     */
    public static final int WRITE=2;

    /**
     * Create and open a Connection.
     */
    public static Connection open(java.lang.String name) throws java.io.IOException{
        return new URLConnection(name, READ_WRITE);
    }

    /**
     * Create and open a Connection.
     */
    public static Connection open(java.lang.String name, int mode) throws java.io.IOException{
        return new URLConnection(name, mode);
    }

    /**
     * Create and open a Connection.
     */
    public static Connection open(java.lang.String name, int mode, boolean timeouts) throws java.io.IOException{
        return new URLConnection(name, mode);
    }

    /**
     * Create and open a connection input stream.
     */
    public static java.io.DataInputStream openDataInputStream(java.lang.String name) throws java.io.IOException{
        return ((StreamConnection)open(name)).openDataInputStream();
    }

    /**
     * Create and open a connection output stream.
     */
    public static java.io.DataOutputStream openDataOutputStream(java.lang.String name) throws java.io.IOException{
        return ((StreamConnection)open(name)).openDataOutputStream();
    }

    /**
     * Create and open a connection input stream.
     */
    public static java.io.InputStream openInputStream(java.lang.String name) throws java.io.IOException{
        return ((StreamConnection)open(name)).openInputStream();
    }

    /**
     * Create and open a connection output stream.
     */
    public static java.io.OutputStream openOutputStream(java.lang.String name) throws java.io.IOException{
        return ((StreamConnection)open(name)).openOutputStream();
    }

    private static class URLConnection implements Connection, HttpConnection, HttpsConnection, FileConnection, StreamConnection {
        private URL url;
        private int mode;
        private java.io.File file;
        private java.net.URLConnection con;

        public URLConnection(String url, int mode) throws IOException {
            try {
            	this.mode = mode;
                this.url = new URL(url);
                if(url.startsWith("file")) {
                	url = url.substring(url.indexOf(':') + 1);
                	file = new File(url);
                } else {
	                con = this.url.openConnection();
	                con.setDoInput(mode == READ || mode == READ_WRITE);
	                con.setDoOutput(mode == WRITE || mode == READ_WRITE);
	                if(con instanceof HttpURLConnection) {
	                	((HttpURLConnection)con).setUseCaches(false);
	                	((HttpURLConnection)con).setAllowUserInteraction(false);
	                	HttpURLConnection.setFollowRedirects(false);
	                }
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                throw new IOException("Malformed URL Exception " + url);
            }
        }

        public void close() throws IOException {
            if(con != null) {
                con = null;
                url = null;
            }
        }

        public long getDate() throws IOException {
        	if(file != null) {
        		return file.lastModified();
        	}
            return con.getDate();
        }

        public long getExpiration() throws IOException {
        	if(file != null) {
        		return 0;
        	}
            return con.getExpiration();
        }

        public String getFile() {
        	if(file != null) {
        		return file.getAbsolutePath();
        	}
            return url.getFile();
        }

        public String getHeaderField(int n) throws IOException {
            return con.getHeaderField(n);
        }

        public String getHeaderField(String name) throws IOException {
            return con.getHeaderField(name);
        }

        public long getHeaderFieldDate(String name, long def) throws IOException {
            return con.getHeaderFieldDate(name, def);
        }

        public int getHeaderFieldInt(String name, int def) throws IOException {
            return con.getHeaderFieldInt(name, def);
        }

        public String getHeaderFieldKey(int n) throws IOException {
            return con.getHeaderFieldKey(n);
        }

        public String getHost() {
            return url.getHost();
        }

        public long getLastModified() throws IOException {
        	if(file != null) {
        		return file.lastModified();
        	}
            return con.getLastModified();
        }

        public int getPort() {
            return url.getPort();
        }

        public String getProtocol() {
            return url.getProtocol();
        }

        public String getQuery() {
            return url.getQuery();
        }

        public String getRef() {
            return url.getRef();
        }

        public String getRequestMethod() {
            return ((HttpURLConnection)con).getRequestMethod();
        }

        public String getRequestProperty(String key) {
            return con.getRequestProperty(key);
        }

        public int getResponseCode() throws IOException {
            return ((HttpURLConnection)con).getResponseCode();
        }

        public String getResponseMessage() throws IOException {
            return ((HttpURLConnection)con).getResponseMessage();
        }

        public String getURL() {
            return url.toExternalForm();
        }

        public void setRequestMethod(String method) throws IOException {
            ((HttpURLConnection)con).setRequestMethod(method);
        }

        public void setRequestProperty(String key, String value) throws IOException {
            con.setRequestProperty(key, value);
        }

        public String getEncoding() {
            return ((HttpURLConnection)con).getContentEncoding();
        }

        public long getLength() {
        	if(file != null) {
        		return file.length();
        	}
            return ((HttpURLConnection)con).getContentLength();
        }

        public String getType() {
            return ((HttpURLConnection)con).getContentType();
        }

        public DataInputStream openDataInputStream() throws IOException {
        	if(file != null) {
        		return new DataInputStream(new FileInputStream(file));
        	}
            return new DataInputStream(con.getInputStream());
        }

        public InputStream openInputStream() throws IOException {
        	if(file != null) {
        		return new FileInputStream(file);
        	}
            return con.getInputStream();
        }

        public DataOutputStream openDataOutputStream() throws IOException {
        	if(file != null) {
        		return new DataOutputStream(new FileOutputStream(file));
        	}
            return new DataOutputStream(con.getOutputStream());
        }

        public OutputStream openOutputStream() throws IOException {
        	if(file != null) {
        		return new FileOutputStream(file);
        	}
            return con.getOutputStream();
        }

        public SecurityInfo getSecurityInfo() throws IOException {
            return null;
        }

        public long availableSize() {
        	if(file != null) {
        		return file.getFreeSpace();
        	}
            return con.getContentLength();
        }

        public boolean canRead() {
        	if(file != null) {
        		return file.canRead();
        	}
            return con.getDoInput();
        }

        public boolean canWrite() {
        	if(file != null) {
        		return file.canWrite();
        	}
            return con.getDoOutput();
        }

        public void create() throws IOException {
        	if(file != null) {
        		return;
        	}
            //con = url.openConnection();
            con.setDoInput(mode == READ || mode == READ_WRITE);
            con.setDoOutput(mode == WRITE || mode == READ_WRITE);
            con.connect();
        }

        public void delete() throws IOException {
            file.delete();
        }

        public long directorySize(boolean includeSubDirs) throws IOException {
            // this doens't work correctly...
            return new File(url.toExternalForm()).length();
        }

        public boolean exists() {
            return file.exists();
        }

        public long fileSize() throws IOException {
            return file.length();
        }

        public String getName() {
            return file.getName();
        }

        public String getPath() {
            return file.getAbsolutePath();
        }

        public boolean isDirectory() {
            return file.isDirectory();
        }

        public boolean isHidden() {
            return file.isHidden();
        }

        public boolean isOpen() {
            return false;
        }

        public long lastModified() {
            return file.lastModified();
        }

        public Enumeration list() throws IOException {
            return Collections.enumeration(Arrays.asList(file.list()));
        }

        public Enumeration list(final String filter, final boolean includeHidden) throws IOException {
            return Collections.enumeration(Arrays.asList(new File(url.toExternalForm()).list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if(!includeHidden) {
                        return name.indexOf(filter) > -1 && new File(dir, name).isHidden();
                    }
                    return name.indexOf(filter) > -1;
                }
            })));
        }

        public void mkdir() throws IOException {
        	file.mkdirs();
        }

        public OutputStream openOutputStream(long byteOffset) throws IOException {
            // doesn't work
        	final RandomAccessFile ra = new RandomAccessFile(file, "rw");
        	ra.seek(byteOffset);
            return new OutputStream() {

				@Override
				public void close() throws IOException {
					ra.close();
				}

				@Override
				public void flush() throws IOException {
				}

				@Override
				public void write(byte[] buffer, int offset, int count)
						throws IOException {
					ra.write(buffer, offset, count);
				}

				@Override
				public void write(byte[] buffer) throws IOException {
					ra.write(buffer);
				}

				@Override
				public void write(int oneByte) throws IOException {
					ra.write(oneByte);
				}
			};
        }

        public void rename(String newName) throws IOException {
            file.renameTo(new File(file.getParent(), newName));
        }

        public void setFileConnection(String fileName) throws IOException {
            close();
            url = new URL(fileName);
        }

        public void setHidden(boolean hidden) throws IOException {
        }

        public void setReadable(boolean readable) throws IOException {
        }

        public void setWritable(boolean writable) throws IOException {
        }

        public long totalSize() {
            //return new File(url.toExternalForm()).getTotalSpace();
        	return 1000000;
        }

        public void truncate(long byteOffset) throws IOException {
        }

        public long usedSize() {
            File f = new File(url.toExternalForm());
            //return f.getTotalSpace() - f.getFreeSpace();
            return 0;
        }
    }
}
