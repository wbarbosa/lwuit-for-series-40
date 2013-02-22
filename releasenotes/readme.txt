===================================================================================
Release Notes               LWUIT for Series 40                       February 25, 2013
===================================================================================
           
Description of the Library:
====================
LWUIT for Series 40 provides a Series 40 optimised port of the famous LightWeight
UI Toolkit, which is licensed under GPLv2 with ClassPath exception licence. It has
been optimised for Series 40 with a number of styling, theming, and functional
changes. For example, the Nokia TextEditor and Series 40 Gesture framework have
been taken into use. LWUIT for Series 40 has been developed and tested with
Nokia SDK 1.1 and 2.0 for Java and it can be used for applications targeted at
Series 40 6th Edition and newer devices with both keypad and touch user interface.

For newer updates and source code of the library, check the LWUIT for Series 40 project in Nokia Developer Projects:
- http://projects.developer.nokia.com/LWUIT_for_Series_40



What's New
===========

- The productised, second release for the Nokia 2.0 SDK.
- More example applications added to the package.
- LWUIT Developer's Library added including both technical and UX parts.
- Loads of feature, functional, performance, styling, and tooling changes. For the list of all changes made after the previous SDK release, see the changelog.txt file.


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
For example, if you write a LWUIT application for SDK 2.0 and it is expected to be run on 1.1 devices, use sdk2.0\S40-touch-and-type.jar. Applications using LWUIT jars under specific release folder are expected to compile and run on the same release but in earlier versions they only run but do not compile.

For networking and storage & file system IO operations, use IO-MIDP.jar with the Series 40 (the IO-SE.jar is Java SE version). 


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
Nokia Asha 309 (Java Runtime 2.0.0 for Series 40)
Nokia Asha 308 (Java Runtime 2.0.0 for Series 40)
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


Developed with:
Netbeans 7.1 & Nokia IDE for Java
Nokia SDK 1.1 for Java
Nokia SDK 2.0 for Java

Integration instructions to developers:
========================================

• To include LWUIT for Series 40 libraries and documentation to Nokia IDE
  - Open Nokia IDE and open your project.
  - Select Project -> Properties -> Java Build Path -> Libraries.
  - Click 'Add External JARs'.
  - Select the LWUIT library for example <LWUIT_directory>/bin/sdk2.0
    /S40-with-themes.jar. and click 'Open' and then 'OK'.
  - In Package Explorer, select the Referenced Libraries and right click on the
    S40-with-themes JAR and select Properties.
  - Select Javadoc Location and click 'Browse'.
  - Select the LWUIT library from <LWUIT_directory>/doc/LWUIT-UI/ and click 'OK'.
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

To run example applications, go to \examples directory and follow instructions of the README.txt file of each project.

Known Problems and Limitations:
===============================

- Sometimes the 2.0 SDK emulator screen is not properly refreshed in the application start-up. If this occurs, a workaround is to press the call termination key of the emulator and select NO, to force a screen refresh and see the UI.

- Overriding the Nokia theme of certain components programmatically using the getStyle method does not work. This is because, for example in Button, the color of the component is done with image borders. In these cases it is recommended to make changes to the theme resource file.

- Avoid using very complex UI scenarios with nested UI components and containers. They will generally result in poor performance and in 2.0 SDK, the application may not launch in these cases.

- Nokia-UI-Enhancement "CanvasHasBackground" can't be used on touch and type and non-touch phones. This manifest attribute makes the canvas background transparent so that the system background can be seen. However, it doesn't work with full-screen canvases, and LWUIT's canvas is always full screen in touch and type and non-touch phones. In full touch devices, the background colour is read at app startup.

- Non-touch limitations: On non-touch devices, when the UI has a spinner component, you can't move focus away from the spinner. Also you should not use Tabs in non-touch phones, or at least you should not use any component that needs horisontal scrolling in Tabs.

- Slider and Tree components and links in HTMLComponent lack highlight on non-touch phones.

- The LWUIT library is added to every LWUIT application. The application needs to
  be obfuscated to keep the binary size moderate. The LWUIT UI framework takes
  700-800 KB of space but with obfuscation it goes down to 350KB or even less,
  depending on the complexity of the application.

- LWUIT is not optimised for applications that require very high-speed graphics
  (full-blown games). LCDUI's Canvas and GameCanvas are better choices there.

- Avoid using 3D transitions. The other transitions like Slide and Fade work.

- The Series 40 device portfolio ranges from very price-competitive entry devices with a 128*160 pixel display and keyboard input to capable touch UI phones with a QVGA display and many times more CPU and RAM. This means that you need to carefully select your application features for each target device as some animations and transitions may not run smoothly on lower-end devices. If those features are in use, scaling down the application for low-end devices is needed to ensure a good user experience on each device.
