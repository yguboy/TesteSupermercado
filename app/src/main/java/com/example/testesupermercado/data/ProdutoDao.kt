package com.example.testesupermercado.data

import androidx.room.*
import com.example.testesupermercado.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun adicionarProduto(produto: Produto)

    @Update
    suspend fun atualizarProduto(produto: Produto)

    @Delete
    suspend fun deletarProduto(produto: Produto)

    @Query("SELECT * FROM produtos")
    fun listarProdutos(): Flow<List<Produto>>
}

