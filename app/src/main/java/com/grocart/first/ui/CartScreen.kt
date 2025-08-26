package com.grocart.first.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.grocart.first.R
import com.grocart.first.data.InternetItem
import com.grocart.first.data.InternetItemWithQuantity



@Composable
fun CartScreen(
    groViewModel: GroViewModel,
    onHomeButtonClicked : () -> Unit

){
    val cartItems by groViewModel.cartItems.collectAsState()
    val cartItemWithQuantity = cartItems.groupBy { it }
        .map {
                (item,cartItems) ->
            InternetItemWithQuantity (item,cartItems.size)
        }
    if (cartItems.isNotEmpty()){
        LazyColumn (contentPadding = PaddingValues(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
            item {
                Image(painter = painterResource(id = R.drawable.cart_banner1), contentDescription = "Offers Banners", modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit)

            }
            item { Text(
                text = "Review Items",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp

            ) }
            items(cartItemWithQuantity){
                CartCard(it.internetItem, groViewModel, it.quantity)
            }
            item{
                Text(text = "Bill Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp)


            }
            val totalPrice = cartItems.sumOf { it.itemPrice }
            val handlingCharge = totalPrice*1/100
            val deliveryFee = 30
            val grandTotal = totalPrice + handlingCharge + deliveryFee
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()){
                    Column (
                        modifier = Modifier.padding(10.dp)
                    ){
                        BillRow("Item Total",totalPrice, FontWeight.Bold)
                        BillRow("Handling Charge",handlingCharge, FontWeight.Light)
                        BillRow("Delivery Fee",deliveryFee, FontWeight.Light)
                        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 5.dp), color = Color.LightGray)
                        BillRow(" To Pay",grandTotal, FontWeight.ExtraBold)
                    }

                }
            }
        }

    }else{
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(painter = painterResource(id = R.drawable.emptycart), contentDescription = "Empty Cart", modifier = Modifier.size(250.dp))
            Text(text = "You Cart is Empty", fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(20.dp))
            FilledTonalButton(onClick = {
                onHomeButtonClicked()
            }) {
                Text(text = "Browse Products")
            }
        }
    }




}

@Composable
fun BillRow(
    itemName:String,
    itemPrice:Int,
    fontWeight: FontWeight

){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = itemName, fontWeight = fontWeight)
        Text(text = "Rs. $itemPrice", fontWeight = fontWeight)
    }
}

@Composable
fun CartCard(cartItem: InternetItem,
             groViewModel: GroViewModel,
             cartItemQuantity:Int = 1){
    Row(modifier = Modifier.fillMaxWidth().height(80.dp), verticalAlignment = Alignment.CenterVertically){
        AsyncImage(model = cartItem.imageUrl, contentDescription = "Apple", modifier = Modifier.fillMaxHeight().padding(start = 5.dp).weight(4f))
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
                .weight(4f)
        ) {
            Text(text = cartItem.itemName,
                fontSize = 20.sp,
                maxLines = 1,)
            Text(
                text = cartItem.itemQuantity,
                fontSize = 15.sp,
                maxLines = 1,

                )

        }
        Column(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .fillMaxWidth()
                .weight(3f)
        ) {
            Text(text = "Rs. ${cartItem.itemPrice}",
                fontSize = 12.sp,
                maxLines = 1,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough)
            Text(
                text = "Rs${cartItem.itemPrice*75/100}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = Color(254,116,105,255)

            )

        }
        Column (
            modifier = Modifier.fillMaxHeight()
                .weight(3f), verticalArrangement = Arrangement.SpaceEvenly
        ){
            Text(text="Quantity: $cartItemQuantity",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())

            Card(modifier = Modifier.clickable{
                groViewModel.removeFromCart(cartItem)
            }.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(254,116,105,255))){
                Text(text = "Remove",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))

            }
        }


    }
}