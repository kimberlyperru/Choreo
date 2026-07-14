package com.perru.choreo



data class Song(
    val id: Int,
    val title: String,
    val composer: String,
    val instruments: List<String>,
    val difficulty: String,
    val emoji: String,
    val isFeatured: Boolean = false
)