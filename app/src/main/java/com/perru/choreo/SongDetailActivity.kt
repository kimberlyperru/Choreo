package com.perru.choreo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SongDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_detail)

        // Retrieve song passed from MainActivity
        val songId = intent.getIntExtra("song_id", 1)
        val songTitle = intent.getStringExtra("song_title") ?: "Unknown"
        val songComposer = intent.getStringExtra("song_composer") ?: ""
        val songEmoji = intent.getStringExtra("song_emoji") ?: "🎵"
        val songDifficulty = intent.getStringExtra("song_difficulty") ?: "Beginner"
        val songInstruments = intent.getStringArrayListExtra("song_instruments") ?: arrayListOf()

        // Bind song info
        findViewById<TextView>(R.id.tvDetailEmoji).text = songEmoji
        findViewById<TextView>(R.id.tvDetailTitle).text = songTitle
        findViewById<TextView>(R.id.tvDetailComposer).text = songComposer
        findViewById<TextView>(R.id.tvDetailDifficulty).text = songDifficulty

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        // Build instrument list from the song's available instruments
        val instrumentMap = mapOf(
            "Piano"  to InstrumentItem("Piano",  "🎹", "Tap keys or use microphone"),
            "Guitar" to InstrumentItem("Guitar", "🎸", "Strum chords or use microphone"),
            "Violin" to InstrumentItem("Violin", "🎻", "Play along with microphone"),
            "Drums"  to InstrumentItem("Drums",  "🥁", "Tap along on screen")
        )

        val items = songInstruments.mapNotNull { instrumentMap[it] }

        val rvInstruments = findViewById<RecyclerView>(R.id.rvInstruments)
        rvInstruments.layoutManager = LinearLayoutManager(this)
        rvInstruments.adapter = InstrumentAdapter(items) { instrument ->
            Toast.makeText(this, "Loading ${instrument.name} for $songTitle…", Toast.LENGTH_SHORT).show()
            // TODO: navigate to PracticeActivity
        }
    }
}