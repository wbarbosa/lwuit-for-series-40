This example is a simple app that you can use to search local
business places by search keyword and address. You can also change 
the application theme from the menu.


Build instructions:


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the Makeover project in ...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples and then click Finish. The project is loaded into your Workspace.
4. Right-click on the Makeover project, select Properties > Java build path > Libraries tab, and click on Add External JARs to add ...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\bin\sdk2.0\s40-with-themes.jar and ...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\bin\IO\IO-MIDP.jar.
5. In the Order and export tab, ensure that S40-with-themes.jar, IO-MIDP.jar and Makeover  are selected and click OK.
6. Compile the Makeover project and run it on the Series 40 Emulator.

Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the Makeover project in ...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples
2. Right-click on the project and open the project's Properties.
3. In the Build | Libraries & Resources category, make sure you have the S40-with-themes.jar and ...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\bin\IO\IO-MIDP.jar.added and the "Package" checkboxes for them are checked.
4. In the Platform category, select the SDK and make sure you have checked the following Optional Packages: 
-File Connection and PIM Optional Packages 1.0
-Mobile 3D Graphics Optional Package 1.1 
-Mobile Media API 1.0 
-Nokia User Interface 1.6 
-Nokia touch UI - Gesture APIs 1.6 (when using Nokia SDK 1.1 for Java ME)
-Scalable 2D Vector Graphics 
5. Make sure Makeover is set as the main project. Then right-click on the project and select Clean & Build.

