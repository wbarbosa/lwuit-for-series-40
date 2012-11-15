package com.nokia.lwuit.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.microemu.DisplayComponent;
import org.microemu.EmulatorContext;
import org.microemu.MIDletBridge;
import org.microemu.app.Common;
import org.microemu.app.ui.Message;
import org.microemu.app.ui.noui.NoUiDisplayComponent;
import org.microemu.app.util.DeviceEntry;
import org.microemu.device.Device;
import org.microemu.device.DeviceDisplay;
import org.microemu.device.FontManager;
import org.microemu.device.InputMethod;
import org.microemu.device.impl.DeviceImpl;
import org.microemu.device.j2se.J2SEDevice;
import org.microemu.device.j2se.J2SEDeviceDisplay;
import org.microemu.device.j2se.J2SEFontManager;
import org.microemu.device.j2se.J2SEInputMethod;

/**
 * Based on org.microemu.app.Headless;
 * 
 * @author okaj
 */
public class HeadlessDevice {

	private Common emulator;

	/**
	 * Constructor.
	 */
	public HeadlessDevice() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param propertyMap
	 */
	public HeadlessDevice(final Map<String, String> propertyMap) {		
		
		EmulatorContext context = getEmulatorContext();
		emulator = new Common(context) {
			@Override
			public String getAppProperty(String key) {				
				if (propertyMap == null) {				
					return super.getAppProperty(key);
				} else {
					return propertyMap.get(key);
				}
			}
		};
	}

	/**
	 * Returns emulator.
	 *  
	 * @return emulator
	 */
	public Common getEmulator() {
		return emulator;
	}

	/**
	 * Initializes test device.
	 * 
	 * @return device
	 * @throws IOException
	 */
	public Device createAndInitializeTestDevice() throws IOException {
				
		ArrayList<String> params = new ArrayList<String>();
	
		params.add("--rms");
		params.add("memory");
		DeviceEntry defaultDevice = new DeviceEntry("Default device", null, DeviceImpl.DEFAULT_LOCATION, true, false);		
		emulator.initParams(params, defaultDevice, J2SEDevice.class);
		MIDletBridge.setMicroEmulator(emulator);
		return emulator.getDevice();
	}

	/**
	 * Returns emulator context.
	 * 
	 * @return EmulatorContext
	 */
	private EmulatorContext getEmulatorContext() {
		
		EmulatorContext context = new EmulatorContext() {
			
			private DisplayComponent displayComponent = new NoUiDisplayComponent();
			private InputMethod inputMethod = new J2SEInputMethod();
			private DeviceDisplay deviceDisplay = new J2SEDeviceDisplay(this);
			private FontManager fontManager = new J2SEFontManager();

			public DisplayComponent getDisplayComponent() {
				return displayComponent;
			}

			public InputMethod getDeviceInputMethod() {
				return inputMethod;
			}

			public DeviceDisplay getDeviceDisplay() {
				return deviceDisplay;
			}	

			public FontManager getDeviceFontManager() {
				return fontManager;
			}

			public InputStream getResourceAsStream(String name) {
				return MIDletBridge.getCurrentMIDlet().getClass().getResourceAsStream(name);
			}
		
			public boolean platformRequest(final String URL) {
				new Thread(new Runnable() {
					public void run() {
						Message.info("MIDlet requests that the device handle the following URL: " + URL);
					}
				}).start();

				return false;
			}
		};
		
		return context;
	}
}
