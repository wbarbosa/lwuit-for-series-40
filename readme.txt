LWUIT Project Building And Hierarchy
------------------------------------

The LWUIT project is organized into two main categories UI & IO.
Within these two categories we have portable and platform specific code, all the portable
code is located within the UI & IO projects respectively. While these projects will compile
easily they will be useless for any purpose since they don't include the binding
glue for the platform, to use the platform one needs to use the appropriate projects
underneath the specific ports directory to a given platform.
When including a JAR/project into your project you only need the ports jar/project
since it already includes the main UI/IO project respectively. E.g. if I want to
develop a MIDP application I will include the projects under Ports/MIDP in my build.
If I want to develop a RIM application I will use the Ports/RIM projects.

The following ports are currently supported although some are currently incomplete:

MIDP - this is the standard LWUIT code for MIDP using canvas, game canvas etc.

Nokia - Adds some nokia specific extensions, this isn't essential at all. Mostly
useful for the IO package where there is support for S60 APN functionality.

RIM - Ports to the blackberry API

SE - Ports to Java SE (desktop Java)

tools - Contains tools for working with LWUIT specifically the resource editor and the
ant task source code



The Resource Editor
-------------------

The resource editor source code is a netbeans project that relies on a specific Swing
version of LWUIT to embed LWUIT code into the application. Due to issues with Netbeans
classpaths in newer versions of Netbeans I only use Netbeans 6.5 to build this code.

The code uses the Matisse GUI builder extensively for most of the UI, it was originally
based on the application framework API but that dependency was removed early on. 