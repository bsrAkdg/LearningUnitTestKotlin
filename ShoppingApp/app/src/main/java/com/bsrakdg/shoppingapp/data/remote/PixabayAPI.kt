package com.bsrakdg.shoppingapp.data.remote

import com.bsrakdg.shoppingapp.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = "dsfdsfds"
    ): Response<ImageResponse>
}