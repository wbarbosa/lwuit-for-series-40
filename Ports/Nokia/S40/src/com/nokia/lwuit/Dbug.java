
package com.nokia.lwuit;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;

public class Dbug {
    
    private  static final Vector logs = new Vector(100);
    private static Writer writer = null;
    
    public synchronized static void clear() {
    	logs.removeAllElements();
    }
    
    /**
     * Write a log message
     * @param tag (optional) tag that will be printed before the message
     * @param message the log message
     */
    public static void log(String tag, String message) {
        StringBuffer buf = new StringBuffer();
        if (tag != null || !tag.equals("")) {
            buf.append(tag);
            buf.append(":");
            buf.append(message);
        }
        if (writer != null) {
            try {
                writer.write(buf.toString() + "\n");
                writer.flush();
            } catch (IOException e) {
            }
        }
        synchronized(logs) {
            logs.addElement(buf.toString());
        }
    }
    /**
     * Get logs
     * @param amount
     * @return logs from the latest - amount of logs, latest being the last item.
     */
    public static synchronized String[] getLogs(int amount) {
        if(amount == 0 || amount < 0 || logs.size() == 0) {
            return new String[0];
        }
        String [] ret = new String[amount];
        if(amount > logs.size()) {
            ret = new String[logs.size()];
            logs.copyInto(ret);
        }else {
            int index = (logs.size() == 1) ? 0 : logs.size() - amount;
            int r_i = 0;
            for(int i = index; i < logs.size(); i++) {
                ret[r_i] = (String)logs.elementAt(i);
                r_i++;
            }
                
        }
        
        return ret;
    }
    
    public static synchronized String[] getAllLogs() {
    	String [] ret = new String[logs.size()];
    	logs.copyInto(ret);
    	return ret;
    	
    }
    
    public static synchronized void setOutputStream(OutputStream out) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
        writer = new OutputStreamWriter(out);

    }
}

