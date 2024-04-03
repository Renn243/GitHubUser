package com.raihanresa.githubuser.ui.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.raihanresa.githubuser.R
import org.junit.Rule
import org.junit.Test

class MainActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testFavoriteScenario() {
        // Menunggu MainActivity dimuat
        Espresso.onView(ViewMatchers.withId(R.id.main))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Menunggu hasil pencarian muncul
        Thread.sleep(1000)

        // Menekan item pertama dalam daftar
        Espresso.onView(ViewMatchers.withId(R.id.recycle_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))

        // Menunggu DetailActivity dimuat
        Thread.sleep(1000)

        // Menekan floating button favorite
        Espresso.onView(ViewMatchers.withId(R.id.favorite))
            .perform(ViewActions.click())

        // Menunggu proses penambahan ke daftar favorit selesai
        Thread.sleep(1000)

        // Menekan tombol back
        Espresso.pressBack()

        // Menunggu kembali ke MainActivity
        Thread.sleep(1000)

        // Menekan tombol untuk berpindah ke FavouriteUserActivity
        Espresso.onView(ViewMatchers.withId(R.id.favourite_page))
            .perform(ViewActions.click())

        Thread.sleep(1000)
    }
}