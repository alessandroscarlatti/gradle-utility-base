@if [%DEBUG%]==[] ( @echo off )
setlocal enabledelayedexpansion
@rem ##########################################################################
@rem README:
@rem ##########################################################################
@rem
@rem Create a new user-specific configuration .properties file in the user
@rem home directory, if it does not already exits.  Use the default properties
@rem file.
@rem
@rem Show the user-specific configuration .properties file if it already
@Rem exists.
@rem
@rem ##########################################################################
@rem PARAMETERS: configure below
@rem ##########################################################################

@rem The corresponding user properties file.
@rem Prefer %USER_PROFILE%\<app>\user.properties
set "user.props.file=%USERPROFILE%\.testapp\user.properties"

@rem The default user properties file.
set "user.props.default.file=%~dp0%~n0.properties"

@rem the powershell command to execute
powershell -command "[regex]::matches((gc -raw '%~f0'), '(?s)(?<=\n::PS_SCRIPT).*').value | iex"
set "ExitCode=%ERRORLEVEL%"
if not [%ExitCode%]==[0] (
    echo Execution Failed. Check log (if available^) for more details.
    pause
)
exit /b %ExitCode%

@rem ##########################################################################
@rem POWERSHELL SCRIPT
@rem ##########################################################################
::PS_SCRIPT
# Create or Show a new user environment config file

$PropsFile = ${env:user.props.file}
$DefaultPropsFile = ${env:user.props.default.file}
write-host "Running User Setup for $PropsFile"

if (test-path $PropsFile) {
    write-host "Showing User Environment Props File $PropsFile"
    explorer "/select,`"$PropsFile`""
} else {
    write-host "No User Props file found...Creating User Props File $PropsFile from default $DefaultPropsFile"
    New-Item -Path $PropsFile -ItemType File -Force -value ((gc $DefaultPropsFile) | out-string)
    write-host "Created User Environment Config File $PropsFile"
    explorer "/select,`"$PropsFile`""
}