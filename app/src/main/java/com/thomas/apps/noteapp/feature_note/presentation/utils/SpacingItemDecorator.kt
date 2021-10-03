package com.thomas.apps.noteapp.feature_note.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.thomas.apps.noteapp.R

class SpacingItemDecorator(@DimenRes val margin: Int) : RecyclerView.ItemDecoration() {

    companion object {
        val DEFAULT = SpacingItemDecorator(R.dimen.margin_large)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val context = parent.context
        val marginInPx = context.resources.getDimensionPixelSize(margin)
        with(outRect) {
            left = marginInPx
            right = marginInPx
            bottom = marginInPx
            if (parent.getChildAdapterPosition(view) == 0) {
                top = marginInPx
            }
        }
    }
}