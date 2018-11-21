@echo off
set a=0
title OdinMS: Inactive
color 1b
:clear
cls
echo Launcher
echo.
echo Type Start to start the server:
echo ---------
echo start - Start OdinMS server
echo.

:command
set /p s="Enter command: "
if "%s%"=="start" goto :start
echo Incorrect command!
echo.
goto :command

:start
if "%a%"=="1" (
echo OdinMS is already active!
echo.
goto :command
)
color 4c
title OdinMS: World Server 0/3
start /b launch_world.bat
title OdinMS: Login Server 1/3
ping localhost -w 100 >nul
start /b launch_login.bat
title OdinMS: Channel Server 2/3
ping localhost -w 100 >nul
start /b launch_channel.bat
title OdinMS: Success! 3/3
ping localhost -w 100 >nul
color 2a
title OdinMS: Online
set a=1
echo.
goto :command