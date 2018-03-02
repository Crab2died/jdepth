@echo off

:: java运行参数设置
set "BIN_DIR=%~dp0"
set "CURRENT_DIR=%BIN_DIR%\.."
set "JAVA_OPTS=-Xms512m -Xmx1g"
set "LIB_DIR=%CURRENT_DIR%\lib"
set "LOG_PATH=%CURRENT_DIR%\config\logback.xml"

java %JAVA_OPTS% -Dlogback.configurationFile=file:%LOG_PATH% ^
-Djava.ext.dirs=%LIB_DIR% ^
-Dfile.encoding=GBK ^
%1

pause &