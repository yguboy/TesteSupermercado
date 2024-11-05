package com.example.testesupermercado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testesupermercado.Produto
import com.example.testesupermercado.ProdutoViewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
    var mostrarCadastro by remember { mutableStateOf(false) }
    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
    val produtos by viewModel.produtos.observeAsState(emptyList())


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {

                produtoSelecionado = null
                mostrarCadastro = true
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Supermercado",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                )
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(produtos) { produto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Nome: ${produto.nome}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) // Nome em negrito
                                )
                                Row {
                                    IconButton(onClick = {
                                        nome = produto.nome
                                        valor = produto.valor.toString()
                                        quantidade = produto.quantidade.toString()
                                        produtoSelecionado = produto
                                        mostrarCadastro = true
                                    }) {
                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(onClick = {
                                        viewModel.deletarProduto(produto)
                                    }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Deletar")
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Valor: R$${produto.valor}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) // Valor em negrito
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Quantidade: ${produto.quantidade}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) // Quantidade em negrito
                                )
                            }
                        }
                    }
                }
            }
        }

        if (mostrarCadastro) {
            AlertDialog(
                onDismissRequest = { mostrarCadastro = false },
                title = { Text("Cadastrar Produto") },
                text = {
                    Column {
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
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (nome.isNotEmpty() && valor.isNotEmpty() && quantidade.isNotEmpty()) {
                            val produto = Produto(
                                id = produtoSelecionado?.id ?: 0,
                                nome = nome,
                                valor = valor.toDoubleOrNull() ?: 0.0,
                                quantidade = quantidade.toIntOrNull() ?: 0
                            )

                            if (produtoSelecionado == null) {
                                viewModel.adicionarProduto(produto)
                            } else {
                                viewModel.atualizarProduto(produto)
                            }

                            nome = ""
                            valor = ""
                            quantidade = ""
                            mostrarCadastro = false
                        }
                    }) {
                        Text(if (produtoSelecionado == null) "Adicionar" else "Atualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarCadastro = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
