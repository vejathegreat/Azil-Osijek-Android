package com.cobeisfresh.azil.ui.my_pets.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.constants.NUMBER_OF_ITEMS
import com.cobeisfresh.azil.data.models.DogModel
import java.util.*

/**
 * Created by Zerina Salitrezic on 21/08/2017.
 */

class MyPetsAdapter(private val onLastItemReached: () -> Unit,
                    private val onDogClick: (String) -> Unit,
                    private val onNoInternetError: () -> Unit,
                    private val onServerError: () -> Unit) : RecyclerView.Adapter<MyPetsHolder>() {

    private val dogs: MutableList<DogModel> = ArrayList()

    fun setMyDogs(myDogs: List<DogModel>) {
        this.dogs.clear()
        this.dogs.addAll(myDogs)
        notifyDataSetChanged()
    }

    fun addMoreDogs(myDogs: List<DogModel>?) {
        if (myDogs != null && !myDogs.isEmpty()) {
            this.dogs.addAll(myDogs)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = dogs.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyPetsHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_dog, parent, false)
        return MyPetsHolder(view, onDogClick, onNoInternetError, onServerError)
    }

    override fun onBindViewHolder(holder: MyPetsHolder?, position: Int) {
        dogs.isNotEmpty().run {
            val dog = dogs[position]
            holder?.setDogData(dog)
            handleItemPosition(position)
        }
    }

    private fun handleItemPosition(position: Int) {
        if (itemCount >= NUMBER_OF_ITEMS && position == itemCount - 1 && position > 0) {
            onLastItemReached()
        }
    }

    fun removeDogItem(dogId: String) {
        for (i in dogs.indices) {
            if (dogs[i].id == dogId) {
                val position = dogs.indexOf(dogs[i])
                dogs.removeAt(position)
                notifyItemRemoved(position)
                return
            }
        }
    }

    fun removeFavourites() {
        dogs.clear()
        notifyDataSetChanged()
    }
}