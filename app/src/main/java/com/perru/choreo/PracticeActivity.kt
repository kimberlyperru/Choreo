package com.perru.choreo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PracticeActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvInstrument: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var tvHint: TextView
    private lateinit var tvCurrentNote: TextView
    private lateinit var progressSong: ProgressBar
    private lateinit var layoutPiano: LinearLayout
    private lateinit var btnSlow: Button
    private lateinit var btnMic: Button
    private lateinit var btnHint: Button
    private lateinit var seekTempo: SeekBar
    private lateinit var tvTempoValue: TextView

    private var currentNoteIndex = 0
    private var wrongAttempts = 0
    private var isMicMode = false
    private var isSlowMode = false
    private var songNotes = listOf<Note>()
    private val pianoKeys = mutableListOf<TextView>()

    private lateinit var pitchManager: PitchDetectionManager
    private val mainHandler = Handler(Looper.getMainLooper())

    companion object {
        private const val MIC_PERMISSION_CODE = 101
        private val KEY_NOTES = listOf("C4","D4","E4","F4","G4","A4","B4","C5")
        private val KEY_LABELS = listOf("C","D","E","F","G","A","B","C")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        bindViews()
        loadSongData()
        buildPianoKeys()
        setupTempoSlider()
        setupButtons()
        updateUI()
    }

    private fun bindViews() {
        tvTitle        = findViewById(R.id.tvPracticeTitle)
        tvInstrument   = findViewById(R.id.tvPracticeInstrument)
        tvFeedback     = findViewById(R.id.tvFeedback)
        tvHint         = findViewById(R.id.tvHint)
        tvCurrentNote  = findViewById(R.id.tvCurrentNote)
        progressSong   = findViewById(R.id.progressSong)
        layoutPiano    = findViewById(R.id.layoutPiano)
        btnSlow        = findViewById(R.id.btnSlow)
        btnMic         = findViewById(R.id.btnMic)
        btnHint        = findViewById(R.id.btnHint)
        seekTempo      = findViewById(R.id.seekTempo)
        tvTempoValue   = findViewById(R.id.tvTempoValue)
        findViewById<View>(R.id.btnPracticeBack).setOnClickListener { finish() }
    }

    private fun loadSongData() {
        val title      = intent.getStringExtra("song_title") ?: "Amazing Grace"
        val instrument = intent.getStringExtra("song_instrument") ?: "Piano"
        tvTitle.text      = title
        tvInstrument.text = instrument
        songNotes = SongNoteRepository.getNotesForSong(title)
    }

    private fun buildPianoKeys() {
        layoutPiano.removeAllViews()
        pianoKeys.clear()

        KEY_NOTES.forEachIndexed { index, noteName ->
            val key = TextView(this).apply {
                text = KEY_LABELS[index]
                textSize = 10f
                gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
                setPadding(0, 0, 0, 6)
                setTextColor(ContextCompat.getColor(this@PracticeActivity, R.color.indigo_dark))
                setBackgroundColor(android.graphics.Color.WHITE)
                setOnClickListener { onNoteInput(noteName) }
            }

            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.setMargins(2, 2, 2, 2)
            key.layoutParams = params

            layoutPiano.addView(key)
            pianoKeys.add(key)
        }
    }

    private fun setupTempoSlider() {
        seekTempo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val bpm = 30 + progress  // range: 30–130 BPM
                tvTempoValue.text = "$bpm BPM"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun setupButtons() {
        btnSlow.setOnClickListener {
            isSlowMode = !isSlowMode
            btnSlow.backgroundTintList = ContextCompat.getColorStateList(
                this,
                if (isSlowMode) R.color.indigo_mid else android.R.color.transparent
            )
            Toast.makeText(this, if (isSlowMode) "Slow mode on" else "Slow mode off", Toast.LENGTH_SHORT).show()
        }

        btnMic.setOnClickListener {
            if (isMicMode) {
                stopMic()
            } else {
                requestMicPermission()
            }
        }

        btnHint.setOnClickListener { showHint() }
    }

    // ── Core note mechanic ──────────────────────────────────────────────

    private fun onNoteInput(playedNote: String) {
        val expected = songNotes[currentNoteIndex].name

        if (playedNote == expected) {
            onCorrectNote()
        } else {
            onWrongNote(expected)
        }
    }

    private fun onCorrectNote() {
        wrongAttempts = 0
        tvHint.visibility = View.GONE
        highlightKey(currentNoteIndex % KEY_NOTES.size, correct = true)

        mainHandler.postDelayed({
            currentNoteIndex++
            if (currentNoteIndex >= songNotes.size) {
                onSongComplete()
            } else {
                updateUI()
            }
        }, 300)
    }

    private fun onWrongNote(expected: String) {
        wrongAttempts++
        tvFeedback.text = "❌ Wrong note — expected: ${songNotes[currentNoteIndex].displayName}"
        tvFeedback.setBackgroundResource(R.drawable.bg_instrument_item)

        val keyIndex = KEY_NOTES.indexOf(expected)
        if (keyIndex >= 0) {
            pianoKeys[keyIndex].setBackgroundColor(
                ContextCompat.getColor(this, R.color.indigo_light)
            )
            mainHandler.postDelayed({
                pianoKeys[keyIndex].setBackgroundColor(android.graphics.Color.WHITE)
            }, 400)
        }

        if (wrongAttempts >= 3) showHint()
    }

    private fun updateUI() {
        val note = songNotes[currentNoteIndex]
        tvCurrentNote.text = "Next: ${note.displayName}"
        tvFeedback.text    = "🎵 Play: ${note.displayName}"

        // Update progress bar
        val progress = (currentNoteIndex.toFloat() / songNotes.size * 100).toInt()
        progressSong.progress = progress

        // Highlight the expected key
        val keyIndex = KEY_NOTES.indexOf(note.name)
        pianoKeys.forEachIndexed { i, key ->
            key.setBackgroundColor(
                if (i == keyIndex)
                    ContextCompat.getColor(this, R.color.indigo_light)
                else
                    android.graphics.Color.WHITE
            )
        }
    }

    private fun highlightKey(index: Int, correct: Boolean) {
        if (index < pianoKeys.size) {
            pianoKeys[index].setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    if (correct) R.color.indigo_mid else android.R.color.holo_red_light
                )
            )
        }
    }

    private fun showHint() {
        tvHint.visibility = View.VISIBLE
        val note = songNotes[currentNoteIndex]
        tvHint.text = "💡 Hint: press the ${note.displayName} key"
    }

    private fun onSongComplete() {
        tvFeedback.text = "🎉 Song complete! Great job!"
        tvCurrentNote.text = "Done!"
        progressSong.progress = 100
        pianoKeys.forEach {
            it.setBackgroundColor(ContextCompat.getColor(this, R.color.indigo_mid))
        }
    }

    // ── Mic / pitch detection ───────────────────────────────────────────

    private fun requestMicPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED) {
            startMic()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                MIC_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MIC_PERMISSION_CODE &&
            grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            startMic()
        } else {
            Toast.makeText(this, "Microphone permission needed for mic mode", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMic() {
        isMicMode = true
        btnMic.text = "🔴 Stop mic"
        pitchManager = PitchDetectionManager { detectedNote ->
            mainHandler.post { onNoteInput(detectedNote) }
        }
        pitchManager.start()
        Toast.makeText(this, "Listening… play your instrument!", Toast.LENGTH_SHORT).show()
    }

    private fun stopMic() {
        isMicMode = false
        btnMic.text = "🎙 Mic"
        pitchManager.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isMicMode) pitchManager.stop()
    }
}