package com.brasuka.tv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.net.URL
import android.view.LayoutInflater
import android.view.ViewGroup
import android.graphics.Color

/**
 * Actividad para visualizar y cargar canales desde una fuente M3U/M3U8
 */
class PlaylistViewerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var statusTextView: TextView
    private lateinit var sourceNameTextView: TextView
    
    private var sourceId: Int = 0
    private var sourceName: String = ""
    private var sourceUrl: String = ""
    private var epgUrl: String = ""
    
    private val channels = mutableListOf<Channel>()
    private lateinit var channelAdapter: ChannelListAdapter
    
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_viewer)

        recyclerView = findViewById(R.id.channelRecyclerView)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        statusTextView = findViewById(R.id.statusTextView)
        sourceNameTextView = findViewById(R.id.sourceNameTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        channelAdapter = ChannelListAdapter(channels) { channel ->
            openPlayer(channel)
        }
        recyclerView.adapter = channelAdapter

        sourceId = intent.getIntExtra("SOURCE_ID", 0)
        sourceName = intent.getStringExtra("SOURCE_NAME") ?: ""
        sourceUrl = intent.getStringExtra("SOURCE_URL") ?: ""
        epgUrl = intent.getStringExtra("EPG_URL") ?: ""

        sourceNameTextView.text = sourceName
        
        loadPlaylist()
    }

    private fun loadPlaylist() {
        loadingIndicator.visibility = View.VISIBLE
        statusTextView.text = "Descargando playlist desde repositorio..."
        
        scope.launch {
            try {
                val parsedChannels = withContext(Dispatchers.IO) {
                    parseM3U(sourceUrl)
                }
                
                channels.clear()
                channels.addAll(parsedChannels)
                channelAdapter.notifyDataSetChanged()
                
                loadingIndicator.visibility = View.GONE
                statusTextView.text = "${channels.size} canales cargados"
                
                if (channels.isEmpty()) {
                    Toast.makeText(
                        this@PlaylistViewerActivity,
                        "No se encontraron canales en esta lista",
                        Toast.LENGTH_LONG
                    ).show()
                }
                
            } catch (e: Exception) {
                loadingIndicator.visibility = View.GONE
                statusTextView.text = "Error al cargar: ${e.message}"
                Toast.makeText(
                    this@PlaylistViewerActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun parseM3U(url: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        var channelId = 1
        
        try {
            val content = URL(url).readText()
            val lines = content.lines()
            
            var currentName = ""
            var currentGroup = ""
            
            for (i in lines.indices) {
                val line = lines[i].trim()
                
                if (line.startsWith("#EXTINF:")) {
                    // Extraer nombre del canal
                    currentName = line.substringAfterLast(",").trim()
                    
                    // Extraer grupo/categoría si existe
                    val groupMatch = """group-title="([^"]+)"""".toRegex().find(line)
                    currentGroup = groupMatch?.groupValues?.get(1) ?: "General"
                    
                } else if (line.isNotEmpty() && !line.startsWith("#")) {
                    // Esta línea es la URL del stream
                    if (currentName.isNotEmpty()) {
                        channels.add(
                            Channel(
                                id = channelId++,
                                name = currentName,
                                url = line,
                                category = currentGroup
                            )
                        )
                        currentName = ""
                    }
                }
            }
        } catch (e: Exception) {
            throw Exception("Error al parsear M3U: ${e.message}")
        }
        
        return channels
    }

    private fun openPlayer(channel: Channel) {
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra("CHANNEL_ID", channel.id)
            putExtra("CHANNEL_NAME", channel.name)
            putExtra("CHANNEL_URL", channel.url)
            putExtra("ALL_CHANNELS", ArrayList(channels.map { it.id }))
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}

class ChannelListAdapter(
    private val channels: List<Channel>,
    private val onChannelClick: (Channel) -> Unit
) : RecyclerView.Adapter<ChannelListAdapter.ChannelViewHolder>() {

    class ChannelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.channelName)
        val categoryTextView: TextView = view.findViewById(R.id.channelCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel = channels[position]
        holder.nameTextView.text = channel.name
        holder.categoryTextView.text = channel.category
        
        holder.itemView.setOnClickListener {
            onChannelClick(channel)
        }
        
        holder.itemView.isFocusable = true
        holder.itemView.isFocusableInTouchMode = true
        
        holder.itemView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.scaleX = 1.05f
                view.scaleY = 1.05f
                view.setBackgroundColor(Color.parseColor("#2196F3"))
            } else {
                view.scaleX = 1.0f
                view.scaleY = 1.0f
                view.setBackgroundColor(Color.parseColor("#424242"))
            }
        }
    }

    override fun getItemCount() = channels.size
}
