package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.extensions.isValid
import com.cobeisfresh.azil.common.extensions.isValidEmail
import com.cobeisfresh.azil.common.extensions.isValidName
import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.models.AdoptDogData
import com.cobeisfresh.azil.data.models.isAdoptDogFormValid
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.data.response.UserResponse
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.network_interaction.UserInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.adopt_pet.AdoptPetInterface
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * Created by Zerina Salitrezic on 07/08/2017.
 */

class AdoptPetPresenter(private val dogsInteractor: DogsInteractor, private val userInteractor: UserInteractor,
                        private val sharedPrefs: SharedPrefs) : AdoptPetInterface.Presenter {

    private lateinit var view: AdoptPetInterface.View
    private val compositeDisposable = CompositeDisposable()
    internal var adoptDogData: AdoptDogData = AdoptDogData()

    override fun setView(view: AdoptPetInterface.View) {
        this.view = view
    }

    override fun setDogData(dogDetailsResponse: DogDetailsResponse) = with(dogDetailsResponse) {
        adoptDogData.petId = id
        showImage(photo)
        showName(name)
    }

    internal fun showImage(images: List<String>) {
        if (images.isNotEmpty() && images[0].isValid()) {
            view.showImage(images[0])
        } else {
            view.showPlaceholder()
        }
    }

    internal fun showName(name: String) = validateString(name, onValid = { view.showName(name) })

    override fun getMe(hasInternet: Boolean) {
        if (sharedPrefs.isLoggedIn()) {
            if (!hasInternet) {
                view.showNoInternetError()
                return
            }

            userInteractor.getMe(getMeObserver())
        }
    }

    internal fun getMeObserver(): SingleObserver<UserResponse> {
        return object : SingleObserver<UserResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: UserResponse) = setUserData(response)

            override fun onError(e: Throwable?) {}
        }
    }

    private fun setUserData(user: UserResponse) = with(user) {
        validateString(value = email, onValid = { view.showUserEmail(it) })
        validateString(value = name, onValid = { view.showUserName(it) })
        checkNameValidation()
        checkEmailValidation()
    }

    override fun adoptPetClicked(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            view.showProgressBar()
            view.disableAdopt()
            dogsInteractor.adoptDog(adoptDogData, getAdoptDogObserver())
        }
    }

    internal fun getAdoptDogObserver(): SingleObserver<BaseResponse> {
        return object : SingleObserver<BaseResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: BaseResponse) {
                view.sendAdoptionFeatureSelectedEvent()
                view.hideProgressBar()
                view.showSuccessAdoptionLayout()
                Observable.timer(2, TimeUnit.SECONDS).subscribe(bindTimerConsumer())
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.enableAdopt()
                view.showServerError()
            }
        }
    }

    internal fun bindTimerConsumer(): Consumer<Long> = Consumer {
        view.goBack()
    }

    override fun setIsAcceptBrochure(isChecked: Boolean) {
        adoptDogData.isAcceptBrochure = isChecked
        checkIsFormValid()
    }

    override fun fullNameChanged(fullName: String) {
        adoptDogData.name = fullName
        view.removeNameError()
        checkIsFormValid()
    }

    override fun emailChanged(email: String) {
        adoptDogData.email = email
        view.removeEmailError()
        checkIsFormValid()
    }

    override fun messageChanged(message: String) {
        adoptDogData.message = message
    }

    internal fun checkIsFormValid() {
        if (adoptDogData.isAdoptDogFormValid()) {
            view.enableAdopt()
        } else {
            view.disableAdopt()
        }
    }

    override fun onNameChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkNameValidation()
        } else {
            view.setNameBlueLine()
        }
    }

    override fun onEmailChangeFocus(hasFocus: Boolean) {
        if (!hasFocus) {
            checkEmailValidation()
        } else {
            view.setEmailBlueLine()
        }
    }

    private fun checkNameValidation() {
        if (adoptDogData.name.isValidName()) {
            view.setNameGreenLine()
            view.removeNameError()
        } else {
            view.setNameRedLine()
            view.setNameError()
        }
    }

    private fun checkEmailValidation() {
        if (adoptDogData.email.isValidEmail()) {
            view.setEmailGreenLine()
            view.removeEmailError()
        } else {
            view.setEmailRedLine()
            view.setEmailError()
        }
    }

    override fun successAdoptionLayoutClicked() = view.goBack()

    override fun iconBackClicked() = view.goBack()

    override fun touchRootLayout() {
        view.clearFocusFromInputFields()
        view.hideKeyboard()
        view.hideErrorLayout()
    }

    override fun unSubscribe() = compositeDisposable.clear()
}