package com.android.rokuapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.android.rokuapp.data.model.App
import com.android.rokuapp.data.network.ApiService.BASE_URL
import com.android.rokuapp.ui.theme.RokuAppTheme
import com.android.rokuapp.view.AppItem
import com.android.rokuapp.view.AppUIScreen
import com.android.rokuapp.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = viewModel()
            RokuAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Main screen with scaffold handle errors
                    //AppUIScreen(modifier = Modifier.padding(innerPadding))

                    // Basic view tutorial
                    MainScreen2(viewModel, modifier = Modifier.padding(innerPadding))

                    // MainScreen with out scaffold
                    // MainScreen() // comment out scaffold
                }
            }
        }
    }
}

/**
 *  View of the app without scaffold
 */
@Composable
fun MainScreen(viewModel: AppViewModel = viewModel()) {

    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        items(state.apps) { app ->
            AppItem(app = app)
        }
    }
}

/**
 * Basic view of the app
 */
@Composable
fun MainScreen2(viewModel: AppViewModel, modifier: Modifier) {

    val state by viewModel.state.collectAsState()

    when {
        // Update
        //-------
        state.isLoading -> CircularProgressIndicator()
        state.error != null -> Text("Error: ${state.error}")
        state.apps.isEmpty() -> Text("No apps found")
        //state.apps.isNotEmpty() -> Log.d("apps", state.apps.toString())
        //-------

        else ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.apps) { app ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 36.dp)
                        ) {
                            Text(
                                text = app.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ID: ${app.id}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            AsyncImage(
                                model = BASE_URL + app.imageUrl,
                                contentDescription = app.name,
                                modifier = Modifier.size(128.dp)
                            )
                        }
                    }
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
fun AppItemPreview() {
    RokuAppTheme {
        AppItem(
            app = App(
                id = "12",
                name = "Netflix",
                imageUrl = BASE_URL + "12.jpg"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppScreenPreview() {
    RokuAppTheme {
        AppUIScreen()
    }
}
