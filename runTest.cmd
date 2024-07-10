
set mjs=filediff-node-module\module\samples\example1.mjs

set dir=.\

set dir1=D:\Github\llvm-project-a
set dir2=D:\Github\llvm-project-b

set dir3=D:\chromium-main

set dir1=%dir%
set dir2=%dir%

set cp=%dir%\demo-edit-jvm\target\classes;%dir%\graphics-jvm\target\classes;%dir%\graphics\target\classes;%dir%\graphics-common\target\classes;%dir%\angle-dll\target\classes;%dir%\demo-edit\target\classes;%dir%\parser\target\classes;%dir%\parser-common\target\classes;C:\Users\Kirill\.m2\repository\org\antlr\antlr4-runtime-teavm-compatible\4.13.2-SNAPSHOT\antlr4-runtime-teavm-compatible-4.13.2-SNAPSHOT.jar;%dir%\diff-model\target\classes;%dir%\parser-activity\target\classes;%dir%\fonts\target\classes;%dir%\codicon\target\classes;%dir%\demo-test-scenes\target\classes

set javaExe=%USERPROFILE%\.jdks\openjdk-21.0.2\bin\java.exe
set javaCmd=%javaExe% -cp %cp% org.sudu.experiments.FolderDiffTestJvm

set nativeExe=demo-edit-jvm\target\FolderDiffTestJvm.exe

mkdir logs

call :CallGraalNative logs\dry_g00 %dir1% %dir2%

call :CallNode logs\dry_n0 %dir1% %dir2%
call :CallJava logs\dry_j_0 %dir1% %dir2%
call :CallGraalNative logs\dry_g0 %dir1% %dir2%

exit

FOR %%i IN (0,1,2,3) DO (
  call :CallNode        logs\nrun%%i %dir1% %dir2% 
  call :CallJava        logs\jrun%%i %dir1% %dir2%
  call :CallGraalNative logs\graalrun%%i %dir1% %dir2%
)

exit

:CallNode
node %mjs% %~2 %~3 content %~4 > %~1 2>&1
EXIT /B 0

:CallGraalNative
%nativeExe% %~2 %~3 content %~4 > %~1 2>&1
EXIT /B 0

:CallJava
%javaCmd% %~2 %~3 content %~4 > %~1 2>&1
EXIT /B 0
