/*
 *  Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package com.nokia.example.touristattractions.network;

import com.sun.lwuit.html.DocumentInfo;
import com.sun.lwuit.html.DocumentRequestHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * An implementation of DocumentRequestHandler that handles fetching HTML documents both from HTTP and from the JAR.
 * This request handler takes care of cookies, redirects and handles both GET and POST requests
 *
 * @author Ofir Leitner
 */
public class HttpRequestHandler implements DocumentRequestHandler {

    //Hashtable connections = new Hashtable();
    /**
     * A hastable containing all cookies - the table keys are domain names, while the value is another hashtbale containing a pair of cookie name and value.
     */
    static Hashtable cookies = Storage.getCookies();

    /**
     * A hastable containing all history - the table keys are domain names, while the value is a vector containing the visited links.
     */
    static Hashtable visitedLinks = Storage.getHistory();

    /**
     * If true will cache HTML pages, this also means that they will be buffered and read fully and only then passed to HTMLComponent - this can have memory implications.
     * Also note that for the cached HTMLs to be written Storage.RMS_ENABLED[TYPE_CACHE] should be true
     */
    static boolean CACHE_HTML = false;

    /**
     * If true will cache images, this also means that they will be buffered and read fully and only then passed to HTMLComponent - this can have memory implications.
     * Also note that for the cached images to be written Storage.RMS_ENABLED[TYPE_CACHE] should be true
     */
    static boolean CACHE_IMAGES = true;

    /**
     * If true will cache CSS files, this also means that they will be buffered and read fully and only then passed to HTMLComponent - this can have memory implications.
     * Also note that for the cached CSS files to be written Storage.RMS_ENABLED[TYPE_CACHE] should be true
     */
    static boolean CACHE_CSS = false;

    /**
     * Returns the domain string we use to identify visited link.
     * Note that this may be different than the domain name returned by HttpConnection.getHost
     * 
     * @param url The link URL
     * @return The link's domain
     */
    static String getDomainForLinks(String url) {
        String domain = null;
        
        if (url.startsWith("file:")) {
            return "localhost"; // Just a common name to store local files under
        } 
        
        int index = -1;
        if (url.startsWith("http://")) {
            index = 7;
        }
        else if (url.startsWith("https://")) {
            index = 8;
        }
        
        if (index != -1) {
            domain=url.substring(index);
            index=domain.indexOf('/');
            
            if (index != -1) {
                domain=domain.substring(0, index);
            }
        }
        return domain;
    }

