package com.brasuka.tv

/**
 * Categorías principales de BrasukaTV
 * Expandidas para incluir más tipos de contenido
 */
enum class ContentCategory(val displayName: String, val icon: String) {
    LIVE_TV("TV ao Vivo", "📺"),
    MOVIES("Filmes", "🎬"),
    SERIES("Séries", "📺"),
    SPORTS("Esportes", "⚽"),
    KIDS("Infantil", "👶"),
    NEWS("Notícias", "📰"),
    MUSIC("Música", "🎵"),
    DOCUMENTARY("Documentários", "🎥"),
    ENTERTAINMENT("Entretenimento", "🎭"),
    ADULT("Adultos", "🔞"),
    RELIGION("Religião", "✝️"),
    COOKING("Culinária", "👨‍🍳"),
    BUSINESS("Negócios", "💼");
    
    companion object {
        fun fromString(category: String): ContentCategory {
            return when (category.lowercase()) {
                "sports", "esportes", "deportes", "sport" -> SPORTS
                "movies", "filmes", "películas", "peliculas", "filme", "movie" -> MOVIES
                "series", "séries", "serie" -> SERIES
                "kids", "infantil", "niños", "children" -> KIDS
                "news", "notícias", "noticias", "noticia" -> NEWS
                "music", "música", "musica", "musik" -> MUSIC
                "documentary", "documentários", "documental", "documentário" -> DOCUMENTARY
                "entertainment", "entretenimento", "entretenimiento" -> ENTERTAINMENT
                "adult", "adultos", "xxx", "18+", "+18" -> ADULT
                "religion", "religião", "religión", "religious" -> RELIGION
                "cooking", "culinária", "cocina", "food" -> COOKING
                "business", "negócios", "negocios" -> BUSINESS
                else -> LIVE_TV
            }
        }
    }
}

data class Channel(
    val id: Int,
    val name: String,
    val url: String,
    val logo: String = "",
    val category: String = "General",
    val country: String = "",
    val language: String = "",
    val contentCategory: ContentCategory = ContentCategory.LIVE_TV,
    val tvgId: String = "",
    val isAdult: Boolean = false
)

data class PlaylistSource(
    val id: Int,
    val name: String,
    val url: String,
    val type: SourceType,
    val region: String = "Global",
    val description: String = "",
    val epgUrl: String = "",
    val contentCategory: ContentCategory = ContentCategory.LIVE_TV,
    val requiresAuth: Boolean = false,
    val isAdult: Boolean = false
)

enum class SourceType {
    LEGAL_TDT,      // 100% Legal, TDT/Abierta
    FAST,           // Free Ad-Supported TV
    IPTV_ORG,       // Comunidad iptv-org
    CUSTOM,         // URLs personalizadas
    PREMIUM         // Fuentes premium (si requieren auth)
}

object PlaylistProvider {
    
    // ==================== CAPA 1: Fuentes Legales ====================
    private val legalSources = listOf(
        PlaylistSource(
            id = 1,
            name = "TDT España (TDTChannels)",
            url = "https://raw.githubusercontent.com/LaQuay/TDTChannels/master/playlist.m3u8",
            type = SourceType.LEGAL_TDT,
            region = "España",
            description = "100% Legal - Canais TDT espanhóis",
            epgUrl = "https://raw.githubusercontent.com/LaQuay/TDTChannels/master/epg.xml",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 2,
            name = "M3U Portugal (Legal)",
            url = "https://m3upt.com/iptv",
            type = SourceType.LEGAL_TDT,
            region = "Portugal",
            description = "100% Legal - Transmissões públicas portuguesas",
            epgUrl = "https://m3upt.com/epg",
            contentCategory = ContentCategory.LIVE_TV
        )
    )
    
