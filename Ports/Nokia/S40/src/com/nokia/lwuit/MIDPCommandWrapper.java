package com.nokia.lwuit;

import javax.microedition.lcdui.Command;

/**
 * Class that wraps lwuit command to midp command
 * @author ilau
 */
public class MIDPCommandWrapper {
    com.sun.lwuit.Command lwuitCommand = null;
    private Command command = null;
    int offset = 0;
    int type = 0;

    /**
     * Create the an instance of this class.
     * @param c
     * @return new MIDPCommandWrapper instance.
     */
    public static MIDPCommandWrapper createInstance(com.sun.lwuit.Command c) {
        if (c.getIcon() != null) {
            
            try {
                Class.forName("com.nokia.mid.ui.IconCommand");
                Class cl = Class.forName("com.nokia.lwuit.MIDPIconCommandWrapper");
                System.out.println("creating iconcommand");
                return (MIDPCommandWrapper) cl.newInstance();
            } catch (Exception e) {
                System.out.println("IconCommand not supported.");
            }
        }
        return new MIDPCommandWrapper();
        
    }
    /**
     * Returns MIDP 2.0 Command class
     * @return javax.microedition.lcdui.Command that has lwuit command data
     */
    public Command getCommand() {
        if (command == null) {
            command = new Command(lwuitCommand.getCommandName(),
                    type, offset);
        }
        return command;
    }
    /**
     * Return original LWUIT Command class
     * @return com.sun.lwuit.Command class
     */
    public com.sun.lwuit.Command getLWUITCommand() {
        return lwuitCommand;
    }

    /**
     * Default Constructor
     */
    public MIDPCommandWrapper() {
        offset = 0;
        type = Command.SCREEN;
    }

    /**
     * Set LWUIT Command class
     * @param c 
     */
    public void setCommand(com.sun.lwuit.Command c) {
        lwuitCommand = c;
    }
    /**
     * Set position of the command. Basically this maps directly to MIDP Command
     * priority
     * @param offset 
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    /**
     * Set MIDP Command type
     * @param type one of the MIDP Command types ie. BACK, SCREEN
     */
    public void setType(int type) {
        this.type = type;
    }

    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.lwuitCommand != null ? this.lwuitCommand.hashCode() : 0);
        hash = 53 * hash + (this.command != null ? this.command.hashCode() : 0);
        hash = 53 * hash + this.offset;
        hash = 53 * hash + this.type;
        return hash;
    }
    /**
     * the equals doesn't use the lcdui command at all since we don't care about
     * that. We only want objects that have same lwuitcommand class
     * @param obj
     * @return true if equal otherwise false
     */
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(obj instanceof MIDPCommandWrapper) {
            MIDPCommandWrapper w = (MIDPCommandWrapper) obj;
            return w.getLWUITCommand() == this.lwuitCommand;
        }else {
            return false;
        }
    }
    
    
}