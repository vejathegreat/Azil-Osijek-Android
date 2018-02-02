package com.cobeisfresh.azil.ui.my_pets

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.data.event.DeletedDogEvent
import com.cobeisfresh.azil.data.event.FavouriteStateEvent
import com.cobeisfresh.azil.data.event.FavouritedDogEvent
import com.cobeisfresh.azil.data.event.UserEvent
import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.ui.base.BaseFragment
import com.cobeisfresh.azil.ui.my_pets.list.MyPetsAdapter
import com.cobeisfresh.azil.ui.pet_details.PetDetailsActivity
import kotlinx.android.synthetic.main.error_full_layout.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_my_pets.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

class MyPetsFragment : BaseFragment(), MyPetsInterface.View {

    @Inject
    lateinit var presenter: MyPetsInterface.Presenter

    companion object {

        fun newInstance(): MyPetsFragment = MyPetsFragment()
    }

    private lateinit var myPetsAdapter: MyPetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        presenter.setView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_my_pets, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUi()
        prepareData()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.MY_PETS)
    }

    override fun prepareUi() {
        setAdapter()
        setListeners()
    }

    private fun setListeners() {
        swipeRefreshLayout.onRefresh {
            presenter.setIsFirstPage(true)
            presenter.onSwipeToRefresh(hasInternet())
        }
        recyclerView.onTouch { hideErrorLayout() }
        errorLayout.onClick { hideErrorLayout() }
        errorFullLayout.onClick { presenter.errorFullScreenClicked(hasInternet()) }
    }

    private fun setAdapter() {
        myPetsAdapter = MyPetsAdapter(
                onLastItemReached = { onLastItemReached() },
                onDogClick = { onDogItemClick(it) },
                onNoInternetError = { showNoInternetError() },
                onServerError = { showServerError() })
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = myPetsAdapter
    }

    override fun prepareData() {
        presenter.setIsFirstPage(true)
        presenter.getMyDogs(hasInternet())
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().removeAllStickyEvents()
        EventBus.getDefault().unregister(this)
    }

    private fun onLastItemReached() {
        presenter.setIsFirstPage(false)
        presenter.onLastItemReached(hasInternet())
    }

    private fun onDogItemClick(dogId: String) {
        activity?.run { startActivity(PetDetailsActivity.getLaunchIntent(this, dogId)) }
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun stopRefreshing() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun showNoInternetErrorFullScreen() {
        errorTextFullLayout.text = getString(R.string.error_no_internet_full_screen)
        errorFullLayout.fadeIn()
    }

    override fun showServerErrorFullScreen() {
        errorTextFullLayout.text = getString(R.string.server_error_full_screen)
        errorFullLayout.fadeIn()
    }

    override fun hideDataLayout() = swipeRefreshLayout.fadeOut()

    override fun showDataLayout() = swipeRefreshLayout.fadeIn()

    override fun setMyDogs(myDogs: List<DogModel>) = myPetsAdapter.setMyDogs(myDogs)

    override fun addMoreDogs(myDogs: List<DogModel>) = myPetsAdapter.addMoreDogs(myDogs)

    override fun hideErrorFullScreen() = errorFullLayout.fadeOut()

    override fun showServerError() {
        errorText.text = getString(R.string.server_error)
        showErrorLayout()
    }

    override fun showNoInternetError() {
        errorText.text = getString(R.string.error_no_internet)
        showErrorLayout()
    }

    private fun showErrorLayout() {
        errorLayout.fadeIn()
        errorLayout.bringToFront()
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun showNoDataLayout() = noDataLayout.fadeIn()

    override fun hideNoDataLayout() = noDataLayout.fadeOut()

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: UserEvent) {
        if (!event.isLogged) {
            myPetsAdapter.removeFavourites()
            showNoDataLayout()
            hideDataLayout()
        } else {
            prepareData()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: FavouritedDogEvent) {
        prepareData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: DeletedDogEvent) {
        myPetsAdapter.removeDogItem(event.id)
        presenter.checkExistingFavourites(myPetsAdapter.itemCount)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: FavouriteStateEvent) {
        prepareData()
    }

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}