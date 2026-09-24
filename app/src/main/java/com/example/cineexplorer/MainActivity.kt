package com.example.cineexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cineexplorer.ui.CineExplorerApp
import com.example.cineexplorer.ui.theme.CineExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineExplorerTheme {
                CineExplorerApp()
            }
        }
    }
}
