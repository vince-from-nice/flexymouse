#!/bin/bash

# Build the JAR with the right structure
jar cvf FlexyMouseListener.jar  -C ../FlexyMouseListener/bin . 

# Sign the JAR (mandatory for setting all permissions in the JNLP)
jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/FlexyMouseListener.jar  FlexyMouseListener.jar FlexyMouseKey




# Create keystore
#keytool -genkey -keystore FlexyMouseKeyStore -alias FlexyMouseKey


# Sign the Java3D JARs :

# J3D JARs
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/j3dcore.jar ../FlexyMouseListener/lib/java3d-1.5.2/j3dcore.jar FlexyMouseKey
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/j3dutils.jar ../FlexyMouseListener/lib/java3d-1.5.2/j3dutils.jar FlexyMouseKey
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/vecmath.jar ../FlexyMouseListener/lib/java3d-1.5.2/vecmath.jar FlexyMouseKey

# Native libs for Windows 32 bits
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/windows-32bits/j3dcore-d3d_dll.jar ../FlexyMouseListener/lib/java3d-1.5.2/windows-32bits/j3dcore-d3d_dll.jar FlexyMouseKey
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/windows-32bits/j3dcore-ogl_dll.jar ../FlexyMouseListener/lib/java3d-1.5.2/windows-32bits/j3dcore-ogl_dll.jar FlexyMouseKey
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/windows-32bits/j3dcore-ogl-chk_dll.jar ../FlexyMouseListener/lib/java3d-1.5.2/windows-32bits/j3dcore-ogl-chk_dll.jar FlexyMouseKey

# Native libs for Windows 64 bits
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/windows-64bits/j3dcore-ogl_dll.jar ../FlexyMouseListener/lib/java3d-1.5.2/windows-64bits/j3dcore-ogl_dll.jar FlexyMouseKey

# Native libs for Linux 32 bits
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/linux-32bits/lib_j3dcore-ogl_so.jar ../FlexyMouseListener/lib/java3d-1.5.2/linux-32bits/lib_j3dcore-ogl_so.jar FlexyMouseKey

# Native libs for Linux 64 bits
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/linux-64bits/lib_j3dcore-ogl_so.jar ../FlexyMouseListener/lib/java3d-1.5.2/linux-64bits/lib_j3dcore-ogl_so.jar FlexyMouseKey

# Native libs for OSX (from JOGL)
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/osx-universal/jogl.jar ../FlexyMouseListener/lib/java3d-1.5.2/osx-universal/jogl.jar FlexyMouseKey
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/osx-universal/jogl-natives-macosx-universal.jar ../FlexyMouseListener/lib/java3d-1.5.2/osx-universal/jogl-natives-macosx-universal.jar FlexyMouseKey
#jarsigner -keystore FlexyMouseKeyStore -signedjar WebContent/java3d-1.5.2/osx-universal/gluegen-rt-natives-macosx-universal.jar ../FlexyMouseListener/lib/java3d-1.5.2/osx-universal/gluegen-rt-natives-macosx-universal.jar FlexyMouseKey

