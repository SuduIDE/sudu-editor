This is an experimental project to write a portable (Web + Desktop) editor in java and kotlin

This demo is designed to investigate performance limits of this approach and compare with other Web editors like Monaco and VSCode

It contains of 
 - ai-demo-js: web demo of tabbed like editor that supports switching between files
 - demo-edit-js: web demo of fullscreen editor
 - demo-edit-jvm: desktop demo of editor in a desktop window
 - demo-edit-es-module: Javascript ESM module - embeddable version of the editor
 - webpack-test: Test for building a webapp with webpack

Build requirements 

1. Web versions requires to clone and build TeaVM compiler locally:

    ```
    git clone https://github.com/kirillp/teavm.git
    cd teavm 
    ./gradlew publishToMavenLocal -x test
    ```

   Note: we don't use original [compiler](https://github.com/konsoletyper/teavm.git) to pin the version and apply internal patches.

2. Clone and build TeaVM-compatible version of ANTLR:
   ```
   git clone https://github.com/pertsevpv/antlr4-teavm-compatible.git
   cd antlr4-teavm-compatible
   mvn install -DskipTests
   ```

3. now you can build demo-edit-es-module and ai-demo-js
      - `mvn package -am -pl demo-edit-es-module -P release`
      - `mvn package -am -pl ai-demo-js -P release`

4. To download fonts (for demo-edit-js and demo-edit-jvm), run the task:

   `mvn package -am -pl :codicon -pl :fonts -f pom.xml`

5. demo-edit-js includes WebAssembly demo, and requires the latest emscripten to compile it. You may skip it if you don't need demo-edit-js:

    ```
    install python from Microsoft Store
    https://apps.microsoft.com/store/detail/python-310/9PJPW5LDXLZ5
   
    git clone https://github.com/emscripten-core/emsdk.git
    cd emsdk
    ./emsdk install latest
    ```  

    ```  
    setx EMSDK D:\emsdk  
    set EMSDK D:\emsdk  
    ```

6. To build the desktop Windows app (demo-edit-jvm), you need Microsoft Visual Studio 2022.

   Do not use installer UX, there is a commandline below that installs only necessary components

   IDE: https://aka.ms/vs/17/release/vs_community.exe
    ```
    vs_community.exe --wait --p --norestart --nocache --locale en-US --add "Microsoft.VisualStudio.Workload.NativeDesktop;includeRecommended"  
    set MSBuildPath=C:\Program Files\Microsoft Visual Studio\2022\Community\Msbuild\Current\Bin\amd64
    ```
   If, for some reason, you do not need IDE to be installed, you can try BuildTools instead, but vs_community is recommended:
   BuildTools: https://aka.ms/vs/17/release/vs_BuildTools.exe  
   https://learn.microsoft.com/en-us/visualstudio/install/workload-component-id-vs-build-tools?view=vs-2022
    ```
   vs_BuildTools.exe --wait --p --norestart --nocache --locale en-US --add "Microsoft.VisualStudio.Workload.VCTools;includeRecommended"  
   set MSBuildPath=C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\MSBuild\Current\Bin
   ```

7. Edit environment to access MSBuild(for graphics-jvm) or Clang(emscripten, for demo-edit-js):
   ```
   rundll32 sysdm.cpl,EditEnvironmentVariables
   ```
   - add the following components to the user `%path%`  
     - `%MSBuildPath%` to run MSBuild for demo-edit-jvm
     - `%EMSDK%\upstream\bin` to run Clang for demo-edit-js
   - check that compilers work, and review Environment Variables.
    You need a new console instance to apply env changes, then try
     ```
     MSBuild --help
     clang --help
     ```

8. Desktop version (demo-edit-jvm) requires libGLESv2.dll - Angle library. 
   One way of getting it is to run org.sudu.experiments.nativelib.CopyAngleToResources
   You may also need to build native parts of project with this maven command:
   `mvn package -am -pl graphics-jvm`
   After this steps you can simply run org.sudu.experiments.DemoEditJvm

9. If you want to build webpack-test, you need Node.js version 18+. 
   Steps:  
   1. Run maven task to build the module (any of "a" or "b")
      1. run IDEA run-configuration "Module package"
      2. or with maven `mvn package -am -pl demo-edit-es-module` in root
   2. Build the webpack demo using file demo-edit-es-module/webpack-test/package.json
      1. run npm script "i"
      2. run npm script "build-sudu-editor-sample"
