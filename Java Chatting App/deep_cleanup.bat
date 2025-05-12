@echo off
title Deep Cleanup - Java Chat Application

echo =============================================
echo        DEEP PROJECT CLEANUP UTILITY
echo =============================================
echo.
echo This script will clean up unnecessary files from the project.
echo Files to be deleted:
echo  - Backup folder and its contents
echo  - Any .new and .bak files
echo  - update_theme_support.bat (no longer needed)
echo.
echo Press Ctrl+C to cancel or any key to continue...
pause > nul

echo.
echo Starting cleanup process...
echo.

REM Delete the backup folder
if exist "backup" (
    rmdir /S /Q "backup"
    echo [DELETED] backup folder and its contents
) else (
    echo [SKIPPED] backup folder (not found)
)

REM Delete any .new files that might be leftover
for /R %%G in (*.new) do (
    del "%%G"
    echo [DELETED] %%G
)

REM Delete any .bak files
for /R %%G in (*.bak) do (
    del "%%G"
    echo [DELETED] %%G
)

REM Delete the update script since it's no longer needed
if exist "update_theme_support.bat" (
    del "update_theme_support.bat"
    echo [DELETED] update_theme_support.bat
) else (
    echo [SKIPPED] update_theme_support.bat (not found)
)

REM Delete the cleanup.bat since we now have this more comprehensive script
if exist "cleanup.bat" (
    del "cleanup.bat"
    echo [DELETED] cleanup.bat
) else (
    echo [SKIPPED] cleanup.bat (not found)
)

echo.
echo Cleanup complete!
echo =============================================
echo.
pause
