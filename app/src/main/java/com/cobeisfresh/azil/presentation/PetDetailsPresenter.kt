package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.models.PetImagesData
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.pet_details.PetDetailsInterface
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 03/08/2017.
 */

class PetDetailsPresenter(private val dogsInteractor: DogsInteractor, private val mSharedPrefs: SharedPrefs) : PetDetailsInterface.Presenter {

    private lateinit var view: PetDetailsInterface.View
    private val compositeDisposable = CompositeDisposable()
    internal var dogId: String = ""
    internal var dogDetailsResponse: DogDetailsResponse? = null
    internal var hasCurrentlyFavourite: Boolean = false

    override fun setView(view: PetDetailsInterface.View) {
        this.view = view
    }

    override fun setDogId(dogId: String) {
        this.dogId = dogId
    }

    override fun userAuthenticated() = view.refreshPetDetails()

    override fun errorFullLayoutClicked(hasInternet: Boolean) {
        view.hideErrorFullScreen()
        view.showProgressBar()
        if (!hasInternet) {
            view.hideProgressBar()
            view.showNoInternetErrorFullScreen()
        } else {
            dogsInteractor.getDogById(dogId, getDogObserver())
        }
    }

    override fun errorLayoutClicked() = view.hideErrorLayout()

    override fun getDogById(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetErrorFullScreen()
        } else {
            view.showProgressBar()
            dogsInteractor.getDogById(dogId, getDogObserver())
        }
    }

    internal fun getDogObserver(): SingleObserver<DogDetailsResponse> {
        return object : SingleObserver<DogDetailsResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: DogDetailsResponse) {
                view.hideProgressBar()
                view.showDogDetailsLayout()
                view.showAdoptButton()
                handleResponse(response)
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.hideDogDetailsLayout()
                view.showServerErrorFullScreen()
            }
        }
    }

    private fun handleResponse(response: DogDetailsResponse) = with(response) {
        dogDetailsResponse = this
        setIsFavourite(isFavorite)
        setImages(photo)
        showName(name)
        showGender(gender)
        showAge(age)
        showSize(size)
        showLongDescription(longDescription)
    }

    internal fun setIsFavourite(isFavorite: Boolean) {
        hasCurrentlyFavourite = isFavorite
        if (isFavorite) {
            view.setFavouriteIcon()
        } else {
            view.setNotFavouriteIcon()
        }
    }

    internal fun setImages(images: List<String>) {
        view.showImages(images)
        handlePageIndicatorVisibility(images.size)
    }

    internal fun handlePageIndicatorVisibility(size: Int) {
        if (size > 1) {
            view.showPageIndicator()
        }
    }

    internal fun showName(name: String) {
        validateString(value = name, onValid = { view.showName(name) })
    }

    internal fun showGender(gender: String) {
        validateString(value = gender, onValid = { view.showGender(it) })
    }

    internal fun showAge(age: String) {
        validateString(value = age, onValid = { view.showAge(it) })
    }

    internal fun showSize(size: String) {
        validateString(value = size, onValid = { view.showSize(it) })
    }

    internal fun showLongDescription(longDescription: String) {
        validateString(value = longDescription, onValid = { view.showDescription(longDescription) })
    }

    override fun iconFavouriteClicked(hasInternet: Boolean) = when {
        !mSharedPrefs.isLoggedIn() -> view.showUserMustExistError()
        !hasInternet -> view.showNoInternetError()
        else -> {
            view.hideFavouriteIcon()
            view.showFavouritesProgressBar()
            dogsInteractor.addRemoveFavourite(dogId, getAddRemoveObserver())
        }
    }

    internal fun getAddRemoveObserver(): SingleObserver<BaseResponse> {
        return object : SingleObserver<BaseResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.clear()
            }

            override fun onSuccess(response: BaseResponse) {
                view.hideFavouritesProgressBar()
                hasCurrentlyFavourite = !hasCurrentlyFavourite
                view.sendFavouriteStateEvent(hasCurrentlyFavourite, dogId)
                handleFavouriteState()
            }

            override fun onError(e: Throwable?) {
                view.hideFavouritesProgressBar()
                handleFavouriteState()
                view.showServerError()
            }
        }
    }

    internal fun handleFavouriteState() {
        if (hasCurrentlyFavourite) {
            view.setFavouriteIcon()
        } else {
            view.setNotFavouriteIcon()
        }
    }

    override fun iconBackClicked() = view.goBack()

    override fun adoptPetClicked() {
        dogDetailsResponse?.run { view.openAdoptPetActivity(this) }
    }

    override fun imageClicked(petImagesData: PetImagesData) = view.openPetImagesActivity(petImagesData)

    override fun unSubscribe() = compositeDisposable.clear()
}