===================================================================================
Release Notes               LWUIT for Series 40                       June 29, 2012
===================================================================================
           
Description of the Library:
====================
LWUIT for Series 40 provides a Series 40 optimised port of the famous LightWeight
UI Toolkit, which is licensed under GPLv2 with ClassPath exception licence. It has
been optimised for Series 40 with a number of styling, theming, and functional
changes. For example, the Nokia TextEditor and Series 40 Gesture framework have
been taken into use. LWUIT for Series 40 has been developed and tested with
Nokia SDK 1.1 and 2.0 for Java and it can be used for applications targeted at
Series 40 5th Edition and newer devices with both keypad and touch user interface.

The application is hosted in Nokia Developer Projects:
- http://projects.developer.nokia.com/LWUIT_for_Series_40

For more information on the implementation, visit the wiki page:
- http://projects.developer.nokia.com/LWUIT_for_Series_40/wiki


What's New
===========

- The first release for the SDK.
- LWUIT for Series 40 now supports Series 40 full touch.


Features supported by the library:
===================================

• Themes for Series 40 full touch, touch and type, and non-touch
• Changes to UI components, softkeys and menus to adapt to Series 40 behaviour
• Support for Nokia TextEditor, and Gesture FW added
• Resource Editor modified to improve the development process
• New and updated example apps for Series 40

Choosing JAR
============

A theme allows us to set the style attributes for an entire class of components in a single place.

To get S40 look and feel to your application, use S40-with-themes.jar. If you want to have a theme of you own and save in size of jar file, use S40-no-themes.jar. Use the jar under the release name folder (sdk2.0/sdk1.1/s40-6th-ed-sdk) for same or earlier version of devices.
 
For example, if you write a LWUIT application for SDK 2.0 and 1.1 devices, use sdk2.0/S40-touch-and-type.jar. Applications using LWUIT jars under specific release folder are expected to compile and run on the same release but in earlier versions they only run but do not compile.

IO-MIDP.jar is S40 LWUIT MIDP version and IO-SE.jar is Java SE version. Both provide tools for networking and storage & file system IO operations.


Supported Devices/Platform:
===========================
• Series 40 6th Edition (non-touch)
• Series 40 6th Edition, FP1 (non-touch and touch and type)
• Series 40 Developer Platform 1.1 (non-touch and touch and type)
• Series 40 Developer Platform 2.0 (full touch)

Supported resolutions:
240 x 320
320 x 240
240 x 400

Tested on:
Nokia Asha 311 (Java Runtime 2.0.0 for Series 40)
Nokia Asha 306 (Java Runtime 2.0.0 for Series 40)
Nokia Asha 305 (Java Runtime 2.0.0 for Series 40)
Nokia Asha 303 (Java Runtime 1.1.0 for Series 40) 
Nokia Asha 302 (Java Runtime 1.1.0 for Series 40)
Nokia Asha 300 (Series 40 6th Edition FP1)
Nokia Asha 202 (Java Runtime 1.1.0 for Series 40)
Nokia Asha 201 (Java Runtime 1.1.0 for Series 40)
Nokia Asha 200 (Java Runtime 1.1.0 for Series 40)
Nokia C2-03 (Java Runtime 1.0.0 for Series 40)
Nokia C3-01 (Series 40 6th Edition FP1)
Nokia X3-02 (Series 40 6th Edition FP1)
Nokia C3-00 (Series 40 6th Edition)
Nokia 6303 (Series 40 6th Edition)
Nokia C1-01 (Series 40 6th Edition Lite)
Nokia 2690 (Series 40 6th Edition Lite)
Nokia 6212 (Series 40 5th Edition FP1)
Nokia 2700 classic (Series 40 5th Edition FP1)
Nokia 2730 classic (Series 40 5th Edition FP1)

Developed with:
Netbeans 7.1
Nokia SDK 1.1 for Java
Nokia SDK 2.0 for Java

Integration instructions to developers:
========================================

• To include LWUIT for Series 40 libraries and documentation to Eclipse IDE
  - Open Eclipse IDE and open your project.
  - Select Project -> Properties -> Java Build Path -> Libraries.
  - Click 'Add External JARs'.
  - Select the LWUIT library for example <LWUIT_directory>/bin/sdk2.0
    /S40-with-themes.jar. and click 'Open' and then 'OK'.
  - In Package Explorer, select the Referenced Libraries and right click on the
    S40-with-themes JAR and select Properties.
  - Select Javadoc Location and click 'Browse'.
  - Select the LWUIT library from <LWUIT_directory>/doc/LWUIT-UI/doc and click 'OK'.
  - Click 'OK' once more and you are ready to go.

• To include LWUIT for Series 40 libraries and documentation to NetBeans IDE
  - Open the NetBeans IDE and open your project.
  - Goto Tools -> Libraries.
  - Click the 'New Library...' button.
  - Name the library for example as 'LWUIT_for_2.0_with_themes'.
  - Set Libary Type to 'Class Libraries' and click 'OK'.
  - Click 'Add JAR/Folder...'.
  - Select the LWUIT library for example <LWUIT_directory>/bin/sdk2.0
    /S40-with-themes.jar.
    and click 'Add JAR/Folder'.
  - Select the Javadoc tab and click 'Add ZIP/Folder'.
  - Select the LWUIT library from <LWUIT_directory>/doc/LWUIT-UI/doc and click 'OK'.
  - Right click your project and select Properties.
  - Select Build -> Libraries & Resources and click 'Add Library...'.
  - Select the library you created and click 'Add Library' and then 'OK'.

Known Problems and Limitations:
===============================

- LWUIT for Series 40 still has not been tested thoroughly and a set of bugs have
  already been reported for this release concerning for example Tab, Dialog and
  List layouts.

- LWUIT Browser is still under active development

- Nokia-UI-Enhancement "CanvasHasBackground" can't be used

- This manifest attribute makes the canvas background transparent so that the
  system background can be seen. However, it   doesn't work with full-screen
  canvases, and LWUIT's canvas is always full screen.

- In full touch devices, the background colour is read at app startup.

- The slider component is practically unusable at the moment.

- On non-touch devices, when the UI has a spinner component, you can't move focus
  away from the spinner.

- The LWUIT library is added to every LWUIT application. The application needs to
  be obfuscated to keep the binary size moderate. The LWUIT UI framework takes
  700-800 KB of space but with obfuscation it goes down to 350KB or even less,
  depending on the complexity of the application.

- LWUIT is not optimised for applications that require very high-speed graphics
  (full-blown games). LCDUI's Canvas and GameCanvas are better choices there.

- 3D transitions will not work. However, other transitions like Slide, Fade and etc work.

- The Series 40 device portfolio ranges from very price-competitive entry devices
  with a 128*160 pixel display and keyboard input to capable touch UI phones with
  a QVGA display and many times more CPU and RAM. This means that you need to
  carefully select your application features for each target device as some
  animations and transitions may not run smoothly on lower-end devices. If those
  features are in use, scaling down the application for low-end devices is needed
  to ensure a good user experience on each device.
