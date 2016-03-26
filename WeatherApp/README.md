**Running Gradle from the Command line**

> WeatherApp$ ls

```
app  build  build.gradle  gradle  gradle.properties  gradlew  gradlew.bat  local.properties  README.md  settings.gradle  WeatherApp.iml
```

> WeatherApp$ ./gradlew tasks

```
Downloading https://services.gradle.org/distributions/gradle-2.4-all.zip
.......................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................
Unzipping /home/droid/.gradle/wrapper/dists/gradle-2.4-all/3i2gobhdl0fm2tosnn15g540i0/gradle-2.4-all.zip to /home/droid/.gradle/wrapper/dists/gradle-2.4-all/3i2gobhdl0fm2tosnn15g540i0
Set executable permissions for: /home/droid/.gradle/wrapper/dists/gradle-2.4-all/3i2gobhdl0fm2tosnn15g540i0/gradle-2.4/bin/gradle
:tasks

------------------------------------------------------------
All tasks runnable from root project
------------------------------------------------------------

Android tasks
-------------
androidDependencies - Displays the Android dependencies of the project.
signingReport - Displays the signing info for each variant.
sourceSets - Prints out all the source sets defined in this project.

Build tasks
-----------
assemble - Assembles all variants of all applications and secondary packages.
assembleAndroidTest - Assembles all the Test applications.
assembleDebug - Assembles all Debug builds.
assembleRelease - Assembles all Release builds.
build - Assembles and tests this project.
buildDependents - Assembles and tests this project and all projects that depend on it.
buildNeeded - Assembles and tests this project and all projects it depends on.
compileDebugAndroidTestSources
compileDebugSources
compileDebugUnitTestSources
compileReleaseSources
compileReleaseUnitTestSources
mockableAndroidJar - Creates a version of android.jar that's suitable for unit tests.

Build Setup tasks
-----------------
init - Initializes a new Gradle build. [incubating]
wrapper - Generates Gradle wrapper files. [incubating]

Help tasks
----------
components - Displays the components produced by root project 'WeatherApp'. [incubating]
dependencies - Displays all dependencies declared in root project 'WeatherApp'.
dependencyInsight - Displays the insight into a specific dependency in root project 'WeatherApp'.
help - Displays a help message.
model - Displays the configuration model of root project 'WeatherApp'. [incubating]
projects - Displays the sub-projects of root project 'WeatherApp'.
properties - Displays the properties of root project 'WeatherApp'.
tasks - Displays the tasks runnable from root project 'WeatherApp' (some of the displayed tasks may belong to subprojects).

Install tasks
-------------
installDebug - Installs the Debug build.
installDebugAndroidTest - Installs the android (on device) tests for the Debug build.
uninstallAll - Uninstall all applications.
uninstallDebug - Uninstalls the Debug build.
uninstallDebugAndroidTest - Uninstalls the android (on device) tests for the Debug build.
uninstallRelease - Uninstalls the Release build.

Verification tasks
------------------
check - Runs all checks.
clean - Deletes the build directory.
connectedAndroidTest - Installs and runs instrumentation tests for all flavors on connected devices.
connectedCheck - Runs all device checks on currently connected devices.
connectedDebugAndroidTest - Installs and runs the tests for debug on connected devices.
deviceAndroidTest - Installs and runs instrumentation tests using all Device Providers.
deviceCheck - Runs all device checks using Device Providers and Test Servers.
lint - Runs lint on all variants.
lintDebug - Runs lint on the Debug build.
lintRelease - Runs lint on the Release build.
test - Run unit tests for all variants.
testDebugUnitTest - Run unit tests for the debug build.
testReleaseUnitTest - Run unit tests for the release build.

Other tasks
-----------
clean
jarDebugClasses
jarReleaseClasses
lintVitalRelease - Runs lint on just the fatal issues in the Release build.

To see all tasks and more detail, run gradlew tasks --all

To see more detail about a task, run gradlew help --task <task>

BUILD SUCCESSFUL

Total time: 1 mins 43.7 secs

This build could be faster, please consider using the Gradle Daemon: http://gradle.org/docs/2.4/userguide/gradle_daemon.html
```

### Testing on a physical device

List out the attached devices 

> WeatherApp$ adb devices

