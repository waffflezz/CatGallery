package ru.sfu.waffflezz.catgallery.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import ru.sfu.waffflezz.catgallery.App
import ru.sfu.waffflezz.catgallery.data.api.CardRequest


class CardViewModel(database: CatGalleryDatabase) : ViewModel() {
    val cardsList = database.dao.getAllCards()

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