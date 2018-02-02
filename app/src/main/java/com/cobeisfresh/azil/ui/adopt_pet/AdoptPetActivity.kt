package com.cobeisfresh.azil.ui.adopt_pet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.FeatureSelectedEvent
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.common.utils.loadImage
import com.cobeisfresh.azil.common.utils.loadPlaceholder
import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_adopt_pet.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.success_adoption_layout.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 04/08/2017.
 */

class AdoptPetActivity : BaseActivity(), AdoptPetInterface.View {

    @Inject
    lateinit var presenter: AdoptPetInterface.Presenter

    companion object {
        private const val DOG = "dog"

        fun getLaunchIntent(from: Context, dogDetailsResponse: DogDetailsResponse): Intent {
            val intent = Intent(from, AdoptPetActivity::class.java)
            intent.putExtra(DOG, dogDetailsResponse)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_adopt_pet)
        presenter.setView(this)
        initUi()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.ADOPT_PET)
    }

    override fun initUi() {
        setDefaultLineColors()
        presenter.getMe(hasInternet())
        getExtras()
        setListeners()
    }

    private fun setDefaultLineColors() {
        fullName.changeLineColor(color(R.color.platinum))
        email.changeLineColor(color(R.color.platinum))
        adoptionMessage.changeLineColor(color(R.color.platinum))
    }

    private fun getExtras() {
        val intent: Intent = intent
        intent.getSerializableExtra(DOG)?.run {
            val dogDetailsModel = this as DogDetailsResponse
            presenter.setDogData(dogDetailsModel)
        }
    }

    private fun setListeners() {
        fullName.onFocusChange { presenter.onNameChangeFocus(it) }
        email.onFocusChange { presenter.onEmailChangeFocus(it) }
        acceptBrochure.onCheckedChange { presenter.setIsAcceptBrochure(it) }
        adoptPet.onClick { presenter.adoptPetClicked(hasInternet()) }
        iconBack.onClick { presenter.iconBackClicked() }
        successAdoptionLayout.onClick { presenter.successAdoptionLayoutClicked() }
        rootLayout.onTouch { presenter.touchRootLayout() }
        fullName.onTextChange { presenter.fullNameChanged(it) }
        email.onTextChange { presenter.emailChanged(it) }
        adoptionMessage.onTextChange { presenter.messageChanged(it) }
    }

    override fun clearFocusFromInputFields() {
        fullName.clearFocus()
        email.clearFocus()
        adoptionMessage.clearFocus()
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun hideKeyboard() = rootLayout.hideKeyboard()

    override fun showImage(image: String) = loadImage(petPhoto, image)

    override fun showPlaceholder() = loadPlaceholder(petPhoto, R.drawable.dog_placeholder)

    override fun showName(name: String) {
        petName.text = name
    }

    override fun showEmailInvalidError() {
        email.error = getString(R.string.error_email_invalid)
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

    override fun showSuccessAdoptionLayout() = successAdoptionLayout.visible()

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showUserEmail(emailText: String) {
        email.setText(emailText)
    }

    override fun showUserName(name: String) {
        fullName.setText(name)
    }

    override fun setEmailBlueLine() = email.changeLineColor(color(R.color.turquoise))

    override fun setNameBlueLine() = fullName.changeLineColor(color(R.color.turquoise))

    override fun setEmailGreenLine() = email.changeLineColor(color(R.color.green))

    override fun setNameGreenLine() = fullName.changeLineColor(color(R.color.green))

    override fun setEmailRedLine() = email.changeLineColor(color(R.color.red))

    override fun setNameRedLine() = fullName.changeLineColor(color(R.color.red))

    override fun setNameError() = fullName.setInputError(getString(R.string.registration_first_last_name_invalid_error))

    override fun setEmailError() = email.setInputError(getString(R.string.registration_email_invalid_error))

    override fun removeEmailError() = email.removeInputError()

    override fun removeNameError() = fullName.removeInputError()

    override fun enableAdopt() = adoptPet.enable()

    override fun disableAdopt() = adoptPet.disable()

    override fun goBack() = finish()

    override fun sendAdoptionFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.ADOPTION, FeatureSelectedEvent.Action.SEND_ADOPTION_REQUEST, "")

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}