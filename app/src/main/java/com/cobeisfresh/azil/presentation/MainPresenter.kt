package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ALL_PETS
import com.cobeisfresh.azil.common.constants.MY_PETS
import com.cobeisfresh.azil.common.constants.PROFILE
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.main.MainInterface

/**
 * Created by Zerina Salitrezic on 01/08/2017.
 */

class MainPresenter(private val sharedPrefs: SharedPrefs) : MainInterface.Presenter {

    private lateinit var view: MainInterface.View
    internal var isAllPetsActive = false
    internal var isMyPetsActive = false
    internal var isProfileActive = false

    override fun setView(view: MainInterface.View) {
        this.view = view
    }

    override fun viewReady() {
        handleAllPetsActive()
        view.activateAllPetsTab()
        view.openTab(ALL_PETS)
        if (!sharedPrefs.isWelcomeShown()) {
            view.showWelcomeOverlay()
            sharedPrefs.setWelcomeShown()
        }
    }

    override fun welcomeOverlayClicked() = view.hideWelcomeOverlay()

    internal fun handleAllPetsActive() {
        isAllPetsActive = true
        isMyPetsActive = false
        isProfileActive = false
    }

    internal fun handleMyPetsActive() {
        isAllPetsActive = false
        isMyPetsActive = true
        isProfileActive = false
    }

    internal fun handleProfileActive() {
        isAllPetsActive = false
        isMyPetsActive = false
        isProfileActive = true
    }

    override fun allPetsClicked() {
        if (!isAllPetsActive) {
            handleAllPetsActive()
            view.activateAllPetsTab()
            view.openTab(ALL_PETS)
        }
    }

    override fun myPetsClicked() {
        val isLoggedIn = sharedPrefs.isLoggedIn()
        if (!isMyPetsActive) {
            if (isLoggedIn) {
                handleMyPetsActive()
                view.activateMyPetsTab()
                view.openTab(MY_PETS)
            } else {
                view.showUserMustExistError()
            }
        }
    }

    override fun profileClicked() {
        if (!isProfileActive) {
            handleProfileActive()
            view.activateProfileTab()
            view.openTab(PROFILE)
        }
    }
}