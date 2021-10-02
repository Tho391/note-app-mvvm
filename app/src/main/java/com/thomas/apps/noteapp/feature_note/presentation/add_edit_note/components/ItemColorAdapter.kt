package com.thomas.apps.noteapp.feature_note.presentation.add_edit_note.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thomas.apps.noteapp.R
import com.thomas.apps.noteapp.databinding.ItemColorBinding

class ItemColorAdapter :
    ListAdapter<ItemColorAdapter.ItemColor, ItemColorAdapter.ViewHolder>(ItemColorDC()) {

    var colorSelectListener: ColorSelectListener? = null

    fun selectColor(value: Int) {
        val newList = currentList.map { ItemColor(it.value, it.value == value) }
        submitList(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.colorSelectListener = colorSelectListener
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(private val binding: ItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var colorSelectListener: ColorSelectListener? = null

        fun bind(item: ItemColor) {
            binding.imageViewColor.setBackgroundColor(item.value)
            val context = binding.root.context
            val dp1 = context.resources.getDimension(R.dimen.stroke_small)

            binding.imageViewColor.strokeWidth = if (item.isSelected) {
                dp1
            } else
                0f
            binding.imageViewColor.setOnClickListener { colorSelectListener?.onSelect(item) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemColorBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class ItemColorDC : DiffUtil.ItemCallback<ItemColor>() {
        override fun areItemsTheSame(oldItem: ItemColor, newItem: ItemColor): Boolean {
            return oldItem.value == newItem.value
        }

        override fun areContentsTheSame(oldItem: ItemColor, newItem: ItemColor): Boolean {
            return oldItem == newItem
        }
    }

    data class ItemColor(val value: Int, val isSelected: Boolean = false) {
        companion object {
            fun from(colors: List<Int>): List<ItemColor> {
                return colors.map { ItemColor(it) }
            }
        }
    }

    interface ColorSelectListener {
        fun onSelect(color: ItemColor)
    }
}