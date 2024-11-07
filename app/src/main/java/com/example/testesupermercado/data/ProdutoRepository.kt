package com.example.testesupermercado.data

import androidx.lifecycle.LiveData
import com.example.testesupermercado.Produto

class ProdutoRepository(private val produtoDao: ProdutoDao) {

    val produtos: LiveData<List<Produto>> = produtoDao.listarProdutos()

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
