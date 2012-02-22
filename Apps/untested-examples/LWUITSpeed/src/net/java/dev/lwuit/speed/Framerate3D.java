/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package net.java.dev.lwuit.speed;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.M3G;
import com.sun.lwuit.geom.Dimension;
import java.io.IOException;
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
 * Tests how fast drawing can be performed on the device with LWUIT
 *
 * @author Shai Almog
 */
public class Framerate3D extends Form implements M3G.Callback {
    private static int averageFramerate;
    private long oneSecond = System.currentTimeMillis();
    
    private static final int TEST_DURATION = 10000;
    private static final int RAMP_UP_DURATION = 1000;
    private long startTime = System.currentTimeMillis();
    private static int paintCalls;
    
    public static int getFramecount() {
        return paintCalls;
    }
    private World world;
    public Framerate3D() {
        // reduce memory usage for S40 phones...
        getStyle().setBgImage(null);
        show();
        new Thread() {
            public void run() {
                startTime = System.currentTimeMillis();
                
                while (System.currentTimeMillis() - startTime < TEST_DURATION) {
                    try {
                        Graphics3D.getInstance().setCamera(null, null);
                        Graphics3D.getInstance().resetLights();
                    } catch(Throwable err) {
                    }
                    world = null;
                    System.gc();
                    initRotation();
                    oneSecond = System.currentTimeMillis();
                    repaint();
                    while(System.currentTimeMillis() - oneSecond < 1000 && System.currentTimeMillis() - startTime < TEST_DURATION) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {}
                        repaint();
                    }
                }
                averageFramerate = paintCalls / ((TEST_DURATION - RAMP_UP_DURATION) / 1000);
                try {
                    Graphics3D.getInstance().setCamera(null, null);
                    Graphics3D.getInstance().resetLights();
                } catch(Throwable err) {
                }
                world = null;
                new Form("Please Wait").show();
                System.gc();
                new ListTest();
            }
        }.start();
    }
    
    public static int getAverageFramerate() {
        return averageFramerate;
    }
    
    public void paintBackground(Graphics g) {
    }
    
    public void paint(Graphics g) {
        if(world != null) {
            if(System.currentTimeMillis() - startTime > RAMP_UP_DURATION) {
                paintCalls++;
            }
            g.setColor(0);
            g.fillRect(0, 0, getWidth(), getHeight());
            M3G.getInstance().renderM3G(g, true, 0, this);
        }
    }

    public void paintM3G(Graphics3D g) {
        int time = (int)(System.currentTimeMillis() - oneSecond);
        world.animate(time);
        g.render(world);
    }

    public void initRotation() {  
        if(!Display.getInstance().isEdt()) {
            Display.getInstance().callSeriallyAndWait(new Runnable() {
                public void run() {
                    initRotation();
                }
            });
            return;
        }
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
        background3D.setColor(0xff000000);
        world.setBackground(background3D);
        
        Form rotatingForm = new Form("Rotation");
        rotatingForm.addComponent(new Label("This form should rotate!"));
        Mesh destMesh = createMesh(rotatingForm);

        group.addChild(destMesh);
        createRotation(group, destMesh, rotatingForm);
        
        world.animate(0);
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

    private Image2D createImage2D(Component c) {
        int w = Display.getInstance().getDisplayWidth();
        int h = Display.getInstance().getDisplayHeight();
        int textureW;
        int textureH;
        c.setVisible(true);
        // use the true texture maximum resolution ignoring light mode...
        int max = M3G.getInstance().getMaxTextureDimension();
        textureW = Math.min(M3G.closestLowerPowerOf2(w), max);
        textureH = Math.min(M3G.closestLowerPowerOf2(h), max);
        Image mutable = Image.createImage(c.getWidth(), c.getHeight());
        c.paintComponent(mutable.getGraphics());
        mutable = mutable.scaled(textureW, textureH);
        return M3G.getInstance().createImage2D(Image2D.RGB, mutable);
    }

    private void createRotation(Group group, Mesh destMesh, Form rotatingForm) {
        Node sourceMesh = (Node)createMesh(rotatingForm);
        group.addChild(sourceMesh);

        sourceMesh.setTranslation(0, 0, -3.79f);
        destMesh.setTranslation(0, 0, -3.8f);
        
        KeyframeSequence rotationSource = new KeyframeSequence(3, 4, KeyframeSequence.SPLINE);
        KeyframeSequence rotationDest = new KeyframeSequence(3, 4, KeyframeSequence.SPLINE);

        rotationSource.setDuration(1000);
        rotationDest.setDuration(1000);
        
        int half = 45;
        int full = 90;

        // prevent the rotation from staying too long in 90 degrees
        int halfDest = 150;
        int fullDest = 180;
        
        rotationSource.setKeyframe(0, 0, getYRoation(0));
        rotationSource.setKeyframe(1, 1000 / 4, getYRoation(half));
        rotationSource.setKeyframe(2, 1000 / 2, getYRoation(full));
        
        rotationDest.setKeyframe(0, 0, getYRoation(full));
        
        rotationDest.setKeyframe(1, 1000 / 2, getYRoation(halfDest));
        rotationDest.setKeyframe(2, 1000, getYRoation(fullDest));
                
        AnimationController animation = new AnimationController();
        AnimationTrack track = new AnimationTrack(rotationSource, AnimationTrack.ORIENTATION);
        sourceMesh.addAnimationTrack(track);
        track.setController(animation);
        
        track = new AnimationTrack(rotationDest, AnimationTrack.ORIENTATION);
        destMesh.addAnimationTrack(track);
        track.setController(animation);
    }

    /**
     * Creates a rotation matrix on the Y axis
     */
    private float[] getYRoation(float angle) {
        angle = (float)Math.toRadians(angle);
        return new float[] {0, (float)Math.sin(angle), 0, (float)Math.cos(angle)};
    }
    
}
