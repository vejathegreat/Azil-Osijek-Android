package com.cobeisfresh.azil.ui.all_pets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.FeatureSelectedEvent
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.FILTER_DATA
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.data.event.DeletedDogEvent
import com.cobeisfresh.azil.data.event.FavouriteStateEvent
import com.cobeisfresh.azil.data.event.UserEvent
import com.cobeisfresh.azil.data.models.DogModel
import com.cobeisfresh.azil.data.models.FilterData
import com.cobeisfresh.azil.data.models.FilterModel
import com.cobeisfresh.azil.ui.all_pets.filter.FilterPetsActivity
import com.cobeisfresh.azil.ui.all_pets.filter.checked_filters.CheckedFiltersAdapter
import com.cobeisfresh.azil.ui.all_pets.list.AllPetsAdapter
import com.cobeisfresh.azil.ui.base.BaseFragment
import com.cobeisfresh.azil.ui.pet_details.PetDetailsActivity
import com.cobeisfresh.azil.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.entered_search_layout.*
import kotlinx.android.synthetic.main.error_full_layout.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_all_pets.*
import kotlinx.android.synthetic.main.search_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 02/08/2017.
 */

class AllPetsFragment : BaseFragment(), AllPetsInterface.View {

    @Inject
    lateinit var presenter: AllPetsInterface.Presenter

    private lateinit var allPetsAdapter: AllPetsAdapter
    private lateinit var checkedFiltersAdapter: CheckedFiltersAdapter

