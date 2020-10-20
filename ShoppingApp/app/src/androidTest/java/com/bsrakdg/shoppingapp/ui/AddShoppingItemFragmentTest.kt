package com.bsrakdg.shoppingapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.bsrakdg.shoppingapp.R
import com.bsrakdg.shoppingapp.data.local.ShoppingItem
import com.bsrakdg.shoppingapp.data.local.getOrAwaitValue
import com.bsrakdg.shoppingapp.launchFragmentInHiltContainer
import com.bsrakdg.shoppingapp.repositories.FakeShoppingRepositoryAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest // Interaction between two fragments (integration test)
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: TestShoppingFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()

        verify(navController).popBackStack()
    }

    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb() {
        val testVieModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            viewModel = testVieModel
        }

        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shoppingItem"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))

        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testVieModel.shoppingItems.getOrAwaitValue())
            .contains(
                ShoppingItem(
                    "shoppingItem",
                    5,
                    5.5f,
                    ""
                )
            )
    }
}