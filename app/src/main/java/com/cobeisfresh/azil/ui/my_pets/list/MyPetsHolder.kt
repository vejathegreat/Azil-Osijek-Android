package com.cobeisfresh.azil.ui.my_pets.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.extensions.fadeIn
import com.cobeisfresh.azil.common.extensions.fadeOut
import com.cobeisfresh.azil.common.extensions.onClick
import com.cobeisfresh.azil.common.extensions.showDeleteDogDialog
import com.cobeisfresh.azil.common.utils.*
import com.cobeisfresh.azil.data.event.DeletedDogEvent
import com.cobeisfresh.azil.data.models.DogModel
import kotlinx.android.synthetic.main.list_item_dog.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

class MyPetsHolder(itemView: View,
                   private val onDogClick: (String) -> Unit,
                   private val onNoInternetError: () -> Unit,
                   private val onServerError: () -> Unit)
    : RecyclerView.ViewHolder(itemView), MyPetsHolderInterface.View {

    @Inject
    lateinit var presenter: MyPetsHolderInterface.Presenter

    init {
        App.get().getComponent().inject(this)
        presenter.setView(this)
        setListeners()
    }

    private fun setListeners() {
        itemView.onClick { presenter.dogLayoutClicked() }
        itemView.iconFavourite.onClick { presenter.iconFavouriteClicked() }
    }

    override fun showDeleteDialog(dog: DogModel) {
        itemView.context.showDeleteDogDialog(dog = dog,
                okAction = { presenter.deleteFavourite(hasInternet(), it) })
    }

    fun setDogData(dog: DogModel?) {
        dog?.run { presenter.setDogModel(this) }
    }

    override fun showName(name: String) {
        itemView.petName.text = name
    }

    override fun showImage(image: String) = loadImage(itemView.petPhoto, image, R.drawable.dog_placeholder)

    override fun showPlaceholder() = loadPlaceholder(itemView.petPhoto, R.drawable.dog_placeholder)

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

    override fun setFavouriteIcon() {
        itemView.iconFavourite.setImageResource(R.drawable.icon_heart_active)
        itemView.iconFavourite.fadeIn()
    }

    override fun setNotFavouriteIcon() {
        itemView.iconFavourite.setImageResource(R.drawable.icon_heart)
        itemView.iconFavourite.fadeIn()
    }

    override fun showNoInternetError() = onNoInternetError()

    override fun showServerError() = onServerError()

    override fun callOnItemClick(id: String) = onDogClick(id)

    override fun hideFavouriteIcon() = itemView.iconFavourite.fadeOut()

    override fun showFavouritesProgressBar() = itemView.progressBar.fadeIn()

    override fun hideFavouritesProgressBar() = itemView.progressBar.fadeOut()

    override fun sendDogDeletedEvent(id: String) = EventBus.getDefault().postSticky(DeletedDogEvent(id))
}