package com.sparshchadha.workout_app.util

object Constants {
    const val DATABASE_NAME = "workout_database"
    const val CARBOHYDRATES_TOTAL_G = "Carbohydrates"
    const val FAT_TOTAL_G = "Fats"
    const val PROTEIN_G = "Protein"
    const val TOTAL_NUTRIENTS_G = "total_nutrients_g"

    val COLOR_TO_NUTRIENT_MAP = hashMapOf(
        PROTEIN_G to ColorsUtil.noAchievementColor,
        CARBOHYDRATES_TOTAL_G to ColorsUtil.partialTargetAchievedColor,
        FAT_TOTAL_G to ColorsUtil.targetAchievedColor
    )
}