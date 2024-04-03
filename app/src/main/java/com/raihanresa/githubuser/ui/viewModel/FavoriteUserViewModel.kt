package com.raihanresa.githubuser.ui.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raihanresa.githubuser.data.remote.response.ItemsItem
import com.raihanresa.githubuser.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _favoriteUsers = MutableLiveData<List<ItemsItem>>()
    val favoriteUsers: LiveData<List<ItemsItem>> = _favoriteUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun getFavoriteUsers() {
        _isLoading.value = true
        mFavoriteUserRepository.getAllFavoriteUsers().observeForever { users ->
            _isLoading.value = false
            if (users.isNullOrEmpty()) {
                _isEmpty.value = true
            } else {
                val items = users.map { user ->
                    ItemsItem(login = user.username, avatarUrl = user.avatarUrl)
                }
                _favoriteUsers.value = items
            }
        }
    }
}