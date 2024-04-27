package com.sparshchadha.workout_app.shared_ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sparshchadha.workout_app.BuildConfig

class GoogleAuthClient(
    private val context: Context,
) {
    fun getSignInIntent(): Intent {
        val googleSignInClient = getGoogleSignInClient()
        googleSignInClient.signOut() // Add this line to prevent auto sign in

        return googleSignInClient.signInIntent
    }

    private fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_SERVER_CLIENT_ID)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.

        return GoogleSignIn.getClient(context as Activity, gso)
    }
}