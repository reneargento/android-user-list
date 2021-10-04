package com.random.user

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
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
import com.random.user.helper.ChildViewClick
import com.random.user.helper.CustomMatchers.Companion.withItemAtPositionAndEmail
import com.random.user.helper.RecyclerViewItemCountAssertion
import com.random.user.presentation.list.view.UserListFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import com.random.user.helper.GetCurrentActivity.getCurrentActivity
import com.random.user.helper.IdlingResourceHelper
import com.random.user.hilt.launchFragmentInHiltContainer
import com.random.user.mock.NetworkMockRule
import org.hamcrest.CoreMatchers.`is`

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UITests {

    companion object {
        private const val IDLING_RESOURCE_NAME = "dataLoad"
    }
    private var idlingResource: IdlingResource? = null

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val networkMockRule = NetworkMockRule()

    @Before
    fun setup() {
        hiltRule.inject()
        launchUserListFragment()
        registerIdlingResourceToWaitItems(3)
    }

    @Test
    fun whenDataIsLoadedThenUsersAreShown() {
        onView(withId(R.id.user_list)).check(matches(isDisplayed()))
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(3))
        onView(withId(R.id.search)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).check(matches(withHint("Filter")))
    }

    @Test
    fun whenUserIsSelectedNavigatesToUserDetails() {
        // when
        onView(withId(R.id.user_list))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                    click()
                ))

        // then
        assertThat(navController.currentDestination?.id, `is`(R.id.UserDetailsFragment))
    }

    @Test
    fun whenUserIsDeletedThenListIsUpdated() {
        // given
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(3))
        IdlingRegistry.getInstance().unregister(idlingResource)

        // when
        onView(withId(R.id.user_list)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(1, ChildViewClick.clickChildViewWithId(R.id.delete_user)))

        // Waits until delete operation completes
        registerIdlingResourceToWaitItems(2)

        // then
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(2))
    }

    @Test
    fun whenFilterIsUsedThenListIsUpdated() {
        // given
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(3))
        IdlingRegistry.getInstance().unregister(idlingResource)

        // when
        onView(withId(R.id.search)).perform(typeText("Ma"))

        registerIdlingResourceToWaitItems(2)
        // then
        onView(withId(R.id.user_list)).check(RecyclerViewItemCountAssertion(2))

        onView(withId(R.id.user_list))
            .check(matches(withItemAtPositionAndEmail(0, "marjorie.alvarez@example.com")))
        onView(withId(R.id.user_list))
            .check(matches(withItemAtPositionAndEmail(1, "margarethe.elmi@example.com")))
    }

    private fun launchUserListFragment() {
        launchFragmentInHiltContainer<UserListFragment> {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.UserListFragment)
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }
    }

    private fun registerIdlingResourceToWaitItems(itemCount: Int) {
        idlingResource = IdlingResourceHelper.createIdlingResource(IDLING_RESOURCE_NAME)
        { isIdle(itemCount) }
        IdlingRegistry.getInstance().register(idlingResource)
    }

    private fun isIdle(itemCount: Int) : Boolean {
        val activity = getCurrentActivity() ?: return false
        val recyclerView: RecyclerView? = activity.findViewById(R.id.user_list)
        return recyclerView?.adapter?.itemCount == itemCount
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}