package com.cobeisfresh.azil.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.widget.ToggleButton

/**
 * Cobe Android Team
 */

class TypefaceToggleButton : ToggleButton {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setCustomFont(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        setCustomFont(context, attrs)
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
}