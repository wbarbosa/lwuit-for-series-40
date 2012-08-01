package com.nokia.lwuit;

import com.nokia.mid.ui.IconCommand;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

/**
 * Class that wraps midp Command to IconCommand
 * @author tkor
 */
class MIDPIconCommandWrapper extends MIDPCommandWrapper {
    private Command command = null;

    /**
     * Creates a IconCommand from the original LWUIT Command class.
     * @return IconCommand class. 
     */
    public Command getCommand() {
        if (command == null) {
            command = new IconCommand(lwuitCommand.getCommandName(),
                    (Image) lwuitCommand.getIcon().getImage(),
                    (Image) lwuitCommand.getIcon().getImage(),
                    type, offset);
        }
        return command;
    }

    /**
     * Default Constructor
     */
    public MIDPIconCommandWrapper() {
        offset = 0;
        type = Command.SCREEN;
    }
}
