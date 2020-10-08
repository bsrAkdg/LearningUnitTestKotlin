package com.bsrakdg.shoppingapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * SmallTest : Unit test
 * MediumTest : Instrumented test
 * LargeTest : UI or end-to-end test
 *
 * Look at : small-medium-large-test.png
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    private lateinit var database: ShoppingItemDatabase
    private lateinit var shoppingDao: ShoppingDao

    // for this reason we should add this rule : java.lang.IllegalStateException: This job has not completed yet
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // inMemoryDatabaseBuilder : not real database
        // allowMainThreadQueries : allow that we access this room database from the main thread
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()

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

}