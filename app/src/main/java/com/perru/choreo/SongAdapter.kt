package com.perru.choreo


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private var songs: List<Song>,
    private val onClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmoji: TextView = view.findViewById(R.id.tvSongEmoji)
        val tvTitle: TextView = view.findViewById(R.id.tvSongTitle)
        val tvComposer: TextView = view.findViewById(R.id.tvComposer)
        val tvDifficulty: TextView = view.findViewById(R.id.tvDifficulty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.tvEmoji.text = song.emoji
        holder.tvTitle.text = song.title
        holder.tvComposer.text = song.composer
        holder.tvDifficulty.text = song.difficulty
        holder.itemView.setOnClickListener { onClick(song) }
    }

    override fun getItemCount() = songs.size

    fun updateList(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }
}