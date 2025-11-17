package com.brasuka.tv

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Gestor de favoritos sin necesidad de login
 * Usa SharedPreferences local del dispositivo
 */
object FavoritesManager {
    
    private const val PREFS_NAME = "brasuka_favorites"
    private const val KEY_FAVORITES = "favorites_list"
    
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun addFavorite(context: Context, channel: Channel) {
        val favorites = getFavorites(context).toMutableList()
        if (!favorites.any { it.id == channel.id }) {
            favorites.add(channel)
            saveFavorites(context, favorites)
        }
    }
    
    fun removeFavorite(context: Context, channelId: Int) {
        val favorites = getFavorites(context).toMutableList()
        favorites.removeAll { it.id == channelId }
        saveFavorites(context, favorites)
    }
    
    fun isFavorite(context: Context, channelId: Int): Boolean {
        return getFavorites(context).any { it.id == channelId }
    }
    
    fun getFavorites(context: Context): List<Channel> {
        val prefs = getPrefs(context)
        val json = prefs.getString(KEY_FAVORITES, null) ?: return emptyList()
        
        return try {
            val type = object : TypeToken<List<Channel>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    private fun saveFavorites(context: Context, favorites: List<Channel>) {
        val prefs = getPrefs(context)
        val json = Gson().toJson(favorites)
        prefs.edit().putString(KEY_FAVORITES, json).apply()
    }
    
    fun toggleFavorite(context: Context, channel: Channel): Boolean {
        return if (isFavorite(context, channel.id)) {
            removeFavorite(context, channel.id)
            false
        } else {
            addFavorite(context, channel)
            true
        }
    }
}
