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
package com.sun.lwuit.impl.midp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 * Simple abstraction to the player API in MMAPI used by the MIDP and Blackberry ports.
 * This class is public only because the blackberry port relies on it and is in a different
 * package, it is not meant for genral use and is an implementation detail subject to change!
 *
 * @deprecated this class might be changed at any moment it is an implementation detail
 * @author Shai Almog
 */
public class MMAPIPlayer implements PlayerListener {
    private static int volume;
    private boolean deleted;
    private int lastTime;
    private Player p;
    private static Vector playing;
    private InputStream sourceStream;
    private Runnable onComplete;

    private MMAPIPlayer(Player p) {
        this.p = p;
    }

    public static int getVolume() {
        if(volume > -1) {
            return volume;
        }
        synchronized(MMAPIPlayer.class) {
            if(playing != null && playing.size() > 0) {
                MMAPIPlayer current = (MMAPIPlayer)playing.elementAt(0);
                VolumeControl volc = (VolumeControl) current.p.getControl("VolumeControl");
                if(volc != null) {
                    return volc.getLevel();
                }
            }
        }
        return -1;
    }

    public static void setVolume(int v) {
        volume = v;
        if(playing != null) {
            synchronized(MMAPIPlayer.class) {
                for(int iter = 0 ; iter < playing.size() ; iter++) {
                    MMAPIPlayer current = (MMAPIPlayer)playing.elementAt(iter);
                    VolumeControl volc = (VolumeControl) current.p.getControl("VolumeControl");
                    if(volc != null) {
                        volc.setLevel(v);
                    }
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    public static MMAPIPlayer createAudio(String uri, Runnable onCompletion) throws IOException {
        try {
            Player p = Manager.createPlayer((String)uri);
            p.realize();
            MMAPIPlayer m = new MMAPIPlayer(p);
            m.bindPlayerCleanupOnComplete(p, null, onCompletion);
            return m;
        } catch (MediaException ex) {
            ex.printStackTrace();
            throw new IOException(ex.toString());
        }
    }

    private void bindPlayerCleanupOnComplete(final Player p, final InputStream i, final Runnable onComplete) {
        if(volume > -1) {
            VolumeControl v = (VolumeControl) p.getControl("VolumeControl");
            if(v != null) {
                v.setLevel(volume);
            }
        }
        sourceStream = i;
        this.onComplete = onComplete;
        p.addPlayerListener(this);
    }

    public static MMAPIPlayer createAudio(InputStream stream, String mimeType, Runnable onCompletion) throws IOException {
        try {
            Player p = Manager.createPlayer(stream, mimeType);
            p.realize();
            MMAPIPlayer m = new MMAPIPlayer(p);
            m.bindPlayerCleanupOnComplete(p, stream, onCompletion);
            return m;
        } catch (MediaException ex) {
            if("audio/mpeg".equals(mimeType)) {
                return createAudio(stream, "audio/mp3", onCompletion);
            }

            ex.printStackTrace();
            throw new IOException(ex.toString());
        }
    }

    public void cleanupAudio() {
        if(deleted) {
            return;
        }
        deleted = true;
        try {
            synchronized(MMAPIPlayer.class) {
                playing.removeElement(this);
            }
            try {
                p.stop();
            } catch(Throwable t) {}
            p.close();
            p = null;
        } catch(Throwable t) {}
    }

    public void playAudio() {
        if(deleted){
            return;
        }
        try {
            if(playing == null) {
                playing = new Vector();
            }
            synchronized(MMAPIPlayer.class) {
                playing.addElement(this);
            }
            p.start();
        } catch (MediaException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        }
    }

    public void pauseAudio() {
        if(deleted){
            return;
        }
        try {
            if(p != null) {
                p.stop();
            }
        } catch (MediaException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        }
    }

    public int getAudioTime() {
        try {
            // this allows us to get the time even on a closed player
            if(p == null || deleted) {
                return lastTime;
            }
            lastTime = (int)(p.getMediaTime() / 1000);
            return lastTime;
        } catch(Throwable t) {
            return lastTime;
        }
    }

    public void setAudioTime(int time) {
        if(deleted){
            return;
        }
        try {
            p.setMediaTime(time * 1000);
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public int getAudioDuration() {
        if(p == null || deleted) {
            return 1000;
        }
        return (int)(p.getDuration() / 1000);
    }

    public void playerUpdate(Player player, String event, Object eventData) {
        if(deleted) {
            return;
        }
        if(PlayerListener.END_OF_MEDIA.equals(event) || PlayerListener.ERROR.equals(event) ||
                PlayerListener.RECORD_ERROR.equals(event)) {
            lastTime = (int)(p.getMediaTime() / 1000);
            cleanupAudio();
            if(sourceStream != null) {
                try {
                    sourceStream.close();
                } catch(Throwable t) {}
            }
            if(onComplete != null) {
                onComplete.run();
            }
        }
    }
}
