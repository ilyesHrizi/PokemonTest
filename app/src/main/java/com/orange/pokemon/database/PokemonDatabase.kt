package com.orange.pokemon.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.orange.pokemon.model.Pokemon

@Database(
    entities = [PokemonEntity::class],
    version = 1
)
abstract class PokemonDatabase : RoomDatabase(){

    abstract fun getPokemon(): PokemonDao

    companion object {

        private var instance: PokemonDatabase? = null

        fun getInstance(context: Context): PokemonDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    PokemonDatabase :: class.java,
                    "pokemon_db"
                ).build()
            }
            return instance!!
        }
    }
}