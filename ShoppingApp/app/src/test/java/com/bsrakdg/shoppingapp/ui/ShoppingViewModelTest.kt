package com.bsrakdg.shoppingapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bsrakdg.shoppingapp.MainCoroutineRule
import com.bsrakdg.shoppingapp.getOrAwaitValueTest
import com.bsrakdg.shoppingapp.repositories.FakeShoppingRepositoryTest
import com.bsrakdg.shoppingapp.util.Constant
import com.bsrakdg.shoppingapp.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepositoryTest())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
        viewModel.insertShoppingItem("name", "", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with too long name, returns error`() {
        val name = buildString {
            for (i in 1..Constant.MAX_NAME_LENGTH + 1) {
                append("a")
            }
        }
        viewModel.insertShoppingItem(name, "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too price name, returns error`() {
        val price = buildString {
            for (i in 1..Constant.MAX_PRICE_LENGTH + 1) {
                append(9)
            }
        }
        viewModel.insertShoppingItem("name", "5", price)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {
        viewModel.insertShoppingItem("name", "999999999999999999999", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        viewModel.insertShoppingItem("name", "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `search for image with query and should network error false` () {
        viewModel.searchForImage("orange")

        val value = viewModel.images.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)

    }

}