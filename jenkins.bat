@if "%DEBUG%"=="" @echo off
setlocal enabledelayedexpansion
@rem #########################################################
@rem Run the specified jenkins task
@rem #########################################################
set "jenkins.task=%1"
echo Running script %1.groovy
gradlew --stacktrace jenkins

rem groovy -cp build/libs/groovy-project-demo.jar scripts/%1.groovy