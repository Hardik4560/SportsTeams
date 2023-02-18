package com.hardik.zujudigital.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hardik.zujudigital.repository.DataRepository

class MainViewModelProviderFactory(
    private val teamRepository: DataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(teamRepository) as T
    }
}