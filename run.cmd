set mjs=filediff-node-module\module\samples\example1.mjs

set dir=.\

set dir1=D:\Github\llvm-project-a
set dir2=D:\Github\llvm-project-b

set dir3=D:\chromium-main

set dir1=%dir3%
set dir2=%dir3%

set cp=%dir%\demo-edit-jvm\target\classes;%dir%\graphics-jvm\target\classes;%dir%\graphics\target\classes;%dir%\graphics-common\target\classes;%dir%\angle-dll\target\classes;%dir%\demo-edit\target\classes;%dir%\parser\target\classes;%dir%\parser-common\target\classes;C:\Users\Kirill\.m2\repository\org\antlr\antlr4-runtime-teavm-compatible\4.13.2-SNAPSHOT\antlr4-runtime-teavm-compatible-4.13.2-SNAPSHOT.jar;%dir%\diff-model\target\classes;%dir%\parser-activity\target\classes;%dir%\fonts\target\classes;%dir%\codicon\target\classes;%dir%\demo-test-scenes\target\classes

set jjj=%USERPROFILE%\.jdks\openjdk-21.0.2\bin\java.exe

set nativeExe=demo-edit-jvm\target\FolderDiffTestJvm.exe

mkdir logs

node %mjs% %dir1% %dir2% > logs\dry_n 2>&1
%jjj% -cp %cp% org.sudu.experiments.FolderDiffTestJvm %dir1% %dir2% content > logs\dry_j 2>&1
%nativeExe% %dir1% %dir2% content > logs\dry_graal 2>&1

FOR %%i IN (0,1,2,3) DO (
  node %mjs% %dir1% %dir2% > logs\nrun%%i 2>&1
  %jjj% -cp %cp% org.sudu.experiments.FolderDiffTestJvm %dir1% %dir2% content > logs\jrun%%i 2>&1
  %nativeExe% %dir1% %dir2% content > logs\graalrun%%i 2>&1
)