    // ==================== CAPA 2: Servicios FAST ====================
    private val fastSources = listOf(
        PlaylistSource(
            id = 10,
            name = "Pluto TV (Global)",
            url = "https://i.mjh.nz/PlutoTV/all.m3u8",
            type = SourceType.FAST,
            region = "Global",
            description = "Centenas de canais FAST - Legal com publicidade",
            epgUrl = "https://i.mjh.nz/PlutoTV/all.xml",
            contentCategory = ContentCategory.ENTERTAINMENT
        ),
        PlaylistSource(
            id = 11,
            name = "Pluto TV Brasil 🇧🇷",
            url = "https://i.mjh.nz/PlutoTV/br.m3u8",
            type = SourceType.FAST,
            region = "Brasil",
            description = "Pluto TV Brasil - Conteúdo em português",
            epgUrl = "https://i.mjh.nz/PlutoTV/br.xml",
            contentCategory = ContentCategory.ENTERTAINMENT
        ),
        PlaylistSource(
            id = 12,
            name = "Plex TV",
            url = "https://i.mjh.nz/Plex/all.m3u8",
            type = SourceType.FAST,
            region = "Global",
            description = "Plex - Filmes e séries gratuitas",
            epgUrl = "https://i.mjh.nz/Plex/all.xml",
            contentCategory = ContentCategory.MOVIES
        ),
        PlaylistSource(
            id = 13,
            name = "PBS (Estados Unidos)",
            url = "https://i.mjh.nz/PBS/all.m3u8",
            type = SourceType.FAST,
            region = "USA",
            description = "PBS - Televisão pública americana",
            epgUrl = "https://i.mjh.nz/PBS/all.xml",
            contentCategory = ContentCategory.NEWS
        ),
        PlaylistSource(
            id = 14,
            name = "Samsung TV Plus",
            url = "https://i.mjh.nz/SamsungTVPlus/all.m3u8",
            type = SourceType.FAST,
            region = "Global",
            description = "Samsung TV Plus - Canais FAST variados",
            contentCategory = ContentCategory.ENTERTAINMENT
        ),
        PlaylistSource(
            id = 15,
            name = "Stirr TV",
            url = "https://i.mjh.nz/Stirr/all.m3u8",
            type = SourceType.FAST,
            region = "USA",
            description = "Stirr - Notícias e entretenimento",
            epgUrl = "https://i.mjh.nz/Stirr/all.xml",
            contentCategory = ContentCategory.NEWS
        ),
        PlaylistSource(
            id = 16,
            name = "Roku Channel",
            url = "https://i.mjh.nz/Roku/all.m3u8",
            type = SourceType.FAST,
            region = "USA",
            description = "The Roku Channel - Streaming gratuito",
            epgUrl = "https://i.mjh.nz/Roku/all.xml",
            contentCategory = ContentCategory.MOVIES
        )
    )
    
