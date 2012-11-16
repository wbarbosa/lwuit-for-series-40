package com.nokia.lwuit.test.util;

import com.sun.lwuit.Display;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.microemu.device.DeviceFactory;



/**
 * Base class for all JUnit tests requiring emulator functionality.
 * Takes care of initializing the device and emulator context.
 * 
 * @author @author okaj
 */
public abstract class BaseTest {
    


	/**
	 * Constructor.
	 * 
	 */
	public BaseTest() {	
	}
	 
    @BeforeClass
    public static void setUp() throws Exception {
        
    	HashMap<String, String> properties = new HashMap<String, String>();
    	properties.put("MIDlet-Version", "1.0.0");    	
        System.setProperty("com.nokia.keyboard.type", "PhoneKeypad");
    	HeadlessDevice device = new HeadlessDevice(properties);    	
    	DeviceFactory.setDevice(device.createAndInitializeTestDevice());
     
    }
    
    /*protected void setUp(HashMap<String, String> properties) throws Exception {
        super.setUp();
    	HeadlessDevice device = new HeadlessDevice(properties);    	
    	DeviceFactory.setDevice(device.createAndInitializeTestDevice());
    }*/

    @AfterClass
    public static void tearDown() throws Exception {
        DeviceFactory.getDevice().destroy();
    }

}
