cl angleImplib\dummyGLESv2.cpp /I..\angle-include /LD /O2s /Fetarget\libGLESv2.dll /Fotarget\ /link /noentry
copy target\libGLESv2.lib ..\angle-include\