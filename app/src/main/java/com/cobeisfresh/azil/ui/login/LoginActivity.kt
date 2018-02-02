package com.cobeisfresh.azil.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.FORGOT_PASSWORD
import com.cobeisfresh.azil.common.constants.REGISTER
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.forgot_reset_password.forgot_password.ForgotPasswordActivity
import com.cobeisfresh.azil.ui.main.MainActivity
import com.cobeisfresh.azil.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.error_layout.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class LoginActivity : BaseActivity(), LoginInterface.View {

    @Inject
    lateinit var presenter: LoginInterface.Presenter

    companion object {
        fun getLaunchIntent(from: Context): Intent = Intent(from, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_login)
        presenter.setView(this)
        initUi()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.LOGIN)
    }

    override fun initUi() {
        setDefaultLineColors()
        setListeners()
    }

    private fun setDefaultLineColors() {
        email.changeLineColor(color(R.color.platinum))
        password.changeLineColor(color(R.color.platinum))
    }

    private fun setListeners() {
        email.onFocusChange { presenter.onEmailChangeFocus(it) }
        password.onFocusChange { presenter.onPasswordChangeFocus(it) }
        login.onClick { presenter.loginClicked(hasInternet()) }
        forgotPassword.onClick { presenter.forgotPasswordClicked() }
        register.onClick { presenter.registerClicked() }
        iconBack.onClick { presenter.iconBackClicked() }
        rootLayout.onTouch { presenter.touchRootLayout() }
        password.onActionDoneClicked { presenter.clickedDoneOnPassword() }
        email.onTextChange { presenter.emailChanged(it) }
        password.onTextChange { presenter.passwordChanged(it) }
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showNoInternetError() {
        errorText.text = getString(R.string.error_no_internet)
        showErrorLayout()
    }

    override fun showServerError() {
        errorText.text = getString(R.string.server_error)
        showErrorLayout()
    }

    override fun showInvalidPasswordError() {
        errorText.text = getString(R.string.login_wrong_password_error)
        showErrorLayout()
    }

    override fun showNotRegisteredUserError() {
        errorText.text = getString(R.string.error_verification_email)
        showErrorLayout()
    }

    private fun showErrorLayout() {
        errorLayout.fadeIn()
        errorLayout.bringToFront()
    }

    override fun clearFocusFromInputFields() {
        email.clearFocus()
        password.clearFocus()
    }

    override fun hideKeyboard() = rootLayout.hideKeyboard()

    override fun requestFocusOnRootLayout() {
        rootLayout.requestFocus()
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun openMainActivity() {
        val intent = MainActivity.getLaunchIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun openForgotPasswordActivity() = startActivityForResult(ForgotPasswordActivity.getLaunchIntent(this), FORGOT_PASSWORD)

    override fun openRegisterActivity() = startActivityForResult(RegisterActivity.getLaunchIntent(this), REGISTER)

    override fun setEmailError() = email.setInputError(getString(R.string.login_email_invalid_error))

    override fun setPasswordError() = password.setInputError(getString(R.string.login_password_too_short_error))

    override fun removeEmailError() = email.removeInputError()

    override fun removePasswordError() = password.removeInputError()

    override fun setEmailRedLine() = email.changeLineColor(color(R.color.red))

    override fun setPasswordRedLine() = password.changeLineColor(color(R.color.red))

    override fun setEmailGreenLine() = email.changeLineColor(color(R.color.green))

    override fun setPasswordGreenLine() = password.changeLineColor(color(R.color.green))

    override fun setEmailBlueLine() = email.changeLineColor(color(R.color.turquoise))

    override fun setPasswordBlueLine() = password.changeLineColor(color(R.color.turquoise))

    override fun enableLogin() = login.enable()

    override fun disableLogin() = login.disable()

    override fun goBack() = finish()

    override fun goBackWithResult() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REGISTER, FORGOT_PASSWORD -> goBackWithResult()
            }
        }
    }

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}