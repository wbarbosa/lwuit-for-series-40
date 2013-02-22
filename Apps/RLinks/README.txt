LWUIT RLinks
============

This Java ME example application demonstrates how to build a network-accessing 
reader app using LWUIT for Series 40. 

The application connects to Reddit, a social news service where registered 
users submit content, typically in the form of a link. Other users then rate 
the submissions, which affects the ranking and position of the posts on the 
site's pages and front page.

This application demonstrates:
- Custom theming with LWUIT using ResourceEditor.
- Building custom lists containing items with various heights.
- Optimising scrolling performance with simple buffering.
- LWUIT lead components.
- Handling network connections and updating the UI from another thread.
- Retrieving and storing data using a JSON REST API.

Network layer was directly adopted from the LCDUI version of RLinks, so 
LWUIT's networking abilities are not used.


This example application is hosted in Nokia Developer Projects:
- http://projects.developer.nokia.com/LWUIT_RLinks

The original Java ME port is hosted in Nokia Developer Projects:
- http://projects.developer.nokia.com/rlinks


1. Usage
-------------------------------------------------------------------------------

The main MIDlet class is a central point for handling the view logic (using
a variety of Listener classes). The application requires network connectivity
in order to be useful. Commenting and voting requires logging in with a Reddit
user account.


2. Prerequisites
-------------------------------------------------------------------------------

- Java ME basics
- Java ME threads
- Java ME networking


3. Project structure and implementation
-------------------------------------------------------------------------------

3.1 Folders
-----------

 |                  The root folder contains the project file, resource files,
 |                  the license information, and this file (release notes).
 |
 |- nbproject       Contains NetBeans project files.
 |
 |- res             Contains application images and resource files.
 |
 |- src             Contains the Java source code files.


3.2 Important files and classes
-------------------------------

| File                           | Description                                |
|--------------------------------|--------------------------------------------|
| \Main.java                     | Control point for the main UI flow,        |
|                                | controlling changes between views using    |
|                                | listeners.                                 |
|--------------------------------|--------------------------------------------|
| ...\view\item\ListItem.java    | Base class for list items with variable    |
|                                | height. Uses Button as a lead component.   |
|--------------------------------|--------------------------------------------|
| ...\view\LinksView.java        | The main view of the application. This     |
|                                | class loads links from Reddit and then     |
|                                | displays them in the view. Additionally    |
|                                | link images are being retrieved            |
|                                | asynchronously on the background.          |
|--------------------------------|--------------------------------------------|
| ...\network\HttpClient.java    | Base class for networking activities.      |
|                                | Handles network threads and cookies.       |
|--------------------------------|--------------------------------------------|
| ...\network\HttpOperation.java | Base class used by all network operations. |
|                                | Specific methods may be overridden to      |
|                                | achieve the desired functionality.         |
|--------------------------------|--------------------------------------------|


3.3 Used APIs
-------------

LWUIT for Series 40
Java ME Web Services 1.0


4. Compatibility
-------------------------------------------------------------------------------

Series 40 6th Edition FP1 devices and newer

Tested on:
Nokia Asha 201 (Java Runtime 1.1.0 for Series 40)
Nokia Asha 306 (Java Runtime 2.0.0 for Series 40)
Nokia Asha 311 (Java Runtime 2.0.0 for Series 40)
Nokia X3-02 (Series 40 6th Edition FP1)

Developed with:
NetBeans 6.9.1
Nokia SDK 2.0 for Java

4.1 Known issues
----------------

Non-implemented application features:
- The application does not support account creation, so a user needs to create 
  her Reddit account at www.reddit.com in order to use the features that require 
  authentication (voting and commenting).
  
- HTTP link URLs contained in comments are not accessible.

- As video playback is not possible, the popular "videos" subreddit
  is excluded from the default list of categories.

Non-touch issues:  
- The application loads a lot of data and thus it cannot be run on older 
  non-touch devices with limited heap size and constrained image handling 
  capabilities. The application is expected to work on all full touch and touch 
  and type devices, and on non-touch devices based on Series 40 6th Edition FP1 
  or newer release.
  
- Non-touch devices are not well-suited for browsing lengthy lists. There are 
  also some LWUIT issues, such as losing focus occasionally while updating UI.
  
- Scrolling lengthy comments (hundreds of characters) is bound to slow down the
  framerate.

Other issues:
- TextEditor does not show text input indication on touch and type and 
  non-touch devices.

- Tickering does not work correctly in the landscape orientation in the full 
  touch devices.

- Reddit does not guarantee the continuous support for the API. A quote from 
  the API documentation: "Changes to this API can happen without warning. (...) 
  The JSON replies can also change without warning."


  

5. Building, installing, and running the application
-------------------------------------------------------------------------------


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the RLinks project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation) 
or  
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

4. Compile the project and run it on the Series 40 Emulator.

Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the RLinks project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation)
or
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

2. Make sure RLinks is set as the main project. Then right-click on the project and select Clean & Build. Then run in the Series 40 emulator.


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

1.0 The first version published in Npkia Developer Projects, at Nokia Developer main site and     and in the Nokia SDK
 2.0 for Java (online installer update February 2013).