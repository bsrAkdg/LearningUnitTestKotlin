package com.bsrakdg.shoppingapp.repositories

import androidx.lifecycle.LiveData
import com.bsrakdg.shoppingapp.data.local.ShoppingDao
import com.bsrakdg.shoppingapp.data.local.ShoppingItem
import com.bsrakdg.shoppingapp.data.remote.PixabayAPI
import com.bsrakdg.shoppingapp.data.remote.responses.ImageResponse
import com.bsrakdg.shoppingapp.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultShoppingRepository
@Inject
constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {


    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> =
        shoppingDao.observeAllShoppingItems()

    override fun observeTotalPrice(): LiveData<Float> = shoppingDao.observeTotalPrice()

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (exception: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

}