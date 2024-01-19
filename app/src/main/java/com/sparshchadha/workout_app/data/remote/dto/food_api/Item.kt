package com.sparshchadha.workout_app.data.remote.dto.food_api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val calories: Double,
    val carbohydrates_total_g: Double,
    val cholesterol_mg: Int,
    val fat_saturated_g: Double,
    val fat_total_g: Double,
    val fiber_g: Double,
    val name: String,
    val potassium_mg: Int,
    val protein_g: Double,
    val serving_size_g: Double,
    val sodium_mg: Int,
    val sugar_g: Double
)  : Parcelable
//    : Serializable {

//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other !is Item) return false
//
//        if (calories != other.calories) return false
//        if (carbohydrates_total_g != other.carbohydrates_total_g) return false
//        if (cholesterol_mg != other.cholesterol_mg) return false
//        if (fat_saturated_g != other.fat_saturated_g) return false
//        if (fat_total_g != other.fat_total_g) return false
//        if (fiber_g != other.fiber_g) return false
//        if (name != other.name) return false
//        if (potassium_mg != other.potassium_mg) return false
//        if (protein_g != other.protein_g) return false
//        if (serving_size_g != other.serving_size_g) return false
//        if (sodium_mg != other.sodium_mg) return false
//        if (sugar_g != other.sugar_g) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = calories.hashCode()
//        result = 31 * result + carbohydrates_total_g.hashCode()
//        result = 31 * result + cholesterol_mg
//        result = 31 * result + fat_saturated_g.hashCode()
//        result = 31 * result + fat_total_g.hashCode()
//        result = 31 * result + fiber_g.hashCode()
//        result = 31 * result + name.hashCode()
//        result = 31 * result + potassium_mg
//        result = 31 * result + protein_g.hashCode()
//        result = 31 * result + serving_size_g.hashCode()
//        result = 31 * result + sodium_mg
//        result = 31 * result + sugar_g.hashCode()
//        return result
//    }
//}