    /**
     * {@inheritDoc}
     */
    public InputStream resourceRequested(DocumentInfo docInfo) {
        InputStream is = null;
        String url = docInfo.getUrl();
        String linkDomain = getDomainForLinks(url);

        // Visited links
        if (docInfo.getExpectedContentType() == DocumentInfo.TYPE_HTML) { // Only mark base documents as visited links
            if (linkDomain != null) {
                Vector hostVisitedLinks = (Vector) visitedLinks.get(linkDomain);
                if (hostVisitedLinks == null) {
                    hostVisitedLinks = new Vector();
                    visitedLinks.put(linkDomain,hostVisitedLinks);
                }
                if (!hostVisitedLinks.contains(url)) {
                    hostVisitedLinks.addElement(url);
                    Storage.addHistory(linkDomain, url);
                }
            } else {
                System.out.println("Link domain null for " + url);
            }
        } 

        String params = docInfo.getParams();
        if ((!docInfo.isPostRequest()) && (params != null) && (!params.equals(""))) {
            url = url + "?" + params;
        }

        // See if page/image is in the cache
        // caching will be used only if there are no parameters and no cookies (Since if they are this is probably dynamic content)
        boolean useCache = false;
        if (((docInfo.getExpectedContentType() == DocumentInfo.TYPE_HTML) && (CACHE_HTML) && ((params == null) || (params.equals(""))) && (!cookiesExistForDomain(linkDomain) )) ||
            ((docInfo.getExpectedContentType() == DocumentInfo.TYPE_IMAGE) && (CACHE_IMAGES)) ||
            ((docInfo.getExpectedContentType() == DocumentInfo.TYPE_CSS) && (CACHE_CSS)))
        {
            useCache = true;
            InputStream imageIS = Storage.getResourcefromCache(url);
            if (imageIS != null) {
                return imageIS;
            }
        }

        // Handle the file protocol
        if (url.startsWith("file://")) {
            return getFileStream(docInfo);
        }

        try {
            HttpConnection hc = (HttpConnection)Connector.open(url);
            String encoding = null;
            
            if (docInfo.isPostRequest()) {
                encoding="application/x-www-form-urlencoded";
            }
            
            if (!docInfo.getEncoding().equals(DocumentInfo.ENCODING_ISO)) {
                encoding=docInfo.getEncoding();
            }

            String domain = linkDomain; // will return one of the following formats: sub.domain.com / sub.domain.co.il
            
            sendCookies(domain, hc);
            domain = domain.substring(domain.indexOf('.')); // .domain.com / .domain.co.il
            if (domain.indexOf('.',1) != -1) { // Make sure that we didn't get just .com - TODO - however note that if the domain was domain.co.il - it can be here .co.il
                sendCookies(domain, hc);
            }

            if (encoding != null) {
               hc.setRequestProperty("Content-Type", encoding);
            }

            if (docInfo.isPostRequest()) {
               hc.setRequestMethod(HttpConnection.POST);
               
               if (params == null) {
                   params = "";
               }
               
               byte[] paramBuf = params.getBytes();
               hc.setRequestProperty("Content-Length", "" + paramBuf.length);
               OutputStream os=hc.openOutputStream();
               os.write(paramBuf);
               os.close();
            }

            String contentTypeStr = hc.getHeaderField("content-type");
            if (contentTypeStr != null) {
                contentTypeStr = contentTypeStr.toLowerCase();
                
                if (docInfo.getExpectedContentType() == DocumentInfo.TYPE_HTML) { // We perform these checks only for text (i.e. main page), for images/css we just send what the server sends and "hope for the best"
                    if (contentTypeStr != null) {
                        if ((contentTypeStr.startsWith("text/")) || (contentTypeStr.startsWith("application/xhtml")) || (contentTypeStr.startsWith("application/vnd.wap"))) {
                            docInfo.setExpectedContentType(DocumentInfo.TYPE_HTML);
                        }
                        else if (contentTypeStr.startsWith("image/")) {
                            docInfo.setExpectedContentType(DocumentInfo.TYPE_IMAGE);
                            hc.close();
                            return getStream("<img src=\""+url+"\">",null);
                        }
                        else {
                            hc.close();
                            return getStream("Content type "+contentTypeStr+" is not supported.","Error");
                        }
                    }
                }

                if ((docInfo.getExpectedContentType() == DocumentInfo.TYPE_HTML) ||
                    (docInfo.getExpectedContentType() == DocumentInfo.TYPE_CSS)) { // Charset is relevant for HTML and CSS only
                    int charsetIndex = contentTypeStr.indexOf("charset=");
                    
                    if (charsetIndex != -1) {
                        String charset=contentTypeStr.substring(charsetIndex + 8);
                        docInfo.setEncoding(charset.trim());
                    }
                }
            }

            int i = 0;
            while (hc.getHeaderFieldKey(i) != null) {
                if (hc.getHeaderFieldKey(i).equalsIgnoreCase("set-cookie")) {
                    addCookie(hc.getHeaderField(i), url);
                }
                i++;
            }

            int response = hc.getResponseCode();
            if (response / 100 == 3) { // 30x code is redirect
                String newURL = hc.getHeaderField("Location");
                if (newURL != null) {
                    hc.close();
                    docInfo.setUrl(newURL);
                    if ((response == 302) || (response == 303)) { // The "302 Found" and "303 See Other" change the request method to GET
                        docInfo.setPostRequest(false);
                        docInfo.setParams(null); //reset params
                    }
                    return resourceRequested(docInfo);
                }
            }
            is = hc.openInputStream();

            if (useCache) {
                byte[] buf = getBuffer(is);
                Storage.addResourceToCache(url, buf, false);
                ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                is.close();
                hc.close(); // all the data is in the buffer
                return bais;
            }

        }
        catch (SecurityException e) {
            return getStream("Network access was disallowed for this session. Only local and cached pages can be viewed.<br><br> To browse external sites please exit the application and when asked for network access allow it.", "Security error");
        }
        catch (IOException e) {
            System.out.println("HttpRequestHandler->IOException: "+e.getMessage());
            return getStream("The page could not be loaded due to an I/O error.", "Error");
        }
        catch (IllegalArgumentException e) { // For malformed URL
            System.out.println("HttpRequestHandler->IllegalArgumentException: "+e.getMessage());
            return getStream("The reuqested URL is not valid.", "Malformed URL");
        }

        return is;
    }

