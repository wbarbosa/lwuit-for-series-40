Making a release

In the root folder of LWUIT there is an ant buildfile.
This script creates proper folder structure and copies
files that are required by release to Series-40-binaries 
folder. It also recompiles all the required libraries so make 
sure that your machine can compile LWUIT and ResourceEditor
before using the release-script. 

The release-script is documented so read it before you use it.

Warning! 

You should never modify the files in the Series-40-binaries
since the folder is always deleted when build-release is run.


