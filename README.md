This is an experimental project to write a portable (Web + Desktop) editor in java and kotlin

This demo is designed to investigate performance limits of this approach and compare with other Web editors like Monaco and VSCode

In order to build it one need to

1. Clone and build TeaVM compiler locally:

    ```
    git clone https://github.com/konsoletyper/teavm.git
    cd teavm 
    mvn -e install -DskipTests -Dteavm.build.all=false -Pwith-cli -Dmaven.javadoc.skip=true -T4
    ```

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
   3a: IDE: https://aka.ms/vs/17/release/vs_community.exe
    ```
    vs_community.exe --wait --p --norestart --nocache --locale en-US --add "Microsoft.VisualStudio.Workload.NativeDesktop;includeRecommended"  
    set MSBuildPath=C:\Program Files\Microsoft Visual Studio\2022\Community\Msbuild\Current\Bin\amd64
    ```

   3b: BuildTools: https://aka.ms/vs/17/release/vs_BuildTools.exe  
   https://learn.microsoft.com/en-us/visualstudio/install/workload-component-id-vs-build-tools?view=vs-2022
    ```
   vs_BuildTools.exe --wait --p --norestart --nocache --locale en-US --add "Microsoft.VisualStudio.Workload.VCTools;includeRecommended"  
   set MSBuildPath=C:\Program Files (x86)\Microsoft Visual Studio\2022\BuildTools\MSBuild\Current\Bin
   ```

4. add MSBuild and Clang to the system path
    ```
    setx Path "%Path%;%EMSDK%\upstream\bin;%MSBuildPath%" 
    ```

5. Finally check that compilers work, and review Environment Variables
   You need a new console instance to apply env changes
    ```
   rundll32 sysdm.cpl,EditEnvironmentVariables
   MSBuild --help
   clang --help
    ```
Now you can build the demo with

`mvn package`

To run desktop version one need to get libGLESv2.dll - Angle library.
One way of getting it is to run org.sudu.experiments.nativelib.CopyAngleToResources

You may also need to build native parts of project with this maven command:
`mvn package -am -pl graphics-jvm`

After this steps you can simply run org.sudu.experiments.DemoEditJvm
