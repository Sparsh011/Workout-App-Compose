package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FoodCard(
    foodItemName: String,
    calories: String,
    sugar: String,
    fiber: String,
    sodium: String,
    cholesterol: String,
    protein: String,
    carbohydrates: String,
    servingSize: String,
    totalFat: String,
    saturatedFat: String,
) {
    var expandCard by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    if (expandCard) {
        ExpandedFoodCard(
            foodItemName,
            calories,
            sugar,
            fiber,
            sodium,
            cholesterol,
            protein,
            carbohydrates,
            servingSize,
            totalFat,
            saturatedFat,
            shrinkCard = {
                expandCard = false
            }
        )
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    expandCard = true
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = foodItemName, modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
                Button(onClick = {
                    Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Add")
                }
            }
            Text(
                text = "Calories per $servingSize grams serving : $calories",
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(20.dp)
            )
        }
    }
}

@Composable
private fun ExpandedFoodCard(
    foodItemName: String,
    calories: String,
    sugar: String,
    fiber: String,
    sodium: String,
    cholesterol: String,
    protein: String,
    carbohydrates: String,
    servingSize: String,
    totalFat: String,
    saturatedFat: String,
    shrinkCard: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                shrinkCard()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = foodItemName, modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
            Button(onClick = {
//                Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Add")
            }
        }
        Text(
            text = "Serving Size : $servingSize",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Calories : $calories",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Protein : $protein grams",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Sugar : $sugar grams",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Total Fat : $totalFat grams",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Saturated Fat : $saturatedFat grams",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Carbohydrates : $carbohydrates grams",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Fiber : $fiber grams",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Sodium : $sodium mg",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
        Text(
            text = "Cholesterol : $cholesterol mg",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
    }
}
