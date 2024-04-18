package ru.sfu.waffflezz.catgallery.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
import ru.sfu.waffflezz.catgallery.App
import ru.sfu.waffflezz.catgallery.Utils
import ru.sfu.waffflezz.catgallery.data.api.CardRequest


class CardViewModel(private val database: CatGalleryDatabase) : ViewModel() {
    val cardsList = database.dao.getAllCards()

//    fun insert(cardRequest: CardRequest) {
//        /* TODO: Make from launched effect */
//        database.dao.insertCard(Utils.fromRequestToEntity(cardRequest))
//    }

    fun hasCardById(cardId: String, callback: () -> Unit) = viewModelScope.launch {
        if (database.dao.hasCardById(cardId)) {
            callback()
        }
    }

    fun insertItem(cardRequest: CardRequest) = viewModelScope.launch {
        database.dao.insertCard(Utils.fromRequestToEntity(cardRequest))
    }

    fun deleteItem(cardRequest: CardRequest) = viewModelScope.launch {
        database.dao.deleteCard(Utils.fromRequestToEntity(cardRequest))
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                return CardViewModel(database) as T
            }
        }
    }
}