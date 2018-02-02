package com.cobeisfresh.azil.presentation

import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.network_interaction.DogsInteractor
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.cobeisfresh.azil.ui.my_pets.MyPetsInterface
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

class MyPetsPresenter(private val dogsInteractor: DogsInteractor,
                      private val sharedPrefs: SharedPrefs) : MyPetsInterface.Presenter {

    private lateinit var view: MyPetsInterface.View
    private val compositeDisposable = CompositeDisposable()
    internal var page = 1

    override fun setView(view: MyPetsInterface.View) {
        this.view = view
    }

    override fun setIsFirstPage(shouldReset: Boolean) {
        page = if (shouldReset) 1 else page + 1
    }

    override fun errorFullScreenClicked(hasInternet: Boolean) {
        view.hideErrorFullScreen()
        view.showProgressBar()
        if (!hasInternet) {
            view.hideProgressBar()
            view.showNoInternetErrorFullScreen()
        } else {
            getDogs()
        }
    }

    override fun getMyDogs(hasInternet: Boolean) {
        if (!sharedPrefs.isLoggedIn()) {
            return
        }
        if (!hasInternet) {
            view.hideDataLayout()
            view.showNoInternetErrorFullScreen()
        } else {
            view.showProgressBar()
            getDogs()
        }
    }

    private fun getDogs() = dogsInteractor.getMyDogs(page, getMyDogsObserver())

    internal fun getMyDogsObserver(): SingleObserver<List<DogModel>> {
        return object : SingleObserver<List<DogModel>> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onSuccess(response: List<DogModel>) {
                view.hideProgressBar()

                if (response.isNotEmpty()) {
                    view.showDataLayout()
                    setMyDogs(response)
                } else {
                    view.hideDataLayout()
                    view.showNoDataLayout()
                }
            }

            override fun onError(e: Throwable?) {
                view.hideProgressBar()
                view.showServerErrorFullScreen()
                view.hideDataLayout()
                view.stopRefreshing()
            }
        }
    }

    internal fun setMyDogs(myDogs: List<DogModel>) {
        if (page == 1) {
            view.setMyDogs(myDogs)
            view.stopRefreshing()
        } else {
            view.addMoreDogs(myDogs)
        }
    }

    override fun onLastItemReached(hasInternet: Boolean) {
        if (!hasInternet) {
            view.showNoInternetErrorFullScreen()
        } else {
            getDogs()
        }
    }

    override fun onSwipeToRefresh(hasInternet: Boolean) {
        view.hideProgressBar()
        if (!hasInternet) {
            view.stopRefreshing()
            view.showNoInternetErrorFullScreen()
        } else {
            getDogs()
        }
    }

    override fun checkExistingFavourites(numberOfFavourites: Int) {
        if (numberOfFavourites == 0) {
            view.showNoDataLayout()
            view.hideDataLayout()
        } else {
            view.stopRefreshing()
            view.showDataLayout()
            view.hideNoDataLayout()
        }
    }

    override fun unSubscribe() = compositeDisposable.clear()
}