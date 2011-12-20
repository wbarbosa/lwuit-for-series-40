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

package javax.microedition.media.control;
/**
 * MIDIControl provides access to MIDI rendering and transmitting devices.
 * Typical devices that are controlled with MIDIControl are internal synthesizers (software/hardware) or external hardware ports. Devices are virtual, i.e. even if there is only one physical synthesizer, all instances of MIDIControl seem to operate on its own synthesizer.
 * General functionality of this control is: Querying current state of the device: The programs that are currently assigned to each of the 16 channels Volume of each channel Querying the banks of the synthesizer: Get a list of internal sound banks Get a list of custom sound banks Get the list of programs of a sound bank Get the name of a specific program Set the volume assigned to a channel Set the bank/program assigned to a channel Send short MIDI messages to the device Send long MIDI messages (system exclusive) In Java Sound terms, MIDIControl combines methods and concepts of the interfaces Transmitter, Receiver, Synthesizer, MidiChannel, Soundbank, and Patch.
 * In this context, the following naming conventions are used: A program refers to a single instrument. This is also known as a patch. A bank is short for sound bank. It contains up to 128 programs, numbered in the range from 0..127. An internal bank is provided by the software implementation or the hardware of the device. A custom bank is installed by an application, e.g. by loading an XMF meta file with an embedded bank.
 * The conception of MIDIControl is based on scope and abstraction level: MIDIControl has methods that are specific to the device or renderer, and do not directly relate to a specific MIDI file or sequence to be played. However, as devices are virtual, MIDIControl's methods only operate on this virtual device. On the other hand, it is also possible to get an instance of MIDIControl without providing a sequence or MIDI file; this is done by specifying a magic Locator: try{ Player p = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR); MIDIControl synth = (MIDIControl)p.getControls("javax.microedition.media.control.MIDIControl"); } catch (MediaException e) { } MIDIControl's methods can be considered advanced, low level functionality. This has 2 implications: MIDIControl is optional, i.e. no Player instance is required to provide an implementation of it Basic media or MIDI player applications will not need MIDIControl; VolumeControl, TempoControl, and PitchControl are sufficient for basic needs.
 * A useful function is Panic: immediately turn off all sounds and notes. It can be implemented using the following code fragment: int CONTROL_ALL_SOUND_OFF = 0x78; for (int channel = 0; channel < 16; channel++) { shortMidiEvent(CONTROL_CHANGE | channel, CONTROL_ALL_SOUND_OFF, 0); }
 * The implementation need not support the various query methods. This is a technical limitation, as the MIDI standard does not provide a standardized means to query the current program or the installed soundbanks. This especially applies to external MIDI ports. Optional methods must not be called if isBankQuerySupported returns false.
 * See Also:Player, RateControl, TempoControl, PitchControl
 */
public interface MIDIControl extends javax.microedition.media.Control{
    /**
     * Command value for Control Change message (0xB0, or 176).
     * See Also:shortMidiEvent(int, int, int), Constant Field Values
     */
    static final int CONTROL_CHANGE=176;

    /**
     * Command value for Note On message (0x90, or 144). To turn a note off, send a NOTE_ON message with 0 velocity. Alternatively, a Note Off message (0x80) can be sent.
     * See Also:shortMidiEvent(int, int, int), Constant Field Values
     */
    static final int NOTE_ON=144;

    /**
     * Returns list of installed banks. If the custom parameter is true, a list of custom banks is returned. Otherwise, a list of all banks (custom and internal) is returned.
     * As there is no MIDI equivalent to this method, this method is optional, indicated by isBankQuerySupported. If it returns false, this function is not supported and throws an exception.
     */
    abstract int[] getBankList(boolean custom) throws javax.microedition.media.MediaException;

    /**
     * Get volume for the given channel. The return value is independent of the master volume, which is set and retrieved with
     * .
     * As there is no MIDI equivalent to this method, the implementation may not always know the current volume for a given channel. In this case the return value is -1.
     */
    abstract int getChannelVolume(int channel);

    /**
     * Given bank, program and key, get name of key. This method applies to key-mapped banks (i.e. percussive banks or effect banks) only. A return value of null means that the specified key is not mapped to a sound. For melodic banks, where each key (=note) produces the same sound at different pitch, this method always returns null. For space-saving reasons, an implementation may return an empty string instead of the key name. To find out which keys in a specific program are mapped to a sound, iterate through all keys (0-127) and compare the return value of getKeyName to non-null.
     * As there is no MIDI equivalent to this method, this method is optional, indicated by isBankQuerySupported. If it returns false, this function is not supported and throws an exception.
     */
    abstract java.lang.String getKeyName(int bank, int prog, int key) throws javax.microedition.media.MediaException;