    companion object {
        private const val FILTER = 123
        private const val AUTH_FROM_ALL_PETS = 88

        fun newInstance(): AllPetsFragment = AllPetsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        presenter.setView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_all_pets, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUi()
        prepareData()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.ALL_PETS)
    }

    override fun prepareUi() {
        setPetsAdapter()
        setCheckedFiltersAdapter()
        setListeners()
    }

    private fun setPetsAdapter() {
        allPetsAdapter = AllPetsAdapter(
                onLastItemReached = { onLastItemReached() },
                onDogClick = { presenter.onDogClick(it) },
                onNoInternetError = { showNoInternetError() },
                onUserMustExistError = { showUserMustExistError() },
                onChangeFavouriteStateError = { showChangeFavouriteStateError() })
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = allPetsAdapter
    }

    private fun onLastItemReached() {
        presenter.setIsFirstPage(false)
        presenter.addMoreDogs()
    }

    override fun openPetDetailsActivity(dogId: String) {
        activity?.run { startActivity(PetDetailsActivity.getLaunchIntent(this, dogId)) }
    }

    private fun setCheckedFiltersAdapter() {
        checkedFiltersAdapter = CheckedFiltersAdapter { presenter.deleteChosenFilter(it) }
        checkedFiltersRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        checkedFiltersRecyclerView.adapter = checkedFiltersAdapter
    }

    private fun setListeners() {
        swipeRefreshLayout.onRefresh { onSwipeToRefresh() }
        recyclerView.onTouch { hideErrorLayout() }
        errorLayout.onClick { hideErrorLayout() }
        iconSearch.onClick { presenter.searchClicked() }
        iconFilter.onClick { presenter.filterClicked() }
        clearButton.onClick { presenter.clearSearchClicked() }
        dimView.onClick { presenter.dimViewClicked() }
        errorFullLayout.onClick { presenter.errorFullScreenClicked() }
        deleteEnteredSearchIcon.onClick { presenter.cancelSearchClicked() }
        searchInput.onTextChange { presenter.searchInputChanged(it) }
        searchInput.onActionSearchClicked { presenter.searchDogs() }
    }

    override fun prepareData() {
        presenter.setIsFirstPage(true)
        presenter.getDogs()
    }

    private fun onSwipeToRefresh() {
        presenter.setIsFirstPage(true)
        presenter.onSwipeToRefresh()
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

    fun showUserMustExistError() {
        activity?.run {
            showInfoDialogWithCancel(title = getString(R.string.add_to_favorites_login_required_alert_title),
                    message = getString(R.string.add_to_favorites_login_required_alert_message),
                    okAction = { startActivityForResult(WelcomeActivity.getLaunchIntent(this), AUTH_FROM_ALL_PETS) })
        }
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun setDogs(data: List<DogModel>) = allPetsAdapter.setDogs(data)

    override fun addMoreDogs(data: List<DogModel>) = allPetsAdapter.addMoreDogs(data)

    override fun showServerErrorFullScreen() {
        errorTextFullLayout.text = getString(R.string.server_error_full_screen)
        showErrorFullLayout()
        errorFullLayout.bringToFront()
    }

    override fun showNoInternetErrorFullScreen() {
        errorTextFullLayout.text = getString(R.string.error_no_internet_full_screen)
        showErrorFullLayout()
    }

    private fun showErrorFullLayout() {
        errorFullLayout.fadeIn()
        errorFullLayout.bringToFront()
    }

    override fun stopRefreshing() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun showSearchInput() {
        searchContainer.visible()
        searchContainer.bringToFront()
    }

    override fun hideSearchInput() = searchContainer.gone()

    override fun showEnteredSearch(searchTerm: String) {
        filterSearchLayout.visible()
        enteredSearchContainer.visible()
        enteredSearch.setText(searchTerm)
    }

    override fun hideEnteredSearch() = enteredSearchContainer.gone()

    override fun showDimView() = dimView.visible()

    override fun hideDimView() = dimView.gone()

    override fun clearSearchInput() = searchInput.text.clear()

    override fun getAllDogs() = prepareData()

    override fun showClearIcon() = clearButton.visible()

    override fun hideClearIcon() = clearButton.invisible()

    override fun hideKeyboard() = rootLayout.hideKeyboard()

    override fun requestFocusSearch() {
        searchInput.requestFocus()
    }

    override fun hideErrorFullScreen() = errorFullLayout.gone()

    private fun showChangeFavouriteStateError() {
        errorText.text = getString(R.string.server_error)
        showErrorLayout()
    }

    private fun showNoInternetError() {
        errorText.text = getString(R.string.error_no_internet)
        showErrorLayout()
    }

    private fun showErrorLayout() {
        errorLayout.fadeIn()
        errorLayout.bringToFront()
        errorLayout.bringToFront()
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
    }

    override fun openFilterPetsActivity(filterData: FilterData) {
        activity?.run { startActivityForResult(FilterPetsActivity.getLaunchIntent(this, filterData), FILTER) }
    }

    override fun removeCheckedFilter(filterValueKey: String) {
        checkedFiltersAdapter.removeCheckedFilter(filterValueKey)
    }

    override fun hideCheckedFiltersRecyclerView() = checkedFiltersRecyclerView.gone()

    override fun showCheckedFiltersList() {
        filterSearchLayout.visible()
        checkedFiltersRecyclerView.visible()
    }

    override fun hideFilterSearchLayout() = filterSearchLayout.gone()

    override fun sendSearchDogsByNameFeatureSelectedEvent() = sendScreenTrackingEvent(ScreenEvent.ScreenName.SEARCH_PETS_BY_NAME)

    override fun setCheckedFilters(filters: MutableList<FilterModel>) {
        checkedFiltersAdapter.setCheckedFilters(filters.filter { it.isChecked })
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: UserEvent) {
        if (!event.isLogged) {
            allPetsAdapter.removeFavourites()
        } else {
            prepareData()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: DeletedDogEvent) {
        allPetsAdapter.updateDogItem(event.id, false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: FavouriteStateEvent) {
        allPetsAdapter.updateDogItem(event.dogId, event.isFavourite)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FILTER -> {
                    val filterData = data?.getSerializableExtra(FILTER_DATA) as FilterData
                    presenter.setIsFirstPage(true)
                    presenter.filterDogs(filterData)
                }
                AUTH_FROM_ALL_PETS -> EventBus.getDefault().postSticky(UserEvent(true))
            }
        }
    }

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }

    override fun sendMaleFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.FILTER,
            FeatureSelectedEvent.Action.FILTER_GENDER_MALE, FeatureSelectedEvent.Value.GENDER_MALE)

    override fun sendFemaleFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.FILTER,
            FeatureSelectedEvent.Action.FILTER_GENDER_FEMALE, FeatureSelectedEvent.Value.GENDER_FEMALE)

    override fun sendPuppiesFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.FILTER,
            FeatureSelectedEvent.Action.FILTER_AGE_PUPPIES, FeatureSelectedEvent.Value.AGE_PUPPIES)

    override fun sendYoungFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.FILTER,
            FeatureSelectedEvent.Action.FILTER_AGE_YOUNG, FeatureSelectedEvent.Value.AGE_YOUNG)

    override fun sendAdultFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.FILTER,
            FeatureSelectedEvent.Action.FILTER_AGE_ADULT, FeatureSelectedEvent.Value.AGE_ADULT)

    override fun sendSmallFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.FILTER,
            FeatureSelectedEvent.Action.FILTER_SIZE_SMALL, FeatureSelectedEvent.Value.SIZE_SMALL)

    override fun sendMidiFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.FILTER,
            FeatureSelectedEvent.Action.FILTER_SIZE_MIDI, FeatureSelectedEvent.Value.SIZE_MIDI)

    override fun sendBigFeatureSelectedEvent() = sendFeatureTrackingEvent(FeatureSelectedEvent.Category.FILTER,
            FeatureSelectedEvent.Action.FILTER_SIZE_BIG, FeatureSelectedEvent.Value.SIZE_BIG)
}