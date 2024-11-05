package com.example.testesupermercado

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProdutoViewModel : ViewModel() {
    private val _produtos = MutableLiveData<List<Produto>>(emptyList())
    val produtos: LiveData<List<Produto>> get() = _produtos

    private var idCounter = 0

    fun adicionarProduto(produto: Produto) {
        idCounter++
        _produtos.value = _produtos.value?.plus(produto.copy(id = idCounter))
    }

    fun atualizarProduto(produto: Produto) {
        _produtos.value = _produtos.value?.map { if (it.id == produto.id) produto else it }
    }

    fun deletarProduto(produto: Produto) {
        val listaAtualizada = _produtos.value?.filter { it.id != produto.id } ?: emptyList()
        _produtos.value = listaAtualizada
    }
}