package com.bsrakdg.shoppingapp.util

/**
 * We emit an error resource and we show a snack bar that an error occured
 * then if we rotate our device that would mean that the live data would automatically
 * emit again we would again show that snack bar even though the event already passed and
 * that is why we have hat Event class here.
 *
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}