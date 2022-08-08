package com.example.demo


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.*
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jwhh.notekeeper.CourseRecyclerAdapter
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule
import java.util.regex.Pattern.matches


@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @Rule @JvmField
    val itemsActivity = ActivityScenarioRule(ItemsActivity::class.java)

    @Test
    fun selectNotesAfterNavigationDrawerChange(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_courses))

        val coursePosition = 0
        onView(withId(R.id.listItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CourseRecyclerAdapter.ViewHolder>(coursePosition, click())
        )

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes))

        val notePosition = 0
        onView(withId(R.id.listItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<NoteRecyclerAdapter.ViewHolder>(notePosition, click())
        )

        val note = DataManager.notes[notePosition]
        onView(withId(R.id.spinnerCourses)).check(matches(withSpinnerText(containsString(note.course?.title))))
        onView(withId(R.id.textNoteTitle)).check(matches(withText(containsString(note.title))))
        onView(withId(R.id.textNoteText)).check(matches(withText(containsString(note.text))))
    }
}

private fun ViewInteraction.check(matches: Boolean) {

}

private fun ViewInteraction.perform(open: ViewAction?) {
    TODO("Not yet implemented")
}
