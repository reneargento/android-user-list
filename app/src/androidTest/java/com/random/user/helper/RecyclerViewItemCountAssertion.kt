package com.random.user.helper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import junit.framework.TestCase.assertEquals

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
    override fun check(view: View, viewNotFoundException: NoMatchingViewException?) {
        if (viewNotFoundException != null) {
            throw viewNotFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertEquals(adapter!!.itemCount, expectedCount)
    }
}