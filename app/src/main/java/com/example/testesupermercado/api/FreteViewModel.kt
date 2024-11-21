package com.example.testesupermercado.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testesupermercado.api.ViaCepResponse
import com.example.testesupermercado.api.FreteRepository
import kotlinx.coroutines.launch

class FreteViewModel : ViewModel() {
    private val repository = FreteRepository()

    var endereco by mutableStateOf<ViaCepResponse?>(null)
    var freteValor by mutableStateOf(0.0)
    var error by mutableStateOf<String?>(null)

    fun buscarEndereco(cep: String, baseValor: Double, quantidade: Int) {
        viewModelScope.launch {
            val result = repository.buscarEnderecoPorCep(cep)
            result.onSuccess {
                endereco = it
                freteValor = repository.calcularFrete(baseValor, quantidade)
                error = null
            }.onFailure {
                error = it.message
            }
        }
    }
}