    /**
     * Checks if there are cookies stored on the client for the specified domain
     *
     * @param domain The domain to check for cookies
     * @return true if cookies for the specified domain exists, false otherwise
     */
    private boolean cookiesExistForDomain(String domain) {
        Object obj = cookies.get(domain);
        if (obj == null) {
            int index = domain.indexOf('.');
            if (index != -1) {
                domain = domain.substring(index); // .domain.com / .domain.co.il
                if (domain.indexOf('.',1) != -1) { // Make sure that we didn't get just .com - TODO - however note that if the domain was domain.co.il - it can be here .co.il
                    obj = cookies.get(domain);
                }
            }
        }
        
        return (obj != null);
    }

    /**
     * Sends the available cookies for the given domain
     * 
     * @param domain The cookies domain
     * @param hc The HTTPConnection
     * @throws IOException
     */
    private void sendCookies(String domain,HttpConnection hc) throws IOException {
        Hashtable hostCookies = (Hashtable)cookies.get(domain);
        String cookieStr = "";
        
        if (hostCookies != null) {
            for (Enumeration e = hostCookies.keys(); e.hasMoreElements();) {
                String name = (String) e.nextElement();
                String value = (String) hostCookies.get(name);
                String cookie=name + "=" + value;
                if (cookieStr.length() != 0) {
                    cookieStr += "; ";
                }
                cookieStr += cookie;
            }
        }
        
        if (cookieStr.length() != 0) {
            hc.setRequestProperty("cookie", cookieStr);
        }
    }

    /**
     * Returns an Inputstream of the specified HTML text
     *
     * @param htmlText The text to get the stream from
     * @param title The page's title
     * @return an Inputstream of the specified HTML text
     */
    private InputStream getStream(String htmlText,String title) {
        String titleStr = "";
        
        if (title != null) {
            titleStr = "<head><title>"+title+"</title></head>";
        }
        
        htmlText = "<html>" + titleStr + "<body>" + htmlText + "</body></html>";
        ByteArrayInputStream bais = new ByteArrayInputStream(htmlText.getBytes());
        return bais;
    }

    /**
     * Adds the given cookie to the cookie collection
     * 
     * @param setCookie The cookie to add
     * @param hc The HttpConnection
     */
    private void addCookie(String setCookie,String url/*HttpConnection hc*/) {
        String urlDomain=getDomainForLinks(url);

        // Determine cookie domain
        String domain = null;
        int index = setCookie.indexOf("domain=");
        if (index != -1) {
            domain = setCookie.substring(index + 7);
            index = domain.indexOf(';');
            if (index != -1) {
                domain = domain.substring(0, index);
            }
            
            if (!urlDomain.endsWith(domain)) {
                System.out.println("Warning: Cookie tried to set to another domain");
                domain = null;
            }
        }
        if (domain == null) {
            domain = urlDomain;
        }

        // Check cookie expiry
        boolean save = false;
        index = setCookie.indexOf("expires=");
        if (index != -1) { // Cookies without the expires= property are valid only for the current session and as such are not saved to RMS
            String expire = setCookie.substring(index + 8);
            index = expire.indexOf(';');
            if (index != -1) {
                expire=expire.substring(0, index);
            }
            save = true;
        }

        // Get cookie name and value
        index = setCookie.indexOf(';');
        if (index != -1) {
            setCookie = setCookie.substring(0, index);
        }
        index = setCookie.indexOf('=');
        String name = setCookie;
        String value = "";
        if (index != -1) {
            name = setCookie.substring(0, index);
            value = setCookie.substring(index + 1);
        }

        Hashtable hostCookies = (Hashtable) cookies.get(domain);
        if (hostCookies == null) {
            hostCookies = new Hashtable();
            cookies.put(domain,hostCookies);
        }
        hostCookies.put(name,value);

        if (save) { // Note that we save all cookies with expiry specified, while not checking the specific expiry date
            Storage.addCookie(domain, name, value);
        }
    }

