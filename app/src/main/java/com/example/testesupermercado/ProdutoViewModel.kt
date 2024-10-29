package com.example.testesupermercado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProdutoViewModel : ViewModel() {

    private val _produtos = MutableLiveData<List<Produto>>(emptyList())
    val produtos: LiveData<List<Produto>> get() = _produtos

    fun adicionarProduto(produto: Produto) {
        _produtos.value = _produtos.value?.plus(produto)
    }

    fun deletarProduto(produto: Produto) {
        _produtos.value = _produtos.value?.filter { it != produto }
    }
}
