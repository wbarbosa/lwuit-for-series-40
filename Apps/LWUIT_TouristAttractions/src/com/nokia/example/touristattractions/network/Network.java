/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.touristattractions.network;

import java.io.*;
import java.util.Vector;
import javax.microedition.io.*;

/**
 * Provides asynchronous HTTP GET and POST operations.
 */
public final class Network
    implements Runnable {

    private static final int MAX_NETWORK_THREADS = 2;
    private static final Thread[] networkThreads =
        new Thread[MAX_NETWORK_THREADS];
    private static final Vector queue = new Vector();

    private Network() {
    }

    /**
     * Queue a network operation to be handled.
     *
     * @param op
     */
    public static void queue(final NetworkOperation op) {
        synchronized (queue) {
            queue.addElement(op);
            queue.notifyAll();
        }
        
        synchronized (networkThreads) {
            for (int i = 0; i < networkThreads.length; i++) {
                if (networkThreads[i] == null) {
                    networkThreads[i] = new Thread(new Network(),
                        "NetworkThread" + i);
                    networkThreads[i].start();
                    return;
                }
            }
        }
    }

    /**
     * Sends a GET request synchronously.
     *
     * @param url Target of the request.
     * @return Data received
     * @throws NetworkError
     */
    private byte[] sendHttpGet(String url)
        throws NetworkException {
        HttpConnection hcon = null;
        DataInputStream dis = null;
        ByteArrayOutputStream response = new ByteArrayOutputStream(4096);
        byte[] buffer = new byte[4096];
        
        try {
            // A standard HttpConnection with READ access
            hcon = (HttpConnection) Connector.open(url);

            if (hcon == null) {
                throw new NetworkException("No network access");
            }

            // Obtain a DataInputStream from the HttpConnection
            dis = new DataInputStream(hcon.openInputStream());
            int len;
            // Retrieve the response from the server
            while ((len = dis.read(buffer)) != -1) {
                response.write(buffer, 0, len);
            }
        }
        catch (Exception e) {
            throw new NetworkException(e.getMessage());
        }
        finally {
            try {
                if (hcon != null) {
                    hcon.close();
                }
                
                if (dis != null) {
                    dis.close();
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return response.toByteArray();
    }

    /**
     * Sends a POST request synchronously.
     *
     * @param url Target for the request.
     * @param data Request body
     * @throws NetworkError
     */
    private String sendHttpPost(String url, String data, String contentType)
        throws NetworkException {
        HttpConnection hcon = null;
        InputStreamReader dis = null;
        DataOutputStream dos = null;
        StringBuffer responseMessage = new StringBuffer(4096);
        char[] buffer = new char[4096];

        try {
            // An HttpConnection with both read and write access
            hcon = (HttpConnection) Connector.open(url, Connector.READ_WRITE);
            if (hcon == null) {
                throw new NetworkException("No network access");
            }

            // Set the request method to POST
            hcon.setRequestMethod(HttpConnection.POST);

            // Content-Type is must to pass parameters in POST Request
            hcon.setRequestProperty("Content-Type", contentType);

            // Obtain DataOutputStream for sending the request string
            dos = hcon.openDataOutputStream();
            byte[] request_body = data.getBytes();

            // Send request string to server
            for (int i = 0; i < request_body.length; i++) {
                dos.writeByte(request_body[i]);
            }

            // Obtain DataInputStream for receiving server response
            dis = new InputStreamReader(hcon.openInputStream());

            // Retrieve the response from server
            int read;
            while ((read = dis.read(buffer, 0, buffer.length)) != -1) {
                responseMessage.append(buffer, 0, read);
            }
        }
        catch (Exception e) {
            throw new NetworkException(e.getMessage());
        }
        finally {
            // Free up i/o streams and http connection
            try {
                if (hcon != null) {
                    hcon.close();
                }
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
            }
            catch (IOException ioe) {
            }
        }
        return responseMessage.toString();
    }

    /**
     * Thread entry point. Network operation is run in a separate thread.
     */
    public final void run() {
        try {
            NetworkOperation op = null;

            while (true) {
                synchronized (queue) {
                    if (queue.size() > 0) {
                        op = (NetworkOperation) queue.elementAt(0);
                        queue.removeElementAt(0);
                    }
                    else {
                        queue.wait();
                    }
                }
                
                if (op != null) {
                    try {
                        switch (op.getMethod()) {
                            case NetworkOperation.METHOD_POST:
                                String response = sendHttpPost(op.getUrl(), op.getData(), op.
                                    getContentType());
                                op.networkHttpPostResponse(response);
                                break;
                            case NetworkOperation.METHOD_GET:
                            default:
                                byte[] data = sendHttpGet(op.getUrl());
                                op.networkHttpGetResponse(data);
                                break;
                        }
                    }
                    catch (NetworkException e) {
                        op.onNetworkFailure();
                        
                        switch (op.getMethod()) {
                            case NetworkOperation.METHOD_POST:
                                op.networkHttpPostResponse(null);
                                break;
                            case NetworkOperation.METHOD_GET:
                            default:
                                op.networkHttpGetResponse(null);
                                break;
                        }
                    }
                }

                op = null;
            }
        }
        catch (Throwable t) {
        }
    }
}
