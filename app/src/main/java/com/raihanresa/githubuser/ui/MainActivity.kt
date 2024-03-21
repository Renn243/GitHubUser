package com.raihanresa.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanresa.githubuser.data.response.ItemsItem
import com.raihanresa.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun setAdapter(listUser: List<ItemsItem>) {
        val adapter = ItemUserAdapter()
        adapter.submitList(listUser)
        binding.recycleView.adapter = adapter
    }
}