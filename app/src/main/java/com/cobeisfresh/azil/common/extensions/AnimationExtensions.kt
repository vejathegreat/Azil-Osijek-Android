package com.cobeisfresh.azil.common.extensions

import android.view.View

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

private const val ANIM_DURATION_SHORT: Long = 250L
private const val ZERO: Long = 0L

fun View.fadeIn(duration: Long = ANIM_DURATION_SHORT, animDelay: Long = ZERO) =
        animate()
                .alpha(1f)
                .setStartDelay(animDelay)
                .setDuration(duration)
                .withStartAction { visibility = View.VISIBLE }
                .start()

fun View.fadeOut(duration: Long = ANIM_DURATION_SHORT, animDelay: Long = ZERO) =
        animate().alpha(0f)
                .setStartDelay(animDelay)
                .setDuration(duration)
                .withEndAction {
                    if (alpha == 0f) {
                        visibility = View.GONE
                    }
                }
                .start()