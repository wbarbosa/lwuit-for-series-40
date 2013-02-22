LWUIT List Demo v1.0
====================

This application shows how to make different types of lists using LWUIT for Series 40.

- The implicit list can be used for drill downs. The list closes after an item
  is selected.
- The implicit + action example shows how to use Commands with lists.
- The exclusive list example demonstrates how to make an exclusive selection.
  The existing selection is displayed as a highlighted radio button and new
  selection can be made by tapping the list.
- The exclusive + confirm list is a list where the selection can be changed
  until it is explicitly confirmed.
- The multiple list can be used for selecting multiple items.
- Truncated shows an implicit list where the list items are truncated to fit
  in one row.
- Wrapped shows an implicit list where the texts are wrapped to multiple lines.
- Thumbnails shows an implicit list with thumbnail icons for each item. 

The application is hosted in Nokia Developer Projects:
http://projects.developer.nokia.com/LWUIT_ListDemo

For more information on the implementation, visit the wiki page: 
http://projects.developer.nokia.com/LWUIT_ListDemo/wiki


1. Usage
-------------------------------------------------------------------------------

Select the type of list you want to see from the main menu. Press Back on the
example to return to main menu. Press Back on the main menu to exit the
application.

The exclusive + confirm and multiple list examples need explicit confirmation
after making changes to the list. You can confirm the changes by pressing Done
(default command on touch devices, on non-touch devices the command is
available in the options menu). If you press Back after making changes a
confirmation Dialog is shown. The changes are accepted if you select Yes, if
you select No the changes are abandoned.


2. Prerequisites
-------------------------------------------------------------------------------

Java ME basics
LWUIT basics


3. Important classes
-------------------------------------------------------------------------------

DemoMidlet.java - The Main MIDlet class
ListView.java - The base class for all list examples.
ImplicitListView.java - Implicit list
ImplicitActionListView.java - Implicit list + action
ExclusiveListView.java - Exclusive list
ExclusiveConfirmListView.java - Exclusive list + confirm
MultipleListView.java - Multiple list
TruncatedListView.java - Truncated list
WrappedListView.java - Wrapped list
ThumbnailsListView.java - Thumbnails list


3.1 Design considerations
-------------------------

The list examples are in the com.nokia.example.listdemo.lists package. All
examples extend the abstract ListView class, which provides the common
functionality such as displaying the selected items in a dialog after exiting
the example, and showing a confirmation query when example is exited.

ExclusiveConfirmListView shows how to use a custom ListCellRenderer, the
RadioButtonListCellRenderer is the renderer implementation.

WrappedListView shows how to use GenericListCellRenderer. The renderer is used
to simply delegate the drawing to a component.


4. Compatibility
-------------------------------------------------------------------------------

All Series 40 and Symbian platforms with CLDC 1.1, MIDP 2.0. Max heap memory of
the device needs to be at least 2 MB.

Tested on:
- Nokia Asha 303, 305, 311
- Nokia X3

Developed with:
Netbeans 7.1
Nokia SDK 2.0


4.1 Required capabilities
-------------------------

CLDC 1.1, MIDP 2.0


4.2 Known issues
----------------

- Sometimes a white screen is displayed after application startup.
- Occasionally the list component selects the next index when tapping a list
  item.



5. Building, installing, and running the application
-------------------------------------------------------------------------------


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the LWUIT_ListDemo project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation) 
or  
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

4. Compile the project and run it on the Series 40 Emulator.

Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the LWUIT_ListDemo project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation)
or
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

5. Make sure LWUIT_ListDemo is set as the main project. Then right-click on the project and select Clean & Build. Then run in the Series 40 emulator.


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

v1.0    Updated with Series 40 list styles. Released in Nokia Developer Projects and in the            Nokia SDK
 2.0 for Java (online installer update February 2013).
v0.1    First release in Nokia Developer projects only.
