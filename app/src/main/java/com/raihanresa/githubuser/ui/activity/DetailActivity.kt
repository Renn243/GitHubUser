package com.raihanresa.githubuser.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.raihanresa.githubuser.R
import com.raihanresa.githubuser.data.local.entity.FavoriteUser
import com.raihanresa.githubuser.databinding.ActivityDetailBinding
import com.raihanresa.githubuser.helper.ViewModelFactory
import com.raihanresa.githubuser.ui.adapter.SectionPagerAdapter
import com.raihanresa.githubuser.ui.viewModel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel>{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra(EXTRA_LOGIN)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(detailViewModel.detailUser.value==null) detailViewModel.detailUser(username.toString())

        detailViewModel.detailUser.observe(this) { user ->
            user?.let { detailUser ->
                with(binding) {
                    numFollowers.text = detailUser.followers.toString()
                    numFollowing.text = detailUser.following.toString()
                    name.text = detailUser.name ?: ""
                    userName.text = detailUser.login.toString()
                    Glide.with(binding.root)
                        .load(detailUser.avatarUrl)
                        .into(binding.profImage)
                        .clearOnDetach()
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
        }

        detailViewModel.isFavorite.observe(this) { isFavorite ->
            if (isFavorite) {
                binding.favorite.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.favorite.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }

        if (username != null) {
            detailViewModel.getFavoriteUserByUsername(username).observe(this) { favoriteUser ->
                if (favoriteUser != null) {
                    binding.favorite.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    binding.favorite.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }

        if (username != null) {
            detailViewModel.getFavoriteUserByUsername(username).observe(this) { favoriteUser ->
                if (favoriteUser != null) {
                    binding.favorite.setImageResource(R.drawable.baseline_favorite_24)
                    binding.favorite.setOnClickListener {
                        detailViewModel.delete(favoriteUser)
                        Toast.makeText(this, "User removed from favorites", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    binding.favorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    binding.favorite.setOnClickListener {
                        val detailUser = detailViewModel.detailUser.value
                        if (detailUser != null) {
                            val userLogin = detailUser.login ?: ""
                            val avatarUrl = detailUser.avatarUrl ?: ""

                            val newFavoriteUser = FavoriteUser(
                                username = userLogin,
                                avatarUrl = avatarUrl
                            )

                            detailViewModel.insert(newFavoriteUser)
                            Toast.makeText(this, "User added to favorites", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "User details not available", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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

    companion object {
        const val EXTRA_LOGIN="login"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }
}