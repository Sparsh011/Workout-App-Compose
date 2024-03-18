package com.sparshchadha.workout_app.ui.activity.components

import android.content.Intent
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
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleSignInResult(
                completedTask = task,
                getId = { idToken ->
                    if (idToken != null) {
                        profileViewModel.updateLoginResult(result = true, token = idToken)
                    } else {
                        profileViewModel.updateLoginResult(result = false)
                    }
                },
                getAccessToken = {
                    // send access token to backend to verify.
                }
            )
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


private fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>,
    getId: (String?) -> Unit,
    getAccessToken: (String?) -> Unit
) {
    try {
        val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
        getId(
            account.id
        )
        getAccessToken(
            account.idToken
        )
    } catch (e: ApiException) {
        getId(null)
        getAccessToken(null)
    }
}