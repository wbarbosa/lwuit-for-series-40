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
 * VideoControl controls the display of video. A Player which supports the playback of video must provide a VideoControl via its getControl and getControls method.
 */
public interface VideoControl extends javax.microedition.media.control.GUIControl{
    /**
     * This defines a mode on how the video is displayed. It is used in conjunction with
     * .
     * USE_DIRECT_VIDEO mode can only be used on platforms with LCDUI support.
     * When USE_DIRECT_VIDEO is specified for initDisplayMode, the arg argument must not be null and must be a javax.microedition.lcdui.Canvas or a subclass of it. In this mode, the video is directly rendered onto the canvas. The region where the video is rendered can be set by the setDisplayLocation method. By default, the location is (0, 0). Drawing any graphics or rendering other video at the same region on the canvas may not be supported.
     * initDisplayMode returns null in this mode.
     * Here is one sample usage scenario: javax.microedition.lcdui.Canvas canvas; // canvas must be created before being used in the following code. try { Player p = Manager.createPlayer("http://mymachine/abc.mpg"); p.realize(); VideoControl vc; if ((vc = (VideoControl)p.getControl("VideoControl")) != null) { vc.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, canvas); vc.setVisible(true); } p.start(); } catch (MediaException pe) { } catch (IOException ioe) { }
     * Value 1 is assigned to USE_DIRECT_VIDEO.
     * See Also:Constant Field Values
     */
    static final int USE_DIRECT_VIDEO=1;

    /**
     * Return the actual height of the current render video.
     */
    abstract int getDisplayHeight();

    /**
     * Return the actual width of the current render video.
     */
    abstract int getDisplayWidth();

    /**
     * Return the X-coordinate of the video with respect to the GUI object where the video is displayed. The coordinate is specified in pixel values relative to the upper left hand corner of the GUI object.
     * The return value is undefined if initDisplayMode has not been called.
     */
    abstract int getDisplayX();

    /**
     * Return the Y-coordinate of the video with respective to the GUI object where the video is displayed. The coordinate is specified in pixel values relative to the upper left hand corner of the GUI object.
     * The return value is undefined if initDisplayMode has not been called.
     */
    abstract int getDisplayY();

    /**
     * Get a snapshot of the displayed content. Features and format of the captured image are specified by imageType. Supported formats can be queried from System.getProperty with
     * as the key. The first format in the supported list is the default capture format.
     */
    abstract byte[] getSnapshot(java.lang.String imageType) throws javax.microedition.media.MediaException;

    /**
     * Return the height of the source video. The height must be a positive number.
     */
    abstract int getSourceHeight();

    /**
     * Return the width of the source video. The width must be a positive number.
     */
    abstract int getSourceWidth();

    /**
     * Initialize the mode on how the video is displayed. This method must be called before video can be displayed.
     * Two modes are defined: USE_GUI_PRIMITIVE (inherited from GUIControl) USE_DIRECT_VIDEO On platforms with LCDUI support, both modes must be supported.
     */
    abstract java.lang.Object initDisplayMode(int mode, java.lang.Object arg);

    /**
     * Set the size of the render region for the video clip to be fullscreen. It is left up to the underlying implementation how fullscreen mode is implemented and what actual dimensions constitutes fullscreen. This is useful when the application does not know the actual width and height dimensions that are needed to make setDisplaySize(width, height) go to fullscreen mode. For example, on a device with a 400 pixel wide by 200 pixel high screen, a video clip that is 50 pixels wide by 100 pixels high in fullscreen mode may be 100 pixels wide by 200 pixels high if the underlying implementation wants to preserve the aspect ratio. In this case, an exception is not thrown.
     */
    abstract void setDisplayFullScreen(boolean fullScreenMode) throws javax.microedition.media.MediaException;

    /**
     * Set the location of the video with respect to the canvas where the video is displayed.
     * This method only works when the USE_DIRECT_VIDEO mode is set. In USE_GUI_PRIMITIVE mode, this call will be ignored.
     * The location is specified in pixel values relative to the upper left hand corner of the GUI object.
     * By default, video appears at location (0,0).
     * The location can be given in negative values or can be greater than the actual size of the canvas. When that happens, the video should be clipped to the boundaries of the canvas.
     */
    abstract void setDisplayLocation(int x, int y);

    /**
     * Resize the video image.
     * If the video mode is set to USE_DIRECT_VIDEO, setting the size of the video will not affect the size of the GUI object that the video is displayed. If the video is scaled to beyond the size of the GUI object, the video will be clipped.
     * If the video mode is set to USE_GUI_PRIMITIVE, Scaling the video will also scale the GUI object.
     * The actual scaling algorithm is left up to the underlying implementation. If the dimensions of the requested display size are smaller than the dimensions of the video clip, some implementations may choose to merely clip the video while other implementations may resize the video.
     * If the dimensions of the requested display size are bigger than the dimensions of the video clip, some implementations may resize the video while others may leave the video clip in the original size and just enlarge the display region. It is left up to the implementation where the video clip is placed in the display region in this instance (i.e., it can be in the center of the window or in a corner of the window).
     */
    abstract void setDisplaySize(int width, int height) throws javax.microedition.media.MediaException;

    /**
     * Show or hide the video.
     * If USE_GUI_PRIMITIVE is set, the video by default is shown when the GUI primitive is displayed. If USE_DIRECT_VIDEO is set, the video by default is not shown when the canvas is displayed until setVisible(true) is called. If the canvas is removed from the screen, the video will not be displayed.
     */
    abstract void setVisible(boolean visible);

}
