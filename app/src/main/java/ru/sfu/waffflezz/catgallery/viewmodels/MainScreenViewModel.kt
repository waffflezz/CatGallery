package ru.sfu.waffflezz.catgallery.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sfu.waffflezz.catgallery.data.api.ApiKey
import ru.sfu.waffflezz.catgallery.data.api.CardRequest
import ru.sfu.waffflezz.catgallery.data.api.TheCatApiService
import ru.sfu.waffflezz.catgallery.data.api.createRetrofit

class MainScreenViewModel(val catApiService: TheCatApiService) : ViewModel() {
    val rememberCatCards = mutableStateOf<List<CardRequest>>(emptyList())
    val refreshing = mutableStateOf(true)

    fun getCatCardById(id: String): CardRequest? {
        val (catCards, setCards) = rememberCatCards
        return catCards.find { card ->
            card.id == id
        }
    }

    fun refreshCatCards() {
        refreshing.value = true
        catApiService.getCatsWithBreeds(ApiKey.key, 10).enqueue(object : Callback<List<CardRequest>> {
            override fun onResponse(call: Call<List<CardRequest>>, response: Response<List<CardRequest>>) {
                if (response.isSuccessful) {
                    val (catCards, setCards) = rememberCatCards
                    val cards = response.body()
                    if (cards != null) {
                        setCards(cards)
                    }
                } else {
                    // Handle error
                }
                refreshing.value = false
            }

            override fun onFailure(call: Call<List<CardRequest>>, t: Throwable) {
                // Handle failure
                refreshing.value = false
            }
        })
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val catApiService = createRetrofit().create(TheCatApiService::class.java)
                return MainScreenViewModel(catApiService) as T
            }
        }
    }
}