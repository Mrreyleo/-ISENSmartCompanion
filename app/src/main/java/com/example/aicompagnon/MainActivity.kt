package com.example.aicompagnon

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.aicompagnon.database.AppDatabase
import com.example.aicompagnon.database.Chat
import com.example.aicompagnon.models.EventModel
import com.example.aicompagnon.sreens.EventScreen
import com.example.aicompagnon.sreens.HistoryScreen
import com.example.aicompagnon.sreens.MessageCard
import com.example.aicompagnon.sreens.TabView
import com.example.aicompagnon.ui.theme.AicompagnonTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "chat-database"
            ).build()
            val chatDao = db.chatDao()
            MessageCard(getString(R.string.app_name),chatDao)

            val homeTab = TabBarItem(title = getString(R.string.home), selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
            val eventsTab = TabBarItem(title = getString(R.string.events), selectedIcon = Icons.Filled.Notifications, unselectedIcon = Icons.Outlined.Notifications, badgeAmount = 7)
            val historyTab = TabBarItem(title = getString(R.string.history), selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings)

            val tabBarItems = listOf(homeTab, eventsTab, historyTab)

            val navController = rememberNavController()

            AicompagnonTheme {
                Scaffold(
                    bottomBar = {
                        TabView(tabBarItems, navController)
                    }
                ) {
                    NavHost(navController = navController, startDestination = homeTab.title) {
                        composable(homeTab.title) { MessageCard(getString(R.string.app_name),chatDao) }
                        composable(eventsTab.title) {
                            EventScreen(eventHandler = {
                                    event -> startEventDataActivity(event)
                            })
                        }
                        composable(historyTab.title) {
                            HistoryScreen(eventHandler = {
                                    event -> startHistoryActivity(event)
                            })
                        }
                    }
                }
            }
        }
    }

    fun startEventDataActivity(event: EventModel) {
        val intent = Intent(this, EventDetailActivity::class.java).apply {
            putExtra(EventDetailActivity.eventExtraKey, event)
        }
        startActivity(intent)
    }

    fun startHistoryActivity(chat: Chat) {
        val intent = Intent(this, HistoryActivity::class.java).apply {
            putExtra(HistoryActivity.chatExtraKey, chat)
        }
        startActivity(intent)
    }
}
