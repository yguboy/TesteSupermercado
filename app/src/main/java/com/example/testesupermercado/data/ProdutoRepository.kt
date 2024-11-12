package com.example.testesupermercado.data

import com.example.testesupermercado.model.Produto
import kotlinx.coroutines.flow.Flow

class ProdutoRepository(private val produtoDao: ProdutoDao) {
    val produtos: Flow<List<Produto>> = produtoDao.listarProdutos()

    suspend fun adicionarProduto(produto: Produto) {
        produtoDao.adicionarProduto(produto)
    }

    suspend fun atualizarProduto(produto: Produto) {
        produtoDao.atualizarProduto(produto)
    }

    suspend fun deletarProduto(produto: Produto) {
        produtoDao.deletarProduto(produto)
    }
}
