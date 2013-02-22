LWUIT Birthdays
===============

LWUIT Birthdays is a MIDlet that demonstrates how to use the LWUIT Calendar 
component together with the Java ME PIM API (JSR-75) to create a simple 
Birthdays application.

This example application is hosted in Nokia Developer Projects:
- http://projects.developer.nokia.com/LWUIT_Birthdays

For more information on implementation and porting, visit the wiki pages:
- http://projects.developer.nokia.com/LWUIT_Birthdays/wiki


1. Usage
-------------------------------------------------------------------------------

The MIDlet starts in a "show upcoming birthdays" view that populates a list
of upcoming birthdays with data from the phone address book. The user can
choose to add new birthday entries by either creating a new contact, or 
assigning a birthday to an existing PIM contact, using the LWUIT Calendar
component.


2. Prerequisites
-------------------------------------------------------------------------------

Java ME basics
LWUIT basics


3. Important files
-------------------------------------------------------------------------------

BirthdayMidlet.java: MIDlet entry point, handling transition between views
BirthdayListModel.java: manages the list of birthdays and their contacts
BirthdaySorter.java: sorts a Vector of birthdays in the order of future occurrence
PIMContactHandler.java: accessing PIM APIs

4. Compatibility
-------------------------------------------------------------------------------

Series 40 phones with touch capabilities, e.g. Nokia Asha 311 or Nokia X3-02.

Developed with Netbeans 7.2 and Nokia SDK 2.0 for Java.

4.1 Required capabilities
-------------------------

CLDC 1.1, MIDP 2.0, and PIM API (JSR-75).

4.2 Known issues
----------------

- The Calendar application on Series 40 6th edition devices may sometimes show
  birthdays to appear on a different date than the one where they're created.


5. Building, installing, and running the application
-------------------------------------------------------------------------------


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the LWUIT_Birthdays project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation) 
or  
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

4. Compile the project and run it on the Series 40 Emulator.

Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the LWUIT_Birthdays project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation)
or
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

5. Make sure LWUIT_Birthdays is set as the main project. Then right-click on the project and select Clean & Build. Then run in the Series 40 emulator.


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