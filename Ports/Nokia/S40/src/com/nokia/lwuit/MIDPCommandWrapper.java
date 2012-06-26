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

    public static MIDPCommandWrapper createInstance(com.sun.lwuit.Command c) {
        if (c.getIcon() != null) {
            try {
                Class.forName("com.nokia.mid.ui.IconCommand");
                Class cl = Class.forName("com.nokia.lwuit.MIDPIconCommandWrapper");
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
}