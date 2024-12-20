package com.example.testesupermercado.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.*
import com.example.testesupermercado.model.Produto
import com.example.testesupermercado.data.ProdutoRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProdutoViewModel(private val repository: ProdutoRepository) : ViewModel() {
    val produtos: StateFlow<List<Produto>> = repository.produtos
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun adicionarProduto(produto: Produto) = viewModelScope.launch {
        repository.adicionarProduto(produto)
    }

    fun atualizarProduto(produto: Produto) = viewModelScope.launch {
        repository.atualizarProduto(produto)
    }

    fun deletarProduto(produto: Produto) = viewModelScope.launch {
        repository.deletarProduto(produto)
    }
}

class ProdutoViewModelFactory(private val repository: ProdutoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProdutoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProdutoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
