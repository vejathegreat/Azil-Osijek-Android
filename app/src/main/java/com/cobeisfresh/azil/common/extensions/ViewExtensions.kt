package com.cobeisfresh.azil.common.extensions

import android.content.Context
import android.graphics.PorterDuff
import android.support.design.widget.TextInputEditText
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.common.utils.onLollipopAndAbove
import com.cobeisfresh.azil.ui.custom_helpers.SimpleTextWatcher

/**
 * Created by Zerina Salitrezic on 31/08/2017.
 */

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.hideKeyboard() {
    val manager = App.get().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    manager?.run { manager.hideSoftInputFromWindow(windowToken, 0) }
}

fun View.changeLineColor(color: Int) = onLollipopAndAbove(onValid = {
    background = DrawableCompat.wrap(background)
    DrawableCompat.setTint(background.mutate(), color)
}, onInvalid = {
    background.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
})

fun View.onClick(onClick: () -> Unit) = setOnClickListener { onClick() }

fun View.onTouch(onTouch: () -> Unit) = setOnTouchListener { _, _ ->
    onTouch()
    false
}

fun View.onFocusChange(onFocusChange: (Boolean) -> Unit) = setOnFocusChangeListener { _, hasFocus -> onFocusChange(hasFocus) }

fun SwipeRefreshLayout.onRefresh(onRefresh: () -> Unit) = setOnRefreshListener { onRefresh() }

fun EditText.onActionDoneClicked(onActionDoneClicked: () -> Unit) = setOnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_DONE) {
        onActionDoneClicked()
    }
    false
}

fun EditText.onActionSearchClicked(onActionSearchClicked: () -> Unit) = setOnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        onActionSearchClicked()
    }
    false
}

fun EditText.onTextChange(onTextChange: (String) -> Unit) = addTextChangedListener(SimpleTextWatcher { onTextChange(it) })

fun CheckBox.onCheckedChange(onCheckedChange: (Boolean) -> Unit) = setOnCheckedChangeListener { _, isChecked -> onCheckedChange(isChecked) }

fun TextInputEditText.setInputError(errorText: CharSequence) {
    error = errorText
}

fun TextInputEditText.removeInputError() {
    if (error != null) {
        error = null
    }
}