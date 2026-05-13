@echo off
setlocal
echo ========================================================
echo   DANG KHOI DONG SERVER TOMCAT...
echo ========================================================
echo.

:: Kiem tra xem may da cai dat Maven chua
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [INFO] Khong tim thay 'mvn' tren he thong.
    echo [INFO] Se tu dong tai Maven Portable (chi chay rieng cho project nay)...
    
    if not exist "%~dp0.maven" mkdir "%~dp0.maven"
    if not exist "%~dp0.maven\apache-maven-3.9.6" (
        echo [INFO] Dang tai Apache Maven 3.9.6 tu dong... (Se mat vai chuc giay)
        powershell -Command "Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile '%~dp0.maven\maven.zip'"
        
        echo [INFO] Dang giai nen Maven...
        powershell -Command "Expand-Archive -Path '%~dp0.maven\maven.zip' -DestinationPath '%~dp0.maven\' -Force"
        
        del "%~dp0.maven\maven.zip"
    )
    
    :: Them maven vua tai vao bien moi truong tam thoi
    set "PATH=%~dp0.maven\apache-maven-3.9.6\bin;%PATH%"
    echo [INFO] Da cai dat Maven Portable thanh cong.
    echo.
)

echo Vui long doi den khi hien thi thong bao "Starting tomcat server..."
echo Sau do ban co the mo trinh duyet va truy cap: 
echo http://localhost:8080
echo.
echo (De dung server, an Ctrl + C va chon Y)
echo.

call mvn jetty:run
pause
