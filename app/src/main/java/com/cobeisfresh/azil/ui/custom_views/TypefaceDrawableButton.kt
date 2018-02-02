package com.cobeisfresh.azil.ui.custom_views


import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet

/**
 * Cobe Android Team
 */

class TypefaceDrawableButton : AppCompatButton {

    private var drawableWidth: Int = 0
    private var drawablePosition: DrawablePositions? = null
    private var iconPadding: Int = 0

    // Cached to prevent allocation during onLayout
    private val bounds: Rect

    constructor(context: Context) : super(context) {
        bounds = Rect()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        bounds = Rect()
        setCustomFont(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        bounds = Rect()
        setCustomFont(context, attrs)
    }

    fun setIconPadding(padding: Int) {
        iconPadding = padding
        requestLayout()
    }

    private fun setCustomFont(context: Context, attrs: AttributeSet?) {
        if (!isInEditMode) {
            Typefaces.setCustomFont(context, attrs, this)
        }
    }

    override fun setTextAppearance(resId: Int) {
        super.setTextAppearance(resId)
        if (!isInEditMode) {
            Typefaces.setCustomFont(context, resId, this)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val textPaint = paint
        val text = text.toString()
        textPaint.getTextBounds(text, 0, text.length, bounds)

        val textWidth = bounds.width()
        val contentWidth = drawableWidth + iconPadding + textWidth

        val contentLeft = (width / 2.0 - contentWidth / 2.0).toInt()
        compoundDrawablePadding = -contentLeft + iconPadding
        when (drawablePosition) {
            DrawablePositions.LEFT -> setPadding(contentLeft, 0, 0, 0)
            DrawablePositions.RIGHT -> setPadding(0, 0, contentLeft, 0)
            else -> setPadding(0, 0, 0, 0)
        }
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
        when {
            left != null -> {
                drawableWidth = left.intrinsicWidth
                drawablePosition = DrawablePositions.LEFT
            }
            right != null -> {
                drawableWidth = right.intrinsicWidth
                drawablePosition = DrawablePositions.RIGHT
            }
            else -> drawablePosition = DrawablePositions.NONE
        }
        requestLayout()
    }

    private enum class DrawablePositions {
        NONE,
        LEFT,
        RIGHT
    }
}