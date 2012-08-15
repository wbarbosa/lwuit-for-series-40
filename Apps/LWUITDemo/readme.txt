Demonstrates different LWUIT UI components and features.

Build instructions:


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the LWUITDemo project in ...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples and then click Finish. The project is loaded into your Workspace.
4. Right-click on the LWUITBrowser project, select Properties > Java build path > Source tab and remove all projects except LWUITDemo/src.
5. In the Libraries tab, clicking on Add External JARs to add ...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\bin\sdk2.0\s40-with-themes.jar. Also remove editor.jar.
6. In the Order and export tab, ensure that S40-with-themes.jar and LWUITDemo are selected and click OK.
7. Compile the LWUITDemo project and run it on the Series 40 Emulator.


Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the LWUITDemo project in ...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples
2) Right-click on the project and open the project's Properties.
3) In the Build | Libraries & Resources category, make sure you have the S40-with-themes.jar added and the "Package" checkbox is checked.
4) In the Platform category, select the SDK and make sure you have checked the following Optional Packages: 
-Nokia User Interface 1.6 
-Nokia touch UI - Gesture APIs 1.6 
-Scalable 2D Vector Graphics 
-Mobile Media API 1.0 
-Mobile 3D Graphics Optional Package 1.1 
5) Make sure LWUITDemo is set as the main project. Then right-click on the project and select Clean & Build.


