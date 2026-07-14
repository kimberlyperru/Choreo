package com.perru.choreo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class InstrumentItem(
    val name: String,
    val emoji: String,
    val subtitle: String
)

class InstrumentAdapter(
    private val items: List<InstrumentItem>,
    private val onClick: (InstrumentItem) -> Unit
) : RecyclerView.Adapter<InstrumentAdapter.InstrumentViewHolder>() {

    inner class InstrumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmoji: TextView = view.findViewById(R.id.tvInstrumentEmoji)
        val tvName: TextView = view.findViewById(R.id.tvInstrumentName)
        val tvSub: TextView = view.findViewById(R.id.tvInstrumentSub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstrumentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_instrument, parent, false)
        return InstrumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstrumentViewHolder, position: Int) {
        val item = items[position]
        holder.tvEmoji.text = item.emoji
        holder.tvName.text = item.name
        holder.tvSub.text = item.subtitle
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}