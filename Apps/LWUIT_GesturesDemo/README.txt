LWUIT Gestures Demo
===================

LWUIT Gestures Demo shows how to use different
types of gestures in LWUIT for Series 40 applications. Additionally it demonstrates
transitions, drag & drop and image scaling.

The application is hosted in Nokia Developer Projects:
http://projects.developer.nokia.com/LWUIT_GesturesDemo

For more information on the implementation, visit the wiki page: 
http://projects.developer.nokia.com/LWUIT_GesturesDemo/wiki


1. Usage
-------------------------------------------------------------------------------

LWUIT Gesture Demo consists of two different views: grid view for browsing and
arranging images, and image view for viewing, zooming and panning images.

Grid view:

- Tap an image to view it in image viewer.
- Long tap the screen to enter edit mode where images can be repositioned by
dragging.

Image view:

- Pinch the image to zoom in/out.
- Drag the zoomed image to pan it horizontally or vertically.
- Double-tapping the image zooms to minimum or maximum level depending on the
current zoom level.
- Flick gesture switches to next or previous image depending on the flick
direction.


2. Prerequisites
-------------------------------------------------------------------------------

Java ME basics
LWUIT basics


3. Important classes
-------------------------------------------------------------------------------

GesturesDemo:  The midlet class that initialises the display and starts the
application.
ImageGrid: Displays a grid of images.
ImageView: Displays an image.


3.1 Design considerations
-------------------------

The application starts in GesturesDemo class where the images are loaded from
resources and ImageGrid view gets created and shown.

ImageGrid extends Form and displays a grid of ImageCell components. Each
ImageCell displays an image scaled to the cell size. ImageGrid has it's own
layout manager, ImageGridLayout, that dynamically positions and resizes the
components to a grid layout with defined amount of columns. 

ImageGrid has a GestureHandler that listens for tap and long press gestures.
On long press the ImageGrid switches to edit mode where the components can be
rearranged by dragging. This is implemented by using LWUIT drag and drop. If
edit mode is not active, tapping an image shows it in ImageView.

ImageView displays the image scaled to screen size and adjusted to zoom and
pan levels. ImageView has a GestureHandler for listening to drag, pinch, tap
and flick gestures for panning, zooming and flicking the images. When flicking
from an image to another a transition animation is shown. The animation is
implemented by using LWUIT transitions.


4. Compatibility
-------------------------------------------------------------------------------

LWUIT Gesture Demo is compatible with Series 40 full touch UI devices. Max heap
memory of the device needs to be at least 2 MB.

Tested on:
- Nokia Asha 306, 311

Developed with:
Netbeans 7.1
Nokia SDK 2.0


4.1 Required capabilities
-------------------------

CLDC 1.1, MIDP 2.0


4.2 Known issues
----------------

- Fast scrolling or zooming may cause an occasional OutOfMemoryException.
- Color and layout issues in touch and type devices.


5. Building, installing, and running the application
-------------------------------------------------------------------------------


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the LWUIT_GesturesDemo project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation) 
or  
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

4. Compile the project and run it on the Series 40 Emulator.

Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the LWUIT_GesturesDemo project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation)
or
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

2. Make sure LWUIT_GesturesDemo is set as the main project. Then right-click on the project and select Clean & Build. Then run in the Series 40 emulator.


Deploying to Series 40 device:
=============================

You can install the application on a phone by transferring the JAR file
via Nokia Suite or over Bluetooth.

Troubleshooting:
================
LWUIT library jar and any other needed LWUIT jars (for example, IO-MIDP.jar in apps that use IO operations) should always be included to the application binary. They are included by default in the example applications. In case of errors, make sure that the LWUIT packages are checked in:
Properties > Java build path Order and Export (in Nokia IDE)
or
Properties > Build > Libraries & Resources > Package column needs to be checked (in NetBeans)

If you change the SDK target, some of the optional API packages may be cleared (for example, Nokia UI API as it has different versions in different SDKs). In this case, go to project properties > Platform to check the needed APIs.

6. Version history

-------------------------------------------------------------------------------



1.0 Initial release in Nokia Developer projects and in the Nokia SDK
 2.0 for Java (online installer update February 2013).