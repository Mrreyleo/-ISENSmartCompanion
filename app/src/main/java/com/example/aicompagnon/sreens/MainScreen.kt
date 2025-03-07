package com.example.aicompagnon.sreens


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aicompagnon.R
import com.example.aicompagnon.models.ChatModel
import androidx.core.content.ContextCompat.getString
import com.example.aicompagnon.dao.ChatDao
import com.example.aicompagnon.database.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun MessageCard(title: String, chatDao: ChatDao) {
    val context = LocalContext.current
    var userInput = remember { mutableStateOf("") }
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyCj1_Id4tXAWmdcIfg62yq1ZfMtFOyrwUQ"
    )
    val coroutineScope = rememberCoroutineScope()
    var historyAI by remember { mutableStateOf<List<Content>>(listOf()) }
    var chats = remember { mutableStateListOf<ChatModel>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = context.getString(R.string.isen_logo),
                modifier = Modifier.size(150.dp)
            )
            Text(text = title, textAlign = TextAlign.Center)
        }

        Column(modifier = Modifier.padding(top = 40.dp)) {
            LazyColumn {
                items(chats) { chat ->
                    chatRow(chat)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
                .padding(8.dp)
        ) {
            TextField(
                value = userInput.value,
                onValueChange = { userInput.value = it },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "${getString(context, R.string.user_input)} : ${userInput.value}",
                                Toast.LENGTH_LONG
                            ).show()
                            if (userInput.value != "") {
                                coroutineScope.launch {
                                    val chat = generativeModel.startChat(
                                        history = historyAI
                                    )
                                    val message = chat.sendMessage(userInput.value)
                                    historyAI = historyAI + listOf(
                                        content(role = "user") { text(userInput.value) },
                                        content(role = "model") { text(message.text.toString()) }
                                    )
                                    chats.add(ChatModel("user", userInput.value))
                                    chats.add(ChatModel("model", message.text.toString()))

                                    // Now insert the chat into the Room database
                                    val chatToInsert = Chat(
                                        uid = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                                        question = userInput.value,
                                        answer = message.text.toString()
                                    )
                                    chatDao.insertChat(chatToInsert)
                                }
                            }
                        },
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.Red, shape = CircleShape)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.arrow_forward),
                            contentDescription = getString(context, R.string.envoyer)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun chatRow(chat: ChatModel) {
    Card(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = chat.message,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
