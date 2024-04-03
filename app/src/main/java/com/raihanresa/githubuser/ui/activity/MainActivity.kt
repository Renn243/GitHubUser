package com.raihanresa.githubuser.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanresa.githubuser.R
import com.raihanresa.githubuser.data.remote.response.ItemsItem
import com.raihanresa.githubuser.databinding.ActivityMainBinding
import com.raihanresa.githubuser.helper.ModeViewModelFactory
import com.raihanresa.githubuser.ui.adapter.ItemUserAdapter
import com.raihanresa.githubuser.ui.viewModel.MainViewModel
import com.raihanresa.githubuser.ui.viewModel.ModeViewModel
import com.raihanresa.githubuser.util.SettingPreferences
import com.raihanresa.githubuser.util.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeThemeSetting()

        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager
        mainViewModel.listUser.observe(this) { listUser->
            setAdapter(listUser)
        }

        mainViewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
        }

        mainViewModel.isEmpty.observe(this) {
            binding.empty.isVisible = it
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    mainViewModel.searchUser(searchView.text.toString())
                    searchView.hide()
                    false
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favourite_page -> {
                val intent = Intent(this, FavoriteUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.mode_page -> {
                val intent = Intent(this, ModeActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeThemeSetting() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ModeViewModelFactory(pref)).get(ModeViewModel::class.java)
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setAdapter(listUser: List<ItemsItem>) {
        val adapter = ItemUserAdapter()
        adapter.submitList(listUser)
        binding.recycleView.adapter = adapter
    }
}