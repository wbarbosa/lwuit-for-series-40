/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.lwuit.test.util;

import com.sun.lwuit.Display;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author tkor
 */
public class LWUITTest extends BaseTest{
    
    static DummyMidlet m;
        
            
    @BeforeClass
    public static  void initMidletAndLWUIT() {
        if(m == null) {
        m = new DummyMidlet();
        m.startApplication();
        }
    }
    
    
    @AfterClass
    public static void killLWUIT() {
        if(m != null) {
            m.stopApplication();
        }
    }
    
    protected void waitEdt() throws InterruptedException {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                synchronized(this) {
                    notifyAll();
                }
            }
        };
        Display.getInstance().callSerially(runnable);
        synchronized(runnable) {
            runnable.wait(2000);
        }
    }
}
