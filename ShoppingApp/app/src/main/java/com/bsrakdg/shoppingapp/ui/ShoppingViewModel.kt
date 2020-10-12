package com.bsrakdg.shoppingapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsrakdg.shoppingapp.data.local.ShoppingItem
import com.bsrakdg.shoppingapp.data.remote.responses.ImageResponse
import com.bsrakdg.shoppingapp.repositories.ShoppingRepository
import com.bsrakdg.shoppingapp.util.Constant
import com.bsrakdg.shoppingapp.util.Event
import com.bsrakdg.shoppingapp.util.Resource
import kotlinx.coroutines.launch
import kotlin.Exception

class ShoppingViewModel
@ViewModelInject
constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()

    // We cannot post new value from fragment, we can only post values to _images from this view model
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error("The fields must not be empty", null)
                )
            )
            return
        }

        if (name.length > Constant.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The name of the item must not exceed "
                                + "${Constant.MAX_NAME_LENGTH} characters", null
                    )
                )
            )
            return
        }

        if (priceString.length > Constant.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The price of the item must not exceed "
                                + "${Constant.MAX_PRICE_LENGTH} characters", null
                    )
                )
            )
            return
        }

        val amount = try {
            amountString.toInt()
        } catch (exception: Exception) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Please enter a valid amount", null
                    )
                )
            )
            return
        }

        val shoppingItem = ShoppingItem(
            name = name,
            amount = amount,
            price = priceString.toFloat(),
            imageUrl = _currentImageUrl.value ?: ""
        )

        insertShoppingItemIntoDb(shoppingItem)
        setCurrentImageUrl("")

        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        _images.value = Event(Resource.loading(null))

        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}