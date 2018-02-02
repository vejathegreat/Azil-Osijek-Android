package com.cobeisfresh.azil.ui.welcome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.FACEBOOK_PERMISSIONS
import com.cobeisfresh.azil.common.constants.LOGIN
import com.cobeisfresh.azil.common.constants.REGISTER
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.getQuoteFormat
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.login.LoginActivity
import com.cobeisfresh.azil.ui.register.RegisterActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.error_layout.*
import java.util.*
import javax.inject.Inject


/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class WelcomeActivity : BaseActivity(), WelcomeInterface.View, FacebookCallback<LoginResult> {

    @Inject
    lateinit var presenter: WelcomeInterface.Presenter

    private var callbackManager: CallbackManager? = null

    companion object {
        fun getLaunchIntent(from: Context): Intent = Intent(from, WelcomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_welcome)
        presenter.setView(this)
        initUi()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.WELCOME)
    }

    override fun initUi() {
        setWelcomeText()
        setListeners()
        initializeFacebook()
    }

    private fun setWelcomeText() {
        welcomeQuote.text = getQuoteFormat()
    }

    private fun setListeners() {
        facebookLogin.onClick { presenter.facebookLoginClicked(hasInternet()) }
        register.onClick { presenter.registerClicked() }
        login.onClick { presenter.loginClicked() }
        skip.onClick { presenter.skipClicked() }
        rootLayout.onClick { hideErrorLayout() }
    }

    private fun initializeFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, this)
    }

    override fun openRegisterActivity() = startActivityForResult(RegisterActivity.getLaunchIntent(this), REGISTER)

    override fun openLoginActivity() = startActivityForResult(LoginActivity.getLaunchIntent(this), LOGIN)

    override fun onCancel() {}

    override fun onError(error: FacebookException?) {}

    override fun onSuccess(result: LoginResult?) {
        result?.accessToken?.token?.run { presenter.handleFacebookLogin(hasInternet(), this) }
    }

    override fun startFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList<String>(*FACEBOOK_PERMISSIONS))
    }

    override fun showNoInternetError() {
        errorText.text = getString(R.string.error_no_internet)
        showErrorLayout()
    }

    override fun showServerError() {
        errorText.text = getString(R.string.server_error)
        showErrorLayout()
    }

    private fun showErrorLayout() {
        errorLayout.fadeIn()
        errorLayout.bringToFront()
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun goBack() = finish()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                LOGIN, REGISTER -> goBackWithResult()
                else -> callbackManager?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun goBackWithResult() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}