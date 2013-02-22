LWUIT Tourist Attractions v1.2
==============================

LWUIT Tourist Attractions is a remake of the original Tourist Attractions. It 
is developed with LWUIT for Series 40 library. The application is a location-
aware tour guide to city highlights. It presents the most notable sights 
nearby and shows detailed information on them. The application demonstrates 
the use of the Location API (JSR-179) and Nokia Maps API for Java ME and uses 
the In-App Purchase API with non-DRM-protected application content.

This example application demonstrates:
- LWUIT lists with a custom renderer
- LWUIT layouts
- Switching between different LWUIT forms
- Showing a map using Nokia Maps API with LWUIT
- Showing the user's location on the map
- In-app purchasable content
- Fetching purchased content from a back-end server

Original Tourist Attractions project page: 
http://projects.developer.nokia.com/JMETouristAttractions

LWUIT Tourist Attractions project page: 
http://projects.developer.nokia.com/LWUIT_TouristAttractions

LWUIT for Series 40: 
http://projects.developer.nokia.com/LWUIT_for_Series_40



1. Usage
-------------------------------------------------------------------------------

The main view of the application shows a list of attractions in the selected 
guide. You can view details of an attraction by tapping it. From the attractions 
list you can open the map to show your current position, or if your position 
is not available, the center of the guide. By opening the map from the details 
of an attraction, the location of the attraction is shown. From the guides 
list you can open a different guide or buy new guides.


2. Prerequisites
-------------------------------------------------------------------------------

Java ME basics
LWUIT basics


3. Important classes
-------------------------------------------------------------------------------

TouristMidlet.java:  The midlet class that initialises the display, views, 
and starts from the splash view.
AttractionList.java:  The implementation of a list of attractions.
AttractionListRenderer.java:  Custom renderer component for achieving a 
Series 40 -like list style (see Known issues).
MapView.java & MapComponent.java:  Handlers for the map.
LocationFinder.java:  Wraps Location API so that running the application is 
possible also without the API being on the device.
Network.java:  Network component that handles all connections to the internet.
Compatibility.java:  Checks compatibilities of current device to make 
feature-based programming easy.


4. Compatibility
-------------------------------------------------------------------------------

Series 40 6th Edition and newer devices which have a 240px wide or wider screen.

Tested on: Nokia Asha 311, Nokia Asha 305, Nokia Asha 303,
Nokia X3-02, Nokia C3, and Nokia 7230.

Developed with: Netbeans 7.1.2 and Nokia SDK 2.0 for Java.

4.1 Required capabilities
-------------------------

CLDC 1.1, MIDP 2.0 and, Web Services API (JSR-172).

4.2 Known issues
----------------

-The list appearance (as of LWUIT 0.6.2) does not fully match the native 
Series 40 look; hence list items are drawn with a custom CellRenderer 
implementation to match the native style.
-On C2-03 there are occasional map problems.
-On Asha 305 the list performance is not optimal.
-The text area height is sometimes set incorrectly.


5. Building, installing, and running the application
-------------------------------------------------------------------------------


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the LWUIT_TouristAttractions project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation) 
or  
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

4. Compile the project and run it on the Series 40 Emulator.

Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the LWUIT_TouristAttractions project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation)
or
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

2. Make sure LWUIT_TouristAttractions is set as the main project. Then right-click on the project and  select Clean & Build. Then run in the Series 40 emulator.


Deploying to Series 40 device:
=============================

You can install the application on a phone by transferring the JAR file
via Nokia Suite or over Bluetooth.

Troubleshooting:
================
LWUIT library jar and any other needed LWUIT jars (for example, IO-MIDP.jar in apps that use  IO operations) should always be included to the application binary. They are included by  default in the example applications. In case of errors, make sure that the LWUIT packages are  checked in:
Properties > Java build path Order and Export (in Nokia IDE)
or
Properties > Build > Libraries & Resources > Package column needs to be checked (in NetBeans)

If you change the SDK target, some of the optional API packages may be cleared (for example,  Nokia UI API as it has different versions in different SDKs). In this case, go to project  properties > Platform to check the needed APIs.


6. Version history
-------------------------------------------------------------------------------

v1.2    Pinch zooming of map and bug fixes. Published at Nokia Developer Projects,         developer.nokia.com, and in the Nokia SDK 2.0 for Java (online installer update                February 2013).
v1.1    Changed the IAP process.
v1.0    Initial release.
