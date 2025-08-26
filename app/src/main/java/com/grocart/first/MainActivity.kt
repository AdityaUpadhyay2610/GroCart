package com.grocart.first

// Android and Jetpack Compose imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.grocart.first.ui.theme.GrocartFirstTheme // Import your new theme
// import androidx.core.view.WindowCompat // WindowCompat is often handled within the theme now

// Main entry point of the app, extends ComponentActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enableEdgeToEdge() // This can often be managed by the theme or set directly.
        // If your GroCartFirstTheme handles status bar and navigation bar
        // coloring and transparency, you might not need this call here.
        // Or, if you keep it, ensure it doesn't conflict with theme settings.

        // Set the content to a composable UI using Jetpack Compose
        setContent {
            GrocartFirstTheme { // Apply your new custom Material theme
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        // Consider if padding for systemBars is still needed here
                        // or if it's better handled within specific screens or by your theme.
                        // For a truly edge-to-edge feel provided by the theme,
                        // you might remove this padding at the root level.
                        .padding(WindowInsets.systemBars.asPaddingValues()),
                    color = MaterialTheme.colorScheme.background // Sets background color from theme
                ) {
                    FirstApp() // Launch your main app UI
                }
            }
        }
    }
}

// Preview function for Android Studio to show a preview in design mode
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() { // Renamed to avoid conflict if GreetingPreview is used elsewhere
    GrocartFirstTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            FirstApp() // Displays the same UI as your main screen for preview
        }
    }
}
