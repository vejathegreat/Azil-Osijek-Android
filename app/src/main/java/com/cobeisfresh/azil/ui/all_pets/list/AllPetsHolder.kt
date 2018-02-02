package com.cobeisfresh.azil.ui.all_pets.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.extensions.fadeIn
import com.cobeisfresh.azil.common.extensions.fadeOut
import com.cobeisfresh.azil.common.extensions.onClick
import com.cobeisfresh.azil.common.utils.*
import com.cobeisfresh.azil.data.event.DeletedDogEvent
import com.cobeisfresh.azil.data.event.FavouritedDogEvent
import com.cobeisfresh.azil.data.models.DogModel
import kotlinx.android.synthetic.main.list_item_dog.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 02/08/2017.
 */

class AllPetsHolder(itemView: View,
                    private val onDogClick: (String) -> Unit,
                    private val onNoInternetError: () -> Unit,
                    private val onUserMustExistError: () -> Unit,
                    private val onChangeFavouriteStateError: () -> Unit) : RecyclerView.ViewHolder(itemView), AllPetsHolderInterface.View {

    @Inject
    lateinit var presenter: AllPetsHolderInterface.Presenter

    init {
        App.get().getComponent().inject(this)
        presenter.setView(this)
        setListeners()
    }

    private fun setListeners() {
        itemView.onClick { presenter.dogLayoutClicked() }
        itemView.iconFavourite.onClick { presenter.iconFavouriteClicked(hasInternet()) }
    }

    fun setDogData(dog: DogModel?) = dog?.run {
        presenter.setDogModel(this)
    }

    override fun showName(name: String) {
        itemView.petName.text = name
    }

    override fun showAge(age: String) {
        itemView.petAge.text = getAgeFormat(age)
    }

    override fun showSize(size: String) {
        itemView.petSize.text = getSizeFormat(size)
    }

    override fun showDescription(description: String) {
        itemView.petShortDescription.text = description
    }

    override fun showGender(gender: String) {
        itemView.petGender.text = String.format(Locale.getDefault(), getString(R.string.gender_format), getGenderFormat(gender))
    }

    override fun showImage(image: String) = loadImage(itemView.petPhoto, image, R.drawable.dog_placeholder)

    override fun callOnItemClick(dogId: String) = onDogClick(dogId)

    override fun showNoInternetError() = onNoInternetError()

    override fun showChangeFavouriteStateError() = onChangeFavouriteStateError()

    override fun setFavouriteIcon() {
        itemView.iconFavourite.setImageResource(R.drawable.icon_heart_active)
        itemView.iconFavourite.fadeIn()
    }

    override fun hideFavouriteIcon() = itemView.iconFavourite.fadeOut()

    override fun setNotFavouriteIcon() {
        itemView.iconFavourite.setImageResource(R.drawable.icon_heart)
        itemView.iconFavourite.fadeIn()
    }

    override fun showUserMustExistError() = onUserMustExistError()

    override fun showPlaceholder() = loadPlaceholder(itemView.petPhoto, R.drawable.dog_placeholder)

    override fun showFavouritesProgressBar() = itemView.progressBar.fadeIn()

    override fun hideFavouritesProgressBar() = itemView.progressBar.fadeOut()

    override fun sendFavouritedDogEvent() = EventBus.getDefault().postSticky(FavouritedDogEvent())

    override fun sendDogDeletedEvent(dogId: String) = EventBus.getDefault().postSticky(DeletedDogEvent(dogId))
}