package ru.sfu.waffflezz.catgallery.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCatApiService {
    @GET("images/search")
    fun getRandomCats(@Query("limit") limit: Int): Call<List<CardRequest>>

    @GET("images/search?has_breeds=1")
    fun getCatsWithBreeds(@Query("api_key") key: String, @Query("limit") limit: Int): Call<List<CardRequest>>
}