Demonstrates different LWUIT UI components and features.

This is one of the original LWUIT example applications. It has been modified to Series 40.

Build instructions:


Building in Nokia IDE for Java ME (Eclipse):
============================================
1. In Nokia IDE for Java ME (Eclipse), from the File menu, select Import.
2. In the Import dialog, select Java ME > NetBeans Project and click Next.
3. In the Import NetBeans Projects dialog, click Browse to select the LWUITDemo project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation) 
or  
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

Then click Finish. The project is loaded into your Workspace.

4. Compile the LWUITDemo project and run it on the Series 40 Emulator.


Building in NetBeans IDE:
=========================
1. In NetBeans IDE, open the LWUITDemo project in:

...\Nokia\Devices\Nokia_SDK_2_0_Java\plugins\lwuit\examples (in the SDK installation)
or
<LWUIT for Series 40 root>\examples (in the LWUIT binary downloaded separately)

2. Make sure LWUITDemo is set as the main project. Then right-click on the project and select Clean & Build. Then run in the Series 40 emulator.


Troubleshooting:
================
LWUIT library jar and any other needed LWUIT jars (for example, IO-MIDP.jar in apps that use  IO operations) should always be included to the application binary. They are included by  default in the example applications. In case of errors, make sure that the LWUIT packages are  checked in:
Properties > Java build path Order and Export (in Nokia IDE)
or
Properties > Build > Libraries & Resources > Package column needs to be checked (in NetBeans)

If you change the SDK target, some of the optional API packages may be cleared (for example,  Nokia UI API as it has different versions in different SDKs). In this case, go to project  properties > Platform to check the needed APIs.