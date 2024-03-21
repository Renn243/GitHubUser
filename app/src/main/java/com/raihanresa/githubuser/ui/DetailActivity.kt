package com.raihanresa.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.raihanresa.githubuser.R
import com.raihanresa.githubuser.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOGIN="login"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra(EXTRA_LOGIN)

        if(detailViewModel.detailUser.value==null) detailViewModel.DetailUser(username.toString())

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

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

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}