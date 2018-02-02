package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.main.MainInterface
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 10/10/2017.
 */

class MainPresenterTest {

    private lateinit var presenter: MainPresenter

    private val view: MainInterface.View = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = MainPresenter(sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun handleAllPetsActiveShouldSetAllPetsActive() {
        presenter.handleAllPetsActive()

        assertEquals(presenter.isAllPetsActive, true)
        assertEquals(presenter.isMyPetsActive, false)
        assertEquals(presenter.isProfileActive, false)
    }

    @Test
    fun handleMyPetsActiveShouldSetMyPetsActive() {
        presenter.handleMyPetsActive()

        assertEquals(presenter.isAllPetsActive, false)
        assertEquals(presenter.isMyPetsActive, true)
        assertEquals(presenter.isProfileActive, false)
    }

    @Test
    fun handleProfileActiveShouldSetProfileActive() {
        presenter.handleProfileActive()

        assertEquals(presenter.isAllPetsActive, false)
        assertEquals(presenter.isMyPetsActive, false)
        assertEquals(presenter.isProfileActive, true)
    }

    @Test
    fun viewReadyShouldOpenAllPets() {
        whenever(sharedPrefs.isWelcomeShown()).thenReturn(true)
        presenter.viewReady()

        verify(view).activateAllPetsTab()
        verify(view).openTab(any())
        verify(sharedPrefs).isWelcomeShown()
        verifyNoMoreInteractions(view, sharedPrefs)
    }

    @Test
    fun viewReadyShouldOpenAllPetsShowWelcomeOverlay() {
        presenter.viewReady()

        verify(view).activateAllPetsTab()
        verify(view).openTab(any())
        verify(sharedPrefs).isWelcomeShown()
        verify(view).showWelcomeOverlay()
        verify(sharedPrefs).setWelcomeShown()
        verifyNoMoreInteractions(view, sharedPrefs)
    }

    @Test
    fun welcomeOverlayClickedShouldHideWelcomeOverlay() {
        presenter.welcomeOverlayClicked()

        verify(view).hideWelcomeOverlay()
        verifyNoMoreInteractions(view, sharedPrefs)
    }

    @Test
    fun allPetsClickedShouldActivateAllPets() {
        presenter.allPetsClicked()

        verify(view).activateAllPetsTab()
        verify(view).openTab(any())
        verifyNoMoreInteractions(view, sharedPrefs)
    }

    @Test
    fun allPetsClickedNoInteractions() {
        presenter.isAllPetsActive = true
        presenter.allPetsClicked()

        verifyZeroInteractions(view, sharedPrefs)
    }

    @Test
    fun myPetsClickedShouldActivateMyPetsTab() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.myPetsClicked()

        verify(sharedPrefs).isLoggedIn()
        verify(view).activateMyPetsTab()
        verify(view).openTab(any())
        verifyNoMoreInteractions(view, sharedPrefs)
    }

    @Test
    fun myPetsClickedShouldShowUserMustExistError() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(false)
        presenter.myPetsClicked()

        verify(sharedPrefs).isLoggedIn()
        verify(view).showUserMustExistError()
        verifyNoMoreInteractions(view, sharedPrefs)
    }

    @Test
    fun myPetsClickedNoInteractions() {
        presenter.isMyPetsActive = true
        presenter.myPetsClicked()

        verify(sharedPrefs).isLoggedIn()
        verifyZeroInteractions(view, sharedPrefs)
    }

    @Test
    fun profileClickedShouldActivateProfile() {
        presenter.profileClicked()

        verify(view).activateProfileTab()
        verify(view).openTab(any())
        verifyNoMoreInteractions(view, sharedPrefs)
    }

    @Test
    fun profileClickedNoInteractions() {
        presenter.isProfileActive = true
        presenter.profileClicked()

        verifyZeroInteractions(view, sharedPrefs)
    }
}