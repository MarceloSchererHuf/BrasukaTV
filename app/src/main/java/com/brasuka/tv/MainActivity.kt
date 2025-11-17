package com.brasuka.tv

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.graphics.Color
import android.view.View
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sourceAdapter: PlaylistSourceAdapter
    private lateinit var favoritesButton: TextView
    private lateinit var searchButton: TextView
    private lateinit var tabLayout: TabLayout
    
    private var currentFilter: FilterType = FilterType.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.sourceRecyclerView)
        favoritesButton = findViewById(R.id.favoritesButton)
        searchButton = findViewById(R.id.searchButton)
        tabLayout = findViewById(R.id.tabLayout)
        
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        
        setupTabs()
        loadSources(FilterType.ALL)
        
        recyclerView.adapter = sourceAdapter
        
        // Configurar botones
        favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
        
        searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }
    
    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("🌎 Todas"))
        tabLayout.addTab(tabLayout.newTab().setText("🇧🇷 Brasil"))
        tabLayout.addTab(tabLayout.newTab().setText("🎬 Filmes"))
        tabLayout.addTab(tabLayout.newTab().setText("⚽ Esportes"))
        tabLayout.addTab(tabLayout.newTab().setText("📰 Notícias"))
        tabLayout.addTab(tabLayout.newTab().setText("👶 Infantil"))
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadSources(FilterType.ALL)
                    1 -> loadSources(FilterType.BRAZIL)
                    2 -> loadSources(FilterType.MOVIES)
                    3 -> loadSources(FilterType.SPORTS)
                    4 -> loadSources(FilterType.NEWS)
                    5 -> loadSources(FilterType.KIDS)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun loadSources(filter: FilterType) {
        currentFilter = filter
        val sources = when (filter) {
            FilterType.ALL -> PlaylistProvider.getAllSources()
            FilterType.BRAZIL -> PlaylistProvider.getBrazilianSources()
            FilterType.MOVIES -> PlaylistProvider.getSourcesByCategory(ContentCategory.MOVIES)
            FilterType.SPORTS -> PlaylistProvider.getSourcesByCategory(ContentCategory.SPORTS)
            FilterType.NEWS -> PlaylistProvider.getSourcesByCategory(ContentCategory.NEWS)
            FilterType.KIDS -> PlaylistProvider.getSourcesByCategory(ContentCategory.KIDS)
        }
        
        sourceAdapter = PlaylistSourceAdapter(sources) { source ->
            openPlaylistViewer(source)
        }
        recyclerView.adapter = sourceAdapter
    }

    private fun openPlaylistViewer(source: PlaylistSource) {
        val intent = Intent(this, PlaylistViewerActivity::class.java).apply {
            putExtra("SOURCE_ID", source.id)
            putExtra("SOURCE_NAME", source.name)
            putExtra("SOURCE_URL", source.url)
            putExtra("EPG_URL", source.epgUrl)
        }
        startActivity(intent)
    }
    
    enum class FilterType {
        ALL, BRAZIL, MOVIES, SPORTS, NEWS, KIDS
    }
}

class PlaylistSourceAdapter(
    private val sources: List<PlaylistSource>,
    private val onSourceClick: (PlaylistSource) -> Unit
) : RecyclerView.Adapter<PlaylistSourceAdapter.SourceViewHolder>() {

    class SourceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.sourceName)
        val regionTextView: TextView = view.findViewById(R.id.sourceRegion)
        val typeTextView: TextView = view.findViewById(R.id.sourceType)
        val descriptionTextView: TextView = view.findViewById(R.id.sourceDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_source, parent, false)
        return SourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val source = sources[position]
        holder.nameTextView.text = source.name
        holder.regionTextView.text = source.region
        holder.descriptionTextView.text = source.description
        
        // Colorear según el tipo de fuente
        val (typeText, typeColor) = when (source.type) {
            SourceType.LEGAL_TDT -> "LEGAL" to "#4CAF50"
            SourceType.FAST -> "FAST" to "#2196F3"
            SourceType.IPTV_ORG -> "COMUNIDADE" to "#FF9800"
            SourceType.CUSTOM -> "CUSTOM" to "#9C27B0"
            SourceType.PREMIUM -> "PREMIUM" to "#F44336"
        }
        holder.typeTextView.text = typeText
        holder.typeTextView.setTextColor(Color.parseColor(typeColor))
        
        holder.itemView.setOnClickListener {
            onSourceClick(source)
        }
        
        // Navegación con control remoto
        holder.itemView.isFocusable = true
        holder.itemView.isFocusableInTouchMode = true
        
        holder.itemView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.scaleX = 1.05f
                view.scaleY = 1.05f
                view.setBackgroundColor(Color.parseColor("#37474F"))
            } else {
                view.scaleX = 1.0f
                view.scaleY = 1.0f
                view.setBackgroundColor(Color.parseColor("#263238"))
            }
        }
    }

    override fun getItemCount() = sources.size
}
