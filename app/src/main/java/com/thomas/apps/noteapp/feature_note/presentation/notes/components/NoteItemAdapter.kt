package com.thomas.apps.noteapp.feature_note.presentation.notes.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thomas.apps.noteapp.databinding.ItemNoteBinding
import com.thomas.apps.noteapp.feature_note.domain.model.Note

class NoteItemAdapter : ListAdapter<Note, NoteItemAdapter.ViewHolder>(NoteDC()) {

    var deleteClickListener: DeleteClickListener? = null
    var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deleteClickListener = deleteClickListener
        holder.itemClickListener = itemClickListener
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var deleteClickListener: DeleteClickListener? = null
        var itemClickListener: ItemClickListener? = null

        fun bind(item: Note) {
            with(binding) {
                textViewTitle.text = item.title
                textViewContent.text = item.content

                imageViewDelete.setOnClickListener {
                    deleteClickListener?.onDelete(item)
                }

                root.setOnClickListener { itemClickListener?.onClick(item) }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class NoteDC : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    interface DeleteClickListener {
        fun onDelete(note: Note)
    }

    interface ItemClickListener {
        fun onClick(note: Note)
    }
}