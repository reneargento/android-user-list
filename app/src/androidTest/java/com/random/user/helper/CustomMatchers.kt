package com.random.user.helper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.random.user.presentation.user.list.UserAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher

class CustomMatchers {

    companion object {
        fun withItemAtPositionAndEmail(position: Int, email: String): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun matchesSafely(item: RecyclerView?): Boolean {
                    val userAdapter = item?.adapter as UserAdapter
                    return userAdapter.userList[position].email == email
                }

                override fun describeTo(description: Description?) {
                    description?.appendText("User at position $position with email $email")
                }
            }
        }
    }
}