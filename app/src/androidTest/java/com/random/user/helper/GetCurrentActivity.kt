package com.random.user.helper

import android.app.Activity
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

object GetCurrentActivity {
    fun getCurrentActivity(): Activity? {
        val currentActivity = arrayOfNulls<Activity>(1)
        val allActivities = ActivityLifecycleMonitorRegistry.getInstance()
            .getActivitiesInStage(Stage.RESUMED)
        if (!allActivities.isEmpty()) {
            currentActivity[0] = allActivities.iterator().next()
        }
        return currentActivity[0]
    }
}