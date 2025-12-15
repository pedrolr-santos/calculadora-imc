package com.example.calculadoraimc.feature.details.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadoraimc.datasource.database.IMCDao
import com.example.calculadoraimc.datasource.database.IMCEntity
import kotlinx.coroutines.launch

class DetailsViewModel(private val dao: IMCDao) : ViewModel() {

    // estado da tela: guarda a medição carregada
    var uiState by mutableStateOf<IMCEntity?>(null)
        private set

    // função para buscar os dados no banco
    fun fetchDetails(id: Int) {
        viewModelScope.launch {
            uiState = dao.getById(id)
        }
    }
}