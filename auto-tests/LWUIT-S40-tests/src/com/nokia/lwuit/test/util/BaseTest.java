package com.nokia.lwuit.test.util;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
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
	 
    @Before
    public void setUp() throws Exception {
        
    	HashMap<String, String> properties = new HashMap<String, String>();
    	properties.put("MIDlet-Version", "1.0.0");    	
    	HeadlessDevice device = new HeadlessDevice(properties);    	
    	DeviceFactory.setDevice(device.createAndInitializeTestDevice());
    	
    }
    
    /*protected void setUp(HashMap<String, String> properties) throws Exception {
        super.setUp();
    	HeadlessDevice device = new HeadlessDevice(properties);    	
    	DeviceFactory.setDevice(device.createAndInitializeTestDevice());
    }*/

    @After
    public void tearDown() throws Exception {
        
    }
	
}
