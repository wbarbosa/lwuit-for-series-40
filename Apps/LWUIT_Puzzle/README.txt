LWUIT Slide Puzzle v1.2
=======================

Slide Puzzle is a simple LWUIT-based puzzle game for Series 40 devices. It 
demonstrates how LWUIT components can be used in a game context. Both non-
touch and touch devices are supported, with drag gestures where available. 
It also gives an idea of how to use the device camera and helper threads for 
animating the interface.

This example application demonstrates:
- LWUIT components in a game context
- Layouts
- Drag gestures on touch UI
- Keyboard events
- Embedding camera viewfinder
- Animations
- Fullscreen mode

The application is hosted in Nokia Developer Projects:
http://projects.developer.nokia.com/LWUIT_Puzzle

For more information on the implementation, visit the wiki page: 
http://projects.developer.nokia.com/LWUIT_Puzzle/wiki


1. Usage
-------------------------------------------------------------------------------

The game is a classical puzzle game where you have a 3x3 grid with 8 tiles. 
The goal of the player is to slide the tiles so that they are in the correct 
order. There are 3 puzzles with different pictures. When you start the game, 
you can continue from the state where you left the game last time. You can 
also take pictures with the camera in your phone and generate puzzles from 
them.

2. Prerequisites
-------------------------------------------------------------------------------

Java ME basics
LWUIT basics


3. Important classes
-------------------------------------------------------------------------------

PuzzleMidlet:  The midlet class that initialises the display and starts the 
application.
GameView:  The main game area and input handling.
PuzzleComponent:  LWUIT component representing the Puzzle, handling game state 
persistence.
Puzzle:  Logical controller component for the puzzle state, used by 
PuzzleComponent.
TileMover:  Helper component for moving tiles around the puzzle.
CameraComponent:  LWUIT component for controlling the camera.


3.1 Design considerations
-------------------------

At the top of the structure hierarchy there is PuzzleMidlet. It checks device 
characteristics and initialises the application but also takes care of 
operations needed when it is being closed.

PuzzleMidlet constructs a GameView (extends LWUIT Form) which functions as the 
main view in the game. In the middle of the view there is a container which 
holds the puzzle component and different menu components and shows them 
depending on the state. More details about the view is available online at 
http://projects.developer.nokia.com/LWUIT_Puzzle/wiki/structure#layout 

PuzzleComponent has a list of Puzzles instances of which one is active at a 
time. Puzzle holds the data and logic of puzzles. Every Puzzle has a two-
dimensional array of Tile instances. When a Puzzle is set active, its image 
is loaded and split to tiles.

LWUIT mostly handles the painting but there is also a thread repainting them 
(for example, when the tiles are moved).


4. Compatibility
-------------------------------------------------------------------------------

All Series 40 and Symbian platforms with CLDC 1.1, MIDP 2.0, Mobile Media API 
(JSR-135), and camera. Max heap memory of the device needs to be at least 2 MB.

Tested on:
- Nokia Asha 303, 305, 311
- Nokia X3
- Nokia 7230
- Nokia C3

Developed with:
Netbeans 7.1
Nokia SDK 2.0

4.1 Required capabilities
-------------------------

CLDC 1.1, MIDP 2.0


4.2 Known issues
----------------

- On Nokia 6700 Classic, there is a memory issue when changing current puzzle.
- On C2-03, the link in info component cannot be opened.
- Asha 203 has an issue with rapidly moving between Next and Menu selections.


5. Building, installing, and running the application
-------------------------------------------------------------------------------


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the LWUIT_Puzzle project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation) 
or  
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

4. Compile the project and run it on the Series 40 Emulator.

Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the LWUIT_Puzzle project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation)
or
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

2. Make sure Slide Puzzle is set as the main project. Then right-click on the project and select Clean & Build. Then run in the Series 40 emulator.


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

v1.2    Bug fix with menu background. Published at Nokia Developer Projects,                 developer.nokia.com, and in the Nokia SDK 2.0 for Java (online installer update         February 2013).
v1.1    Added camera feature, bug fixes. Published at  
        developer.nokia.com.
v1.0    First release (at Nokia Developer projects only).
