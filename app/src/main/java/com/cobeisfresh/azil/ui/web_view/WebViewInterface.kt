package com.cobeisfresh.azil.ui.web_view

import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 10/10/2017.
 */

interface WebViewInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun showNoInternetError()

        fun showAboutShelterTitle()

        fun showAboutAssociationTitle()

        fun showDonationsTitle()

        fun showTermsAndConditionsTitle()

        fun loadUrl(urlType: Int)

        fun showData()

        fun goBack()

        fun sendAboutShelterScreenEvent()

        fun sendAboutAssociationScreenEvent()

        fun sendDonationsScreenEvent()

        fun sendTermsAndConditionsScreenEvent()

        fun showPrivacyPolicyTitle()

        fun sendPrivacyPolicyScreenEvent()
    }

    interface Presenter : BasePresenter<View> {

        fun setType(type: Int)

        fun loadingPageStarted()

        fun loadingPageFinished()

        fun loadUrl(hasInternet: Boolean)

        fun iconBackClicked()
    }
}