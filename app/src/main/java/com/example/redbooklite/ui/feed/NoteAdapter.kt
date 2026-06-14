package com.example.redbooklite.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.redbooklite.R
import com.example.redbooklite.model.Note
import com.example.redbooklite.util.AvatarHelper
import com.example.redbooklite.util.CoverLayoutHelper
import com.example.redbooklite.util.ImageLoader

class NoteAdapter(
    private val listener: OnNoteClickListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnNoteClickListener {
        fun onNoteClick(note: Note)

        fun onAuthorClick(note: Note)
    }

    private val items = ArrayList<Note>()

    fun submitList(notes: List<Note>) {
        items.clear()
        items.addAll(notes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note_card, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCover: ImageView = itemView.findViewById(R.id.ivCover)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)

        init {
            ivAvatar.outlineProvider = ViewOutlineProvider.BACKGROUND
            ivAvatar.clipToOutline = true
        }

        fun bind(note: Note) {
            CoverLayoutHelper.applyStaggeredCoverHeight(ivCover, note.coverAspectRatio)
            ImageLoader.loadCover(ivCover, note.coverPath)

            if (note.authorAvatarUrl.isNotBlank()) {
                ImageLoader.loadCover(ivAvatar, note.authorAvatarUrl)
            } else {
                ivAvatar.setImageResource(AvatarHelper.getAvatarResId(note.id))
            }

            tvTitle.text = note.title
            tvAuthor.text = note.authorName
            tvLikeCount.text = note.likeCount.toString()

            itemView.setOnClickListener {
                listener.onNoteClick(note)
            }
            ivAvatar.setOnClickListener {
                listener.onAuthorClick(note)
            }
            tvAuthor.setOnClickListener {
                listener.onAuthorClick(note)
            }
        }
    }
}