    // ==================== CAPA 3: IPTV-ORG ====================
    private val iptvOrgSources = listOf(
        // === Por Idioma ===
        PlaylistSource(
            id = 20,
            name = "🇧🇷 IPTV-ORG: Português",
            url = "https://iptv-org.github.io/iptv/languages/por.m3u",
            type = SourceType.IPTV_ORG,
            region = "Português",
            description = "Todos os canais em português - Prioridade Brasil",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 21,
            name = "🇪🇸 IPTV-ORG: Español",
            url = "https://iptv-org.github.io/iptv/languages/spa.m3u",
            type = SourceType.IPTV_ORG,
            region = "Español",
            description = "Todos os canais em espanhol",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 22,
            name = "🇬🇧 IPTV-ORG: English",
            url = "https://iptv-org.github.io/iptv/languages/eng.m3u",
            type = SourceType.IPTV_ORG,
            region = "English",
            description = "Canais em inglês - Mundial",
            contentCategory = ContentCategory.LIVE_TV
        ),
        
        // === Por País ===
        PlaylistSource(
            id = 30,
            name = "🇧🇷 Brasil - TV ao Vivo",
            url = "https://iptv-org.github.io/iptv/countries/br.m3u",
            type = SourceType.IPTV_ORG,
            region = "Brasil",
            description = "Canais brasileiros - Globo, SBT, Record, Band e mais",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 31,
            name = "🇵🇹 Portugal",
            url = "https://iptv-org.github.io/iptv/countries/pt.m3u",
            type = SourceType.IPTV_ORG,
            region = "Portugal",
            description = "Canais portugueses - RTP, SIC, TVI",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 32,
            name = "🇪🇸 España",
            url = "https://iptv-org.github.io/iptv/countries/es.m3u",
            type = SourceType.IPTV_ORG,
            region = "España",
            description = "Canais da Espanha - TVE, Antena 3, Telecinco",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 33,
            name = "🇦🇷 Argentina",
            url = "https://iptv-org.github.io/iptv/countries/ar.m3u",
            type = SourceType.IPTV_ORG,
            region = "Argentina",
            description = "Canais argentinos - Telefe, Canal 13, América",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 34,
            name = "🇲🇽 México",
            url = "https://iptv-org.github.io/iptv/countries/mx.m3u",
            type = SourceType.IPTV_ORG,
            region = "México",
            description = "Canais mexicanos - Televisa, TV Azteca",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 35,
            name = "🇺🇸 Estados Unidos",
            url = "https://iptv-org.github.io/iptv/countries/us.m3u",
            type = SourceType.IPTV_ORG,
            region = "USA",
            description = "Canais americanos - ABC, CBS, NBC, FOX",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 36,
            name = "🇬🇧 Reino Unido",
            url = "https://iptv-org.github.io/iptv/countries/uk.m3u",
            type = SourceType.IPTV_ORG,
            region = "UK",
            description = "Canais britânicos - BBC, ITV, Channel 4",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 37,
            name = "🇫🇷 França",
            url = "https://iptv-org.github.io/iptv/countries/fr.m3u",
            type = SourceType.IPTV_ORG,
            region = "França",
            description = "Canais franceses - TF1, France 2, M6",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 38,
            name = "🇮🇹 Itália",
            url = "https://iptv-org.github.io/iptv/countries/it.m3u",
            type = SourceType.IPTV_ORG,
            region = "Itália",
            description = "Canais italianos - RAI, Mediaset",
            contentCategory = ContentCategory.LIVE_TV
        ),
        PlaylistSource(
            id = 39,
            name = "🇩🇪 Alemanha",
            url = "https://iptv-org.github.io/iptv/countries/de.m3u",
            type = SourceType.IPTV_ORG,
            region = "Alemanha",
            description = "Canais alemães - ARD, ZDF, RTL",
            contentCategory = ContentCategory.LIVE_TV
        ),
        
        // === Por Categoría ===
        PlaylistSource(
            id = 50,
            name = "🎬 Filmes 24/7",
            url = "https://iptv-org.github.io/iptv/categories/movies.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Canais dedicados a filmes - Maratonas",
            contentCategory = ContentCategory.MOVIES
        ),
        PlaylistSource(
            id = 51,
            name = "📺 Séries 24/7",
            url = "https://iptv-org.github.io/iptv/categories/series.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Canais de séries - Maratonas completas",
            contentCategory = ContentCategory.SERIES
        ),
        PlaylistSource(
            id = 52,
            name = "⚽ Esportes ao Vivo",
            url = "https://iptv-org.github.io/iptv/categories/sports.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Canais esportivos - Futebol, UFC, NBA, NFL",
            contentCategory = ContentCategory.SPORTS
        ),
        PlaylistSource(
            id = 53,
            name = "📰 Notícias 24h",
            url = "https://iptv-org.github.io/iptv/categories/news.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Canais de notícias - CNN, BBC, Al Jazeera",
            contentCategory = ContentCategory.NEWS
        ),
        PlaylistSource(
            id = 54,
            name = "👶 Infantil",
            url = "https://iptv-org.github.io/iptv/categories/kids.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Desenhos e conteúdo infantil",
            contentCategory = ContentCategory.KIDS
        ),
        PlaylistSource(
            id = 55,
            name = "🎵 Música",
            url = "https://iptv-org.github.io/iptv/categories/music.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Canais de música e clipes - MTV, VH1",
            contentCategory = ContentCategory.MUSIC
        ),
        PlaylistSource(
            id = 56,
            name = "🎭 Entretenimento",
            url = "https://iptv-org.github.io/iptv/categories/entertainment.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Variedades e entretenimento geral",
            contentCategory = ContentCategory.ENTERTAINMENT
        ),
        PlaylistSource(
            id = 57,
            name = "🎥 Documentários",
            url = "https://iptv-org.github.io/iptv/categories/documentary.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Documentários - Discovery, National Geographic",
            contentCategory = ContentCategory.DOCUMENTARY
        ),
        PlaylistSource(
            id = 58,
            name = "✝️ Religião",
            url = "https://iptv-org.github.io/iptv/categories/religious.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Canais religiosos - Católicos, Evangélicos",
            contentCategory = ContentCategory.RELIGION
        ),
        PlaylistSource(
            id = 59,
            name = "👨‍🍳 Culinária",
            url = "https://iptv-org.github.io/iptv/categories/cooking.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Programas de culinária e gastronomia",
            contentCategory = ContentCategory.COOKING
        ),
        PlaylistSource(
            id = 60,
            name = "💼 Negócios",
            url = "https://iptv-org.github.io/iptv/categories/business.m3u",
            type = SourceType.IPTV_ORG,
            region = "Global",
            description = "Bloomberg, CNBC, Fox Business",
            contentCategory = ContentCategory.BUSINESS
        )
    )
    
