package com.cobeisfresh.azil.ui.profile

import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

interface ProfileInterface {

    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun showName(name: String)

        fun showEmail(emailText: String)

        fun showProfileImage(image: String)

        fun showProfileImagePlaceholder()

        fun showProfileLayout()

        fun hideProfileLayout()

        fun showLogoutDialog()

        fun showNoInternetError()

        fun showServerError()

        fun goToEditProfileActivity(userResponse: UserResponse)

        fun hideNoProfileLayout()

        fun showNoProfileLayout()

        fun hideEditIcon()

        fun hideLogOutOption()

        fun showEditIcon()

        fun showLogOutOption()

        fun goToAboutShelter()

        fun goToAboutAssociation()

        fun goToDonations()

        fun goToTermsAndConditions()

        fun goToLoginActivity()

        fun goToRegisterActivity()

        fun startFacebookLogin()

        fun successFbLogin()

        fun hideChangePasswordOption()

        fun showChangePasswordOption()

        fun goToEditPasswordActivity()

        fun stopRefreshing()

        fun sendLogOutFeatureSelectedEvent()

        fun goToPrivacyPolicy()

        fun sendUserLoggedInEvent()

        fun sendUserLoggedOutEvent()
    }

    interface Presenter : BasePresenter<View> {

        fun getMe(hasInternet: Boolean)

        fun onSwipeToRefresh(hasInternet: Boolean)

        fun handleFacebookLogin(hasInternet: Boolean, fbToken: String)

        fun updateUserModel(userResponse: UserResponse?)

        fun editProfileClicked()

        fun logOutClicked()

        fun logOutUser()

        fun aboutShelterClicked()

        fun aboutAssociationClicked()

        fun donateClicked()

        fun termsAndConditionsClicked()

        fun privacyPolicyClicked()

        fun facebookLoginClicked(hasInternet: Boolean)

        fun loginClicked()

        fun registerClicked()

        fun changePasswordClicked()

        fun unSubscribe()
    }
}