
set mjs=filediff-node-module\module\samples\example1.mjs

set dir=.\

set dir1=D:\Github\llvm-project-a
set dir2=D:\Github\llvm-project-b

set dir3=D:\chromium-main

set dir1=%dir%
set dir2=%dir%

set cp=%dir%\demo-edit-jvm\target\classes;%dir%\graphics-jvm\target\classes;%dir%\graphics\target\classes;%dir%\graphics-common\target\classes;%dir%\angle-dll\target\classes;%dir%\demo-edit\target\classes;%dir%\parser\target\classes;%dir%\parser-common\target\classes;C:\Users\Kirill\.m2\repository\org\antlr\antlr4-runtime-teavm-compatible\4.13.2-SNAPSHOT\antlr4-runtime-teavm-compatible-4.13.2-SNAPSHOT.jar;%dir%\diff-model\target\classes;%dir%\parser-activity\target\classes;%dir%\fonts\target\classes;%dir%\codicon\target\classes;%dir%\demo-test-scenes\target\classes

set jjj=%USERPROFILE%\.jdks\openjdk-21.0.2\bin\java.exe

set nativeExe=demo-edit-jvm\target\FolderDiffTestJvm.exe

mkdir logs
rem goto :graal

call :CallNode logs\dry_n0 %dir1% %dir2%
call :CallNode logs\dry_n1 %dir1% %dir2%
call :CallNode logs\dry_n1s %dir1% %dir2% sync

call :CallJava logs\dry_j0 %dir1% %dir2% 
call :CallJava logs\dry_j1 %dir1% %dir2% 
call :CallJava logs\dry_j1s %dir1% %dir2% sync

:graal

call :CallGraalNative logs\dry_g0 %dir1% %dir2% 
call :CallGraalNative logs\dry_g1 %dir1% %dir2% 
call :CallGraalNative logs\dry_g1s %dir1% %dir2% sync

exit

FOR %%i IN (0,1,2,3) DO (
  node %mjs% %dir1% %dir2% > logs\nrun%%i 2>&1
  %jjj% -cp %cp% org.sudu.experiments.FolderDiffTestJvm %dir1% %dir2% content > logs\jrun%%i 2>&1
  %nativeExe% %dir1% %dir2% content > logs\graalrun%%i 2>&1
)

:CallNode
node %mjs% %~2 %~3 content %~4 > %~1 2>&1
EXIT /B 0

:CallGraalNative
%nativeExe% %~2 %~3 content %~4 > %~1 2>&1
EXIT /B 0

:CallJava
%jjj% -cp %cp% org.sudu.experiments.FolderDiffTestJvm %~2 %~3 content %~4 > %~1 2>&1
EXIT /B 0