    // ==================== Funciones de Acceso ====================
    
    fun getAllSources(): List<PlaylistSource> {
        return legalSources + fastSources + iptvOrgSources
    }
    
    fun getSourcesByType(type: SourceType): List<PlaylistSource> {
        return getAllSources().filter { it.type == type }
    }
    
    fun getSourcesByCategory(category: ContentCategory): List<PlaylistSource> {
        return getAllSources().filter { it.contentCategory == category }
    }
    
    fun getSourcesByRegion(region: String): List<PlaylistSource> {
        return getAllSources().filter { 
            it.region.contains(region, ignoreCase = true) 
        }
    }
    
    fun getSourceById(id: Int): PlaylistSource? {
        return getAllSources().find { it.id == id }
    }
    
    fun getBrazilianSources(): List<PlaylistSource> {
        return getAllSources().filter { 
            it.region.contains("Brasil", ignoreCase = true) ||
            it.region.contains("Português", ignoreCase = true) ||
            it.name.contains("🇧🇷")
        }
    }
    
    fun getRecommendedSources(): List<PlaylistSource> {
        return listOf(
            getSourceById(30),  // Brasil TV
            getSourceById(11),  // Pluto TV Brasil
            getSourceById(50),  // Filmes
            getSourceById(52),  // Esportes
            getSourceById(53),  // Notícias
            getSourceById(54)   // Infantil
        ).filterNotNull()
    }
}

object ChannelProvider {
    
    /**
     * Canales demo para pruebas
     */
    fun getDemoChannels(): List<Channel> {
        return listOf(
            Channel(
                id = 1,
                name = "Big Buck Bunny (Demo HD)",
                url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
                category = "Demo",
                country = "Global",
                language = "eng",
                contentCategory = ContentCategory.MOVIES,
                logo = ""
            ),
            Channel(
                id = 2,
                name = "Test Stream HLS",
                url = "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8",
                category = "Demo",
                country = "Global",
                language = "eng",
                contentCategory = ContentCategory.LIVE_TV,
                logo = ""
            ),
            Channel(
                id = 3,
                name = "Sintel (Demo 4K)",
                url = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                category = "Demo",
                country = "Global",
                language = "eng",
                contentCategory = ContentCategory.MOVIES,
                logo = ""
            )
        )
    }
    
    fun getChannelById(id: Int): Channel? {
        return getDemoChannels().find { it.id == id }
    }
}
