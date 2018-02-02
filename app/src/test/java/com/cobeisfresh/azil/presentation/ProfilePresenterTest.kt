package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.*
import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.profile.ProfileInterface
import com.cobeisfresh.azil.utils.TestDisposable
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Zerina Salitrezic on 09/10/2017.
 */

class ProfilePresenterTest {

    private lateinit var presenter: ProfilePresenter

    private val view: ProfileInterface.View = mock()
    private val userInteractor: UserInteractor = mock()
    private val sharedPrefs: SharedPrefs = mock()

    @Before
    fun setUp() {
        presenter = ProfilePresenter(userInteractor, sharedPrefs)
        presenter.setView(view)
    }

    @Test
    fun onSwipeToRefreshUserNotLoggedIn() {
        presenter.onSwipeToRefresh(true)

        verify(sharedPrefs).isLoggedIn()
        verify(view).hideProgressBar()
        verify(view).hideEditIcon()
        verify(view).hideLogOutOption()
        verify(view).showProfileImagePlaceholder()
        verify(view).hideProfileLayout()
        verify(view).showNoProfileLayout()
        verify(view).stopRefreshing()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onSwipeToRefreshNoInternet() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.onSwipeToRefresh(false)

        verify(sharedPrefs).isLoggedIn()
        verify(view).hideProgressBar()
        verify(view).showNoInternetError()
        verify(view).stopRefreshing()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun onSwipeToRefreshShouldSendGetMeRequest() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.onSwipeToRefresh(true)

        verify(sharedPrefs).isLoggedIn()
        verify(view).hideProgressBar()
        verify(userInteractor).getMe(any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getMeUserNotLoggedIn() {
        presenter.getMe(false)

        verify(view).hideEditIcon()
        verify(view).hideLogOutOption()
        verify(view).showProfileImagePlaceholder()
        verify(view).hideProfileLayout()
        verify(view).showNoProfileLayout()
        verify(sharedPrefs).isLoggedIn()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getMeNoInternet() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.getMe(false)

        verify(sharedPrefs).isLoggedIn()
        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getMeShouldSendRequestGetMe() {
        whenever(sharedPrefs.isLoggedIn()).thenReturn(true)
        presenter.getMe(true)

        verify(sharedPrefs).isLoggedIn()
        verify(view).showProgressBar()
        verify(userInteractor).getMe(any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getMeObserverOnSubscribe() {
        presenter.getMeObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getMeObserverOnSuccessValidResponse() {
        presenter.getMeObserver().onSuccess(mockUserResponse())

        verify(view).hideProgressBar()
        verify(view).showProfileLayout()
        verify(view).hideNoProfileLayout()
        verify(view).showEditIcon()
        verify(view).stopRefreshing()
        verify(view).showLogOutOption()
        verify(view).showName(any())
        verify(view).showEmail(any())
        verify(view).showProfileImage(any())
        verify(view).showChangePasswordOption()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getMeObserverOnError() {
        presenter.getMeObserver().onError(null)

        verify(view).hideProgressBar()
        verify(view).hideEditIcon()
        verify(view).hideLogOutOption()
        verify(view).stopRefreshing()
        verify(view).showProfileImagePlaceholder()
        verify(view).hideProfileLayout()
        verify(view).showNoProfileLayout()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun handleChangePasswordVisibilityFbUser() {
        presenter.handleChangePasswordVisibility(true)

        verify(view).hideChangePasswordOption()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun handleChangePasswordVisibilityNotFbUser() {
        presenter.handleChangePasswordVisibility(false)

        verify(view).showChangePasswordOption()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun showNameEmptyName() {
        presenter.showName("")

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun showNameValidName() {
        presenter.showName(NAME)

        verify(view).showName(any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun showEmailEmptyEmail() {
        presenter.showEmail("")

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun showEmailValidEmail() {
        presenter.showEmail(EMAIL)

        verify(view).showEmail(any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun showProfileImageEmptyUrl() {
        presenter.showProfileImage("")

        verify(view).showProfileImagePlaceholder()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun showProfileImageValidUrl() {
        presenter.showProfileImage(IMAGE_URL)

        verify(view).showProfileImage(any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun updateUserModelNullData() {
        presenter.updateUserModel(null)

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun updateUserModelEmptyData() {
        presenter.updateUserModel(UserResponse())

        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun updateUserModelValidData() {
        presenter.updateUserModel(mockUserResponse())

        verify(view).showName(any())
        verify(view).showProfileImage(any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun handleFacebookLoginNoInternet() {
        presenter.handleFacebookLogin(false, TOKEN)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun handleFacebookLoginShouldRequestLoginUserWithFacebook() {
        presenter.handleFacebookLogin(true, TOKEN)

        verify(view).showProgressBar()
        verify(userInteractor).loginUserWithFacebook(any(), any())
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getFbLoginObserverOnSubscribe() {
        presenter.getFbLoginObserver().onSubscribe(TestDisposable())

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getFbLoginObserverOnSuccess() {
        presenter.getFbLoginObserver().onSuccess(mockLoginResponse())

        verify(view).hideProgressBar()
        verify(sharedPrefs).setLoggedIn(any())
        verify(sharedPrefs).setToken(any())
        verify(view).sendUserLoggedInEvent()
        verify(view).successFbLogin()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun getFbLoginObserverOnError() {
        presenter.getFbLoginObserver().onError(mockHttpResponse(ERROR_CODE_BAD_REQUEST))

        verify(view).hideProgressBar()
        verify(view).showServerError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun editProfileClickedNoUserResponse() {
        presenter.editProfileClicked()

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun logOutClickedShouldShowLogoutDialog() {
        presenter.logOutClicked()

        verify(view).showLogoutDialog()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun logOutUserShouldResetPrefs() {
        presenter.logOutUser()

        verify(sharedPrefs).setLoggedIn(any())
        verify(sharedPrefs).setToken(any())
        verify(view).sendUserLoggedOutEvent()
        verify(view).hideEditIcon()
        verify(view).hideLogOutOption()
        verify(view).showProfileImagePlaceholder()
        verify(view).hideProfileLayout()
        verify(view).showNoProfileLayout()
        verify(view).sendLogOutFeatureSelectedEvent()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun aboutShelterClickedShouldGoToAboutShelter() {
        presenter.aboutShelterClicked()

        verify(view).goToAboutShelter()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun aboutAssociationClickedShouldGoToAboutAssociation() {
        presenter.aboutAssociationClicked()

        verify(view).goToAboutAssociation()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun donateClickedShouldGoToDonations() {
        presenter.donateClicked()

        verify(view).goToDonations()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun termsAndConditionsClickedShouldGoToTermsAndConditions() {
        presenter.termsAndConditionsClicked()

        verify(view).goToTermsAndConditions()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun privacyPolicyClickedShouldGoToPrivacyPolicy() {
        presenter.privacyPolicyClicked()

        verify(view).goToPrivacyPolicy()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun facebookLoginClickedNoInternet() {
        presenter.facebookLoginClicked(false)

        verify(view).showNoInternetError()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun facebookLoginClickedShouldStartFacebookLogin() {
        presenter.facebookLoginClicked(true)

        verify(view).startFacebookLogin()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun loginClickedShouldGoToLoginActivity() {
        presenter.loginClicked()

        verify(view).goToLoginActivity()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun registerClickedShouldGoToRegisterActivity() {
        presenter.registerClicked()

        verify(view).goToRegisterActivity()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun changePasswordClickedShouldGoToEditPassword() {
        presenter.changePasswordClicked()

        verify(view).goToEditPasswordActivity()
        verifyNoMoreInteractions(view, userInteractor, sharedPrefs)
    }

    @Test
    fun unSubscribe() {
        presenter.unSubscribe()

        verifyZeroInteractions(view, userInteractor, sharedPrefs)
    }
}