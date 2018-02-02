package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.constants.ERROR_CODE_BAD_REQUEST
import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.response.LoginResponse
import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.profile.ProfileInterface
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class ProfilePresenter(private val userInteractor: UserInteractor,
                       private val sharedPrefs: SharedPrefs) : ProfileInterface.Presenter {

    private lateinit var view: ProfileInterface.View
    private val compositeDisposable = CompositeDisposable()
    private var userResponse: UserResponse? = null

    override fun setView(view: ProfileInterface.View) {
        this.view = view
    }

    override fun getMe(hasInternet: Boolean) = when {
        !sharedPrefs.isLoggedIn() -> handleNoProfile()
        !hasInternet -> view.showNoInternetError()
        else -> {
            view.showProgressBar()
            userInteractor.getMe(getMeObserver())
        }
    }

    override fun onSwipeToRefresh(hasInternet: Boolean) {
        view.hideProgressBar()
        when {
            !sharedPrefs.isLoggedIn() -> {
                handleNoProfile()
                view.stopRefreshing()
            }
            !hasInternet -> {
                view.showNoInternetError()
                view.stopRefreshing()
            }
            else -> userInteractor.getMe(getMeObserver())
        }
    }

    internal fun getMeObserver(): SingleObserver<UserResponse> {
        return object : SingleObserver<UserResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: UserResponse) {
                view.hideProgressBar()
                view.stopRefreshing()
                handleExistProfile()
                handleUserResponse(response)
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.stopRefreshing()
                handleNoProfile()
            }
        }
    }

    private fun handleExistProfile() {
        view.showProfileLayout()
        view.hideNoProfileLayout()
        view.showEditIcon()
        view.showLogOutOption()
    }

    private fun handleNoProfile() {
        view.hideEditIcon()
        view.hideLogOutOption()
        view.showProfileImagePlaceholder()
        view.hideProfileLayout()
        view.showNoProfileLayout()
    }

    private fun handleUserResponse(response: UserResponse) = with(response) {
        userResponse = this
        showName(name)
        showEmail(email)
        showProfileImage(profileImage)
        handleChangePasswordVisibility(isFacebookUser)
    }

    internal fun handleChangePasswordVisibility(isFacebookUser: Boolean) {
        if (isFacebookUser) {
            view.hideChangePasswordOption()
        } else {
            view.showChangePasswordOption()
        }
    }

    internal fun showName(name: String) {
        validateString(value = name, onValid = { view.showName(it) })
    }

    internal fun showEmail(email: String) {
        validateString(value = email, onValid = { view.showEmail(it) })
    }

    internal fun showProfileImage(avatar: String) {
        validateString(value = avatar,
                onValid = { view.showProfileImage(it) },
                onInvalid = {
                    userResponse?.profileImage = ""
                    view.showProfileImagePlaceholder()
                })
    }

    override fun updateUserModel(userResponse: UserResponse?) {
        userResponse?.run {
            with(this) {
                updateName(name)
                updateProfileImage(profileImage)
            }
        }
    }

    private fun updateName(name: String) {
        validateString(value = name, onValid = {
            userResponse?.name = it
            showName(it)
        })
    }

    private fun updateProfileImage(avatar: String) {
        validateString(value = avatar, onValid = {
            userResponse?.profileImage = it
            showProfileImage(it)
        })
    }

    override fun handleFacebookLogin(hasInternet: Boolean, fbToken: String) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            view.showProgressBar()
            userInteractor.loginUserWithFacebook(fbToken, getFbLoginObserver())
        }
    }

    internal fun getFbLoginObserver(): SingleObserver<LoginResponse> {
        return object : SingleObserver<LoginResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: LoginResponse) {
                view.hideProgressBar()
                handleLoginResponse(response)
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()

                if (e is HttpException) {
                    handleErrorCode((e).code())
                }
            }
        }
    }

    private fun handleErrorCode(code: Int) {
        if (code == ERROR_CODE_BAD_REQUEST) {
            view.showServerError()
        }
    }

    private fun handleLoginResponse(response: LoginResponse) {
        sharedPrefs.setLoggedIn(true)
        sharedPrefs.setToken(response.token)
        view.sendUserLoggedInEvent()
        view.successFbLogin()
    }

    override fun editProfileClicked() {
        userResponse?.run { view.goToEditProfileActivity(this) }
    }

    override fun logOutClicked() = view.showLogoutDialog()

    override fun logOutUser() {
        resetPrefs()
        handleNoProfile()
        view.sendLogOutFeatureSelectedEvent()
        view.sendUserLoggedOutEvent()
    }

    private fun resetPrefs() = with(sharedPrefs) {
        setLoggedIn(false)
        setToken("")
    }

    override fun aboutShelterClicked() = view.goToAboutShelter()

    override fun aboutAssociationClicked() = view.goToAboutAssociation()

    override fun donateClicked() = view.goToDonations()

    override fun termsAndConditionsClicked() = view.goToTermsAndConditions()

    override fun privacyPolicyClicked() = view.goToPrivacyPolicy()

    override fun facebookLoginClicked(hasInternet: Boolean) {
        if (hasInternet) {
            view.startFacebookLogin()
        } else {
            view.showNoInternetError()
        }
    }

    override fun loginClicked() = view.goToLoginActivity()

    override fun registerClicked() = view.goToRegisterActivity()

    override fun changePasswordClicked() = view.goToEditPasswordActivity()

    override fun unSubscribe() = compositeDisposable.clear()
}