package com.example.testesupermercado.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

data class ViaCepResponse(
    val cep: String,
    val logradouro: String,
    val complemento: String?,
    val bairro: String,
    val localidade: String,
    val uf: String
)

interface ViaCepApi {
    @GET("{cep}/json/")
    fun getEnderecoPorCep(@Path("cep") cep: String): Call<ViaCepResponse>
}
