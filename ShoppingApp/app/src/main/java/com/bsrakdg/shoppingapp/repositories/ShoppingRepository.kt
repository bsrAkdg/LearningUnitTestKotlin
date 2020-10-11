package com.bsrakdg.shoppingapp.repositories

import androidx.lifecycle.LiveData
import com.bsrakdg.shoppingapp.data.local.ShoppingItem
import com.bsrakdg.shoppingapp.data.remote.responses.ImageResponse
import com.bsrakdg.shoppingapp.util.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}