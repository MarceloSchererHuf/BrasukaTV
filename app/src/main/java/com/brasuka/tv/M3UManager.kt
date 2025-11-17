package com.brasuka.tv

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

/**
 * Gestor de listas M3U remotas y locales
 * Combina funcionalidades de ambas apps de referencia
 */
object M3UManager {
    
    /**
     * Carga y parsea una lista M3U desde una URL
     */
    suspend fun loadM3UFromUrl(url: String): List<Channel> = withContext(Dispatchers.IO) {
        try {
            val content = URL(url).readText()
            parseM3U(content)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Parsea contenido M3U y extrae canales
     */
    fun parseM3U(content: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = content.lines()
        
        var channelId = 1
        var currentName = ""
        var currentLogo = ""
        var currentGroup = ""
        var currentTvgId = ""
        
        for (i in lines.indices) {
            val line = lines[i].trim()
            
            if (line.startsWith("#EXTINF:")) {
                // Extraer nombre del canal
                currentName = line.substringAfterLast(",").trim()
                
                // Extraer logo
                val logoMatch = """tvg-logo="([^"]+)"""".toRegex().find(line)
                currentLogo = logoMatch?.groupValues?.get(1) ?: ""
                
                // Extraer grupo/categoría
                val groupMatch = """group-title="([^"]+)"""".toRegex().find(line)
                currentGroup = groupMatch?.groupValues?.get(1) ?: "General"
                
                // Extraer tvg-id
                val idMatch = """tvg-id="([^"]+)"""".toRegex().find(line)
                currentTvgId = idMatch?.groupValues?.get(1) ?: ""
                
            } else if (line.isNotEmpty() && !line.startsWith("#")) {
                // Esta línea es la URL del stream
                if (currentName.isNotEmpty()) {
                    channels.add(
                        Channel(
                            id = channelId++,
                            name = currentName,
                            url = line,
                            logo = currentLogo,
                            category = currentGroup,
                            contentCategory = ContentCategory.fromString(currentGroup)
                        )
                    )
                    currentName = ""
                    currentLogo = ""
                }
            }
        }
        
        return channels
    }
    
    /**
     * Carga lista desde JSON (formato alternativo)
     */
    fun parseChannelsFromJSON(jsonString: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        
        try {
            val jsonArray = JSONArray(jsonString)
            
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                
                channels.add(
                    Channel(
                        id = obj.optInt("id", i + 1),
                        name = obj.optString("name", "Canal $i"),
                        url = obj.optString("url", ""),
                        logo = obj.optString("logo", ""),
                        category = obj.optString("category", "General"),
                        country = obj.optString("country", ""),
                        language = obj.optString("language", "")
                    )
                )
            }
        } catch (e: Exception) {
            // Formato inválido
        }
        
        return channels
    }
    
    /**
     * Guarda canales personalizados localmente
     */
    fun saveCustomChannels(context: Context, channels: List<Channel>) {
        val prefs = context.getSharedPreferences("custom_channels", Context.MODE_PRIVATE)
        val json = com.google.gson.Gson().toJson(channels)
        prefs.edit().putString("channels", json).apply()
    }
    
    /**
     * Carga canales personalizados guardados
     */
    fun loadCustomChannels(context: Context): List<Channel> {
        val prefs = context.getSharedPreferences("custom_channels", Context.MODE_PRIVATE)
        val json = prefs.getString("channels", null) ?: return emptyList()
        
        return try {
            val type = object : com.google.gson.reflect.TypeToken<List<Channel>>() {}.type
            com.google.gson.Gson().fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

/**
 * Gestor de EPG (Electronic Program Guide)
 */
object EPGManager {
    
    data class EPGProgram(
        val channelId: String,
        val title: String,
        val description: String,
        val startTime: Long,
        val endTime: Long,
        val category: String
    )
    
    /**
     * Carga EPG desde URL XML
     */
    suspend fun loadEPGFromUrl(url: String): Map<String, List<EPGProgram>> = withContext(Dispatchers.IO) {
        try {
            val content = URL(url).readText()
            parseEPGXML(content)
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    /**
     * Parsea XML de EPG (formato XMLTV)
     */
    fun parseEPGXML(xmlContent: String): Map<String, List<EPGProgram>> {
        val programs = mutableMapOf<String, MutableList<EPGProgram>>()
        
        // Parseo básico de XML (en producción usar un parser XML real)
        val programPattern = """<programme[^>]*channel="([^"]+)"[^>]*start="([^"]+)"[^>]*stop="([^"]+)"[^>]*>.*?<title[^>]*>([^<]+)</title>.*?</programme>""".toRegex(RegexOption.DOT_MATCHES_ALL)
        
        programPattern.findAll(xmlContent).forEach { match ->
            val channelId = match.groupValues[1]
            val startTime = match.groupValues[2]
            val endTime = match.groupValues[3]
            val title = match.groupValues[4]
            
            val program = EPGProgram(
                channelId = channelId,
                title = title,
                description = "",
                startTime = parseXMLTVTime(startTime),
                endTime = parseXMLTVTime(endTime),
                category = ""
            )
            
            programs.getOrPut(channelId) { mutableListOf() }.add(program)
        }
        
        return programs
    }
    
    private fun parseXMLTVTime(xmltvTime: String): Long {
        // Formato: 20231115120000 +0000
        return try {
            val timeStr = xmltvTime.take(14)
            val year = timeStr.substring(0, 4).toInt()
            val month = timeStr.substring(4, 6).toInt()
            val day = timeStr.substring(6, 8).toInt()
            val hour = timeStr.substring(8, 10).toInt()
            val minute = timeStr.substring(10, 12).toInt()
            val second = timeStr.substring(12, 14).toInt()
            
            java.util.Calendar.getInstance().apply {
                set(year, month - 1, day, hour, minute, second)
            }.timeInMillis
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}
