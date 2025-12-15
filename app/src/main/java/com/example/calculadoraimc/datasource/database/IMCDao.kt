package com.example.calculadoraimc.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IMCDao {

    // 1. Comando para salvar um novo IMC
    @Insert
    suspend fun insert(imc: IMCEntity)

    // 2. Comando para ler TODOS os históricos (do mais recente para o mais antigo)
    // O 'Flow' é um tipo especial que avisa a tela automaticamente se entrar um dado novo
    @Query("SELECT * FROM imc_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<IMCEntity>>

    // 3. Comando para pegar UM histórico específico pelo ID (para a tela de Detalhes)
    @Query("SELECT * FROM imc_history WHERE id = :id")
    suspend fun getById(id: Int): IMCEntity?
}