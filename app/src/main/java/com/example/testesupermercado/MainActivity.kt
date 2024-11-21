    package com.example.testesupermercado

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Toast
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.viewModels
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
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
    import com.example.testesupermercado.data.AppDatabase
    import com.example.testesupermercado.data.ProdutoRepository
    import com.example.testesupermercado.view.ProdutoViewModel
    import com.example.testesupermercado.view.ProdutoViewModelFactory
    import com.example.testesupermercado.model.Produto
    import com.example.testesupermercado.api.FreteViewModel
    import com.example.testesupermercado.api.FreteRepository
    import com.example.testesupermercado.api.ViaCepApi

    class MainActivity : ComponentActivity() {
        private val database by lazy { AppDatabase.getDatabase(this) }
        private val repository by lazy { ProdutoRepository(database.produtoDao()) }
        private val produtoViewModel: ProdutoViewModel by viewModels {
            ProdutoViewModelFactory(repository)
        }
        private val freteViewModel: FreteViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                AppScreen(produtoViewModel, freteViewModel)
            }
        }
    }

    @Composable
    fun AppScreen(produtoViewModel: ProdutoViewModel, freteViewModel: FreteViewModel) {
        var nome by remember { mutableStateOf("") }
        var valor by remember { mutableStateOf("") }
        var quantidade by remember { mutableStateOf("") }
        var mostrarCadastro by remember { mutableStateOf(false) }
        var mostrarCompartilhar by remember { mutableStateOf(false) }
        var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
        var cep by remember { mutableStateOf("") }
        val produtos by produtoViewModel.produtos.collectAsState()
        val context = LocalContext.current

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
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Supermercado",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Clique em + para adicionar um produto no carrinho",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
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
                                        IconButton(onClick = {
                                            nome = produto.nome
                                            valor = produto.valor.toString()
                                            quantidade = produto.quantidade.toString()
                                            produtoSelecionado = produto
                                            mostrarCompartilhar = true
                                        }) {
                                            Icon(imageVector = Icons.Default.Share, contentDescription = "Compartilhar")
                                        }
                                        IconButton(onClick = {
                                            produtoViewModel.deletarProduto(produto)
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
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Quantidade: ${produto.quantidade}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
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
                    title = { Text("Cadastrar ou Atualizar Produto") },
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
                                    produtoViewModel.adicionarProduto(produto)
                                } else {
                                    produtoViewModel.atualizarProduto(produto)
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
            if (mostrarCompartilhar) {
                AlertDialog(
                    onDismissRequest = { mostrarCompartilhar = false },
                    title = { Text("Compartilhar Produto") },
                    text = {
                        Column {
                            Text("Nome: $nome")
                            Text("Valor: R$ $valor")
                            Text("Quantidade: $quantidade")
                            TextField(
                                value = cep,
                                onValueChange = { cep = it },
                                label = { Text("Digite o CEP") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                if (cep.isNotEmpty()) {
                                    freteViewModel.buscarEndereco(cep, valor.toDoubleOrNull() ?: 0.0, quantidade.toIntOrNull() ?: 0)
                                }
                            }) {
                                Text("Calcular Frete")
                            }
                            if (freteViewModel.endereco != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Endereço: ${freteViewModel.endereco!!.logradouro}, ${freteViewModel.endereco!!.localidade}-${freteViewModel.endereco!!.uf}")
                                Text("Valor do Frete: R$ ${freteViewModel.freteValor}")
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val shareText = """
                            Produto: $nome
                            Valor: R$ $valor
                            Quantidade: $quantidade
                            Endereço: ${freteViewModel.endereco?.logradouro ?: "N/A"}
                            Valor do Frete: R$ ${freteViewModel.freteValor}
                        """.trimIndent()

                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, "Compartilhar via")
                            context.startActivity(shareIntent)

                            mostrarCompartilhar = false
                        }) {
                            Text("Compartilhar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarCompartilhar = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }


    @Composable
    fun CompartilharProdutoComFrete(produto: Produto, freteViewModel: FreteViewModel) {
        var cep by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = cep,
                onValueChange = { cep = it },
                label = { Text("Digite o CEP") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                if (cep.isNotEmpty()) {
                    freteViewModel.buscarEndereco(cep, produto.valor, produto.quantidade)
                }
            }) {
                Text("Calcular Frete")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (freteViewModel.endereco != null) {
                Text("Endereço: ${freteViewModel.endereco!!.logradouro}, ${freteViewModel.endereco!!.localidade}-${freteViewModel.endereco!!.uf}")
                Text("Valor do Frete: R$ ${freteViewModel.freteValor}")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    val compartilharIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Produto: ${produto.nome}\nValor: R$${produto.valor}\nQuantidade: ${produto.quantidade}\nFrete: R$${freteViewModel.freteValor}\nEndereço: ${freteViewModel.endereco!!.logradouro}, ${freteViewModel.endereco!!.localidade}-${freteViewModel.endereco!!.uf}"
                        )
                        type = "text/plain"
                    }
                    val intentChooser = Intent.createChooser(compartilharIntent, "Compartilhar Produto com Frete via")
                    context.startActivity(intentChooser)
                }) {
                    Text("Compartilhar")
                }
            }
        }
    }