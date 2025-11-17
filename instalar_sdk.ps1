# ========================================
# Instalador Automático Android SDK
# ========================================

$ANDROID_SDK = "$env:LOCALAPPDATA\Android\Sdk"
$CMDLINE_TOOLS = "$ANDROID_SDK\cmdline-tools\latest"

Write-Host "========================================" -ForegroundColor Green
Write-Host " Instalando Android SDK para BrasukaTV" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

# Crear directorio
New-Item -ItemType Directory -Force -Path "$ANDROID_SDK\cmdline-tools" | Out-Null

# Descargar Command Line Tools
$url = "https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip"
$zip = "$env:TEMP\cmdline-tools.zip"

Write-Host "[1/5] Descargando Android SDK Command Line Tools (150 MB)..." -ForegroundColor Cyan
Invoke-WebRequest -Uri $url -OutFile $zip -UseBasicParsing

Write-Host "[2/5] Extrayendo..." -ForegroundColor Cyan
Expand-Archive -Path $zip -DestinationPath "$ANDROID_SDK\cmdline-tools" -Force

# Renombrar carpeta
Move-Item "$ANDROID_SDK\cmdline-tools\cmdline-tools" "$CMDLINE_TOOLS" -Force

Write-Host "[3/5] Configurando variables de entorno..." -ForegroundColor Cyan
[Environment]::SetEnvironmentVariable("ANDROID_HOME", $ANDROID_SDK, "User")
$env:ANDROID_HOME = $ANDROID_SDK

Write-Host "[4/5] Aceptando licencias..." -ForegroundColor Cyan
Write-Host "Presiona 'y' y Enter cuando se solicite" -ForegroundColor Yellow
& "$CMDLINE_TOOLS\bin\sdkmanager.bat" --licenses

Write-Host "[5/5] Instalando componentes necesarios..." -ForegroundColor Cyan
& "$CMDLINE_TOOLS\bin\sdkmanager.bat" "platform-tools" "platforms;android-34" "build-tools;33.0.1"

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host " ✅ Android SDK instalado correctamente" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Ahora ejecuta:" -ForegroundColor Yellow
Write-Host "  cd C:\AndroidTV-LiveStream" -ForegroundColor White
Write-Host "  .\gradlew.bat assembleRelease" -ForegroundColor White
Write-Host ""
Write-Host "La APK estará en:" -ForegroundColor Yellow
Write-Host "  app\build\outputs\apk\release\app-release.apk" -ForegroundColor White