    /**
     * Returns program assigned to channel. It represents the current state of the channel. During playback of a MIDI file, the program may change due to program change events in the MIDI file.
     * To set a program for a channel, use setProgram(int, int, int).
     * The returned array is represented by an array {bank,program}.
     * If the device has not been initialized with a MIDI file, or the MIDI file does not contain a program change for this channel, an implementation specific default value is returned.
     * As there is no MIDI equivalent to this method, this method is optional, indicated by isBankQuerySupported. If it returns false, this function is not supported and throws an exception.
     */
    abstract int[] getProgram(int channel) throws javax.microedition.media.MediaException;

    /**
     * Given bank, get list of program numbers. If and only if this bank is not installed, an empty array is returned.
     * As there is no MIDI equivalent to this method, this method is optional, indicated by isBankQuerySupported. If it returns false, this function is not supported and throws an exception.
     */
    abstract int[] getProgramList(int bank) throws javax.microedition.media.MediaException;

    /**
     * Given bank and program, get name of program. For space-saving reasons, an implementation may return an empty string.
     * As there is no MIDI equivalent to this method, this method is optional, indicated by isBankQuerySupported. If it returns false, this function is not supported and throws an exception.
     */
    abstract java.lang.String getProgramName(int bank, int prog) throws javax.microedition.media.MediaException;

    /**
     * Returns whether banks of the synthesizer can be queried.
     * If this functions returns true, then the following methods can be used to query banks: getProgram(int) getBankList(boolean) getProgramList(int) getProgramName(int, int) getKeyName(int, int, int)
     */
    abstract boolean isBankQuerySupported();

    /**
     * Sends a long MIDI event to the device, typically a system exclusive message. This method passes the data directly to the receiving device. The data array's contents are not checked for validity.
     * It is possible to send short events, or even a series of short events with this method.
     */
    abstract int longMidiEvent(byte[] data, int offset, int length);

    /**
     * Set volume for the given channel. To mute, set to 0. This sets the current volume for the channel and may be overwritten during playback by events in a MIDI sequence.
     * It is a high level convenience function. Internally, the following command is executed:
     * shortMidiEvent(CONTROL_CHANGE | channel, CONTROL_MAIN_VOLUME, 0);
     * where this constant is used:
     * int CONTROL_MAIN_VOLUME = 0x07
     * The channel volume is independent of the master volume, which is accessed with VolumeControl. Setting the channel volume does not modify the value of the master volume - and vice versa: changing the value of master volume does not change any channel's volume value. The synthesizer mixes the output of up to 16 channels, each channel with its own channel volume. The master volume then controls the volume of the mix. Consequently, the effective output volume of a channel is the product of master volume and channel volume.
     * Setting the channel volume does not generate a VOLUME_CHANGED event.
     */
    abstract void setChannelVolume(int channel, int volume);

    /**
     * Set program of a channel. This sets the current program for the channel and may be overwritten during playback by events in a MIDI sequence.
     * It is a high level convenience function. Internally, these method calls are executed:
     * shortMidiEvent(CONTROL_CHANGE | channel, CONTROL_BANK_CHANGE_MSB, bank >> 7); shortMidiEvent(CONTROL_CHANGE | channel, CONTROL_BANK_CHANGE_LSB, bank & 0x7F); shortMidiEvent(PROGRAM_CHANGE | channel, program, 0);
     * In order to use the default bank (the initial bank), set the bank parameter to -1.
     * In order to set a program without explicitly setting the bank, use the following call:
     * shortMidiEvent(PROGRAM_CHANGE | channel, program, 0);
     * In both examples, the following constants are used:
     * int PROGRAM_CHANGE = 0xC0; int CONTROL_BANK_CHANGE_MSB = 0x00; int CONTROL_BANK_CHANGE_LSB = 0x20;
     */
    abstract void setProgram(int channel, int bank, int program);

    /**
     * Sends a short MIDI event to the device. Short MIDI events consist of 1, 2, or 3 unsigned bytes. For non-realtime events, the first byte is split up into status (upper nibble, 0x80-0xF0) and channel (0x00-0x0F). For example, to send a Note On event on a given channel, use this line:
     * shortMidiEvent(NOTE_ON | channel, note, velocity);
     * For events with less than 3 bytes, set the remaining data bytes to 0.
     * There is no guarantee that a specific implementation of a MIDI device supports all event types. Also, the MIDI protocol does not implement flow control and it is not guaranteed that an event reaches the destination. In both these cases, this method fails silently.
     * Static error checking is performed on the passed parameters. They have to specify a valid, complete MIDI event. Events with type 0x80 are not valid MIDI events (- running status). When an invalid event is encountered, an IllegalArgumentException is thrown.
     */
    abstract void shortMidiEvent(int type, int data1, int data2);

}
