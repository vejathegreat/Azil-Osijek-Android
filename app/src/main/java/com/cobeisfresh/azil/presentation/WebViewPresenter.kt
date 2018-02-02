package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.*
import com.cobeisfresh.azil.ui.web_view.WebViewInterface

/**
 * Created by Zerina Salitrezic on 10/10/2017.
 */

class WebViewPresenter : WebViewInterface.Presenter {

    private lateinit var view: WebViewInterface.View
    internal var urlType: Int = -1

    override fun setView(view: WebViewInterface.View) {
        this.view = view
    }

    override fun loadingPageStarted() = view.showProgressBar()

    override fun loadingPageFinished() {
        view.hideProgressBar()
        view.showData()
    }

    override fun setType(type: Int) {
        when (type) {
            ABOUT_SHELTER -> {
                urlType = ABOUT_SHELTER
                view.showAboutShelterTitle()
                view.sendAboutShelterScreenEvent()
            }
            ABOUT_ASSOCIATION -> {
                urlType = ABOUT_ASSOCIATION
                view.showAboutAssociationTitle()
                view.sendAboutAssociationScreenEvent()
            }
            DONATIONS -> {
                urlType = DONATIONS
                view.showDonationsTitle()
                view.sendDonationsScreenEvent()
            }
            TERMS_AND_CONDITIONS -> {
                urlType = TERMS_AND_CONDITIONS
                view.showTermsAndConditionsTitle()
                view.sendTermsAndConditionsScreenEvent()
            }
            PRIVACY_POLICY -> {
                urlType = PRIVACY_POLICY
                view.showPrivacyPolicyTitle()
                view.sendPrivacyPolicyScreenEvent()
            }
        }
    }

    override fun loadUrl(hasInternet: Boolean) {
        if (!hasInternet) {
            view.hideProgressBar()
            view.showNoInternetError()
        } else {
            view.loadUrl(urlType)
        }
    }

    override fun iconBackClicked() = view.goBack()
}