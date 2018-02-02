package com.cobeisfresh.azil.ui.web_view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.*
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.error_layout.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 10/10/2017.
 */

class WebViewActivity : BaseActivity(), WebViewInterface.View {

    @Inject
    lateinit var presenter: WebViewInterface.Presenter

    companion object {
        private const val WEB_VIEW_TYPE = "web_view_type"
        private const val ABOUT_SHELTER_URL = "about/shelter"
        private const val ABOUT_ASSOCIATION_URL = "about/organisation"
        private const val DONATIONS_URL = "about/donate"
        private const val TERMS_AND_CONDITIONS_URL = "about/terms"
        private const val PRIVACY_POLICY_URL = "about/privacy"

        fun getLaunchIntent(from: Context, type: Int): Intent {
            val intent = Intent(from, WebViewActivity::class.java)
            intent.putExtra(WEB_VIEW_TYPE, type)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_web_view)
        presenter.setView(this)
        initUi()
    }

    override fun initUi() {
        getExtras()
        setWebSettings()
        setWebClient()
        presenter.loadUrl(hasInternet())
        setListeners()
    }

    private fun setListeners() {
        iconBack.onClick { presenter.iconBackClicked() }
        errorLayout.onClick { hideErrorLayout() }
        rootLayout.onTouch { hideErrorLayout() }
    }

    private fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    private fun getExtras() {
        val extras = intent.extras
        extras?.run { presenter.setType(extras.getInt(WEB_VIEW_TYPE)) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebSettings() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        }
    }

    private fun setWebClient() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                presenter.loadingPageStarted()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                presenter.loadingPageFinished()
            }
        }
    }

    override fun sendAboutShelterScreenEvent() = sendScreenTrackingEvent(ScreenEvent.ScreenName.ABOUT_SHELTER)

    override fun sendAboutAssociationScreenEvent() = sendScreenTrackingEvent(ScreenEvent.ScreenName.ABOUT_ASSOCIATION)

    override fun sendDonationsScreenEvent() = sendScreenTrackingEvent(ScreenEvent.ScreenName.DONATIONS)

    override fun sendTermsAndConditionsScreenEvent() = sendScreenTrackingEvent(ScreenEvent.ScreenName.TERMS_AND_CONDITIONS)

    override fun sendPrivacyPolicyScreenEvent() = sendScreenTrackingEvent(ScreenEvent.ScreenName.PRIVACY_POLICY)

    override fun showAboutShelterTitle() {
        toolbarTitle.text = getString(R.string.about_shelter)
    }

    override fun showAboutAssociationTitle() {
        toolbarTitle.text = getString(R.string.about_association)
    }

    override fun showDonationsTitle() {
        toolbarTitle.text = getString(R.string.donations)
    }

    override fun showTermsAndConditionsTitle() {
        toolbarTitle.text = getString(R.string.terms_and_conditions_text)
    }

    override fun showPrivacyPolicyTitle() {
        toolbarTitle.text = getString(R.string.privacy_policy_text)
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showNoInternetError() {
        errorText.text = getString(R.string.error_no_internet)
        errorLayout.fadeIn()
        errorLayout.bringToFront()
    }

    override fun loadUrl(urlType: Int) {
        val url = when (urlType) {
            ABOUT_SHELTER -> getString(R.string.backend_url) + ABOUT_SHELTER_URL
            ABOUT_ASSOCIATION -> getString(R.string.backend_url) + ABOUT_ASSOCIATION_URL
            DONATIONS -> getString(R.string.backend_url) + DONATIONS_URL
            TERMS_AND_CONDITIONS -> getString(R.string.backend_url) + TERMS_AND_CONDITIONS_URL
            PRIVACY_POLICY -> getString(R.string.backend_url) + PRIVACY_POLICY_URL
            else -> ""
        }
        webView.loadUrl(url)
    }

    override fun showData() = scrollView.visible()

    override fun goBack() = finish()
}