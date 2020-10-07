package com.bsrakdg.testingapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class ResourceComparerTest {

    private lateinit var resourceComparer: ResourceComparer
    private lateinit var context: Context

    @Before
    fun init() {
        resourceComparer = ResourceComparer()
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun stringResourceSameAsGivenString_returnsTrue() {
        val result = resourceComparer.isEqual(context, R.string.app_name, "TestingApp")
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun stringResourceSameAsGivenString_returnsFalse() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context, R.string.app_name, "Hello")
        Truth.assertThat(result).isFalse()
    }
}