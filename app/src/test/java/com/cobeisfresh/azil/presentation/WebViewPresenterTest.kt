package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.URL_TYPE
import com.cobeisfresh.azil.common.constants.*
import com.cobeisfresh.azil.ui.web_view.WebViewInterface
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 10/10/2017.
 */

class WebViewPresenterTest {

    private lateinit var presenter: WebViewPresenter

    private val view: WebViewInterface.View = mock()

    @Before
    fun setUp() {
        presenter = WebViewPresenter()
        presenter.setView(view)
    }

    @Test
    fun loadingPageStartedShouldShowProgressBar() {
        presenter.loadingPageStarted()

        verify(view).showProgressBar()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun loadingPageFinishedShouldShowData() {
        presenter.loadingPageFinished()

        verify(view).hideProgressBar()
        verify(view).showData()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun setTypeWhenAboutShelterType() {
        presenter.setType(ABOUT_SHELTER)

        verify(view).showAboutShelterTitle()
        verify(view).sendAboutShelterScreenEvent()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun setTypeWhenAboutAssociationType() {
        presenter.setType(ABOUT_ASSOCIATION)

        verify(view).showAboutAssociationTitle()
        verify(view).sendAboutAssociationScreenEvent()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun setTypeWhenDonationsType() {
        presenter.setType(DONATIONS)

        verify(view).showDonationsTitle()
        verify(view).sendDonationsScreenEvent()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun setTypeWhenTermsAndConditionsType() {
        presenter.setType(TERMS_AND_CONDITIONS)

        verify(view).showTermsAndConditionsTitle()
        verify(view).sendTermsAndConditionsScreenEvent()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun setTypeWhenPrivacyPolicyType() {
        presenter.setType(PRIVACY_POLICY)

        verify(view).showPrivacyPolicyTitle()
        verify(view).sendPrivacyPolicyScreenEvent()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun loadUrlNoInternet() {
        presenter.loadUrl(false)

        verify(view).showNoInternetError()
        verify(view).hideProgressBar()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun loadUrlShouldLoadUrl() {
        presenter.urlType = URL_TYPE
        presenter.loadUrl(true)

        verify(view).loadUrl(any())
        verifyNoMoreInteractions(view)
    }

    @Test
    fun iconBackClickedShouldGoBack() {
        presenter.iconBackClicked()

        verify(view).goBack()
        verifyNoMoreInteractions(view)
    }
}