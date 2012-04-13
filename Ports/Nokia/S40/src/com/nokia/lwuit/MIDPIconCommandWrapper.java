package com.nokia.lwuit;

import com.nokia.mid.ui.IconCommand;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

public class MIDPIconCommandWrapper extends MIDPCommandWrapper {
    private IconCommand command = null;

    public Command getCommand() {
        if (command == null) {
            command = new IconCommand(lwuitCommand.getCommandName(),
                    (Image) lwuitCommand.getIcon().getImage(),
                    (Image) lwuitCommand.getIcon().getImage(),
                    type, offset);
        }
        return command;
    }

    public MIDPIconCommandWrapper() {
        offset = 0;
        type = Command.SCREEN;
    }
}
