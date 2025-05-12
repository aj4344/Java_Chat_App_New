@echo off
title Chat Application Launcher

echo ===================================
echo     Java Chat App with Themes
echo ===================================
echo.
echo Compiling Chat Application...
cd /d "%~dp0"
echo Current directory: %CD%

if not exist "src\main\java" (
    echo Error: Could not find the source directory.
    echo Please make sure you are running this batch file from the project root directory.
    pause
    exit /b 1
)

REM Clean any existing class files
echo Cleaning previous compilation...
del /s /q "src\main\java\chat\model\*.class" >nul 2>&1
del /s /q "src\main\java\chat\server\*.class" >nul 2>&1
del /s /q "src\main\java\chat\client\*.class" >nul 2>&1
del /s /q "src\main\java\chat\client\gui\*.class" >nul 2>&1
del /s /q "src\main\java\chat\launcher\*.class" >nul 2>&1

REM Compile all files at once with proper encoding and classpath
echo Compiling Java files...
javac -encoding UTF-8 -d src\main\java -cp src\main\java ^
    src\main\java\chat\model\*.java ^
    src\main\java\chat\server\*.java ^
    src\main\java\chat\client\*.java ^
    src\main\java\chat\client\gui\*.java ^
    src\main\java\chat\launcher\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed. Please check for errors.
    pause
    exit /b 1
)

echo Starting Chat Application Launcher...
java -Dfile.encoding=UTF-8 -cp src\main\java chat.launcher.SimpleLauncher

pause
