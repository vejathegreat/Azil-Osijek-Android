package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.extensions.isValid
import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.all_pets.list.AllPetsHolderInterface
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 02/08/2017.
 */

class AllPetsHolderPresenter(private val dogsInteractor: DogsInteractor,
                             private val sharedPrefs: SharedPrefs) : AllPetsHolderInterface.Presenter {

    private lateinit var view: AllPetsHolderInterface.View
    private val compositeDisposable = CompositeDisposable()
    internal var dogId: String = ""
    internal var dogModel: DogModel = DogModel()
    internal var isCurrentlyFavourite: Boolean = false

    override fun setView(view: AllPetsHolderInterface.View) {
        this.view = view
    }

    override fun setDogModel(dog: DogModel) = with(dog) {
        dogModel = this
        dogId = id
        setIsFavourite(isFavorite)
        showName(name)
        showGender(gender)
        showAge(age)
        showSize(size)
        showDescription(shortDescription)
        showImage(photo)
    }

    internal fun setIsFavourite(isFavorite: Boolean) {
        isCurrentlyFavourite = isFavorite
        handleFavouriteState()
    }

    internal fun handleFavouriteState() {
        if (isCurrentlyFavourite) {
            view.setFavouriteIcon()
        } else {
            view.setNotFavouriteIcon()
        }
    }

    internal fun showName(name: String) {
        validateString(value = name, onValid = { view.showName(it) })
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

    internal fun showDescription(description: String) {
        validateString(value = description, onValid = { view.showDescription(description) })
    }

    internal fun showImage(images: List<String>) {
        if (images.isNotEmpty() && images[0].isValid()) {
            view.showImage(images[0])
        } else {
            view.showPlaceholder()
        }
    }

    override fun dogLayoutClicked() = view.callOnItemClick(dogId)

    override fun iconFavouriteClicked(hasInternet: Boolean) = when {
        !sharedPrefs.isLoggedIn() -> view.showUserMustExistError()
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
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: BaseResponse) {
                view.hideFavouritesProgressBar()
                isCurrentlyFavourite = !isCurrentlyFavourite
                handleFavouriteState()
                sendFavouriteEvent()
            }

            override fun onError(e: Throwable?) {
                view.hideFavouritesProgressBar()
                view.showChangeFavouriteStateError()
                handleFavouriteState()
            }
        }
    }

    internal fun sendFavouriteEvent() {
        dogModel.isFavorite = isCurrentlyFavourite
        if (isCurrentlyFavourite) {
            view.sendFavouritedDogEvent()
        } else {
            view.sendDogDeletedEvent(dogId)
        }
    }
}