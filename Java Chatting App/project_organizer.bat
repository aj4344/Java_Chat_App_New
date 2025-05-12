@echo off
title Complete Project Cleanup & Organization

REM Set colors for better output
color 0A

echo ===================================================
echo    Java Chat App - Project Cleanup & Organization
echo ===================================================
echo.

cd /d "%~dp0"

REM Display current project structure
echo Current project structure:
echo -------------------------
dir /B /S *.java | find /V "\backup\" | sort

echo.
echo Checking for unnecessary files...
echo -------------------------

REM Check for any .new files that might still exist
set FOUND_NEW=0
for /R %%G in (*.new) do (
    echo Found: %%G
    set FOUND_NEW=1
)

if %FOUND_NEW%==0 (
    echo No .new files found.
)

echo.
echo Checking class file consistency...
echo -------------------------

REM Count Java files and class files
set /A JAVA_COUNT=0
set /A CLASS_COUNT=0
for /R %%G in (*.java) do (
    if not "%%G"=="\backup\" (
        set /A JAVA_COUNT+=1
    )
)

for /R %%G in (*.class) do (
    set /A CLASS_COUNT+=1
)

echo Found %JAVA_COUNT% Java source files and %CLASS_COUNT% compiled class files.
echo.

REM Check if class files are up to date
echo Note: If you've made changes to the code, it's recommended to 
echo recompile the project by running run_chat.bat

echo.
echo Project organization tips:
echo -------------------------
echo 1. Keep the backup folder for safety until you're sure everything works
echo 2. The main code is organized into client, server, and shared model packages
echo 3. User interface components are in chat.client.gui package
echo 4. The launcher provides an easy way to start the application

echo.
echo Cleanup complete!
echo ===================================================
echo.

pause
