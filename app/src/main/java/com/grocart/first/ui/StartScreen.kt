package com.grocart.first.ui

// Importing necessary Android and Compose libraries
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.grocart.first.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.grocart.first.data.DataSource

// Composable function for the start screen, which shows a grid of category cards
@Composable
fun StartScreen(
    groViewModel: GroViewModel, // ViewModel for state management
    onCategoryClicked : (Int) -> Unit // Callback function when category card is clicked
){
    val context = LocalContext.current // Get current context for showing Toast

    // Creating a vertically scrollable grid layout
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp), // Each cell is at least 128.dp wide
        contentPadding = PaddingValues(10.dp), // Padding around grid content
        verticalArrangement = Arrangement.spacedBy(5.dp), // Vertical spacing between grid items
        horizontalArrangement = Arrangement.spacedBy(5.dp) // Horizontal spacing between grid items
    ) {
        item(
            span = { GridItemSpan(2) }
        ){
            Column {
                Image(painter = painterResource(R.drawable.banner2),
                    contentDescription = "Offers Banners",
                    modifier = Modifier.fillMaxWidth().aspectRatio(16f/9f).size(250.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(105,190,110,250)

                    ), modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 1.dp)
                ) {
                    Text(text = "Shop By Category",
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp))
                }
            }
        }


        // Load categories from DataSource and display each as a card
        items(DataSource.loadCategories()) {
            CategoryCard(
                context = context,
                stringResourceId = it.stringResourceId,
                imageResourceId = it.imageResourceId,
                groViewModel = groViewModel,
                onCategoryClicked = onCategoryClicked
            )
        }
    }
}

// Composable function to show each individual category card
@Composable
fun CategoryCard(
    context: Context, // Context for Toast
    stringResourceId: Int, // String resource ID for category name
    imageResourceId: Int,  // Image resource ID for category image
    groViewModel: GroViewModel, // ViewModel instance
    onCategoryClicked: (Int) -> Unit // Callback on card click
) {
    val categoryName = stringResource(id = stringResourceId) // Resolve category name from resources

    // Creating a clickable card for the category
    Card(
        modifier = Modifier.clickable {
            groViewModel.updateClickText(categoryName) // Update selected category name in ViewModel
            Toast.makeText(context, "THIS card was Clicked", Toast.LENGTH_SHORT).show() // Show Toast on click
            onCategoryClicked(stringResourceId) // Trigger navigation callback with category ID
        },
        colors = CardDefaults.cardColors(
            containerColor = Color(249,219,242,50))
    ) {
        // Layout for image and text inside the card
        Column(
            modifier = Modifier
                .padding(20.dp) // Padding inside card
                .fillMaxWidth(), // Card takes full width
            horizontalAlignment = Alignment.CenterHorizontally // Center contents horizontally
        ) {
            Text(
                text = categoryName, // Display category name
                fontSize = 17.sp, // Font size for text
                modifier = Modifier.width(150.dp), // Set fixed width for text
                textAlign = TextAlign.Center // Center align text
            )
            Image(
                painter = painterResource(imageResourceId), // Load image from resource ID
                contentDescription = "Fresh Fruits", // Static content description (can be made dynamic)
                modifier = Modifier.size(150.dp) // Set image size
            )
        }
    }
}
