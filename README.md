# TV Live Stream - App para Android TV

Aplicación de streaming en vivo para Android TV con **30+ fuentes IPTV integradas**.

## 🎯 Características Principales

✅ **30+ listas M3U** pre-configuradas desde repositorios verificados
✅ **Actualización automática** desde GitHub (iptv-org, TDTChannels, i.mjh.nz)
✅ **3 capas de fuentes**: Legal/TDT, FAST, Comunidad
✅ **Parser M3U integrado** - carga cualquier lista M3U8
✅ **Reproductor ExoPlayer** con soporte HLS, DASH, TS
✅ **Interfaz Android TV** optimizada para control remoto
✅ **Categorización automática** por país, idioma y tipo de contenido
✅ **Miles de canales** de todo el mundo

## 📚 Fuentes Integradas

La app incluye **30+ fuentes verificadas**, organizadas en 3 capas:

### 🟢 CAPA 1: Legal (TDT)
- TDT España (TDTChannels) - 100% legal
- M3U Portugal - Transmisiones públicas

### 🔵 CAPA 2: FAST (Legal con publicidad)
- Pluto TV, Plex TV, PBS
- Samsung TV Plus, Stirr TV

### 🟠 CAPA 3: Comunidad (IPTV-ORG)
- Por país: España, Argentina, México, USA, etc.
- Por idioma: Español, Francés, etc.
- Por categoría: Noticias, Películas, Infantil, Música, Deportes

**Ver [README_FUENTES.md](README_FUENTES.md) para la lista completa y detalles**

## 🚀 Inicio Rápido

1. **Abre la app** en tu Android TV
2. **Selecciona una fuente** (recomendado: "TDT España" o "Pluto TV")
3. **Espera a que cargue** - la app descarga la lista M3U desde GitHub
4. **Elige un canal** y disfruta

## 🎮 Controles

- **Flechas**: Navegar menús
- **OK/Enter**: Seleccionar fuente/canal
- **↑↓ o CH+/-**: Cambiar canal durante reproducción
- **Atrás**: Volver

## 📡 Cómo Funciona

La app NO tiene canales "quemados" en el código. En su lugar:

1. **Descarga listas M3U** en tiempo real desde repositorios GitHub
2. **Parsea el formato** M3U/M3U8 automáticamente
3. **Extrae canales** con nombres y categorías
4. **Reproduce** con ExoPlayer

Esto significa que siempre tienes **enlaces actualizados** mantenidos por la comunidad.

## 🔧 Requisitos

- Android 5.0+ (API 21)
- Conexión a Internet (5+ Mbps recomendado)
- Compatible con Android TV, Fire TV, boxes Android

## ⚙️ Configuración Avanzada

### Agregar tus propias fuentes

Edita `app/src/main/java/com/tv/livestream/Channel.kt`:

```kotlin
PlaylistSource(
    id = 100,
    name = "Mi Lista Personal",
    url = "https://ejemplo.com/mi-lista.m3u8",
    type = SourceType.CUSTOM,
    region = "Personal"
)
```

### Usar con VPN

Muchos canales están geobloqueados. Para acceder:
1. Instala una VPN en tu Android TV
2. Conéctate al país del contenido deseado
3. Abre la app y selecciona la fuente

## 🛠️ Compilar desde código

```bash
# Clonar el repositorio
git clone <tu-repo>
cd AndroidTV-LiveStream

# Abrir en Android Studio
# File > Open > Seleccionar carpeta

# Compilar
./gradlew assembleDebug

# O usar el botón "Run" en Android Studio
```

## 📊 Fuentes de Datos

Todos los enlaces provienen de repositorios verificados:

- **iptv-org** (101k+ ⭐): https://github.com/iptv-org/iptv
- **TDTChannels**: https://github.com/LaQuay/TDTChannels  
- **M3UPT**: https://github.com/LITUATUI/M3UPT
- **i.mjh.nz**: Agregador FAST comunitario

## ⚠️ Consideraciones

