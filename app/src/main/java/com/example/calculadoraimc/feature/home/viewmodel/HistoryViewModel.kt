package com.example.calculadoraimc.feature.history.viewmodel

import androidx.lifecycle.ViewModel
import com.example.calculadoraimc.datasource.database.IMCDao
import com.example.calculadoraimc.datasource.database.IMCEntity
import kotlinx.coroutines.flow.Flow

class HistoryViewModel(dao: IMCDao) : ViewModel() {
    // o viewmodel expõe o flow direto do banco para a tela observar
    val historyList: Flow<List<IMCEntity>> = dao.getAll()
}