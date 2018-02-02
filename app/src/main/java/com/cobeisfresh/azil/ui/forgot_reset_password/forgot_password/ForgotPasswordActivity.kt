package com.cobeisfresh.azil.ui.forgot_reset_password.forgot_password

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.FORGOT_PASSWORD
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.forgot_reset_password.reset_password.ResetPasswordActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.error_layout.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 07/08/2017.
 */

class ForgotPasswordActivity : BaseActivity(), ForgotPasswordInterface.View {

    @Inject
    lateinit var presenter: ForgotPasswordInterface.Presenter

    companion object {
        fun getLaunchIntent(from: Context): Intent = Intent(from, ForgotPasswordActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_forgot_password)
        presenter.setView(this)
        initUi()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.FORGOT_PASSWORD)
    }

    override fun initUi() {
        setDefaultLineColor()
        setListeners()
    }

    private fun setDefaultLineColor() = email.changeLineColor(color(R.color.platinum))

    private fun setListeners() {
        email.onFocusChange { presenter.onEmailChangeFocus(it) }
        send.onClick { presenter.sendButtonClicked(hasInternet()) }
        iconBack.onClick { presenter.iconBackClicked() }
        rootLayout.onTouch { presenter.touchRootLayout() }
        email.onActionDoneClicked { presenter.clickedDoneOnEmail() }
        email.onTextChange { presenter.emailChanged(it) }
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun hideKeyboard() = rootLayout.hideKeyboard()

    override fun requestFocusOnRootLayout() {
        rootLayout.requestFocus()
    }

    override fun clearFocusFromEmail() = email.clearFocus()

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showUnknownUserError() {
        errorText.text = getString(R.string.forgot_password_unknown_user_error)
        showErrorLayout()
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

    override fun goBack() = finish()

    override fun disableSendButton() = send.disable()

    override fun enableSendButton() = send.enable()

    override fun setEmailError() = email.setInputError(getString(R.string.forgot_password_email_invalid_error))

    override fun removeEmailError() = email.removeInputError()

    override fun setEmailBlueLine() = email.changeLineColor(color(R.color.turquoise))

    override fun setEmailGreenLine() = email.changeLineColor(color(R.color.green))

    override fun setEmailRedLine() = email.changeLineColor(color(R.color.red))

    override fun goToResetPasswordActivity(email: String) {
        startActivityForResult(ResetPasswordActivity.getLaunchIntent(this, email), FORGOT_PASSWORD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == FORGOT_PASSWORD) {
            goBackWithResult()
        }
    }

    private fun goBackWithResult() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}