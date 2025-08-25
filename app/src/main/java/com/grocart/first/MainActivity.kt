package com.grocart.first

// Android and Jetpack Compose imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grocart.first.ui.FirstApp
import com.grocart.first.ui.theme.FirstTheme
import androidx.core.view.WindowCompat

// Main entry point of the app, extends ComponentActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window , false)

        enableEdgeToEdge() // Optional: enables edge-to-edge drawing behind system bars

        // Set the content to a composable UI using Jetpack Compose
        setContent {
            FirstTheme { // Apply custom Material theme defined in your app
                Surface(
                    modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues()), // Fills entire screen
                    color = MaterialTheme.colorScheme.background // Sets background color from theme
                ) {
                    FirstApp() // Launch your main app UI (StartScreen inside it)
                }
            }
        }
    }
}
// Preview function for Android Studio to show a preview in design mode
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirstTheme {
        FirstApp() // Displays the same UI as your main screen for preview
    }
}
