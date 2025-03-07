package com.example.aicompagnon.sreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aicompagnon.database.Chat
import com.example.aicompagnon.models.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(eventHandler: (Chat) -> Unit) {
    val chatViewModel: ViewModel = viewModel()

    val chats by chatViewModel.chats.observeAsState(listOf()) // Utiliser observeAsState

    LaunchedEffect(Unit) {
        chatViewModel.getAllChats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historique des Conversations") }
            )
        }
    ) { paddingValues ->
        // Affichage des chats dans une liste
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(chats.size) { index ->
                val chat = chats[index]
                ChatCard(chat = chat, onClick = { eventHandler(chat) })
            }
        }
    }
}

@Composable
fun ChatCard(chat: Chat, onClick: (Chat) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(chat) }, // Gestionnaire de clic pour l'élément
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Question : ${chat.question}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Réponse : ${chat.answer}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date : ${chat.timestamp}")
        }
    }
}
