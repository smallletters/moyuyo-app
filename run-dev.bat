@echo off
chcp 65001 >nul

set BASE_DIR=d:\moyuyowpc\moyuyo-server
set API_DIR=%BASE_DIR%\moyuyo-api
set COMMON_DIR=%BASE_DIR%\moyuyo-common
set DAO_DIR=%BASE_DIR%\moyuyo-dao
set SERVICE_DIR=%BASE_DIR%\moyuyo-service

REM First build classpath
cd /d %BASE_DIR%
call mvn dependency:build-classpath -Dmdep.outputFile="%TEMP%\moyuyo-cp.txt" -pl moyuyo-api -am -q 2>nul

setlocal enabledelayedexpansion

REM Read the classpath file
set /p CP=<"%TEMP%\moyuyo-cp.txt"

REM Add all target/classes directories
set CP=%API_DIR%\target\classes;%COMMON_DIR%\target\classes;%DAO_DIR%\target\classes;%SERVICE_DIR%\target\classes;%CP%

echo Starting MOYUYO backend...
echo.
java --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED -cp "%CP%" com.moyuyo.api.MoyuyoApplication

pause