```
List of devices attached
emulator-5554	device
```
**Enable USB Debugging on the device**

Connect an Android device to the laptop

Go to Settings -> About Phone and tap on Build Number option for 7 times to "unhide" Setting -> Developer Options menu.

In Setting -> Developer Options, Turn on USB Debugging

**Setup computer to detect device**

Not sure if following any of these helped, but I tried them before I was able to install the app on the device.

*Created the following file*

/etc/udev/rules.d/51-android.rules

with the following content

SUBSYSTEM=="usb", ATTR{idVendor}=="18d1", MODE="0666", GROUP="plugdev"

> sudo service udev restart

> lsusb

```
Bus 002 Device 002: ID 8087:0024 Intel Corp. Integrated Rate Matching Hub
Bus 002 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub
Bus 001 Device 005: ID 0c45:64ad Microdia 
Bus 001 Device 004: ID 0bda:0129 Realtek Semiconductor Corp. RTS5129 Card Reader Controller
Bus 001 Device 003: ID 8087:07da Intel Corp. 
Bus 001 Device 002: ID 8087:0024 Intel Corp. Integrated Rate Matching Hub
Bus 001 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub
Bus 004 Device 001: ID 1d6b:0003 Linux Foundation 3.0 root hub
Bus 003 Device 002: ID 046d:c52f Logitech, Inc. Unifying Receiver
Bus 003 Device 037: ID 18d1:4ee2 Google Inc. Nexus 4 (debug)
Bus 003 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub
```

*References*

http://developer.android.com/tools/device.html

http://bernaerts.dyndns.org/linux/74-ubuntu/328-ubuntu-trusty-android-adb-fastboot-qtadb

**Verify if the device is detected**

> WeatherApp$ adb devices

```
List of devices attached
06f3f1240074bbbd	device   <-----
emulator-5554	device
```

**Build the app from the command line using gradle**

Grant execution permission to the gradle wrapper script, if needed.

> WeatherApp$ ./chmod +x gradlew

> WeatherApp$ ./gradlew assembleDebug

```
:app:preBuild UP-TO-DATE
:app:preDebugBuild UP-TO-DATE
:app:checkDebugManifest
:app:preReleaseBuild UP-TO-DATE
:app:prepareComAndroidSupportAppcompatV72311Library UP-TO-DATE
:app:prepareComAndroidSupportDesign2311Library UP-TO-DATE
:app:prepareComAndroidSupportRecyclerviewV72311Library UP-TO-DATE
:app:prepareComAndroidSupportSupportV42311Library UP-TO-DATE
:app:prepareDebugDependencies
:app:compileDebugAidl UP-TO-DATE
:app:compileDebugRenderscript UP-TO-DATE
:app:generateDebugBuildConfig UP-TO-DATE
:app:generateDebugAssets UP-TO-DATE
:app:mergeDebugAssets UP-TO-DATE
:app:generateDebugResValues UP-TO-DATE
:app:generateDebugResources UP-TO-DATE
:app:mergeDebugResources UP-TO-DATE
:app:processDebugManifest UP-TO-DATE
:app:processDebugResources UP-TO-DATE
:app:generateDebugSources UP-TO-DATE
:app:processDebugJavaRes UP-TO-DATE
:app:compileDebugJavaWithJavac UP-TO-DATE
:app:compileDebugNdk UP-TO-DATE
:app:compileDebugSources UP-TO-DATE
:app:preDexDebug UP-TO-DATE
:app:dexDebug UP-TO-DATE
:app:validateDebugSigning
:app:packageDebug UP-TO-DATE
:app:zipalignDebug UP-TO-DATE
:app:assembleDebug UP-TO-DATE

BUILD SUCCESSFUL

Total time: 25.355 secs

This build could be faster, please consider using the Gradle Daemon: http://gradle.org/docs/2.4/userguide/gradle_daemon.html
```

The apk file is generated at *WeatherApp/app/build/outputs/apk* directory

> WeatherApp$ ls app/build/outputs/apk

```
app-debug.apk  app-debug-unaligned.apk
```

**Install app on the physical device from the command line**

> WeatherApp$ adb -d install -r app/build/outputs/apk/app-debug-unaligned.apk

-d, a switch to adb command => directs command to the only connected USB device

-r, a switch to install subcommand => replace existing application

