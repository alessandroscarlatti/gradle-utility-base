setlocal enabledelayedexpansion
@if "%DEBUG%"=="" @echo off
@rem #########################################################
@rem Run the specified jenkins task
@rem #########################################################
set "jenkins.task=%1"
gradlew -q jenkins