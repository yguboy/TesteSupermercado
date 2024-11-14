package com.example.testesupermercado

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.testesupermercado.data.AppDatabase
import com.example.testesupermercado.data.ProdutoRepository
import com.example.testesupermercado.view.ProdutoViewModel
import com.example.testesupermercado.view.ProdutoViewModelFactory
import com.example.testesupermercado.model.Produto

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { ProdutoRepository(database.produtoDao()) }
    private val viewModel: ProdutoViewModel by viewModels {
        ProdutoViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(viewModel: ProdutoViewModel) {
    var nome by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var mostrarCadastro by remember { mutableStateOf(false) }
    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
    val produtos by viewModel.produtos.collectAsState()
    val context = LocalContext.current

    fun compartilharProduto(produto: Produto) {
        val compartilharIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Produto: ${produto.nome}\nValor: R$${produto.valor}\nQuantidade: ${produto.quantidade}"
            )
            type = "text/plain"
        }
        val intentChooser = Intent.createChooser(compartilharIntent, "Compartilhar Produto via")
        context.startActivity(intentChooser)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Supermercado",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // Ação de navegação ou outro recurso no futuro
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartilhar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                produtoSelecionado = null
                mostrarCadastro = true
            }) {
                Text("+", fontSize = 28.sp)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Instruções no topo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Clique em + para adicionar um produto",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            // Lista de produtos
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(produtos) { produto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
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
                                    text = produto.nome,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
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
                                    IconButton(onClick = { compartilharProduto(produto) }) {
                                        Icon(imageVector = Icons.Default.Share, contentDescription = "Compartilhar")
                                    }
                                    IconButton(onClick = { viewModel.deletarProduto(produto) }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Deletar")
                                    }
                                }
                            }

                            Text(
                                text = "Valor: R$${produto.valor}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Text(
                                text = "Quantidade: ${produto.quantidade}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // Caixa de diálogo para cadastro/edição
        if (mostrarCadastro) {
            AlertDialog(
                onDismissRequest = { mostrarCadastro = false },
                title = { Text(if (produtoSelecionado == null) "Cadastrar Produto" else "Editar Produto") },
                text = {
                    Column {
                        TextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome do Produto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = valor,
                            onValueChange = { valor = it },
                            label = { Text("Valor") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = quantidade,
                            onValueChange = { quantidade = it },
                            label = { Text("Quantidade") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
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
