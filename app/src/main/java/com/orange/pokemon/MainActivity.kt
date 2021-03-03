package com.orange.pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.retrofitapplication.adapter.PokemoneAdapter
import com.orange.pokemon.database.PokemonDao
import com.orange.pokemon.database.PokemonDatabase
import com.orange.pokemon.database.PokemonEntity
import com.orange.pokemon.databinding.ActivityMainBinding
import com.orange.pokemon.model.Pokemon
import com.orange.pokemon.networking.ApiService
import com.orange.pokemon.networking.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pokemonAdapter: PokemoneAdapter
    private lateinit var Pokemondata: Pokemon
    private lateinit var database: PokemonDatabase
    private lateinit var dao: PokemonDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokemonAdapter = PokemoneAdapter()
        val service = NetworkClient().getRetrofit().create(ApiService::class.java)
        service.getAllPokemons().enqueue(object : Callback<List<Pokemon>> {
            override fun onResponse(call: Call<List<Pokemon>>, response: Response<List<Pokemon>>) {
                if (response.isSuccessful) {
                    Log.e(TAG, "onResponse: ${response.body()?.get(0)}")
                    val listPokemon : List<Pokemon>? = response.body()
                     database = PokemonDatabase.getInstance(this@MainActivity)
                     dao = database.getPokemon()

                        GlobalScope.launch(Dispatchers.Main) {
                            dao.insertAll(
                                listPokemon!!.map {
                                    PokemonEntity(
                                        name = it.name,
                                        evolvedfrom =  it.evolvedfrom,
                                        reason = it.reason,
                                        xdescription = it.xdescription,
                                        imageurl = it.imageurl
                                    )
                                }
                            )
                            val list = dao.getAll()
                            pokemonAdapter.submitList(list)
                            binding.recycler.apply {
                                layoutManager = LinearLayoutManager(this@MainActivity)
                                adapter = pokemonAdapter
                            }
                        }


                }
            }

            override fun onFailure(call: Call<List<Pokemon>>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                database = PokemonDatabase.getInstance(this@MainActivity)
                dao = database.getPokemon()
                GlobalScope.launch(Dispatchers.Main) {
                    val list = dao.getAll()
                    pokemonAdapter.submitList(list)
                    binding.recycler.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = pokemonAdapter
                    }
                }
            }

        })
    }
}