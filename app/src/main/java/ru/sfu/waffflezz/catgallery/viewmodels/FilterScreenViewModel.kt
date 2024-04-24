package ru.sfu.waffflezz.catgallery.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sfu.waffflezz.catgallery.data.api.ApiKey
import ru.sfu.waffflezz.catgallery.data.api.BreedRequest
import ru.sfu.waffflezz.catgallery.data.api.CardRequest
import ru.sfu.waffflezz.catgallery.data.api.CategoriesRequest
import ru.sfu.waffflezz.catgallery.data.api.TheCatApiService
import ru.sfu.waffflezz.catgallery.data.api.createRetrofit

@OptIn(FlowPreview::class)
class FilterScreenViewModel: ViewModel() {
    private val catApiService = createRetrofit().create(TheCatApiService::class.java)

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _rememberBreeds = MutableStateFlow<List<BreedHandle>>(emptyList())
    val rememberBreeds = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_rememberBreeds) { text, breeds ->
            if (text.isBlank()) {
                breeds
            } else {
                delay(1000L)
                breeds.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _rememberBreeds.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    val rememberCategories = mutableStateOf<List<CategoriesRequest>>(emptyList())

    var selectedCategory = mutableStateOf<Int?>(null)
    var selectedCategoryText = mutableStateOf("")

    init {
        catApiService.getBreads().enqueue(object: Callback<List<BreedRequest>> {
            override fun onResponse(
                call: Call<List<BreedRequest>>,
                response: Response<List<BreedRequest>>
            ) {
                if (response.isSuccessful) {
                    val breedsResponse = response.body()
                    if (breedsResponse != null) {
                        _rememberBreeds.value = breedsResponse.map { item -> BreedHandle(item, mutableStateOf(false)) }
                    }
                }
            }

            override fun onFailure(
                call: Call<List<BreedRequest>>,
                t: Throwable
            ) {
                TODO("Not yet implemented")
            }
        })

        catApiService.getCategories().enqueue(object: Callback<List<CategoriesRequest>> {
            override fun onResponse(
                call: Call<List<CategoriesRequest>>,
                response: Response<List<CategoriesRequest>>
            ) {
                if (response.isSuccessful) {
                    val (categories, setCategories) = rememberCategories
                    val categoriesRequest = response.body()
                    if (categoriesRequest != null) {
                        setCategories(categoriesRequest.plus(CategoriesRequest(null, "None")))
                    }
                }
            }

            override fun onFailure(
                call: Call<List<CategoriesRequest>>,
                t: Throwable
            ) {
                TODO("Not yet implemented")
            }
        })
    }

    val rememberFilteredCatCards = mutableStateOf<List<CardRequest>>(emptyList())
    val refreshing = mutableStateOf(false)

    fun refreshFilteredCatCards() {
        refreshing.value = true
        catApiService.getFilteredCats(
            ApiKey.key,
            10,
            rememberBreeds.value.filter { it.isSelected.value }.map { it.breedRequest.id },
            selectedCategory.value
        ).enqueue(object : Callback<List<CardRequest>> {
            override fun onResponse(call: Call<List<CardRequest>>, response: Response<List<CardRequest>>) {
                if (response.isSuccessful) {
                    val (catCards, setCards) = rememberFilteredCatCards
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

    fun getCatCardById(id: String): CardRequest? {
        return rememberFilteredCatCards.value.find { card ->
            card.id == id
        }
    }
}

data class BreedHandle(
    val breedRequest: BreedRequest,
    val isSelected: MutableState<Boolean>
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombination = listOf(
            breedRequest.id,
            breedRequest.name,
            "$breedRequest.id$breedRequest.name",
            "$breedRequest.name$breedRequest.id"
        )

        return matchingCombination.any {
            it.contains(query, ignoreCase = true)
        }
    }
}