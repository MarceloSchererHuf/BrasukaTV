# BrasukaTV - Script de Compilación Automática
Write-Host "============================================" -ForegroundColor Green
Write-Host " BRASUKA TV - Compilador Automatico" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Green
Write-Host ""

# Paso 1: Instalar SDK components
Write-Host "[1/5] Configurando Android SDK..." -ForegroundColor Cyan
Set-Location "C:\Android\sdk\cmdline-tools\bin"
cmd /c "echo y | sdkmanager.bat --sdk_root=C:\Android\sdk `"platform-tools`" `"platforms;android-34`" `"build-tools;34.0.0`" 2>&1"

# Paso 2: Aceptar licencias
Write-Host ""
Write-Host "[2/5] Aceptando licencias Android..." -ForegroundColor Cyan
cmd /c "echo y | sdkmanager.bat --sdk_root=C:\Android\sdk --licenses 2>&1"

# Paso 3: Compilar APK
Write-Host ""
Write-Host "[3/5] Compilando BrasukaTV (esto puede tardar 3-5 minutos)..." -ForegroundColor Cyan
Set-Location "C:\AndroidTV-LiveStream"
& ".\gradlew.bat" assembleDebug

# Paso 4: Copiar APK
Write-Host ""
Write-Host "[4/5] Buscando APK generado..." -ForegroundColor Cyan
$apkPath = "app\build\outputs\apk\debug\app-debug.apk"
if (Test-Path $apkPath) {
    $desktop = [Environment]::GetFolderPath("Desktop")
    if (Test-Path $desktop) {
        Copy-Item $apkPath "$desktop\BrasukaTV.apk" -Force
        Write-Host ""
        Write-Host "============================================" -ForegroundColor Green
        Write-Host " LISTO! APK creado exitosamente" -ForegroundColor Yellow
        Write-Host "============================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Ubicacion: $desktop\BrasukaTV.apk" -ForegroundColor White
        Write-Host "Tamano: $([math]::Round((Get-Item $apkPath).Length / 1MB, 2)) MB" -ForegroundColor White
    } else {
        Copy-Item $apkPath "C:\BrasukaTV.apk" -Force
        Write-Host "APK creado en: C:\BrasukaTV.apk" -ForegroundColor White
    }
    
    Write-Host ""
    Write-Host "Ahora puedes:" -ForegroundColor Cyan
    Write-Host "1. Copiar BrasukaTV.apk a tu Android TV/Celular via USB" -ForegroundColor White
    Write-Host "2. Activar 'Origenes desconocidos' en Configuracion" -ForegroundColor White
    Write-Host "3. Instalar y disfrutar 60+ canales IPTV!" -ForegroundColor White
} else {
    Write-Host "Error: No se encontro el APK. Revisa los errores arriba." -ForegroundColor Red
}

Write-Host ""
Read-Host "Presiona Enter para salir"
