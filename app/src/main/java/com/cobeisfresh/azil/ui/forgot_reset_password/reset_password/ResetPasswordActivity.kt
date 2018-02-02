package com.cobeisfresh.azil.ui.forgot_reset_password.reset_password

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.constants.EMAIL
import com.cobeisfresh.azil.common.constants.FORGOT_PASSWORD
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.error_layout.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

class ResetPasswordActivity : BaseActivity(), ResetPasswordInterface.View {

    @Inject
    lateinit var presenter: ResetPasswordInterface.Presenter

    companion object {
        fun getLaunchIntent(from: Context, email: String): Intent {
            val intent = Intent(from, ResetPasswordActivity::class.java)
            intent.putExtra(EMAIL, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_reset_password)
        presenter.setView(this)
        initUi()
    }

    override fun initUi() {
        setDefaultLineColors()
        getExtras()
        setListeners()
    }

    private fun setDefaultLineColors() {
        code.changeLineColor(color(R.color.platinum))
        newPassword.changeLineColor(color(R.color.platinum))
        retypedNewPassword.changeLineColor(color(R.color.platinum))
    }

    private fun getExtras() {
        val intent = intent
        intent.getStringExtra(EMAIL)?.let { presenter.setEmail(it) }
    }

    private fun setListeners() {
        code.onFocusChange { presenter.onCodeChangeFocus(it) }
        newPassword.onFocusChange { presenter.onNewPasswordChangeFocus(it) }
        retypedNewPassword.onFocusChange { presenter.onRetypedNewPasswordChangeFocus(it) }
        rootLayout.onTouch { presenter.touchRootLayout() }
        retypedNewPassword.onActionDoneClicked { presenter.clickedDoneOnRetypedPassword() }
        resetPassword.onClick { presenter.resetPasswordClicked(hasInternet()) }
        iconBack.onClick { presenter.iconBackClicked() }
        code.onTextChange { presenter.codeChanged(it) }
        newPassword.onTextChange { presenter.newPasswordChanged(it) }
        retypedNewPassword.onTextChange { presenter.retypedNewPasswordChanged(it) }
    }

    override fun clearFocusFromInputFields() {
        code.clearFocus()
        newPassword.clearFocus()
        retypedNewPassword.clearFocus()
    }

    override fun requestFocusOnRootLayout() {
        rootLayout.requestFocus()
    }

    override fun hideKeyboard() = rootLayout.hideKeyboard()

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun goToLoginActivity() = startActivityForResult(LoginActivity.getLaunchIntent(this), FORGOT_PASSWORD)

    override fun showNoInternetError() {
        errorText.text = getString(R.string.error_no_internet)
        showErrorLayout()
    }

    override fun showServerError() {
        errorText.text = getString(R.string.server_error)
        showErrorLayout()
    }

    override fun showCodeExpiredError() {
        errorText.text = getString(R.string.forgot_password_code_expired_error)
        showErrorLayout()
    }

    override fun showCodeWrongError() {
        errorText.text = getString(R.string.reset_password_code_invalid_error)
        showErrorLayout()
    }

    override fun showPasswordInvalidError() {
        errorText.text = getString(R.string.reset_password_code_invalid_error)
        showErrorLayout()
    }

    private fun showErrorLayout() {
        errorLayout.fadeIn()
        errorLayout.bringToFront()
    }

    override fun setPasswordsNotMatchError() = retypedNewPassword.setInputError(getString(R.string.reset_password_passwords_dont_match_error))

    override fun setCodeError() = code.setInputError(getString(R.string.reset_password_code_invalid_error))

    override fun setNewPasswordError() = newPassword.setInputError(getString(R.string.reset_password_password_too_short_error))

    override fun setRetypedNewPasswordError() = retypedNewPassword.setInputError(getString(R.string.reset_password_password_too_short_error))

    override fun removeCodeError() = code.removeInputError()

    override fun removeNewPasswordError() = newPassword.removeInputError()

    override fun removeRetypedNewPasswordError() = retypedNewPassword.removeInputError()

    override fun setCodeGreenLine() = code.changeLineColor(color(R.color.green))

    override fun setNewPasswordGreenLine() = newPassword.changeLineColor(color(R.color.green))

    override fun setRetypedNewPasswordGreenLine() = retypedNewPassword.changeLineColor(color(R.color.green))

    override fun setCodeRedLine() = code.changeLineColor(color(R.color.red))

    override fun setNewPasswordRedLine() = newPassword.changeLineColor(color(R.color.red))

    override fun setRetypedNewPasswordRedLine() = retypedNewPassword.changeLineColor(color(R.color.red))

    override fun setCodeBlueLine() = code.changeLineColor(color(R.color.turquoise))

    override fun setNewPasswordBlueLine() = newPassword.changeLineColor(color(R.color.turquoise))

    override fun setRetypedNewPasswordBlueLine() = retypedNewPassword.changeLineColor(color(R.color.turquoise))

    override fun enableReset() = resetPassword.enable()

    override fun disableReset() = resetPassword.disable()

    override fun goBack() = finish()

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