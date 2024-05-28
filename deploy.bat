@echo off

setlocal
cd %~dp0

mvnw.cmd -DaltDeploymentRepository=in-project::file:///%local_maven_repo% clean deploy
endlocal
