package com.example.healthbuddy.others

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager

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
        val position = parent.getChildAdapterPosition(view)
        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams

        val spanIndex = lp.spanIndex
        if (position > 0) {
            // set top & left margin to all
            outRect.top = margin
            outRect.left = margin

            if (spanIndex == 0) {
                outRect.right = 0
            } else {
                // set right margin to 2nd column only
                outRect.right = margin
            }
        }
    }
}