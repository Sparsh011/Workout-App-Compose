package com.sparshchadha.workout_app.ui.activity.components

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.ui.activity.GoogleAuthClient

@Composable
fun GoogleSignInLauncher(
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val startLogin = profileViewModel.startGoogleSignIn.collectAsStateWithLifecycle().value
    val googleAuthClient = GoogleAuthClient(context)

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(it.data)
            val token = handleSignInResult(task)

            if (token != null) {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                profileViewModel.updateLoginResult(result = true, token = token)
            } else {
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                profileViewModel.updateLoginResult(result = false)
            }
        }
    )

    observeLoginState(
        startLogin = startLogin,
        googleAuthClient = googleAuthClient,
        signInLauncher = googleSignInLauncher
    )
}

private fun observeLoginState(
    startLogin: Boolean,
    googleAuthClient: GoogleAuthClient,
    signInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    when (startLogin) {
        true -> {
            val signInIntent = googleAuthClient.getSignInIntent()
            signInLauncher.launch(signInIntent)
        }

        false -> {

        }
    }
}


private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): String? {
    return try {
        val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
        account.idToken
    } catch (e: ApiException) {
        null
    }
}