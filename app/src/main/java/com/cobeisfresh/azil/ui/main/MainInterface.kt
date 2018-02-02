package com.cobeisfresh.azil.ui.main

import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

interface MainInterface {

    interface View {

        fun activateAllPetsTab()

        fun activateMyPetsTab()

        fun activateProfileTab()

        fun showUserMustExistError()

        fun showWelcomeOverlay()

        fun hideWelcomeOverlay()

        fun openTab(tab: Int)
    }

    interface Presenter : BasePresenter<View> {

        fun viewReady()

        fun allPetsClicked()

        fun myPetsClicked()

        fun profileClicked()

        fun welcomeOverlayClicked()
    }
}