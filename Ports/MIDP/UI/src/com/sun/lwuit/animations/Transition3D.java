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
package com.sun.lwuit.animations;

import com.sun.lwuit.animations.*;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.M3G;
import javax.microedition.m3g.AnimationController;
import javax.microedition.m3g.AnimationTrack;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.Background;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Group;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.KeyframeSequence;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.Node;
import javax.microedition.m3g.PolygonMode;
import javax.microedition.m3g.Texture2D;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;
import javax.microedition.m3g.World;

/**
 * Transitions utilizing the M3G API for 3D effects, this transition requires
 * M3G (JSR 184) support on the device in order to work properly. Currently
 * none of these transitions work with dialogs or any component type that 
 * is not a form
 *
 * @author Shai Almog
 */
public final class Transition3D extends Transition implements M3G.Callback {
    private static final int FLY_IN = 2;
    private static final int ROTATION = 1;
    private static final int CUBE_ROTATION = 3;
    private static final int STATIC_ROTATION = 4;
    private static final int SWING_IN = 5;
    private static final int VERTICAL_CUBE_ROTATION = 6;
    
    private int transitionType;
    private World world;
    //private int rotation;
    private long startTime;
    private int duration = 1000;
    private static int maxTextureResolution = -1;
    private boolean rotateRight;
    private boolean highQualityMode;
    private boolean firstFinished;
    private int clipX, clipY, clipW, clipH;
    private boolean firstTime = true;
    
    
    private Transition3D(int transitionType) {
        this.transitionType = transitionType;
    }
    
    /**
     * @inheritDoc
     */
    public void cleanup() {
        super.cleanup();
        world = null;
        try {
            Graphics3D.getInstance().setCamera(null, null);
            Graphics3D.getInstance().resetLights();
        } catch(Throwable err) {
        }
        firstTime = true;
    }
    
    private static void initMaxTexture() {
        maxTextureResolution = M3G.getInstance().getMaxTextureDimension();
    }
    
    /**
     * Allows performance/memory sensitive devices to define a maximum size for the 
     * texture thus increasing performance on the expense of quality.
     * @param size
     */
    public static void setMaxTextureDimension(int size) {
        maxTextureResolution = size;
    }
    
    /**
     * Creates a rotation transition from the current form to the next form
     * 
     * @param duration duration in milliseconds of the transition
     * @param rotateRight indicates rotating towards the right side or the left side true == right
     * @return newly created transition object
     */
    public static Transition3D createRotation(int duration, boolean rotateRight) {
        initMaxTexture();
        Transition3D t3d = new Transition3D(ROTATION);
        t3d.duration = duration;
        t3d.rotateRight = rotateRight;
        return t3d;
    }

    /**
     * Creates a rotation transition from the current form to the next dialog, in this rotation only
     * the dialog will rotate and the form will remain static
     * 
     * @param duration duration in milliseconds of the transition
     * @param rotateRight indicates rotating towards the right side or the left side true == right
     * @return newly created transition object
     */
    public static Transition3D createStaticRotation(int duration, boolean rotateRight) {
        initMaxTexture();
        Transition3D t3d = new Transition3D(STATIC_ROTATION);
        t3d.duration = duration;
        t3d.rotateRight = rotateRight;
        return t3d;
    }

    /**
     * Creates a rotation transition from the top to the bottom giving a feeling of Swinging into place
     * 
     * @param duration duration in milliseconds of the transition
     * @return new transtion object
     */
    public static Transition3D createSwingIn(int duration) {
        return createSwingIn(duration, true);
    }

    /**
     * Creates a rotation transition from the top to the bottom giving a feeling of Swinging into place
     * 
     * @param duration duration in milliseconds of the transition
     * @param topDown indicates rotating downwards or upwards
     * @return new transtion object
     */
    public static Transition3D createSwingIn(int duration, boolean topDown) {
        initMaxTexture();
        Transition3D t3d = new Transition3D(SWING_IN);
        t3d.duration = duration;
        t3d.rotateRight = topDown;
        return t3d;
    }

