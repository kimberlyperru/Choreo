package com.perru.choreo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var rvFeatured: RecyclerView
    private lateinit var rvSongs: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private lateinit var featuredAdapter: SongAdapter

    private val allSongs = listOf(
        Song(1, "Amazing Grace", "John Newton", listOf("Piano", "Guitar"), "Beginner", "🎵", true),
        Song(2, "Ode to Joy", "Beethoven", listOf("Piano", "Violin"), "Beginner", "🎶", true),
        Song(3, "Canon in D", "Pachelbel", listOf("Piano", "Violin", "Guitar"), "Intermediate", "🎼", true),
        Song(4, "Fur Elise", "Beethoven", listOf("Piano"), "Intermediate", "🎹"),
        Song(5, "Greensleeves", "Traditional", listOf("Guitar", "Violin"), "Beginner", "🎸"),
        Song(6, "Twinkle Twinkle", "Traditional", listOf("Piano", "Violin"), "Beginner", "⭐"),
        Song(7, "Moonlight Sonata", "Beethoven", listOf("Piano"), "Advanced", "🌙"),
        Song(8, "Ave Maria", "Schubert", listOf("Piano", "Violin"), "Intermediate", "🎻"),
        Song(9, "The Entertainer", "Scott Joplin", listOf("Piano"), "Intermediate", "🎩"),
        Song(10, "Scarborough Fair", "Traditional", listOf("Guitar"), "Beginner", "🌿")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setGreeting()
        setupFeatured()
        setupSongList()
        setupSearch()
    }

    private fun setGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Good morning 👋"
            hour < 17 -> "Good afternoon 👋"
            else      -> "Good evening 👋"
        }
        findViewById<TextView>(R.id.tvGreeting).text = greeting
    }

    private fun openSong(song: Song) {
        val intent = Intent(this, SongDetailActivity::class.java).apply {
            putExtra("song_id", song.id)
            putExtra("song_title", song.title)
            putExtra("song_composer", song.composer)
            putExtra("song_emoji", song.emoji)
            putExtra("song_difficulty", song.difficulty)
            putStringArrayListExtra("song_instruments", ArrayList(song.instruments))
        }
        startActivity(intent)
    }

    private fun setupFeatured() {
        rvFeatured = findViewById(R.id.rvFeatured)
        val featured = allSongs.filter { it.isFeatured }
        featuredAdapter = SongAdapter(featured) { song -> openSong(song) }
        rvFeatured.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFeatured.adapter = featuredAdapter
    }

    private fun setupSongList() {
        rvSongs = findViewById(R.id.rvSongs)
        songAdapter = SongAdapter(allSongs) { song -> openSong(song) }
        rvSongs.layoutManager = LinearLayoutManager(this)
        rvSongs.adapter = songAdapter
    }

    private fun setupSearch() {
        etSearch = findViewById(R.id.etSearch)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                val filtered = allSongs.filter {
                    it.title.lowercase().contains(query) ||
                            it.composer.lowercase().contains(query)
                }
                songAdapter.updateList(filtered)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}