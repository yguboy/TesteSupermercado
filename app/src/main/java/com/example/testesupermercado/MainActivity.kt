package com.example.testesupermercado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testesupermercado.Produto
import com.example.testesupermercado.ProdutoViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ProdutoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen(viewModel)
        }
    }
}

@Composable
fun AppScreen(viewModel: ProdutoViewModel) {
    var nome by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Adiciona o produto apenas se todos os campos forem preenchidos
                if (nome.isNotEmpty() && valor.isNotEmpty() && quantidade.isNotEmpty()) {
                    viewModel.adicionarProduto(
                        Produto(
                            id = 0,  // o ID pode ser gerado automaticamente no futuro com o Room
                            nome = nome,
                            valor = valor.toDoubleOrNull() ?: 0.0,
                            quantidade = quantidade.toIntOrNull() ?: 0
                        )
                    )
                    // Limpa os campos após a adição
                    nome = ""
                    valor = ""
                    quantidade = ""
                }
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Produto") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = quantidade,
                onValueChange = { quantidade = it },
                label = { Text("Quantidade") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(viewModel.produtos.value ?: emptyList()) { produto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { viewModel.deletarProduto(produto) }
                    ) {
                        Text(
                            text = "Nome: ${produto.nome} - Valor: R$${produto.valor} - Quantidade: ${produto.quantidade}",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
