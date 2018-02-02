package com.cobeisfresh.azil.presentation.base

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

interface BasePresenter<in T> {

    fun setView(view: T)
}