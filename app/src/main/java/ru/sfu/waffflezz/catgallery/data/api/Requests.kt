package ru.sfu.waffflezz.catgallery.data.api

data class CardRequest(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    val breeds: List<BreedRequest>
)

data class BreedRequest(
    val description: String,
    val id: String,
    val life_span: String,
    val name: String,
    val origin: String,
    val temperament: String,
    val wikipedia_url: String
) {

}

data class CategoriesRequest(
    val id: Int?,
    val name: String
)