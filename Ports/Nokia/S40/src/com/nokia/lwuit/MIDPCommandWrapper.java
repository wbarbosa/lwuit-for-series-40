package com.nokia.lwuit;

import javax.microedition.lcdui.Command;

public class MIDPCommandWrapper {
    com.sun.lwuit.Command lwuitCommand = null;
    private Command command = null;
    int offset = 0;
    int type = 0;

    public Command getCommand() {
        if (command == null) {
            command = new Command(lwuitCommand.getCommandName(),
                    type, offset);
        }
        return command;
    }

    public com.sun.lwuit.Command getLWUITCommand() {
        return lwuitCommand;
    }

    public MIDPCommandWrapper() {
        offset = 0;
        type = Command.SCREEN;
    }

    public void setCommand(com.sun.lwuit.Command c) {
        lwuitCommand = c;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setType(int type) {
        this.type = type;
    }
}