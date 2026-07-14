package com.perru.choreo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PractiseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practise)

        val songTitle = intent.getStringExtra("SONG_TITLE") ?: "Song"
        findViewById<TextView>(R.id.tvSongInfo).text = "Now playing: $songTitle"

        findViewById<Button>(R.id.btnStop).setOnClickListener {
            finish()
        }
    }
}