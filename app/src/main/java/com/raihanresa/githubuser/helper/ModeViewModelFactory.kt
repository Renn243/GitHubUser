package com.raihanresa.githubuser.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raihanresa.githubuser.ui.viewModel.ModeViewModel
import com.raihanresa.githubuser.util.SettingPreferences

class ModeViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModeViewModel::class.java)) {
            return ModeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}