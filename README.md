This is an experimental project to write a portable (Web + Desktop) editor in java and kotlin

This demo is designed to investigate performance limits of this approach and compare with other Web editors like Monaco and VSCode

In order to build it one need to

1. Clone and build TeaVM compiler locally:

    ```
    git clone https://github.com/kirillp/teavm.git
    cd teavm 
    ./gradlew publishToMavenLocal -x test
    ```

Note: we don't use git clone https://github.com/konsoletyper/teavm.git due to a chance of incompatible changes in master of TeaVM.

2. Also, you need to get the latest emscripten for wasm compilation:

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

3. If you also need to build the desktop app, you need Microsoft Visual Studio 2022

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

4. Edit environment:
   ```
   rundll32 sysdm.cpl,EditEnvironmentVariables
   ```
   add the components to the user %path% manually or (not recommended) by **setx** command 
    - MSBuild: %MSBuildPath% from step 3 
    - Clang: %EMSDK%\upstream\bin from step 2
    ```
    setx Path "%Path%;%EMSDK%\upstream\bin;%MSBuildPath%" 
    ```

5. Finally check that compilers work, and review Environment Variables.
   You need a new console instance to apply env changes
    ```
   MSBuild --help
   clang --help
    ```

6. Clone and build TeaVM-compatible version of ANTLR:
   ```
   git clone https://github.com/pertsevpv/antlr4-teavm-compatible.git
   cd antlr4-teavm-compatible
   mvn install -DskipTests
   ```

7. Run download fonts task:
   `mvn package -am -pl :codicon -pl :fonts -f pom.xml`

   Now you can build the demo with

   `mvn package`


8. To run desktop version one need to get libGLESv2.dll - Angle library. 
   One way of getting it is to run org.sudu.experiments.nativelib.CopyAngleToResources
   You may also need to build native parts of project with this maven command:
   `mvn package -am -pl graphics-jvm`
   After this steps you can simply run org.sudu.experiments.DemoEditJvm


9. If you want to build ES module with its demo, you need Node.js version 18+. 
   Steps:  
   1. Run maven task to build the module (any of "a" or "b")
      1. run IDEA run-configuration "Module package"
      2. or with maven `mvn package -am -pl demo-edit-es-module` in root
   2. Build the webpack demo using file demo-edit-es-module/webpack-test/package.json
      1. run npm script "i"
      2. run npm script "build-sudu-editor-sample"
      