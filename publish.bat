@echo off

set MODULE_NAME=fearthgdk
set GITHUB_REPO=https://github.com/phucanh1939/fearth-android-gdk

echo Cleaning previous builds...
call gradlew clean
IF %ERRORLEVEL% NEQ 0 (
    echo Error: Cleaning failed!
    exit /b %ERRORLEVEL%
)

echo Building AAR for %MODULE_NAME%...
call gradlew :%MODULE_NAME%:assembleRelease
IF %ERRORLEVEL% NEQ 0 (
    echo Error: Build failed!
    exit /b %ERRORLEVEL%
)

echo Publishing %MODULE_NAME% to GitHub Packages...
call gradlew.bat :%MODULE_NAME%:publish
IF %ERRORLEVEL% NEQ 0 (
    echo Error: Publish failed!
    exit /b %ERRORLEVEL%
)
echo Publish successful!
echo Check your package at %GITHUB_REPO%/packages

@REM Publish to local maven
@REM echo Publishing %MODULE_NAME% to local Maven
@REM call gradlew.bat :%MODULE_NAME%:publishToMavenLocal
@REM echo Publish successful!
@REM echo Local maven package can be found at ~/.m2/repository/com/fearth/gdk/version

