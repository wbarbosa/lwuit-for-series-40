===================================================================================
Release Notes               LWUIT for Series 40                       June 29, 2012
===================================================================================
           
Description of the Library:
====================
LWUIT for Series 40 provides a Series 40 optimised port of the famous LightWeight
UI Toolkit that is licensed under GPLv2 with ClassPath exception licence.

The LWUIT project is organized into two main categories UI & IO.
Within these two categories we have portable and platform specific code, all the portable
code is located within the UI & IO projects respectively. While these projects will compile
easily they will be useless for any purpose since they don't include the binding
glue for the platform, to use the platform one needs to use the appropriate projects
underneath the specific ports directory to a given platform.
When including a JAR/project into your project you only need the ports jar/project
since it already includes the main UI/IO project respectively. E.g. if I want to
develop a MIDP application I will include the projects under Ports/MIDP in my build.
If I want to develop a RIM application I will use the Ports/RIM projects.

The following ports are currently supported although some are currently incomplete:

MIDP - this is the standard LWUIT code for MIDP using canvas, game canvas etc.

Nokia - Adds some nokia specific extensions, this isn't essential at all. Mostly
useful for the IO package where there is support for S60 APN functionality.

RIM - Ports to the blackberry API

SE - Ports to Java SE (desktop Java)

tools - Contains tools for working with LWUIT specifically the resource editor and the
ant task source code


Resource Editor:

The resource editor source code is a netbeans project that relies on a specific Swing
version of LWUIT to embed LWUIT code into the application. Due to issues with Netbeans
classpaths in newer versions of Netbeans I only use Netbeans 6.5 to build this code.

The code uses the Matisse GUI builder extensively for most of the UI, it was originally
based on the application framework API but that dependency was removed early on.


What's New
===========

The first release for the SDK.


Features supported by the library:
===================================

• Themes for Series 40 full touch, touch and type, and non-touch
• Changes to UI components, softkeys and menus to adapt to Series 40 behaviour
• Support for Nokia TextEditor, and Gesture FW added
• Resource Editor modified to improve the development process
• New and updated example apps for Series 40


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

Integration instructions to developers:
========================================

• To include LWUIT for Series 40 libraries and documentation to Eclipse IDE
  - Open Eclipse IDE and open your project.
  - Select Project -> Properties -> Java Build Path -> Libraries.
  - Click 'Add External JARs'.
  - Select the LWUIT library for example <LWUIT_directory>/bin/sdk2.0/S40-with-themes.jar.
    and click 'Open' and then 'OK'.
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
  - Select the LWUIT library for example <LWUIT_directory>/bin/sdk2.0/S40-with-themes.jar.
    and click 'Add JAR/Folder'.
  - Select the Javadoc tab and click 'Add ZIP/Folder'.
  - Select the LWUIT library from <LWUIT_directory>/doc/LWUIT-UI/doc and click 'OK'.
  - Right click your project and select Properties.
  - Select Build -> Libraries & Resources and click 'Add Library...'.
  - Select the library you created and click 'Add Library' and then 'OK'.

Known Problems and Limitations:
===============================

- LWUIT for Series 40 still has not been tested thoroughly and a set of bugs have already
  been reported for this release concerning for example Tab, Dialog and List layouts.

- LWUIT Browser is still under active development

- Nokia-UI-Enhancement "CanvasHasBackground" can't be used

- This manifest attribute makes the canvas background transparent so that the system
  background can be seen. However, it   doesn't work with full-screen canvases, and
  LWUIT's canvas is always full screen.

- In full touch devices, the background colour is read at app startup.

- The slider component is practically unusable at the moment.

- On non-touch devices, when the UI has a spinner component, you can't move focus away
  from the spinner.

- The LWUIT library is added to every LWUIT application. The application needs to be
  obfuscated to keep the binary size moderate. The LWUIT UI framework takes 700-800 KB
  of space but with obfuscation it goes down to 350KB or even less, depending on the
  complexity of the application.

- LWUIT is not optimised for applications that require very high-speed graphics
  (full-blown games). LCDUI's Canvas and GameCanvas are better choices there.

- The Series 40 device portfolio ranges from very price-competitive entry devices with
  a 128*160 pixel display and keyboard input to capable touch UI phones with a QVGA
  display and many times more CPU and RAM. This means that you need to carefully select
  your application features for each target device as some animations and transitions may
  not run smoothly on lower-end devices. If those features are in use, scaling down the
  application for low-end devices is needed to ensure a good user experience on each device.