    /**
     * Creates a cube rotation transition from the current form to the next form
     * 
     * @param duration duration in milliseconds of the transition
     * @param rotateRight indicates rotating towards the right side or the left side true == right
     * @return newly created transition object
     */
    public static Transition3D createCube(int duration, boolean rotateRight) {
        initMaxTexture();
        Transition3D t3d = new Transition3D(CUBE_ROTATION);
        t3d.duration = duration;
        t3d.rotateRight = rotateRight;
        return t3d;
    }

    /**
     * Creates a cube rotation transition from the current form to the next form
     * 
     * @param duration duration in milliseconds of the transition
     * @param rotateDown indicates rotating towards the upper side when true
     * @return newly created transition object
     */
    public static Transition3D createVerticalCube(int duration, boolean rotateDown) {
        initMaxTexture();
        Transition3D t3d = new Transition3D(VERTICAL_CUBE_ROTATION);
        t3d.duration = duration;
        t3d.rotateRight = rotateDown;
        return t3d;
    }

    /**
     * Creates a fly in transition object.
     * 
     * @param duration duration in milliseconds of the transition
     * @return newly created transition object
     */
    public static Transition3D createFlyIn(int duration) {
        initMaxTexture();
        Transition3D t3d = new Transition3D(FLY_IN);
        t3d.duration = duration;
        return t3d;
    }
    
    /**
     * @inheritDoc
     */
    public Transition copy(boolean reverse) {
        Transition3D t3d = new Transition3D(transitionType);
        t3d.duration = duration;
        if(reverse) {
            t3d.rotateRight = !rotateRight;
        } else {
            t3d.rotateRight = rotateRight;
        }
        return t3d;
    }

    /**
     * @inheritDoc
     */
    public boolean animate() {
        if(world == null) {
            return false;
        }
        long time = System.currentTimeMillis();
        int current = (int)(time - startTime);
        world.animate(current);
        
        // after the motion finished we need to paint one last time otherwise
        // there will be a "bump" in sliding
        if(firstFinished) {
            return false;
        }
        boolean finished = current >= duration;
        if(finished) {
            if(!firstFinished) {
                firstFinished = true;
            }
        }
        return true;
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        // prevents painting when the components are too small
        if(world == null) {
            return;
        }
        
        // paint the finished component if the animation completed
        if(firstFinished) {
            getDestination().paintComponent(g);
            return;
        }
        Component c = getSource();
        int titleHeight = 0;
        if(c instanceof Dialog) {
            Dialog dlg = (Dialog)c;
            c = dlg.getContentPane();
            titleHeight = dlg.getTitleComponent().getHeight();
        } else {
            if(getDestination() instanceof Dialog) {
                Dialog dlg = (Dialog)getDestination();
                c = dlg.getContentPane();
                titleHeight = dlg.getTitleComponent().getHeight();
            } else {
                if(firstTime) {
                    startTime = System.currentTimeMillis();
                    firstTime = false;
                }
            }
        }
        if(firstTime) {
            startTime = System.currentTimeMillis();
            firstTime = false;
            // paint the whole source form once to make sure we have the proper
            // tinting behavior
            if(getDestination() instanceof Dialog) { 
                getSource().paintComponent(g);
            } else {
                getDestination().paintComponent(g);
            }
        }
        clipY = c.getAbsoluteY() - titleHeight;
        clipX = c.getAbsoluteX();
        clipW = c.getWidth();
        clipH = c.getHeight() + titleHeight;
        g.setClip(clipX, clipY, clipW, clipH);
        M3G.getInstance().renderM3G(g, false, Graphics3D.ANTIALIAS, this);
    }