    /**
     * This method is used when the requested document is a file in the JAR
     *
     * @param url The URL of the file
     * @return An InputStream of the specified file
     */
    private InputStream getFileStream(DocumentInfo docInfo) {
        String url = docInfo.getUrl();

        // If a from was submitted on a local file, just display the parameters
        if ((docInfo.getParams() != null) && (!docInfo.getParams().equals(""))) {
            String method = "GET";
            if (docInfo.isPostRequest()) {
                method = "POST";
            }
            
            String params = docInfo.getParams();
            String newParams = "";
            
            if (params != null) {
                for (int i = 0; i < params.length(); i++) {
                    char c = params.charAt(i);
                    if (c == '&') {
                        newParams += ", ";
                    } else {
                        newParams += c;
                    }
                }
            }
            return getStream("<h2>Form submitted locally.</h2><b>Method:</b> "+method+"<br><br><b>Parameters:</b><br>"+newParams+"<hr><a href=\""+docInfo.getUrl()+"\">Continue to local URL</a>","Form Results");
        }

        url = url.substring(7); // Cut the file://

        int hash = url.indexOf('#'); //trim anchors
        if (hash != -1) {
           url = url.substring(0, hash);
        }

        int param = url.indexOf('?'); //trim parameters, not relvant for files
        if (param != -1) {
            url = url.substring(0, param);
        }

        // Use the following commented segment for loading HTML files saved with the UTF8 header added by some utils - 0xEF, 0xBB, 0xBF
        // This is a simple code to skip automatically 3 chars on a certain file suffix (.htm isntead of .html)
        // A better solution is to detect these bytes, but that requires buffering of the stream (to "unread" if these are not the right chars)
        /*
        if (url.endsWith(".htm")) {
            System.out.println("Notepad UTF - Skipping 3 chars");
            docInfo.setEncoding(DocumentInfo.ENCODING_UTF8); 
            // If the UTF8 encoding string doesn't work on your device, try the following instead of the line above:
            //docInfo.setEncoding("UTF-8");
            InputStream is= getClass().getResourceAsStream(url);
            try {
                is.read();
                is.read();
                is.read();
                return is; 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
         */

        return getClass().getResourceAsStream(url);
    }

    /**
     * Reads an inputstream completely and places it into a buffer
     * 
     * @param is The InputStream to read
     * @return A buffer containing the stream's contents
     * @throws IOException
     */
    static byte[] getBuffer(InputStream is) throws IOException {
            int chunk = 50000;
            byte[] buf = new byte[chunk];
            int i = 0;
            int b = is.read();
            while (b != -1) {
                if (i >= buf.length) {
                    byte[] tempbuf = new byte[buf.length + chunk];
                    for (int j = 0; j < buf.length; j++) {
                        tempbuf[j] = buf[j];
                    }
                    buf = tempbuf;
                }
                buf[i] = (byte) b;
                i++;
                b = is.read();
            }
            byte[] tempbuf = new byte[i];
            for (int j = 0; j < tempbuf.length; j++) {
                tempbuf[j] = buf[j];
            }

            buf = tempbuf;
            return buf;
    }

}

