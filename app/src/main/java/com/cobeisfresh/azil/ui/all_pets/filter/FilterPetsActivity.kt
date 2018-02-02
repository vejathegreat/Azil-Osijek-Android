package com.cobeisfresh.azil.ui.all_pets.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.constants.FILTER_DATA
import com.cobeisfresh.azil.common.extensions.onClick
import com.cobeisfresh.azil.data.models.FilterData
import com.cobeisfresh.azil.data.models.FilterModel
import com.cobeisfresh.azil.ui.all_pets.filter.filters.FiltersAdapter
import com.cobeisfresh.azil.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_filter_pets.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 04/10/2017.
 */

class FilterPetsActivity : BaseActivity(), FilterPetsInterface.View {

    @Inject
    lateinit var presenter: FilterPetsInterface.Presenter

    private lateinit var filtersAdapter: FiltersAdapter

    companion object {
        fun getLaunchIntent(from: Context, filterData: FilterData): Intent {
            val intent = Intent(from, FilterPetsActivity::class.java)
            intent.putExtra(FILTER_DATA, filterData)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_filter_pets)
        presenter.setView(this)
        initUi()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.FILTER_PETS)
    }

    override fun initUi() {
        setFiltersAdapter()
        getExtras()
        setListeners()
    }

    private fun getExtras() {
        val intent = intent
        intent.getSerializableExtra(FILTER_DATA)?.let {
            val filterData = it as FilterData
            presenter.setFilterData(filterData)
        }
    }

    private fun setFiltersAdapter() {
        filtersAdapter = FiltersAdapter(
                onAddFilter = { presenter.addFilter(it) },
                onRemoveFilter = { presenter.removeFilter(it) })
        filtersRecyclerView.layoutManager = LinearLayoutManager(this)
        filtersRecyclerView.adapter = filtersAdapter
    }

    private fun setListeners() {
        closeButton.onClick { presenter.filterCloseButtonClicked() }
        fetchResultsButton.onClick { presenter.fetchResultsButtonClicked() }
        resetButton.onClick { presenter.resetButtonClicked() }
    }

    override fun setFilters(filters: MutableList<FilterModel>) = filtersAdapter.setFilters(filters)

    override fun goBack(filterData: FilterData) {
        val returnIntent = Intent()
        returnIntent.putExtra(FILTER_DATA, filterData)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun goBack() = finish()

    override fun unCheckAllFilters() = filtersAdapter.unCheckAllFilters()

    override fun onBackPressed() = presenter.backButtonClicked()
}