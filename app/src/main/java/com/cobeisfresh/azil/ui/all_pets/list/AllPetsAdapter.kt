package com.cobeisfresh.azil.ui.all_pets.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.constants.NUMBER_OF_ITEMS
import com.cobeisfresh.azil.data.models.DogModel
import java.util.*

/**
 * Created by Zerina Salitrezic on 02/08/2017.
 */

class AllPetsAdapter(private val onLastItemReached: () -> Unit,
                     private val onDogClick: (String) -> Unit,
                     private val onNoInternetError: () -> Unit,
                     private val onUserMustExistError: () -> Unit,
                     private val onChangeFavouriteStateError: () -> Unit) : RecyclerView.Adapter<AllPetsHolder>() {

    private val dogs: MutableList<DogModel> = ArrayList()

    fun setDogs(dogs: List<DogModel>) {
        this.dogs.clear()
        this.dogs.addAll(dogs)
        notifyDataSetChanged()
    }

    fun addMoreDogs(dogs: List<DogModel>?) {
        if (dogs != null && !dogs.isEmpty()) {
            this.dogs.addAll(dogs)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = dogs.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AllPetsHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_dog, parent, false)
        return AllPetsHolder(view, onDogClick, onNoInternetError, onUserMustExistError, onChangeFavouriteStateError)
    }

    override fun onBindViewHolder(holder: AllPetsHolder?, position: Int) = dogs.isNotEmpty().let {
        val dog = dogs[position]
        holder?.setDogData(dog)
        handleItemPosition(position)
    }

    private fun handleItemPosition(position: Int) {
        if (itemCount >= NUMBER_OF_ITEMS && position == itemCount - 1 && position > 0) {
            onLastItemReached()
        }
    }

    fun updateDogItem(dogId: String, hasFavourite: Boolean) {
        for (i in dogs.indices) {
            if (dogs[i].id == dogId) {
                dogs[i].isFavorite = hasFavourite
                val position = dogs.indexOf(dogs[i])
                notifyItemChanged(position)
            }
        }
    }

    fun removeFavourites() {
        for (dog in dogs) {
            dog.isFavorite = false
        }
        notifyDataSetChanged()
    }
}