package com.cobeisfresh.azil.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.constants.ALL_PETS
import com.cobeisfresh.azil.common.constants.MY_PETS
import com.cobeisfresh.azil.common.constants.PROFILE
import com.cobeisfresh.azil.common.extensions.gone
import com.cobeisfresh.azil.common.extensions.onClick
import com.cobeisfresh.azil.common.extensions.showInfoDialogWithCancel
import com.cobeisfresh.azil.common.extensions.visible
import com.cobeisfresh.azil.data.event.UserEvent
import com.cobeisfresh.azil.ui.all_pets.AllPetsFragment
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.main.pager.MainPagerAdapter
import com.cobeisfresh.azil.ui.my_pets.MyPetsFragment
import com.cobeisfresh.azil.ui.profile.ProfileFragment
import com.cobeisfresh.azil.ui.welcome.WelcomeActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit
import javax.inject.Inject



/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */

class MainActivity : BaseActivity(), MainInterface.View {

    @Inject
    lateinit var presenter: MainInterface.Presenter

    private lateinit var mainPagerAdapter: MainPagerAdapter

    companion object {
        private const val AUTH_FROM_MY_PETS = 89

        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_main)
        presenter.setView(this)
        initUi()
    }

    override fun initUi() {
        setPagerAdapter()
        addTabListeners()
        presenter.viewReady()
        welcomeLayout.onClick { presenter.welcomeOverlayClicked() }
    }

    private fun setPagerAdapter() {
        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)
        mainViewPager.offscreenPageLimit = 3
        mainPagerAdapter.addFragment(ALL_PETS, AllPetsFragment.newInstance())
        mainPagerAdapter.addFragment(MY_PETS, MyPetsFragment.newInstance())
        mainPagerAdapter.addFragment(PROFILE, ProfileFragment.newInstance())
        mainViewPager.adapter = mainPagerAdapter
    }

    private fun addTabListeners() {
        allPetsTab.onClick { presenter.allPetsClicked() }
        myPetsTab.onClick { presenter.myPetsClicked() }
        profileTab.onClick { presenter.profileClicked() }
    }

    override fun activateAllPetsTab() {
        allPetsTab.setImageResource(R.drawable.icon_home_active)
        myPetsTab.setImageResource(R.drawable.icon_heart)
        profileTab.setImageResource(R.drawable.icon_person)
    }

    override fun activateMyPetsTab() {
        allPetsTab.setImageResource(R.drawable.icon_home)
        myPetsTab.setImageResource(R.drawable.icon_heart_active)
        profileTab.setImageResource(R.drawable.icon_person)
    }

    override fun activateProfileTab() {
        allPetsTab.setImageResource(R.drawable.icon_home)
        myPetsTab.setImageResource(R.drawable.icon_heart)
        profileTab.setImageResource(R.drawable.icon_person_active)
    }

    override fun openTab(tab: Int) = mainViewPager.setCurrentItem(tab, true)

    override fun showUserMustExistError() {
        showInfoDialogWithCancel(title = getString(R.string.add_to_favorites_login_required_alert_title),
                message = getString(R.string.add_to_favorites_login_required_alert_message),
                okAction = { clickedOkOnDialog() })
    }

    private fun clickedOkOnDialog() = startActivityForResult(WelcomeActivity.getLaunchIntent(this), AUTH_FROM_MY_PETS)

    override fun hideWelcomeOverlay() = welcomeLayout.gone()

    override fun showWelcomeOverlay() {
        welcomeLayout.visible()
        Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(bindTimerConsumer())
    }

    private fun bindTimerConsumer(): Consumer<Long> = Consumer {
        hideWelcomeOverlay()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == AUTH_FROM_MY_PETS) {
            presenter.myPetsClicked()
            EventBus.getDefault().postSticky(UserEvent(true))
        }
    }
}