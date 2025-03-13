@echo off

set MODULE_NAME=fearthgdk

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

set AAR_PATH=%MODULE_NAME%\build\outputs\aar\%MODULE_NAME%-release.aar

:: Check if AAR was built successfully
if exist "%AAR_PATH%" (
    echo Build successful! AAR located at:
    echo %AAR_PATH%
) else (
    echo Build failed!
    exit /b 1
)

exit /b 0