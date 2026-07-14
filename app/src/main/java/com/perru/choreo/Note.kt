package com.perru.choreo

data class Note(
    val name: String,       // e.g. "C4", "E4", "G4"
    val frequency: Float,   // Hz value for pitch detection
    val displayName: String // e.g. "C (Middle)"
)