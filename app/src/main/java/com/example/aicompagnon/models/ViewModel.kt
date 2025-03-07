package com.example.aicompagnon.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.aicompagnon.database.AppDatabase
import com.example.aicompagnon.database.Chat

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,
        "database-history"
    ).build()

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> = _chats

    suspend fun getAllChats() {
        val chatList = db.chatDao().getAllChats()
        _chats.postValue(chatList)
    }
}
