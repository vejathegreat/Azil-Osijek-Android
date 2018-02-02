package com.cobeisfresh.azil.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.FeatureSelectedEvent
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.*
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.common.utils.loadImage
import com.cobeisfresh.azil.common.utils.loadPlaceholder
import com.cobeisfresh.azil.data.event.UserEvent
import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.ui.base.BaseFragment
import com.cobeisfresh.azil.ui.edit_password.EditPasswordActivity
import com.cobeisfresh.azil.ui.edit_profile.EditProfileActivity
import com.cobeisfresh.azil.ui.login.LoginActivity
import com.cobeisfresh.azil.ui.register.RegisterActivity
import com.cobeisfresh.azil.ui.web_view.WebViewActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.no_profile_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject


/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class ProfileFragment : BaseFragment(), ProfileInterface.View, FacebookCallback<LoginResult> {

    @Inject
    lateinit var presenter: ProfileInterface.Presenter

    private var callbackManager: CallbackManager? = null

    companion object {
        private const val REQUEST_CODE_USER_MODEL = 44

        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        presenter.setView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUi()
        prepareData()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.INFO)
    }

    override fun prepareUi() {
        initializeFacebook()
        setListeners()
    }

    private fun initializeFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, this)
    }

    private fun setListeners() {
        facebookLogin.onClick { presenter.facebookLoginClicked(hasInternet()) }
        register.onClick { presenter.registerClicked() }
        login.onClick { presenter.loginClicked() }
        iconEdit.onClick { presenter.editProfileClicked() }
        logOut.onClick { presenter.logOutClicked() }
        aboutShelter.onClick { presenter.aboutShelterClicked() }
        aboutAssociation.onClick { presenter.aboutAssociationClicked() }
        donate.onClick { presenter.donateClicked() }
        changePassword.onClick { presenter.changePasswordClicked() }
        termsAndConditions.onClick { presenter.termsAndConditionsClicked() }
        privacyPolicy.onClick { presenter.privacyPolicyClicked() }
        rootLayout.onClick { hideErrorLayout() }
        swipeRefreshLayout.onRefresh { presenter.onSwipeToRefresh(hasInternet()) }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().removeAllStickyEvents()
        EventBus.getDefault().unregister(this)
    }

    override fun stopRefreshing() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun prepareData() = presenter.getMe(hasInternet())

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showName(name: String) {
        username.text = name
    }

    override fun showEmail(emailText: String) {
        email.text = emailText
    }

    override fun showProfileImage(image: String) = loadImage(profileImage, image)

    override fun showProfileImagePlaceholder() = loadPlaceholder(profileImage, R.drawable.user_placeholder)

    override fun showProfileLayout() = profileLayout.fadeIn()

    override fun hideProfileLayout() = profileLayout.fadeOut()

    override fun hideNoProfileLayout() = noProfileLayout.fadeOut()

    override fun showNoProfileLayout() = noProfileLayout.fadeIn()

    override fun hideEditIcon() = iconEdit.gone()

    override fun hideLogOutOption() = logOut.gone()

    override fun showEditIcon() = iconEdit.visible()

    override fun showLogOutOption() = logOut.visible()

    override fun showLogoutDialog() {
        activity?.run {
            showYesNoDialog(message = getString(R.string.info_logout_are_you_sure_alert),
                    yesAction = { presenter.logOutUser() })
        }
    }

    override fun sendLogOutFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.LOGIN, FeatureSelectedEvent.Action.LOG_OUT, "")

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

    override fun hideChangePasswordOption() = changePassword.fadeOut()

    override fun showChangePasswordOption() = changePassword.fadeIn()

    override fun goToEditProfileActivity(userResponse: UserResponse) {
        activity?.run { startActivityForResult(EditProfileActivity.getLaunchIntent(this, userResponse), REQUEST_CODE_USER_MODEL) }
    }

    override fun goToAboutShelter() {
        activity?.run { startActivity(WebViewActivity.getLaunchIntent(this, ABOUT_SHELTER)) }
    }

    override fun goToAboutAssociation() {
        activity?.run { startActivity(WebViewActivity.getLaunchIntent(this, ABOUT_ASSOCIATION)) }
    }

    override fun goToDonations() {
        activity?.run { startActivity(WebViewActivity.getLaunchIntent(this, DONATIONS)) }
    }

    override fun goToTermsAndConditions() {
        activity?.run { startActivity(WebViewActivity.getLaunchIntent(this, TERMS_AND_CONDITIONS)) }
    }

    override fun goToPrivacyPolicy() {
        activity?.run { startActivity(WebViewActivity.getLaunchIntent(this, PRIVACY_POLICY)) }
    }

    override fun goToLoginActivity() {
        activity?.run { startActivityForResult(LoginActivity.getLaunchIntent(this), LOGIN) }
    }

    override fun goToRegisterActivity() {
        activity?.run { startActivityForResult(RegisterActivity.getLaunchIntent(this), REGISTER) }
    }

    override fun onCancel() {}

    override fun onSuccess(result: LoginResult?) {
        result?.accessToken?.token?.let { presenter.handleFacebookLogin(hasInternet(), it) }
    }

    override fun onError(error: FacebookException?) {}

    override fun startFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList<String>(*FACEBOOK_PERMISSIONS))
    }

    override fun successFbLogin() = presenter.getMe(hasInternet())

    override fun goToEditPasswordActivity() {
        activity?.run { startActivity(EditPasswordActivity.getLaunchIntent(this)) }
    }

    override fun sendUserLoggedInEvent() = EventBus.getDefault().postSticky(UserEvent(true))

    override fun sendUserLoggedOutEvent() = EventBus.getDefault().postSticky(UserEvent(false))

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_USER_MODEL -> {
                    val userModel = data?.getSerializableExtra(USER_MODEL) as UserResponse
                    presenter.updateUserModel(userModel)
                }
                REGISTER, LOGIN -> {
                    presenter.getMe(hasInternet())
                    sendUserLoggedInEvent()
                }
                else -> callbackManager?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: UserEvent) {
        if (event.isLogged) {
            presenter.getMe(hasInternet())
        }
    }

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}