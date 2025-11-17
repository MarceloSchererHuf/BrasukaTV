package com.brasuka.tv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.*

/**
 * Actividad de búsqueda global de canales
 */
class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var emptyTextView: TextView
    
    private val allChannels = mutableListOf<Channel>()
    private lateinit var channelAdapter: ChannelListAdapter
    
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.searchRecyclerView)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        emptyTextView = findViewById(R.id.emptyTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        channelAdapter = ChannelListAdapter(listOf()) { channel ->
            openPlayer(channel)
        }
        recyclerView.adapter = channelAdapter

        // Búsqueda en tiempo real
        searchEditText.addTextChangedListener { text ->
            performSearch(text.toString())
        }
        
        // Mostrar canales demo inicialmente
        allChannels.addAll(ChannelProvider.getDemoChannels())
        updateResults(allChannels)
    }

    private fun performSearch(query: String) {
        if (query.length < 2) {
            updateResults(emptyList())
            return
        }
        
        val filtered = allChannels.filter { channel ->
            channel.name.contains(query, ignoreCase = true) ||
            channel.category.contains(query, ignoreCase = true) ||
            channel.country.contains(query, ignoreCase = true)
        }
        
        updateResults(filtered)
    }

    private fun updateResults(channels: List<Channel>) {
        if (channels.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
            
            channelAdapter = ChannelListAdapter(channels) { channel ->
                openPlayer(channel)
            }
            recyclerView.adapter = channelAdapter
        }
    }

    private fun openPlayer(channel: Channel) {
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra("CHANNEL_ID", channel.id)
            putExtra("CHANNEL_NAME", channel.name)
            putExtra("CHANNEL_URL", channel.url)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
