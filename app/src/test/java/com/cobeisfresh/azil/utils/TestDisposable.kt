package com.cobeisfresh.azil.utils

import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 03/10/2017.
 */

class TestDisposable : Disposable {

    override fun isDisposed(): Boolean = false

    override fun dispose() {}
}