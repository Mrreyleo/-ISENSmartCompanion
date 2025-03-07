package com.example.aicompagnon.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.aicompagnon.database.Chat

@Dao
interface ChatDao {

    @Query("SELECT * FROM Chat")
    fun getAll(): List<Chat>

    @Query("SELECT * FROM Chat ORDER BY timestamp DESC")
    suspend fun getAllChats(): List<Chat>

    @Query("SELECT * FROM Chat WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Chat>

    @Query("SELECT * FROM Chat WHERE question LIKE :first AND answer LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Chat?

    @Insert
    fun insertAll(vararg chats: Chat)

    @Delete
    fun delete(chats: Chat)

    @Insert
    suspend fun insertChat(chat: Chat)
}