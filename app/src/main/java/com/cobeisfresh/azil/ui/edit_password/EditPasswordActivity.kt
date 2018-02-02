package com.cobeisfresh.azil.ui.edit_password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_password.*
import kotlinx.android.synthetic.main.error_layout.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 18/08/2017.
 */

class EditPasswordActivity : BaseActivity(), EditPasswordInterface.View {

    @Inject
    lateinit var presenter: EditPasswordInterface.Presenter

    companion object {
        fun getLaunchIntent(from: Context): Intent = Intent(from, EditPasswordActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_edit_password)
        presenter.setView(this)
        initUi()
    }

    override fun initUi() {
        setDefaultLineColors()
        setListeners()
    }

    private fun setDefaultLineColors() {
        oldPassword.changeLineColor(color(R.color.platinum))
        newPassword.changeLineColor(color(R.color.platinum))
        retypePassword.changeLineColor(color(R.color.platinum))
    }

    private fun setListeners() {
        oldPassword.onFocusChange { presenter.onOldPasswordChangeFocus(it) }
        newPassword.onFocusChange { presenter.onNewPasswordChangeFocus(it) }
        retypePassword.onFocusChange { presenter.onRetypedNewPasswordChangeFocus(it) }
        oldPassword.onTextChange { presenter.oldPasswordChanged(it) }
        newPassword.onTextChange { presenter.newPasswordChanged(it) }
        retypePassword.onTextChange { presenter.retypedNewPasswordChanged(it) }
        change.onClick { presenter.changePasswordClicked(hasInternet()) }
        iconBack.onClick { presenter.iconBackClicked() }
        retypePassword.onActionDoneClicked { presenter.clickedDoneOnRetypePassword() }
        rootLayout.onTouch { presenter.touchRootLayout() }
    }

    override fun hideKeyboard() = rootLayout.hideKeyboard()

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun clearFocusFromInputFields() {
        oldPassword.clearFocus()
        newPassword.clearFocus()
        retypePassword.clearFocus()
    }

    override fun requestFocusOnRootLayout() {
        rootLayout.requestFocus()
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showPasswordChangedSuccess() {
        showInfoDialog(message = getString(R.string.edit_password_success_alert), okAction = { goBack() })
    }

    override fun showPasswordInvalidError() {
        errorText.text = getString(R.string.edit_password_password_too_short_error)
        showErrorLayout()
    }

    override fun showPasswordsMismatchedError() {
        errorText.text = getString(R.string.edit_password_current_password_invalid_error)
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

    override fun setPasswordsNotSameError() = retypePassword.setInputError(getString(R.string.edit_password_passwords_dont_match_placeholder))

    override fun setNewPasswordError() = newPassword.setInputError(getString(R.string.error_password_too_short))

    override fun setRetypedPasswordError() = retypePassword.setInputError(getString(R.string.error_password_too_short))

    override fun setOldPasswordError() = oldPassword.setInputError(getString(R.string.error_password_too_short))

    override fun removeOldPasswordError() = oldPassword.removeInputError()

    override fun removeNewPasswordError() = newPassword.removeInputError()

    override fun removeRetypedPasswordError() = retypePassword.removeInputError()

    override fun setOldPasswordGreenLine() = oldPassword.changeLineColor(color(R.color.green))

    override fun setNewPasswordGreenLine() = newPassword.changeLineColor(color(R.color.green))

    override fun setRetypedPasswordGreenLine() = retypePassword.changeLineColor(color(R.color.green))

    override fun setOldPasswordBlueLine() = oldPassword.changeLineColor(color(R.color.turquoise))

    override fun setNewPasswordBlueLine() = newPassword.changeLineColor(color(R.color.turquoise))

    override fun setRetypedPasswordBlueLine() = retypePassword.changeLineColor(color(R.color.turquoise))

    override fun setOldPasswordRedLine() = oldPassword.changeLineColor(color(R.color.red))

    override fun setNewPasswordRedLine() = newPassword.changeLineColor(color(R.color.red))

    override fun setRetypedPasswordRedLine() = retypePassword.changeLineColor(color(R.color.red))

    override fun disableChange() = change.disable()

    override fun enableChange() = change.enable()

    override fun goBack() = finish()

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}