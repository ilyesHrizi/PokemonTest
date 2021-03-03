package com.orange.pokemon.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "pokemon_table")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val Id: Int = 0,

    val name: String,
    val reason: String,
    val evolvedfrom: String,
    val imageurl: String,
    val xdescription: String

)