package com.example.aicompagnon

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aicompagnon.models.EventModel
import com.example.aicompagnon.ui.theme.AicompagnonTheme

class EventDetailActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val event = intent.getSerializableExtra(EventDetailActivity.eventExtraKey) as? EventModel
        enableEdgeToEdge()
        setContent {
            AicompagnonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    if (event != null){
                        EventDetail(event)
                    }
                }
            }
        }
    }
    companion object {
        val eventExtraKey = "eventExtraKey"
    }

}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun EventDetail(event:EventModel){
    Column (modifier = Modifier.padding(40.dp)){
        Text(event.title)
        Text(event.date)
        Text(event.description)
        Text(event.location)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AicompagnonTheme {
        Greeting("Android")
    }
}