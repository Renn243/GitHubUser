package com.raihanresa.githubuser.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanresa.githubuser.databinding.ActivityFavoriteUserBinding
import com.raihanresa.githubuser.helper.ViewModelFactory
import com.raihanresa.githubuser.ui.adapter.ItemUserAdapter
import com.raihanresa.githubuser.ui.viewModel.FavoriteUserViewModel

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    private val favoriteUserViewModel by viewModels<FavoriteUserViewModel>{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager

        val adapter = ItemUserAdapter()
        binding.recycleView.adapter = adapter

        favoriteUserViewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
        }

        favoriteUserViewModel.isEmpty.observe(this) { isEmpty ->
            binding.empty.isVisible = isEmpty
        }

        favoriteUserViewModel.favoriteUsers.observe(this) { users ->
            adapter.submitList(users)
        }

        favoriteUserViewModel.isEmpty.observe(this) { isEmpty ->
            if (isEmpty) {
                binding.recycleView.isVisible = false
                binding.empty.isVisible = true
            } else {
                binding.recycleView.isVisible = true
                binding.empty.isVisible = false
            }
        }

        favoriteUserViewModel.getFavoriteUsers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}