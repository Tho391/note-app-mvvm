package com.thomas.apps.noteapp.feature_note.presentation.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import kotlin.math.min
import kotlin.math.roundToInt

class NoteCardView : MaterialCardView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawTopRightCorner(this)
        }
    }

    private fun drawTopRightCorner(canvas: Canvas) {
        val backgroundColor = cardBackgroundColor.defaultColor
        val darkerColor = darkerColor(backgroundColor)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.isAntiAlias = true
            color = darkerColor
            style = Paint.Style.FILL
            strokeWidth = 1f
            strokeCap = Paint.Cap.ROUND
        }
        val rectF = RectF()
        val rectF2 = RectF()
        val cornerRadius = shapeAppearanceModel.topLeftCornerSize.getCornerSize(rectF2)
        val cornerSize = shapeAppearanceModel.topRightCornerSize.getCornerSize(rectF)

        val x1 = width - cornerSize
        val y1 = 0f

        val x2 = width - cornerSize
        val y2 = cornerSize - cornerRadius

        val x3 = width - cornerSize + cornerRadius
        val y3 = cornerSize

        val x4 = width.toFloat()
        val y4 = cornerSize

        val x5 = x2
        val y5 = y2 - cornerRadius

        val x6 = x3 + cornerRadius
        val y6 = y3

        val path = Path().apply {
            moveTo(x1, y1)
            lineTo(x2, y2)
            lineTo(x3, y3)
            lineTo(x4, y4)
            lineTo(x1, y1)
            addArc(x5, y5, x6, y6, 90f, 90f)
            close()
        }
        canvas.drawPath(path, paint)
    }

    private fun darkerColor(color: Int, factor: Float = 1.5f): Int {
        val a = Color.alpha(color)
        val r = (Color.red(color) * factor).roundToInt()
        val g = (Color.green(color) * factor).roundToInt()
        val b = (Color.blue(color) * factor).roundToInt()
        return Color.argb(a, min(r, 255), min(g, 255), min(b, 255))
    }
}