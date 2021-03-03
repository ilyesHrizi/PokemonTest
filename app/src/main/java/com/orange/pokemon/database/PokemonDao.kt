package com.orange.pokemon.database

import androidx.room.*
import com.orange.pokemon.model.Pokemon
@Dao
interface PokemonDao {
    @Insert
    suspend fun insertOne(entity: PokemonEntity)

    @Insert
    suspend fun insertAll(entities: List<PokemonEntity>)

    @Delete
    suspend fun deleteOne(entity: PokemonEntity)

    @Query("SELECT * FROM pokemon_table")
    suspend fun getAll(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon_table WHERE id = :id")
    suspend fun getOneByID(id: Int): PokemonEntity

    @Update
    suspend fun updateOne(transactionEntity: PokemonEntity)

}