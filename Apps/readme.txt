LWUIT for Series 40 example applications
========================================

This folder contains five example applications:
LWUITBrowser, LWUITDemo, Makeover, and Tipster are modified from the LWUIT 1.5 package. The fifth, new application is ContactBookDemo.

The project structure of the original LWUIT example applications LWUITBrowser, LWUITDemo, Makeover, and Tipster has been simplified to make building process easier. These examples applications are meant for the MIDP target only.

The projects are NetBeans projects. Thus when importing them to Nokia IDE for Java, LWUIT library needs to be added to the projects. In ContactBookDemo, Makeover, and Tipster projects  also the IO-MIDP library needs to be added. In LWUITDemo you also need to remove Editor.jar from the package (it is there only because it is used as an example UI in Resource Editor).

In NetBeans the libraries are added automatically.

For more information, see readme text files in each example project.