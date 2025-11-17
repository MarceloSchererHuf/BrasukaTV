# TV Live Stream - Guía de Fuentes IPTV

Aplicación de streaming en vivo para Android TV con acceso a múltiples fuentes IPTV categorizadas.

## 🎯 Fuentes Integradas

La aplicación incluye **30+ listas de reproducción** pre-configuradas, organizadas en 3 capas según estabilidad y legalidad:

### 📗 CAPA 1: Fuentes Legales (TDT)
**100% Legal - Máxima Estabilidad**

- **TDT España (TDTChannels)** - Canales terrestres españoles oficiales
- **M3U Portugal** - Transmisiones públicas portuguesas

Estas fuentes contienen solo canales de emisión abierta y son completamente legales.

### 📘 CAPA 2: Servicios FAST (Legal con publicidad)
**Legal - Alta Estabilidad**

- **Pluto TV** - Miles de canales gratuitos con publicidad
- **Plex TV** - Contenido curado con publicidad
- **PBS** - Televisión pública estadounidense
- **Samsung TV Plus** - Canales FAST de Samsung
- **Stirr TV** - Canales con publicidad

Todos estos servicios son completamente legales y financiados por publicidad.

### 📙 CAPA 3: IPTV-ORG (Comunidad)
**Zona gris - Estabilidad variable**

#### Por Idioma:
- Español (todos los canales en español)

#### Por País:
- España
- Argentina
- México
- Estados Unidos

#### Por Categoría:
- Noticias (categoría más estable)
- Entretenimiento
- Películas
- Infantil
- Música
- Deportes (alta inestabilidad)

## 🚀 Cómo usar la app

1. **Pantalla principal**: Verás todas las fuentes organizadas por tipo
   - 🟢 **LEGAL** = TDT (100% legal)
   - 🔵 **FAST** = Servicios con publicidad (legal)
   - 🟠 **COMUNIDAD** = iptv-org (zona gris)

2. **Selecciona una fuente**: La app descargará automáticamente la lista M3U desde GitHub

3. **Elige un canal**: Se cargará la lista de canales disponibles

4. **Reproduce**: El reproductor ExoPlayer comenzará la transmisión

## 📡 Características Técnicas

### ✅ Actualización Automática
Las listas se descargan **en tiempo real** desde los repositorios GitHub. Esto significa:
- Siempre tienes los enlaces más recientes
- Los enlaces rotos son corregidos por la comunidad
- No necesitas actualizar la app para tener nuevos canales

### ✅ Parser M3U Integrado
La app incluye un parser que:
- Lee el formato M3U/M3U8 estándar
- Extrae nombres de canales y categorías
- Organiza automáticamente el contenido

### ✅ Navegación con Control Remoto
- **Flechas**: Navegar entre fuentes/canales
- **OK/Enter**: Seleccionar
- **Atrás**: Volver
- **↑↓ / CH+/-**: Cambiar canal (durante reproducción)

## 🔧 Formatos Soportados

La app reproduce:
- **HLS** (.m3u8) - Recomendado
- **DASH** (.mpd)
- **MP4** progresivo
- Streams de transporte (.ts)

## ⚠️ Consideraciones Importantes

### Estabilidad de Enlaces
- **Fuentes Legales**: ~95% de uptime
- **FAST**: ~90% de uptime
- **IPTV-ORG**: Variable (50-80% según categoría)
- **Deportes**: Muy inestable (~30-40%)

### Geobloqueo
Muchos canales están bloqueados por región. Necesitarás una **VPN** para:
- Canales TDT españoles (requiere IP de España)
- Canales FAST de USA (requiere IP americana)
- Cualquier contenido con restricción geográfica

### Legalidad
- ✅ **TDT y FAST**: 100% legal
- ⚠️ **IPTV-ORG**: Zona gris - enlaces públicos pero algunos pueden ser ilícitos
- ❌ **Evitar**: Listas que prometan "ESPN HD", "Sky Sports", etc.

## 📚 Basado en el Análisis

Esta aplicación implementa las recomendaciones del informe:

1. **Estrategia de "Suscripción a Repositorios"** en lugar de "caza de enlaces"
2. **Uso de repositorios mantenidos activamente** (iptv-org, TDTChannels, i.mjh.nz)
3. **Actualización en tiempo real** desde las fuentes maestras
4. **Evita Gist/Pastebin** y otras fuentes estáticas muertas
5. **Sistema de capas** priorizando estabilidad y legalidad

## 🔗 Fuentes de los Datos

Todos los enlaces provienen de:

- **GitHub iptv-org**: https://github.com/iptv-org/iptv
- **TDTChannels**: https://github.com/LaQuay/TDTChannels
- **M3UPT**: https://github.com/LITUATUI/M3UPT
- **i.mjh.nz**: Agregador FAST comunitario

## 🛠️ Agregar Fuentes Personalizadas

Para agregar tus propias URLs, edita `Channel.kt`:

```kotlin
// En el objeto PlaylistProvider, agrega a cualquier lista:
PlaylistSource(
    id = 100,
    name = "Mi Lista Personalizada",
    url = "https://tu-servidor.com/lista.m3u8",
    type = SourceType.CUSTOM,
    region = "Tu Región",
    description = "Tu descripción"
)
```

## 📱 Reproductores Recomendados

Si prefieres usar reproductores externos:
- **TiviMate** (Android TV - Mejor opción)
- **VLC** (Universal)
- **IPTV Smarters**
- **OTT Navigator**

Simplemente copia las URLs de las fuentes desde el código.

## 🔐 Privacidad y Seguridad

- ✅ Sin telemetría
- ✅ Sin recolección de datos
- ✅ Solo conexiones a fuentes públicas
- ⚠️ Usa VPN para privacidad adicional

## 📊 Estadísticas de Contenido

- **~40+ fuentes** pre-configuradas
- **Miles de canales** disponibles
- **100+ países** representados
- **20+ categorías** diferentes

## ⚙️ Requisitos del Sistema

- Android 5.0+ (API 21)
- Conexión a Internet estable (5+ Mbps recomendado)
- 50MB de espacio libre
- Optimizado para Android TV

---

**Nota Legal**: Esta app solo agrega enlaces públicamente disponibles. No aloja ningún contenido de video. El usuario es responsable del contenido que visualiza.
