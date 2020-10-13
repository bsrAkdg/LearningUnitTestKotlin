package com.bsrakdg.shoppingapp.di

import android.content.Context
import androidx.room.Room
import com.bsrakdg.shoppingapp.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    // We don't need to annotate this with at Singleton now which we did in our real app
    // there it was important but in test cases we want to create a new instance for every test case
    // so we don't want these to be singletons here
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        // inMemoryDatabaseBuilder : not real database
        // allowMainThreadQueries : allow that we access this room database from the main thread
        Room
            .inMemoryDatabaseBuilder(context, ShoppingItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}