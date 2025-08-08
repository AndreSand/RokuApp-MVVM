package com.android.rokuapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.rokuapp.data.model.App
import com.android.rokuapp.ui.theme.RokuAppTheme
import com.android.rokuapp.view.AppItem
import com.android.rokuapp.view.AppUIScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RokuAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppUIScreen(modifier = Modifier.padding(innerPadding))
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
                imageUrl = "https://rokumobileinterview.s3.us-west-2.amazonaws.com/12.jpg"
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
