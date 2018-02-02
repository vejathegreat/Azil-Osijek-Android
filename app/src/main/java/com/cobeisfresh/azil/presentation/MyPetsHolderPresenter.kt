package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.common.extensions.isValid
import com.cobeisfresh.azil.common.utils.validateString
import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.response.BaseResponse
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.ui.my_pets.list.MyPetsHolderInterface
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

class MyPetsHolderPresenter(private val dogsInteractor: DogsInteractor) : MyPetsHolderInterface.Presenter {

    private lateinit var view: MyPetsHolderInterface.View
    private val compositeDisposable = CompositeDisposable()
    private var dog: DogModel = DogModel()

    override fun setView(view: MyPetsHolderInterface.View) {
        this.view = view
    }

    override fun setDogModel(dogModel: DogModel) = with(dogModel) {
        dog = this
        view.setFavouriteIcon()
        showName(name)
        showGender(gender)
        showAge(age)
        showSize(size)
        showDescription(shortDescription)
        showImage(photo)
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

    override fun dogLayoutClicked() = view.callOnItemClick(dog.id)

    override fun iconFavouriteClicked() = view.showDeleteDialog(dog)

    override fun deleteFavourite(hasInternet: Boolean, dog: DogModel) {
        if (!hasInternet) {
            view.showNoInternetError()
        } else {
            view.hideFavouriteIcon()
            view.showFavouritesProgressBar()
            dogsInteractor.addRemoveFavourite(dog.id, getAddRemoveObserver())
        }
    }

    internal fun getAddRemoveObserver(): SingleObserver<BaseResponse> {
        return object : SingleObserver<BaseResponse> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: BaseResponse) {
                view.hideFavouritesProgressBar()
                view.sendDogDeletedEvent(dog.id)
            }

            override fun onError(e: Throwable?) {
                view.hideFavouritesProgressBar()
                view.setFavouriteIcon()
                view.showServerError()
            }
        }
    }
}