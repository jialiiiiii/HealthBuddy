package com.example.healthbuddy.post

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class RecyclerViewItemDecoration (
    private val margin: Int,
    private val columns: Int
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        // set right margin to all
        outRect.top = margin
        outRect.left = margin

        // add right margin only to the first column
        if (position % columns == 0) {
            outRect.right = 0
        }
        else {
            outRect.right = margin
        }
    }
}