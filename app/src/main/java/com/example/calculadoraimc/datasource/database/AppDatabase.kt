package com.example.calculadoraimc.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Aqui dizemos quais tabelas existem (entities) e a versão do banco
@Database(entities = [IMCEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imcDao(): IMCDao

    companion object {
        // Singleton: Garante que só exista UM banco de dados aberto ao mesmo tempo
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "imc_database" // Nome do arquivo do banco no celular
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}