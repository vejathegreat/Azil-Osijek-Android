package com.cobeisfresh.azil.ui.pet_details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.analytics.firebase.events.ScreenEvent
import com.cobeisfresh.azil.common.extensions.*
import com.cobeisfresh.azil.common.utils.getAgeFormat
import com.cobeisfresh.azil.common.utils.getGenderFormat
import com.cobeisfresh.azil.common.utils.getSizeFormat
import com.cobeisfresh.azil.common.utils.hasInternet
import com.cobeisfresh.azil.data.event.FavouriteStateEvent
import com.cobeisfresh.azil.data.event.UserEvent
import com.cobeisfresh.azil.data.models.PetImagesData
import com.cobeisfresh.azil.data.response.DogDetailsResponse
import com.cobeisfresh.azil.ui.adopt_pet.AdoptPetActivity
import com.cobeisfresh.azil.ui.base.BaseActivity
import com.cobeisfresh.azil.ui.pet_details.adapter.ImagePagerAdapter
import com.cobeisfresh.azil.ui.pet_details.pet_images.PetImagesActivity
import com.cobeisfresh.azil.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_pet_details.*
import kotlinx.android.synthetic.main.error_full_layout.*
import kotlinx.android.synthetic.main.error_layout.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject


/**
 * Created by Zerina Salitrezic on 03/08/2017.
 */

class PetDetailsActivity : BaseActivity(), PetDetailsInterface.View {

    @Inject
    lateinit var presenter: PetDetailsInterface.Presenter

    private lateinit var imagesPagerAdapter: ImagePagerAdapter

    companion object {
        private const val DOG_ID = "dog_id"
        private const val AUTH_FROM_PET_DETAILS = 90

        fun getLaunchIntent(from: Context, dogId: String): Intent {
            val intent = Intent(from, PetDetailsActivity::class.java)
            intent.putExtra(DOG_ID, dogId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().getComponent().inject(this)
        setContentView(R.layout.activity_pet_details)
        presenter.setView(this)
        initUi()
        sendScreenTrackingEvent(ScreenEvent.ScreenName.PET_DETAILS)
    }

    override fun initUi() {
        getExtras()
        setListeners()
        setImagePagerAdapter()
        presenter.getDogById(hasInternet())
    }

    private fun setImagePagerAdapter() {
        imagesPagerAdapter = ImagePagerAdapter(onImageClick = { presenter.imageClicked(it) })
        imagesPager.adapter = imagesPagerAdapter
    }

    private fun getExtras() {
        val intent = intent
        val dogId = intent.getStringExtra(DOG_ID)
        presenter.setDogId(dogId)
    }

    private fun setListeners() {
        adoptPet.onClick { presenter.adoptPetClicked() }
        iconFavourite.onClick { presenter.iconFavouriteClicked(hasInternet()) }
        iconBack.onClick { presenter.iconBackClicked() }
        errorFullLayout.onClick { presenter.errorFullLayoutClicked(hasInternet()) }
        dogLayout.onTouch { hideErrorLayout() }
    }

    override fun showImages(images: List<String>) {
        imagesPagerAdapter.setImages(images)
        dogImagesLayout.fadeIn()
    }

    override fun showPageIndicator() = circlePageIndicator.setViewPager(imagesPager)

    override fun showName(name: String) {
        petName.text = name
    }

    override fun showAge(age: String) {
        petAge.text = getAgeFormat(age)
    }

    override fun showDescription(description: String) {
        petLongDescription.text = description
    }

    override fun showProgressBar() = progressBar.visible()

    override fun hideProgressBar() = progressBar.gone()

    override fun showNoInternetErrorFullScreen() {
        errorTextFullLayout.text = getString(R.string.error_no_internet_full_screen)
        errorFullLayout.fadeIn()
    }

    override fun hideErrorFullScreen() = errorFullLayout.fadeOut()

    override fun showServerErrorFullScreen() {
        errorTextFullLayout.text = getString(R.string.server_error_full_screen)
        errorFullLayout.fadeIn()
    }

    override fun setFavouriteIcon() {
        iconFavourite.setImageResource(R.drawable.icon_heart_active_white)
        iconFavourite.fadeIn()
    }

    override fun setNotFavouriteIcon() {
        iconFavourite.setImageResource(R.drawable.icon_heart_white)
        iconFavourite.fadeIn()
    }

    override fun showDogDetailsLayout() = scrollView.fadeIn()

    override fun hideDogDetailsLayout() = scrollView.fadeOut()

    override fun hideFavouriteIcon() = iconFavourite.fadeOut()

    override fun showAdoptButton() = adoptPet.fadeIn()

    override fun showUserMustExistError() {
        showInfoDialogWithCancel(title = getString(R.string.add_to_favorites_login_required_alert_title),
                message = getString(R.string.add_to_favorites_login_required_alert_message),
                okAction = { startActivityForResult(WelcomeActivity.getLaunchIntent(this), AUTH_FROM_PET_DETAILS) })
    }

    override fun showFavouritesProgressBar() = favouriteProgressBar.visible()

    override fun hideFavouritesProgressBar() = favouriteProgressBar.gone()

    override fun showSize(size: String) {
        petSize.text = getSizeFormat(size)
    }

    override fun showGender(gender: String) {
        petGender.text = String.format(Locale.getDefault(), getString(R.string.gender_format), getGenderFormat(gender))
    }

    override fun hideErrorLayout() {
        if (errorLayout.isVisible()) {
            errorLayout.fadeOut()
        }
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

    override fun openAdoptPetActivity(dogDetailsResponse: DogDetailsResponse) {
        startActivity(AdoptPetActivity.getLaunchIntent(this, dogDetailsResponse))
    }

    override fun goBack() = finish()

    override fun onBackPressed() = presenter.iconBackClicked()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == AUTH_FROM_PET_DETAILS) {
            presenter.userAuthenticated()
            EventBus.getDefault().postSticky(UserEvent(true))
        }
    }

    override fun refreshPetDetails() = presenter.getDogById(hasInternet())

    override fun openPetImagesActivity(petImagesData: PetImagesData) {
        startActivity(PetImagesActivity.getLaunchIntent(this, petImagesData))
    }

    override fun sendFavouriteStateEvent(isFavourite: Boolean, dogId: String) {
        EventBus.getDefault().postSticky(FavouriteStateEvent(isFavourite, dogId))
    }

    override fun onPause() {
        presenter.unSubscribe()
        super.onPause()
    }
}