    /**
     * @inheritDoc
     */
    public void initTransition() {
        try {
            Component source = getSource();
            Component dest = getDestination();

            // prevent painting when the components are too small
            if(source == null || dest == null ||
                    source.getWidth() < 4 || source.getHeight() < 4 ||
                    dest.getWidth() < 4 || dest.getHeight() < 4) {
                return;
            }
            firstFinished = false;
            world = new World();
            Camera camera = new Camera();

            camera.setPerspective(30.0f, 1, 1.0f, 45.0f);
            Transform cameraTransform = new Transform();
            Graphics3D g3d = Graphics3D.getInstance();
            g3d.setCamera(camera, cameraTransform);

            // the whole world is within a single group in the scene graph tree
            Group group = new Group();
            world.addChild(group);
            world.setActiveCamera(camera);
            group.addChild(camera);

            Background background3D = new Background();
            background3D.setColorClearEnable(true);
            background3D.setDepthClearEnable(true);
            background3D.setColor(0xff000000 | source.getStyle().getBgColor());
            world.setBackground(background3D);

            switch(transitionType) {
                case FLY_IN: {
                    Mesh destMesh = createMesh(dest);
                    group.addChild(destMesh);
                    createFlyIn(destMesh, background3D);
                    break;
                }
                case CUBE_ROTATION: {
                    Mesh destMesh = createMesh(dest);
                    createCubeRotation(group, destMesh, false);
                    break;
                }
                case VERTICAL_CUBE_ROTATION: {
                    Mesh destMesh = createMesh(dest);
                    createCubeRotation(group, destMesh, true);
                    break;
                }
                case ROTATION: {
                    Mesh destMesh = createMesh(dest);
                    group.addChild(destMesh);
                    createRotation(group, destMesh);
                    break;
                }
                case SWING_IN:
                case STATIC_ROTATION: {
                    Mesh destMesh;
                    if(source instanceof Dialog) {
                        destMesh = createMesh(source);
                        source = dest;
                    } else {
                        destMesh = createMesh(dest);
                    }
                    createStaticRotation(group, destMesh, background3D, source, transitionType == SWING_IN);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Bad 3D mode : " + transitionType);
            }

            world.animate(0);

            startTime = System.currentTimeMillis();
        } catch(Throwable t) {
            t.printStackTrace();
            cleanup();
        }
    }
    
    private void createFlyIn(Mesh destMesh, Background background3D) {
        background3D.setImage(createImage2D(getSource()));
        KeyframeSequence trans = new KeyframeSequence(10, 3, KeyframeSequence.LINEAR);
        KeyframeSequence alpha = new KeyframeSequence(2, 1, KeyframeSequence.LINEAR);
        alpha.setDuration(duration);
        trans.setDuration(duration);
        
        alpha.setKeyframe(0, 0, new float[] {0.1f});
        alpha.setKeyframe(1, duration, new float[] {1});
        int frac = duration / 10;
        int time = 0;
        
        float position = -31;
        for(int iter = 0 ; iter < 10 ; iter++) {
            float[] pos = new float[] {0, 0, position};
            trans.setKeyframe(iter, time, pos);
            position += 3;
            time += frac;
        }
                
        AnimationController animation = new AnimationController();
        AnimationTrack track = new AnimationTrack(trans, AnimationTrack.TRANSLATION);
        destMesh.addAnimationTrack(track);
        track.setController(animation);

        track = new AnimationTrack(alpha, AnimationTrack.ALPHA);
        destMesh.addAnimationTrack(track);
        track.setController(animation);
    }
    
    private void createCubeRotation(Group parent, Mesh destMesh, boolean vertical) {
        Group group = new Group();
        parent.addChild(group);
        Mesh sourceMesh = createMesh(getSource());

        group.addChild(destMesh);
        group.addChild(sourceMesh);
        sourceMesh.setTranslation(0f, 0, 1.0f);
        group.translate(0f, 0, -4.7f);
        KeyframeSequence rotation = new KeyframeSequence(3, 4, KeyframeSequence.SPLINE);

        rotation.setDuration(duration);
        
        if(!vertical) {
            if(rotateRight) {
                destMesh.setOrientation(270, 0, 1, 0);
                destMesh.setTranslation(-1.0f, 0, 0f);
            } else {
                destMesh.setOrientation(90, 0, 1, 0);
                destMesh.setTranslation(1.0f, 0, 0f);
            }
            rotation.setKeyframe(0, 0, getYRoation(0));
            if(rotateRight) {
                rotation.setKeyframe(1, duration / 2, getYRoation(20));
                rotation.setKeyframe(2, duration, getYRoation(45));
            } else {
                rotation.setKeyframe(1, duration / 2, getYRoation(-20));
                rotation.setKeyframe(2, duration, getYRoation(-45));
            }
        } else {
            if(rotateRight) {
                destMesh.setOrientation(270, 1, 0, 0);
                destMesh.setTranslation(0.0f, 1.0f, 0f);
            } else {
                destMesh.setOrientation(90, 1, 0, 0);
                destMesh.setTranslation(0.0f, -1.0f, 0f);
            }
            rotation.setKeyframe(0, 0, getXRoationTop(0));
            if(rotateRight) {
                rotation.setKeyframe(1, duration / 2, getXRoationTop(20));
                rotation.setKeyframe(2, duration, getXRoationTop(45));
            } else {
                rotation.setKeyframe(1, duration / 2, getXRoationTop(-20));
                rotation.setKeyframe(2, duration, getXRoationTop(-45));
            }
        }
                
        AnimationController animation = new AnimationController();
        AnimationTrack track = new AnimationTrack(rotation, AnimationTrack.ORIENTATION);
        group.addAnimationTrack(track);
        track.setController(animation);
    }

    private void createRotation(Group group, Mesh destMesh) {
        Node sourceMesh = (Node)createMesh(getSource());
        group.addChild(sourceMesh);

        sourceMesh.setTranslation(0, 0, -3.79f);
        destMesh.setTranslation(0, 0, -3.8f);
        
        KeyframeSequence rotationSource = new KeyframeSequence(3, 4, KeyframeSequence.SPLINE);
        KeyframeSequence rotationDest = new KeyframeSequence(3, 4, KeyframeSequence.SPLINE);

        rotationSource.setDuration(duration);
        rotationDest.setDuration(duration);
        
        int half = 45;
        int full = 90;

        // prevent the rotation from staying too long in 90 degrees
        int halfDest = 150;
        int fullDest = 180;
        if(rotateRight) {
            half = -half;
            full = -full;
            halfDest = -halfDest;
            fullDest = -fullDest;
        }
        
        rotationSource.setKeyframe(0, 0, getYRoation(0));
        rotationSource.setKeyframe(1, duration / 4, getYRoation(half));
        rotationSource.setKeyframe(2, duration / 2, getYRoation(full));
        
        rotationDest.setKeyframe(0, 0, getYRoation(full));
        
        rotationDest.setKeyframe(1, duration / 2, getYRoation(halfDest));
        rotationDest.setKeyframe(2, duration, getYRoation(fullDest));
                
        AnimationController animation = new AnimationController();
        AnimationTrack track = new AnimationTrack(rotationSource, AnimationTrack.ORIENTATION);
        sourceMesh.addAnimationTrack(track);
        track.setController(animation);
        
        track = new AnimationTrack(rotationDest, AnimationTrack.ORIENTATION);
        destMesh.addAnimationTrack(track);
        track.setController(animation);
    }

    private void createStaticRotation(Group group, Mesh destMesh, Background background3D, Component source, boolean swingIn) {
        background3D.setImage(createImage2D(source));

        KeyframeSequence rotationDest;
        
        // prevent the rotation from staying too long in 90 degrees
        int halfDest = 90;
        int fullDest = 180;
        int mid = 135;
                
        AnimationController animation = new AnimationController();
        Node rotatingNode;
        if(swingIn) {   
            rotationDest = new KeyframeSequence(5, 4, KeyframeSequence.SPLINE);
            rotationDest.setDuration(duration);
            Group rotation = new Group();
            group.addChild(rotation);
            rotation.addChild(destMesh);
            
            if(rotateRight) {
                rotation.translate(0, 0.8f, -4.0f);
                destMesh.translate(0, -0.8f, 0);
            } else {
                rotation.translate(0, -0.8f, -4.0f);
                destMesh.translate(0, 0.8f, 0);
            }
            int quarter = duration / 4;
            if(source == getSource()) {
                rotationDest.setKeyframe(0, 0, getXRoationTop(halfDest));        
                rotationDest.setKeyframe(1, quarter, getXRoationTop(mid));
                rotationDest.setKeyframe(2, quarter * 2, getXRoationTop(fullDest));
                rotationDest.setKeyframe(3, quarter * 3, getXRoationTop(fullDest + halfDest / 6));
                rotationDest.setKeyframe(4, duration, getXRoationTop(fullDest));
            } else {
                rotationDest.setKeyframe(0, 0, getXRoationTop(fullDest));        
                rotationDest.setKeyframe(1, quarter, getXRoationTop(fullDest + halfDest / 6));
                rotationDest.setKeyframe(2, quarter * 2, getXRoationTop(fullDest));        
                rotationDest.setKeyframe(3, quarter * 3, getXRoationTop(mid));
                rotationDest.setKeyframe(4, duration, getXRoationTop(halfDest));
            }
            rotatingNode = rotation;
        } else {
            if(rotateRight) {
                halfDest = -halfDest;
                fullDest = -fullDest;
                mid = -mid;
            }

            destMesh.setTranslation(0, 0, -3.8f);        
            rotationDest = new KeyframeSequence(3, 4, KeyframeSequence.SPLINE);
            rotationDest.setDuration(duration); 
            group.addChild(destMesh);
            if(source == getSource()) {
                rotationDest.setKeyframe(0, 0, getYRoation(halfDest));        
                rotationDest.setKeyframe(1, duration / 2, getYRoation(mid));
                rotationDest.setKeyframe(2, duration, getYRoation(fullDest));
            } else {
                rotationDest.setKeyframe(0, 0, getYRoation(fullDest));        
                rotationDest.setKeyframe(1, duration / 2, getYRoation(mid));
                rotationDest.setKeyframe(2, duration, getYRoation(halfDest));
            }
            rotatingNode = destMesh;
        }
        
        AnimationTrack track = new AnimationTrack(rotationDest, AnimationTrack.ORIENTATION);
        rotatingNode.addAnimationTrack(track);
        track.setController(animation);
    }
    
    /**
     * Creates a rotation matrix on the Y axis
     */
    private float[] getYRoation(float angle) {
        angle = (float)Math.toRadians(angle);
        return new float[] {0, (float)Math.sin(angle), 0, (float)Math.cos(angle)};
    }
    
    /**
     * Creates a rotation matrix on the top X axis
     */
    private float[] getXRoationTop(float angle) {
        angle = (float)Math.toRadians(angle);
        return new float[] {(float)Math.sin(angle), 0, 0, (float)Math.cos(angle)};
    }

    private Image2D createImage2D(Component c) {
        int w = c.getWidth();
        int h = c.getHeight();
        Dialog dlg = null;
        if(getSource() instanceof Dialog) {
            dlg = (Dialog)getSource();
            w = dlg.getContentPane().getWidth();
            h = dlg.getContentPane().getHeight() + dlg.getTitleComponent().getHeight();
        } else {
            if(getDestination() instanceof Dialog) {
                dlg = (Dialog)getDestination();
                w = dlg.getContentPane().getWidth();
                h = dlg.getContentPane().getHeight() + dlg.getTitleComponent().getHeight();
            } 
        }
        int textureW;
        int textureH;
        if(highQualityMode) {
            // use the true texture maximum resolution ignoring light mode...
            int max = M3G.getInstance().getMaxTextureDimension();
            textureW = Math.min(M3G.closestHigherPowerOf2(w - 1), max);
            textureH = Math.min(M3G.closestHigherPowerOf2(h - 1), max);
        } else {
            textureW = Math.min(M3G.closestLowerPowerOf2(w + 1), maxTextureResolution);
            textureH = Math.min(M3G.closestLowerPowerOf2(h + 1), maxTextureResolution);
        }
        Image mutable = Image.createImage(w, h);
        Graphics g = mutable.getGraphics();
        if(c instanceof Dialog) {
            c = dlg.getContentPane();
            g.translate(-c.getAbsoluteX(), -c.getAbsoluteY() + dlg.getTitleComponent().getHeight());
            dlg.getContentPane().paintComponent(g, false);
            dlg.getTitleComponent().paintComponent(g, false);
            //g.setClip(c.getAbsoluteX(), c.getAbsoluteY() + dlg.getTitleComponent().getHeight(), mutable.getWidth(), mutable.getHeight());
        } else {
            if(dlg != null) {
                Component content = dlg.getContentPane();
                g.translate(-content.getAbsoluteX(), -content.getAbsoluteY() + dlg.getTitleComponent().getHeight());
            } else {
                g.translate(-c.getAbsoluteX(), -c.getAbsoluteY());
            }
            c.paintComponent(g);
        }
        mutable = mutable.scaled(textureW, textureH);
        return M3G.getInstance().createImage2D(Image2D.RGB, mutable);
    }
    
    private Mesh createMesh(Component c) {
        Texture2D tex = createTexture(createImage2D(c));
        CompositingMode cm = new CompositingMode();
        cm.setBlending(CompositingMode.ALPHA);
        cm.setAlphaWriteEnable(true);
        cm.setDepthTestEnable(true);
        Appearance appearance = new Appearance();
        PolygonMode polyMode = new PolygonMode();
        polyMode.setPerspectiveCorrectionEnable(true);
        appearance.setPolygonMode(polyMode);
        appearance.setCompositingMode(cm);
        appearance.setTexture(0, tex);
            
        VertexBuffer vb = makeGeometry(); 
        int[] indicies = {1,2,0,3};  // one quad 
        int[] stripLens = {4}; 
        IndexBuffer ib = new TriangleStripArray(indicies, stripLens); 

        Mesh m = new Mesh(vb, ib, appearance);
        return m; 
    }

    private VertexBuffer makeGeometry() {
        // create vertices
        short[] verts = { -1, -1, 0, 1, -1, 0, 1, 1, 0, -1, 1, 0 };

        VertexArray va = new VertexArray(verts.length / 3, 3, 2);
        va.set(0, verts.length / 3, verts);

        // create texture coordinates
        short[] tcs = { 0, 1, 1, 1, 1, 0, 0, 0 };

        VertexArray texArray = new VertexArray(tcs.length / 2, 2, 2);
        texArray.set(0, tcs.length / 2, tcs);

        VertexBuffer vb = new VertexBuffer();
        vb.setPositions(va, 1.0f, null); // no scale, bias
        vb.setTexCoords(0, texArray, 1.0f, null);

        return vb;
    }
    
    /**
     * Creates a texture making 
     */
    private Texture2D createTexture(Image2D img) {
        Texture2D tex = new Texture2D(img); 
        tex.setFiltering(Texture2D.FILTER_NEAREST, Texture2D.FILTER_NEAREST); 
        tex.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP); 
        tex.setBlending(Texture2D.FUNC_REPLACE); 
        return tex;
    }
    
    /**
     * @inheritDoc
     */
    public void paintM3G(Graphics3D g) {
        g.render(world);
    }


    /**
     * High quality mode renders the transition using smoother graphics but can
     * take a whole lot more memory per texture and bitmap resulting in a likely
     * out of memory error on high resolution/low memory devices with complex UI
     * elements.
     * 
     * @return whether this is high quality rendering mode
     */
    public boolean isHighQualityMode() {
        return highQualityMode;
    }

    /**
     * High quality mode renders the transition using smoother graphics but can
     * take a whole lot more memory per texture and bitmap resulting in a likely
     * out of memory error on high resolution/low memory devices with complex UI
     * elements.
     * 
     * @param highQualityMode indicates whether this is the high quality mode
     */
    public void setHighQualityMode(boolean highQualityMode) {
        this.highQualityMode = highQualityMode;
    }
}
