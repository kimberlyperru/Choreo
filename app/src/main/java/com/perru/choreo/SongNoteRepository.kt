package com.perru.choreo


object SongNoteRepository {

    private val amazingGrace = listOf(
        Note("C4", 261.63f, "C"),
        Note("F4", 349.23f, "F"),
        Note("A4", 440.00f, "A"),
        Note("F4", 349.23f, "F"),
        Note("A4", 440.00f, "A"),
        Note("C5", 523.25f, "C"),
        Note("A4", 440.00f, "A"),
        Note("F4", 349.23f, "F"),
        Note("A4", 440.00f, "A"),
        Note("G4", 392.00f, "G"),
        Note("E4", 329.63f, "E"),
        Note("C4", 261.63f, "C"),
        Note("E4", 329.63f, "E"),
        Note("G4", 392.00f, "G")
    )

    private val odeToJoy = listOf(
        Note("E4", 329.63f, "E"),
        Note("E4", 329.63f, "E"),
        Note("F4", 349.23f, "F"),
        Note("G4", 392.00f, "G"),
        Note("G4", 392.00f, "G"),
        Note("F4", 349.23f, "F"),
        Note("E4", 329.63f, "E"),
        Note("D4", 293.66f, "D"),
        Note("C4", 261.63f, "C"),
        Note("C4", 261.63f, "C"),
        Note("D4", 293.66f, "D"),
        Note("E4", 329.63f, "E"),
        Note("E4", 329.63f, "E"),
        Note("D4", 293.66f, "D")
    )

    private val twinkleTwinkle = listOf(
        Note("C4", 261.63f, "C"),
        Note("C4", 261.63f, "C"),
        Note("G4", 392.00f, "G"),
        Note("G4", 392.00f, "G"),
        Note("A4", 440.00f, "A"),
        Note("A4", 440.00f, "A"),
        Note("G4", 392.00f, "G"),
        Note("F4", 349.23f, "F"),
        Note("F4", 349.23f, "F"),
        Note("E4", 329.63f, "E"),
        Note("E4", 329.63f, "E"),
        Note("D4", 293.66f, "D"),
        Note("D4", 293.66f, "D"),
        Note("C4", 261.63f, "C")
    )

    fun getNotesForSong(songTitle: String): List<Note> {
        return when (songTitle.lowercase()) {
            "amazing grace"   -> amazingGrace
            "ode to joy"      -> odeToJoy
            "twinkle twinkle" -> twinkleTwinkle
            else              -> amazingGrace // default fallback
        }
    }
}