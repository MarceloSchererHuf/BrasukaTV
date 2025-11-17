@echo off
echo ============================================
echo  BRASUKA TV - Compilador Automatico
echo ============================================
echo.

echo [1/5] Configurando Android SDK...
cd C:\Android\sdk\cmdline-tools\bin
echo y | sdkmanager.bat --sdk_root=C:\Android\sdk "platform-tools" "platforms;android-34" "build-tools;34.0.0"

echo.
echo [2/5] Aceptando licencias...
echo y | sdkmanager.bat --sdk_root=C:\Android\sdk --licenses

echo.
echo [3/5] Compilando BrasukaTV...
cd C:\AndroidTV-LiveStream
call gradlew.bat assembleDebug

echo.
echo [4/5] Copiando APK al escritorio...
copy "app\build\outputs\apk\debug\app-debug.apk" "%USERPROFILE%\Desktop\BrasukaTV.apk"

echo.
echo ============================================
echo  LISTO! APK creado en tu Escritorio
echo  Archivo: BrasukaTV.apk
echo ============================================
echo.
echo Ahora puedes:
echo 1. Copiar BrasukaTV.apk a tu Android TV/Celular
echo 2. Instalarlo (activa "Origenes desconocidos")
echo 3. Disfrutar 60+ canales IPTV!
echo.
pause
