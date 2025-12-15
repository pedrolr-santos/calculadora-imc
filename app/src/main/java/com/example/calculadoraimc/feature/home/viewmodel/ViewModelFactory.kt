package com.example.calculadoraimc.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculadoraimc.datasource.database.IMCDao
import com.example.calculadoraimc.feature.details.viewmodel.DetailsViewModel
import com.example.calculadoraimc.feature.history.viewmodel.HistoryViewModel

class AppViewModelFactory(private val dao: IMCDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // se pedir HomeViewModel...
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        // se pedir HistoryViewModel...
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(dao) as T
        }
        // se pedir DetailsViewModel...
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}