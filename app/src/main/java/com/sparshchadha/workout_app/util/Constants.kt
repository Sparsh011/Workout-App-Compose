package com.sparshchadha.workout_app.util

object Constants {
    const val DATABASE_NAME = "workout_database"
    const val CARBOHYDRATES_TOTAL_G = "Carbohydrates"
    const val FAT_TOTAL_G = "Fats"
    const val PROTEIN_G = "Protein"
    const val TOTAL_NUTRIENTS_G = "total_nutrients_g"

    val COLOR_TO_NUTRIENT_MAP = hashMapOf(
        PROTEIN_G to ColorsUtil.proteinColor,
        CARBOHYDRATES_TOTAL_G to ColorsUtil.carbohydratesColor,
        FAT_TOTAL_G to ColorsUtil.fatsColor
    )

    const val REMINDER_DESCRIPTION_KEY = "REMINDER_DESCRIPTION"
//    val FIREBASE_WEB_CLIENT_ID = "421641706634-a14flpbiidpdsbp85ffnr7vlaujbeoa9.apps.googleusercontent.com"
}