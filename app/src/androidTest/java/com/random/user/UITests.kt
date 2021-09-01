package com.random.user

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.random.user.helper.ChildViewClick
import com.random.user.helper.CustomMatchers
import com.random.user.helper.RecyclerViewItemCountAssertion
import com.random.user.view.user.list.UserListActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class UITests {

    companion object {
        private const val IDLING_RESOURCE_NAME = "dataLoad"
    }
    private var idlingResource: IdlingResource? = null

    @Rule
    @JvmField
    var activityTestRule: ActivityTestRule<UserListActivity> =
        ActivityTestRule(UserListActivity::class.java, true, false)

    @get:Rule
    val networkMockRule = NetworkMockRule()

    @Before
    fun setup() {
        clearData()
        activityTestRule.launchActivity(null)
    }

    @Test
    fun whenDataIsLoadedThenUsersAreShown() {
        registerIdlingResource(3)
        onView(withId(R.id.user_list)).check(matches(isDisplayed()))
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(3))
        onView(withId(R.id.search)).check(matches(isDisplayed()))
        onView(withId(R.id.filter)).check(matches(withText("Filter")))
    }

    @Test
    fun whenUserIsSelectedNavigatesToUserDetails() {
        registerIdlingResource(3)
        onView(withId(R.id.user_list))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                    click()
                ))
        onView(withId(R.id.registered_date)).check(matches(isDisplayed()))
    }

    @Test
    fun whenUserIsDeletedThenListIsUpdated() {
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(3))

        onView(withId(R.id.user_list)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(1, ChildViewClick().clickChildViewWithId(R.id.delete_user)))

        // Waits until delete operation completes
        registerIdlingResource(2)

        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(2))
    }

    @Test
    fun whenFilterIsUsedThenListIsUpdated() {
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(3))
        onView(withId(R.id.search)).perform(typeText("Ma"))

        registerIdlingResource(2)
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(2))
        IdlingRegistry.getInstance().unregister(idlingResource)

        onView(withId(R.id.user_list))
            .check(matches(CustomMatchers.withItemAtPositionAndEmail(0, "marjorie.alvarez@example.com")))
        onView(withId(R.id.user_list))
            .check(matches(CustomMatchers.withItemAtPositionAndEmail(1, "margarethe.elmi@example.com")))
    }

    private fun clearData() {
        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase("users_db")
        File(
            ApplicationProvider.getApplicationContext<Context>().filesDir, "datastore"
        ).deleteRecursively()
    }

    private fun registerIdlingResource(itemCount: Int) {
        idlingResource = object : IdlingResource {
            var resourceCallback: IdlingResource.ResourceCallback? = null

            override fun getName() = IDLING_RESOURCE_NAME

            override fun isIdleNow(): Boolean {
                val recyclerView: RecyclerView? =
                    activityTestRule.activity.findViewById(R.id.user_list)
                val idle = recyclerView?.adapter?.itemCount == itemCount

                resourceCallback?.let {
                    if (idle) {
                        it.onTransitionToIdle()
                    }
                }
                return idle
            }

            override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
                resourceCallback = callback
            }
        }
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}