package com.bsrakdg.shoppingapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.bsrakdg.shoppingapp.launchFragmentInHiltContainer
import com.bsrakdg.shoppingapp.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

/**
 * SmallTest : Unit test
 * MediumTest : Instrumented test
 * LargeTest : UI or end-to-end test
 *
 * Look at : small-medium-large-test.png
 */

// Remove below annotation because we specified our own health test runner.
// @RunWith(AndroidJUnit4::class)

// If you want to use hilt in android components you should add @HiltAndroidTest annotation
// Then we can inject dependencies into test class
@HiltAndroidTest
@ExperimentalCoroutinesApi
@SmallTest
class ShoppingDaoTest {

    // We need to define rule for hilt
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // for this reason we should add this rule : java.lang.IllegalStateException: This job has not completed yet
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Hilt doesn't know which database it should inject, because inside of our test class
    // we also have access to our real app module in which we also provide such a shopping item database.
    // so it doesn't know if it should take the shopping item database from our app module or from our test app module.
    // to solve that problem we can simply add such @Named annotation this function and provide database function on TestAppModule.provideInMemoryDb
    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var shoppingDao: ShoppingDao

    @Before
    fun setup() {
        hiltRule.inject()
        shoppingDao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest { // runBlockingTest optimized for test cases
        val shoppingItem = ShoppingItem("item", 100, 1f, "url", 1)
        shoppingDao.insertShoppingItem(shoppingItem)

        val allShoppingItems = shoppingDao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest { // runBlockingTest optimized for test cases
        val shoppingItem = ShoppingItem("item", 100, 1f, "url", 1)
        shoppingDao.insertShoppingItem(shoppingItem)
        shoppingDao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = shoppingDao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest { // runBlockingTest optimized for test cases
        val shoppingItem1 = ShoppingItem("item1", 2, 10f, "url1", 1)
        val shoppingItem2 = ShoppingItem("item2", 4, 5.5f, "url2", 2)
        val shoppingItem3 = ShoppingItem("item3", 0, 100f, "url3", 3)

        shoppingDao.insertShoppingItem(shoppingItem1)
        shoppingDao.insertShoppingItem(shoppingItem2)
        shoppingDao.insertShoppingItem(shoppingItem3)


        val totalPriceSum = shoppingDao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)
    }

    @Test
    fun testLaunchFragmentInHiltContainer() {
        launchFragmentInHiltContainer<ShoppingFragment>() {

        }
    }

}