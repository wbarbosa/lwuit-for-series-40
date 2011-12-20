Pick one of the library files in this directory to link to your application based on your requirements.
We suggest you also point your IDE at the sources/docs for the appropriate platform to provide
the best possible code completion and debugging integration.

LWUIT_MIDP.jar - Standard MIDP/J2ME binary (does NOT work with blackberry devices, works with standard MIDP 2.x/CLDC 1.1 devices)
MIDP_IO.jar - LWUIT4IO's MIDP version, allows IO operations such as networking, storage & filesystem
LWUIT_Desktop.jar - Works with Java SE applications, very useful for debugging/developing/demoing without a mobile environment
SE_IO.jar - LWUIT4IO's Java SE version, allows IO operations such as networking, storage & filesystem
NOKIA_IO.jar - same as the MIDP_IO.jar with a couple of Nokia specific patches for APN support

LWUIT_Blackberry_Legacy_Pre4_7.jar - Blackberry specific version for older devices from OS 4.2 to blackberry OS 4.7
LWUIT_Blackberry_4_7_OrNewer.jar - Blackberry version for the current generation of blackberry devices, supports touch devices such as Storm, Torch etc.
LWUIT_Blackberry_4_7_OrNewerSigned.jar - Same as the version above, however requires code signing certificate from RIM to work properly
RIM_IO.jar - RIM specific version of LWUIT4IO, notice that in most cases the MIDP version will work just fine. The RIM version includes a couple of RIM specific enhancements for APN

LWUIT_CDC_PBP_UI.jar - version of LWUIT compiled to run on top of the CDC PBP (Personal Basis Profile)