/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.nokia.example.slidepuzzle.components;

import com.nokia.example.slidepuzzle.views.CameraView;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.VideoComponent;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

public class CameraComponent
    extends Container {

    private CameraView cameraView;
    private VideoComponent videoComponent; // component showing the camera display
    private VideoControl videoControl; // controls the display of video
    private Player player; // control the rendering of media data
    private boolean takingPic = false; // indicates if a picture is being taken

    public CameraComponent(CameraView cameraView) {
        super();
        this.cameraView = cameraView;
    }

    public void start() {
        try {
            // Initialize camera
            videoComponent = VideoComponent.createVideoPeer("capture://image");
            videoComponent.setPreferredH(Display.getInstance().getDisplayHeight() -
                cameraView.getMenuBarHeight() - 3);
            videoComponent.setPreferredW(Display.getInstance().getDisplayWidth());

            player = (Player) videoComponent.getNativePeer();
            player.realize();
            videoControl = (VideoControl) player.getControl("VideoControl");
            
            // Fix to prevent showing 1/4 viewport for a moment in some devices
            videoControl.setDisplaySize(5, 5);

            addComponent(videoComponent);

            // Start the video component
            videoComponent.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop showing the camera screen
     */
    public void stop() {
        videoComponent.stop();
        player.deallocate();
        player.close();
        removeComponent(videoComponent);
    }

    /**
     * Takes a snapshot.
     *
     * @return Image taken by the camera
     */
    public Image getSnapshot() {
        byte[] imageData = null;
        try {
            imageData = videoControl.getSnapshot(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            stop();
        }
        
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        
        return Image.createImage(imageData, 0, imageData.length);
    }

    /**
     * Takes a snapshot, creates a puzzle from it and shows it.
     */
    public void takePicAndShow() {
        if (takingPic) {
            return;
        }
        
        takingPic = true;
        cameraView.showLoading(true);
        new Thread() {
            public void run() {
                // Give time to EDT to paint loading text before
                // blocking it in getSnapshot()
                try {
                    Thread.sleep(50);
                }
                catch (Exception e) {
                }
                
                cameraView.showOwnPicPuzzle(getSnapshot());
                cameraView.showLoading(false);
                takingPic = false;
            }
        }.start();
    }
}