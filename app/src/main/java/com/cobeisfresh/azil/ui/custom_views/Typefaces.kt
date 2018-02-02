package com.cobeisfresh.azil.ui.custom_views


import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.utils.isEmpty
import java.util.*

/**
 * Cobe Android Team
 */

class Typefaces {

    companion object {
        private const val FONTS = "fonts/"
        private val cache: Hashtable<String, Typeface> = Hashtable()
        private const val FONTS_FOLDER: String = FONTS

        fun get(context: Context, assetPath: String?): Typeface? = synchronized(cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    val t = Typeface.createFromAsset(context.assets, Typefaces.FONTS_FOLDER + assetPath)
                    cache.put(assetPath, t)
                } catch (e: Exception) {
                    return null
                }

            }
            return cache[assetPath]
        }

        fun setCustomFont(context: Context, attrs: AttributeSet?, view: TextView) {
            val assetPath = getCustomFontPath(context, attrs)
            setCustomFont(context, assetPath, view)
        }

        fun setCustomFont(context: Context, resid: Int, view: TextView) {
            val assetPath = getCustomFontPath(context, resid)
            setCustomFont(context, assetPath, view)
        }

        private fun setCustomFont(context: Context, assetPath: String?, view: TextView) {
            if (!isEmpty(assetPath)) {
                val tf: Typeface? = Typefaces.get(context, assetPath)
                tf?.let { view.typeface = tf }
            }
        }

        private fun getCustomFontPath(context: Context, attrs: AttributeSet?): String? {
            val a: TypedArray? = context.obtainStyledAttributes(attrs, R.styleable.CustomFont)
            val assetPath: String? = a?.getString(R.styleable.CustomFont_fontName)
            a?.recycle()
            return assetPath
        }

        private fun getCustomFontPath(context: Context, resid: Int): String {
            val a: TypedArray = context.obtainStyledAttributes(resid, R.styleable.CustomFont)
            val assetPath: String = a.getString(R.styleable.CustomFont_fontName)
            a.recycle()
            return assetPath
        }
    }
}