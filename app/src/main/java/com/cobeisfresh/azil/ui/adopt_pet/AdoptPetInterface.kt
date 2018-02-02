package com.cobeisfresh.azil.ui.adopt_pet

import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.presentation.base.BasePresenter

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

interface AdoptPetInterface {

    interface View {

        fun showImage(image: String)

        fun showPlaceholder()

        fun showName(name: String)

        fun showEmailInvalidError()

        fun showNoInternetError()

        fun showServerError()

        fun showSuccessAdoptionLayout()

        fun showProgressBar()

        fun hideProgressBar()

        fun showUserEmail(emailText: String)

        fun showUserName(name: String)

        fun goBack()

        fun setEmailBlueLine()

        fun setNameBlueLine()

        fun setEmailGreenLine()

        fun setNameGreenLine()

        fun setEmailRedLine()

        fun setNameRedLine()

        fun removeEmailError()

        fun removeNameError()

        fun enableAdopt()

        fun disableAdopt()

        fun setNameError()

        fun setEmailError()

        fun sendAdoptionFeatureSelectedEvent()

        fun clearFocusFromInputFields()

        fun hideKeyboard()

        fun hideErrorLayout()
    }

    interface Presenter : BasePresenter<View> {

        fun setDogData(dogDetailsResponse: DogDetailsResponse)

        fun getMe(hasInternet: Boolean)

        fun iconBackClicked()

        fun adoptPetClicked(hasInternet: Boolean)

        fun setIsAcceptBrochure(isChecked: Boolean)

        fun fullNameChanged(fullName: String)

        fun emailChanged(email: String)

        fun messageChanged(message: String)

        fun onNameChangeFocus(hasFocus: Boolean)

        fun onEmailChangeFocus(hasFocus: Boolean)

        fun successAdoptionLayoutClicked()

        fun touchRootLayout()

        fun unSubscribe()
    }
}