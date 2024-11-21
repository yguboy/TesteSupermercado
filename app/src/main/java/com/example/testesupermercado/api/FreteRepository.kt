package com.example.testesupermercado.api

import com.example.testesupermercado.api.RetrofitClient
import com.example.testesupermercado.api.ViaCepResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FreteRepository {
    suspend fun buscarEnderecoPorCep(cep: String): Result<ViaCepResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.viaCepApi.getEnderecoPorCep(cep).execute()
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Erro ao buscar endereço: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun calcularFrete(baseValor: Double, quantidade: Int): Double {
        val taxaPorProduto = 5.0
        return baseValor + (quantidade * taxaPorProduto)
    }
}
