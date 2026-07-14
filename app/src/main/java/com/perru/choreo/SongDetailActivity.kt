package com.perru.choreo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SongDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_detail)

        val songTitle = intent.getStringExtra("song_title") ?: "Unknown"
        val songComposer = intent.getStringExtra("song_composer") ?: ""
        val songEmoji = intent.getStringExtra("song_emoji") ?: "🎵"
        val songDifficulty = intent.getStringExtra("song_difficulty") ?: "Beginner"
        val songInstruments = intent.getStringArrayListExtra("song_instruments") ?: arrayListOf()

        findViewById<TextView>(R.id.tvDetailEmoji).text = songEmoji
        findViewById<TextView>(R.id.tvDetailTitle).text = songTitle
        findViewById<TextView>(R.id.tvDetailComposer).text = songComposer
        findViewById<TextView>(R.id.tvDetailDifficulty).text = songDifficulty

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

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
            val intent = Intent(this, PracticeActivity::class.java).apply {
                putExtra("song_title", songTitle)
                putExtra("song_instrument", instrument.name)
            }
            startActivity(intent)
        }
    }
}