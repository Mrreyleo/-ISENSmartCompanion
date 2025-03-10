package com.example.aicompagnon.sreens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aicompagnon.api.NetworkManager
import com.example.aicompagnon.models.EventModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EventScreen(eventHandler: (EventModel) -> Unit) {
    var events = remember{ mutableStateOf<List<EventModel>>(listOf())}
    LaunchedEffect(Unit) {
        val call = NetworkManager.api.getEvents()
        call.enqueue(object: Callback<List<EventModel>>{
            override fun onResponse(
                p0: Call<List<EventModel>>,
                p1: Response<List<EventModel>>
            ) {
                events.value = p1.body()?: listOf()
            }

            override fun onFailure(p0: Call<List<EventModel>>, p1: Throwable) {
                Log.e("request", p1.message ?:"request")
            }
        })
    }

    Column (modifier = Modifier.padding(top = 40.dp)){
        LazyColumn {
            items(events.value){ event ->
                EventRow(event, eventHandler)
            }

        }
    }
}
@Composable
fun EventRow(event: EventModel, eventHandler:(EventModel) -> Unit) {
    Card (
        modifier = Modifier
            .padding(16.dp)
            .clickable { eventHandler(event) }
    ){
        Column (modifier = Modifier.padding(16.dp)) {
            Row (horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(event.title)
                Text(event.date)
            }

            Text(event.description)
        }
    }
}