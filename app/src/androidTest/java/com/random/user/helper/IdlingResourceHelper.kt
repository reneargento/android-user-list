package com.random.user.helper

import androidx.test.espresso.IdlingResource

object IdlingResourceHelper {

    fun createIdlingResource(name: String, isIdleCondition: () -> Boolean) =
        object : IdlingResource {
            var resourceCallback: IdlingResource.ResourceCallback? = null

            override fun getName() = name

            override fun isIdleNow(): Boolean {
                resourceCallback?.let {
                    if (isIdleCondition()) {
                        it.onTransitionToIdle()
                    }
                }
                return isIdleCondition()
            }

            override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
                resourceCallback = callback
            }
        }
}