### Estabilidad
- **TDT/FAST**: ~90-95% uptime (muy estable)
- **IPTV-ORG**: Variable según categoría
- **Noticias**: Alta estabilidad
- **Deportes**: Baja estabilidad (muchos enlaces mueren)

### Legalidad
- ✅ **TDT y FAST**: 100% legal
- ⚠️ **IPTV-ORG**: Zona gris - enlaces públicos, algunos pueden ser ilícitos
- ❌ **Evitar**: Canales premium (ESPN, Sky Sports) son generalmente ilegales

### Privacidad
- La app no recopila datos
- Todas las conexiones son directas a las fuentes
- Recomendado: Usar VPN para privacidad adicional

## 🔍 Basado en Análisis Profesional

Esta app implementa las mejores prácticas del informe "Directorio de Inteligencia de Ecosistemas M3U":

1. ✅ **Suscripción a repositorios** (no caza de enlaces estáticos)
2. ✅ **Actualización automática** desde fuentes mantenidas
3. ✅ **Evita fuentes muertas** (Gist, Pastebin, Scribd)
4. ✅ **Sistema de capas** por estabilidad/legalidad
5. ✅ **Fuentes verificadas** solamente

## 🎯 Casos de Uso

### Usuario casual
1. Abre la app
2. Selecciona "Pluto TV" o "TDT España"
3. Disfruta contenido legal y estable

### Usuario avanzado  
1. Explora las 30+ fuentes disponibles
2. Combina múltiples listas
3. Agrega tus propias URLs M3U8
4. Usa VPN para acceso internacional

### Desarrollador
1. Fork el proyecto
2. Modifica `Channel.kt` para agregar fuentes
3. Personaliza la UI en los archivos XML
4. Compila tu versión personalizada

## 📱 Reproductores Alternativos

Si prefieres usar reproductores externos, puedes copiar las URLs de `Channel.kt` a:
- **TiviMate** (recomendado para Android TV)
- **VLC Media Player**
- **IPTV Smarters**
- **OTT Navigator**

## 🆘 Solución de Problemas

### "No se cargan los canales"
- Verifica tu conexión a Internet
- Algunos repositorios pueden estar temporalmente inaccesibles
- Prueba con otra fuente (ej: cambia de IPTV-ORG a TDT)

### "El canal no reproduce"
- El enlace puede estar caído (normal en listas comunitarias)
- Prueba con otro canal de la misma lista
- Algunos canales requieren VPN (geobloqueo)

### "Pantalla negra"
- Verifica Logcat para errores específicos
- El formato del stream puede no ser compatible
- Verifica permisos de Internet en AndroidManifest

### "Control remoto no funciona"
- Asegúrate de que la app está en foco
- Verifica que tu dispositivo tiene Leanback support
- Algunos controles BT pueden requerir configuración

## 📈 Estadísticas

- **Fuentes pre-configuradas**: 30+
- **Canales disponibles**: Miles (varía por lista)
- **Países cubiertos**: 100+
- **Categorías**: 20+
- **Formatos soportados**: HLS, DASH, TS, MP4

## 🔮 Roadmap

- [ ] Soporte para EPG (Guía de programación)
- [ ] Sistema de favoritos
- [ ] Historial de reproducción
- [ ] Filtros por categoría/país
- [ ] Verificador de enlaces integrado
- [ ] Modo Picture-in-Picture
- [ ] Control parental

## 📄 Licencia

Proyecto de código abierto para uso personal y educativo.

**Disclaimer**: Esta aplicación solo agrega enlaces públicamente disponibles. No aloja contenido de video. El usuario es responsable del contenido que visualiza y debe cumplir con las leyes locales de derechos de autor.

## 🤝 Contribuciones

¿Quieres agregar más fuentes o mejorar el código? ¡Los PRs son bienvenidos!

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/nueva-fuente`)
3. Commit tus cambios (`git commit -m 'Agregar fuente XYZ'`)
4. Push a la rama (`git push origin feature/nueva-fuente`)
5. Abre un Pull Request

---

**Desarrollado con** ❤️ **para la comunidad de Android TV**
